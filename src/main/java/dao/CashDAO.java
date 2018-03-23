package dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gateweb.charge.repository.CashMasterRepository;
import com.gateweb.charge.vo.CashVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.NullConstants;
import com.gate.utils.SendEmailFileUtils;
import com.gate.utils.TimeUtils;
import com.gate.web.beans.CashDetailBean;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CashMasterEntity;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.DeductDetailEntity;
import com.gateweb.charge.model.GradeEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository("cashDAO")
public class CashDAO extends BaseDAO {

    public Map getCashMaster(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from cash_master cm left join company cp on cm.company_id = cp.company_id  where 1=1 ");
        dataSb.append(" select cm.cash_master_id,cm.company_id,cp.name,cm.out_ym,cm.bank_ym,cm.no_tax_inclusive_amount,cm.tax_type," );
        dataSb.append("cm.tax_rate,cm.tax_amount,cm.tax_inclusive_amount,to_char(cm.out_date,'YYYY/MM/DD') out_date,cm.in_amount,to_char(cm.in_date, 'YYYY/MM/DD') in_date,cm.in_note,cm.email_sent_date,cm.is_first " );
        dataSb.append("from cash_master cm left join company cp on cm.company_id = cp.company_id ");
        dataSb.append(" where 1=1 ");


        if (querySettingVO.getSearchMap().size() > 0) {
            Map searchMap = querySettingVO.getSearchMap();
            if (searchMap.containsKey("outYM")) {
                if (StringUtils.isNotEmpty(searchMap.get("outYM").toString())) {
                    whereSb.append(" and  cm.out_ym = ?");
                    parameters.add(searchMap.get("outYM") );
                }
            }
            if (searchMap.containsKey("userCompanyId")) {
                if (StringUtils.isNotEmpty(searchMap.get("userCompanyId").toString())) {
                    whereSb.append(" and cp.company_id = ?");
                    parameters.add( Integer.parseInt((String)searchMap.get("userCompanyId")));
                }
            }
            if (searchMap.containsKey("companyName")) {
                if (StringUtils.isNotEmpty(searchMap.get("companyName").toString())) {
                    whereSb.append(" and cp.name like ?");
                    parameters.add( "%"+(String)searchMap.get("companyName")+"%");
                }
            }
            if (searchMap.containsKey("payStatus")) {
                if (StringUtils.isNotEmpty(searchMap.get("payStatus").toString())) {
                    if(searchMap.get("payStatus").toString().equals("all")){

                    }else if (searchMap.get("payStatus").toString().equals("paid")){
                        whereSb.append(" and cm.in_date is not null ");
                    }else if(searchMap.get("payStatus").toString().equals("unpay")){
                        whereSb.append(" and cm.in_date is null");
                    }
                }
            }
        }

        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by " + querySettingVO.getSidx() + " " + querySettingVO.getSord() + ", cm.out_ym, cm.cash_master_id desc ");
        int first = (querySettingVO.getPage() - 1) * querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);
        return returnMap;
    }

    //產生年月下拉選單。
    public List getYM() throws Exception {
        List list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -24);

        for(int i=0; i<40; i++){
            YM ym = new YM();
            ym.setCalYear("" + now.get(Calendar.YEAR));
            if ((now.get(Calendar.MONTH) + 1)<10){
                ym.setCalMonth("0" + (now.get(Calendar.MONTH) + 1));
            }else{
                ym.setCalMonth("" + (now.get(Calendar.MONTH) + 1));
            }

            list.add(ym);
            now.add(Calendar.MONTH, 1);
        }
        return list;
    }

    //下拉選單物件
    class YM{
        private String calYear;
        private String calMonth;

        public String getCalYear() {
            return calYear;
        }

        public void setCalYear(String calYear) {
            this.calYear = calYear;
        }

        public String getCalMonth() {
            return calMonth;
        }

        public void setCalMonth(String calMonth) {
            this.calMonth = calMonth;
        }
    }

    public List getCompnay() throws Exception {
        String sql = "select company_id, name from company order by company_id " ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql,parameterList,null);
        return query.list();
    }


    //出帳-多筆
    public Integer transactionSumOut(String masterIdAry) throws Exception{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(masterIdAry, collectionType);

        int exeCnt = 0;
        //找出所有要計算的MasterId
        for(int i=0; i<cashMasterList.size(); i++) {
            CashMasterBean bean = (CashMasterBean)cashMasterList.get(i);

            Integer cashMasterId = (null == bean.getCashMasterId())?0:bean.getCashMasterId();
            CashMasterEntity  cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);

            boolean isOut = sumOut(cashMasterEntity);
            if(isOut){ //是否出帳?
                exeCnt++; //出帳
            }
        }
        return exeCnt;
    }

    //出帳-年月
    public Integer transactionSumOutYM(String outYM, Integer userCompanyId) throws Exception {
        //1.Find all cashMaster
        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
        searchCashMasterEntity.setOutYm(outYM);
        if(userCompanyId != 0){
            searchCashMasterEntity.setCompanyId(userCompanyId);
        }
        List cashMasterList =  getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);

        int exeCnt = 0;
        for(int i=0; i<cashMasterList.size(); i++){
            CashMasterEntity  cashMasterEntity = (CashMasterEntity)cashMasterList.get(i);
            boolean isOut = sumOut(cashMasterEntity);
            if(isOut){ //是否出帳?
                exeCnt++; //出帳
            }
        }
        return exeCnt;
    }

    //出帳
    public boolean sumOut(CashMasterEntity cashMasterEntity) throws Exception{
        //已出帳，不允許直接「重新出帳」，除非先「取消出帳」，才可重新計算
        if(3 <= cashMasterEntity.getStatus()){
            return false;
        }

        //sumCashMaster
        cashMasterEntity = sumCashMaster(cashMasterEntity);

        Timestamp tmestamp = timeUtils.getCurrentTimestamp();
        cashMasterEntity.setOutDate(tmestamp);
        cashMasterEntity.setStatus(3); //1.生效 3.出帳 4.入帳 5.佣金
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
        return true;
    }

    //把所有cash_master底下的cash_detail(不含作廢)的錢全部加總起來
    public CashMasterEntity sumCashMaster(CashMasterEntity cashMasterEntity) throws Exception{

        Integer cashMasterId = cashMasterEntity.getCashMasterId();
        //2.find cashMaster's all cashDetail
        CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
        searchCashDetailEntity.setCashMasterId(cashMasterId);
        List cashDetailList =  getSearchEntity(CashDetailEntity.class, searchCashDetailEntity);

        BigDecimal sumNoTaxInclusivePrice = new BigDecimal(0);
        BigDecimal sumTaxPrice = new BigDecimal(0);
        BigDecimal sumTaxInclusivePrice = new BigDecimal(0);

        //3.sum the values of cashDetails
        for(int j=0; j<cashDetailList.size(); j++){
            CashDetailEntity  cashDetailEntity = (CashDetailEntity)cashDetailList.get(j);

            Integer status = cashDetailEntity.getStatus();
            if(null != status && 2 != status){ //作廢的不計 status=2(作廢)
                BigDecimal noTaxInclusivePrice = cashDetailEntity.getNoTaxInclusivePrice(); //noTaxPrice
                BigDecimal taxPrice= cashDetailEntity.getTaxPrice(); //taxPrice
                BigDecimal taxInclusivePrice= cashDetailEntity.getTaxInclusivePrice(); //includeTaxPrice

                sumNoTaxInclusivePrice = sumNoTaxInclusivePrice.add(noTaxInclusivePrice);
                sumTaxPrice = sumTaxPrice.add(taxPrice);
                sumTaxInclusivePrice = sumTaxInclusivePrice.add(taxInclusivePrice);
            }
        }

        sumNoTaxInclusivePrice = sumNoTaxInclusivePrice.setScale(0, BigDecimal.ROUND_HALF_UP);
        sumTaxPrice = sumTaxPrice.setScale(0,BigDecimal.ROUND_HALF_UP);
        sumTaxInclusivePrice = sumTaxInclusivePrice.setScale(0,BigDecimal.ROUND_HALF_UP);
        //4.update cashMaster's value
        cashMasterEntity.setNoTaxInclusiveAmount(sumNoTaxInclusivePrice);
        cashMasterEntity.setTaxAmount(sumTaxPrice);
        cashMasterEntity.setTaxInclusiveAmount(sumTaxInclusivePrice);
        cashMasterEntity.setTaxType("1");
        cashMasterEntity.setTaxRate(0.05f);

        return cashMasterEntity;
    }

    public CashMasterEntity sumCashMaster(Integer cashMasterId) throws Exception{
        CashMasterEntity entity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
        CashMasterEntity cashMasterEntity = sumCashMaster(entity);
        return cashMasterEntity;
    }

    //入帳-單筆
    public boolean transactionIn(Integer cashMasterId, Double inAmount, String inDate, String inNote) throws Exception, ParseException {
        CashMasterEntity cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
        cashMasterEntity.setInAmount(new BigDecimal(inAmount));
        cashMasterEntity.setInNote(inNote);

        //is_inout_money_unmatch: cash_master的出帳和入帳金額不同時，為1
        BigDecimal taxInclusiveAmount = cashMasterEntity.getTaxInclusiveAmount(); //出帳金額
        int compResult = taxInclusiveAmount.compareTo(new BigDecimal(inAmount));  //i可能為-1、0、1，分别表示小於、等於、大於
        if(0 != compResult){ //cash_master的出帳和入帳金額不同時
            cashMasterEntity.setIsInoutMoneyUnmatch("1"); //cash_master.is_inout_money_unmatch = 1
        }

        Timestamp tmestamp = timeUtils.stringToTimestamp(inDate, "yyyy-MM-dd");
        //Timestamp tmestamp = TimeUtils.getCurrentTimestamp();
        cashMasterEntity.setInDate(tmestamp);
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());

        cashSumIn(cashMasterEntity);
        return true;
    }

    //入帳-批次(excel表匯入)
    public String transactionExcelSumIn(String businesscode, String inDate, String bankYM, Double inMoney) throws Exception{
        if(null == bankYM ||"".equals(bankYM)){
            return "bankYM is null or empty.";
        }
        if(null == businesscode ||"".equals(businesscode)){
            return "businesscode is null or empty.";
        }

        //1.找出要作入帳的cash_master是哪一筆
        String masterSql = " select cm.cash_master_id from cash_master cm left join company cp on cm.company_id=cp.company_id " +
                           " where bank_ym=? and business_no=? ";
        List parameterList = new ArrayList();
        parameterList.add(bankYM.trim());
        parameterList.add(businesscode.trim());
        Query query = createQuery(masterSql, parameterList, null);
        List list = query.list();

        if(null == list || list.size() == 0){
            return "businesscode="+businesscode+", inDate="+inDate+", bankYM="+bankYM+", inMoney="+inMoney + ", 系統沒有此筆出帳資料!!<br>";
        }

        for(int i=0; i<list.size(); i++){
            Map map = (Map)list.get(0);
            Integer cashMasterId = (Integer)map.get("cash_master_id");
            CashMasterEntity cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);

            if(null == cashMasterEntity.getOutDate()){
                return "businesscode="+businesscode+", inDate="+inDate+", bankYM="+bankYM+", inMoney="+inMoney + ",此筆資料尚未出帳!!<br>";
            }
            if(null != cashMasterEntity.getInDate()){
                return "businesscode="+businesscode+", inDate="+inDate+", bankYM="+bankYM+", inMoney="+inMoney + ", 系統已有此筆出帳資料!!<br>";
            }

            cashMasterEntity.setInAmount(cashMasterEntity.getTaxInclusiveAmount());
            //cashMasterEntity.setInNote("");
            Timestamp tmestamp = timeUtils.stringToTimestamp(inDate, "yyyy/MM/dd");
            cashMasterEntity.setInDate(tmestamp);
            saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());

            cashSumIn(cashMasterEntity);
        }

        return "businesscode="+businesscode+", inDate="+inDate+", bankYM="+bankYM+", inMoney="+inMoney + ", ok!!<br>";
    }

    /**
     * 執行入帳(update cash_master(這個method不作)、cash_detail、bill_cycle、package_mode)
     * @param cashMasterEntity
     * @return
     * @throws Exception
     */
    public boolean cashSumIn(CashMasterEntity  cashMasterEntity) throws Exception{
        Integer cashMasterId = cashMasterEntity.getCashMasterId();
        cashMasterEntity.setStatus(4); //1.生效 3.出帳 4.入帳 5.佣金
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());

        //2.find cashMaster's all cashDetail
        CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
        searchCashDetailEntity.setCashMasterId(cashMasterId);
        List cashDetailList =  getSearchEntity(CashDetailEntity.class, searchCashDetailEntity);

        for(int j=0; j<cashDetailList.size(); j++){
            CashDetailEntity  cashDetailEntity = (CashDetailEntity)cashDetailList.get(j);
            //find cash_detail's id
            Integer cashDetailId = cashDetailEntity.getCashDetailId();

            //3.Find bill_cycle' by detail_id-month_Id(月租)
            BillCycleEntity searchBillCycleEntity = new BillCycleEntity();
            searchBillCycleEntity.setCashOutMonthId(cashDetailId);
            List billCycleMonthList =  getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);
            for(int k=0; k<billCycleMonthList.size(); k++){
                //4.Update bill_cycle's in_id
                BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleMonthList.get(k);
                billCycleEntity.setCashInMonthId(cashDetailId);
                saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());
            }

            //3.Find bill_cycle' by detail_id-over_Id(超額)
            searchBillCycleEntity = new BillCycleEntity();
            searchBillCycleEntity.setCashOutOverId(cashDetailId);
            List billCycleOverList =  getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);
            for(int k=0; k<billCycleOverList.size(); k++){
                //4.Update bill_cycle's in_id
                BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleOverList.get(k);
                billCycleEntity.setCashInOverId(cashDetailId);
                saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());
            }

            //4.update pcakge_mode's status(續約第一筆入帳時，如果狀態為0，要把狀態改為1)
            Integer cashType = cashDetailEntity.getCashType(); //計費類型 1.月租 2.月租超額 3.代印代計 4.加值型服務 5.儲值 6.預繳 7.扣抵
            if(1 == cashType){ //繳費類型是月租，才需要作生效。
                Integer packageId = cashDetailEntity.getPackageId();
                if(null != packageId){
                    PackageModeEntity packageModeEntity = (PackageModeEntity) getEntity(PackageModeEntity.class,packageId);
                    String status = packageModeEntity.getStatus();
                    if(null != status && status.equals("0")){
                        packageModeEntity.setStatus("1"); //0.未生效 1.生效 2.作廢
                    }
                    saveOrUpdateEntity(packageModeEntity, packageModeEntity.getPackageId());
                }
            }

            //5.如果是預繳的cashDetail，就要把繳的錢加到prepay_deduct_master裡
            if(6 == cashType){
                //註:6:預繳 7:扣抵 (註:入帳/取消入帳時 只處理預繳的資料(修改prepay_deduct_master、deduct_detail))
                // (註:扣抵的資料不在此處理。在超額計算時，會算出超額的錢&要扣抵超額的錢，並修改prepay_deduct_master、deduct_detail)
                PrepayDetailEntity seachPrepayDetailEntity = new PrepayDetailEntity();
                seachPrepayDetailEntity.setCashDetailId(cashDetailId);
                List prepayDetailList = getSearchEntity(PrepayDetailEntity.class, seachPrepayDetailEntity);
                if(null != prepayDetailList && prepayDetailList.size()>0){
                    PrepayDetailEntity prepayDetailEntity = (PrepayDetailEntity)prepayDetailList.get(0);
                    Integer money = prepayDetailEntity.getMoney();

                    //把繳的錢加到prepay_deduct_master裡
                    Integer prepayDeductMasterId = prepayDetailEntity.getPrepayDeductMasterId();
                    PrepayDeductMasterEntity prepayDeductMasterEntity = (PrepayDeductMasterEntity)getEntity(PrepayDeductMasterEntity.class, prepayDeductMasterId);
                    Integer amount = prepayDeductMasterEntity.getAmount();
                    amount = amount + money;
                    prepayDeductMasterEntity.setAmount(amount);
                    saveOrUpdateEntity(prepayDeductMasterEntity, prepayDeductMasterEntity.getPrepayDeductMasterId());

                    //增加歷史紀錄
                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(prepayDeductMasterId);
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(prepayDetailEntity.getCompanyId());
                    deductDetailEntity.setCalYm(cashDetailEntity.getCalYm());
                    deductDetailEntity.setDeductType(cashDetailEntity.getCashType()); //1.月租 2.超額(月租&級距型) 6.扣抵預繳
                    deductDetailEntity.setMoney(money);
                    saveEntity(deductDetailEntity);
                }
            }
        }

        return true;
    }

    //取消入帳
    public Integer transactionCancelIn(String strCashMasterId) throws Exception{
        int exeCnt = 0;
        Integer cashMasterId = Integer.parseInt(strCashMasterId);

        //如果該筆帳單有算過佣金，就不能「取消入帳」(思考:佣金拿的回來嗎?)
        //取消入帳就是:把入帳會作的資料還原
        CashMasterEntity cashMasterEntity = (CashMasterEntity) getEntity(CashMasterEntity.class, cashMasterId);
        CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
        searchCashDetailEntity.setCashMasterId(cashMasterId);
        List cashDetailList = getSearchEntity(CashDetailEntity.class, searchCashDetailEntity);

        //如果cash_master.status不為4(入帳)，則不作「取消入帳」
        Integer cashMasterStatus = cashMasterEntity.getStatus();
        if(null != cashMasterStatus && 4 > cashMasterStatus){
            return exeCnt;
        }

        //如果該筆帳單有算過佣金，就不能「取消入帳」(思考:佣金拿的回來嗎?)
        //檢查cash_detail，算過佣金的帳單，就不可「取消入帳」
        for(int i=0; i<cashDetailList.size(); i++){
            CashDetailEntity cashDetailEntity = (CashDetailEntity)cashDetailList.get(i);
            Integer commissionLogId = cashDetailEntity.getCommissionLogId();
            if(null != commissionLogId){
                return exeCnt;
            }
        }

        //1.update cash_master
        //is_inout_money_unmatch: cash_master的出帳和入帳金額不同時，為1
        // update cash_master的inAmount、inNote、IsInoutMoneyUnmatch、InDate、status
        cashMasterEntity.setIsInoutMoneyUnmatch(null);
        cashMasterEntity.setInAmount(new BigDecimal(0));
        cashMasterEntity.setInDate(null);
        cashMasterEntity.setStatus(1); //狀態 1.出帳 2.入帳 3.作廢
        updateEntityIncludeNULL(cashMasterEntity, cashMasterEntity.getCashMasterId());

        //2.find cash_detail
        // 3.find cash_detail's bill_cycle們，update cashInMonthId、CashInOverId
        // update pcakge_mode's status(續約第一筆入帳時，如果狀態為0，要把狀態改為1)
        for(int i=0; i<cashDetailList.size(); i++){
            CashDetailEntity cashDetailEntity = (CashDetailEntity)cashDetailList.get(i);
            Integer cashDetailId = cashDetailEntity.getCashDetailId();

            //月租的
            BillCycleEntity searchBillCycleMonthEntity = new BillCycleEntity();
            searchBillCycleMonthEntity.setCashInMonthId(cashDetailId);
            List billCycleMonthList = getSearchEntity(BillCycleEntity.class, searchBillCycleMonthEntity);
            for(int j=0; j<billCycleMonthList.size(); j++){
                BillCycleEntity billCycleEntity = (BillCycleEntity) billCycleMonthList.get(j);
                billCycleEntity.setCashInMonthId(null);
                updateEntityIncludeNULL(billCycleEntity, billCycleEntity.getBillId());
            }

            //超額的
            BillCycleEntity searchBillCycleOverEntity = new BillCycleEntity();
            searchBillCycleOverEntity.setCashInOverId(cashDetailId);
            List billCycleOverList = getSearchEntity(BillCycleEntity.class, searchBillCycleOverEntity);
            for(int j=0; j<billCycleOverList.size(); j++){
                BillCycleEntity billCycleEntity = (BillCycleEntity) billCycleOverList.get(j);
                billCycleEntity.setCashInOverId(null);
                updateEntityIncludeNULL(billCycleEntity, billCycleEntity.getBillId());
            }

            //4.update pcakge_mode's status(如果狀態為1，要把狀態改為0)
            Integer cashType = cashDetailEntity.getCashType();
            if(6 != cashType && 7 != cashType){ //cashType=6或7的話，不會有packageId。
                Integer packageId = cashDetailEntity.getPackageId();
                PackageModeEntity packageModeEntity = (PackageModeEntity) getEntity(PackageModeEntity.class,packageId);
                String status = packageModeEntity.getStatus();
                if(null != status && status.equals("1")){
                    packageModeEntity.setStatus("0"); //0.未生效 1.生效 2.作廢
                    saveOrUpdateEntity(packageModeEntity, packageModeEntity.getPackageId());
                }
            }


            //5.如果是預繳的cashDetail，就要把繳的錢扣到prepay_deduct_master裡
            //增加歷史紀錄(錢扣回去)
            if(6 == cashType){
                //註:6:預繳 7:扣抵 (註:入帳/取消入帳時 只處理預繳的資料(修改prepay_deduct_master、deduct_detail))
                // (註:扣抵的資料不在此處理。在超額計算時，會算出超額的錢&要扣抵超額的錢，並修改prepay_deduct_master、deduct_detail)
                PrepayDetailEntity seachPrepayDetailEntity = new PrepayDetailEntity();
                seachPrepayDetailEntity.setCashDetailId(cashDetailId);
                List prepayDetailList = getSearchEntity(PrepayDetailEntity.class, seachPrepayDetailEntity);
                if(null != prepayDetailList && prepayDetailList.size()>0){
                    PrepayDetailEntity prepayDetailEntity = (PrepayDetailEntity)prepayDetailList.get(0);
                    Integer money = prepayDetailEntity.getMoney();

                    //把繳的錢加到prepay_deduct_master裡
                    Integer prepayDeductMasterId = prepayDetailEntity.getPrepayDeductMasterId();
                    PrepayDeductMasterEntity prepayDeductMasterEntity = (PrepayDeductMasterEntity)getEntity(PrepayDeductMasterEntity.class, prepayDeductMasterId);
                    Integer amount = prepayDeductMasterEntity.getAmount();
                    amount = amount - money;
                    prepayDeductMasterEntity.setAmount(amount);
                    saveOrUpdateEntity(prepayDeductMasterEntity, prepayDeductMasterEntity.getPrepayDeductMasterId());

                    //增加歷史紀錄
                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(prepayDeductMasterId);
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(prepayDetailEntity.getCompanyId());
                    deductDetailEntity.setCalYm(cashDetailEntity.getCalYm());
                    deductDetailEntity.setDeductType(cashDetailEntity.getCashType()); //1.月租 2.超額(月租&級距型) 6.扣抵預繳
                    deductDetailEntity.setMoney(-money);
                    saveEntity(deductDetailEntity);
                }
            }
        }

        exeCnt++;
        return exeCnt;
    }

    public List<CashDetailVO> getCashDetailListByMasterId(Integer cashMasterId) throws Exception{
        CashDetailEntity searchcashDetailEntity = new CashDetailEntity();
        searchcashDetailEntity.setCashMasterId(cashMasterId);
        List cashDetailList = getSearchEntity(CashDetailEntity.class, searchcashDetailEntity);

        //cashDetailList依cashDetailId排序
        Collections.sort(cashDetailList,
                new Comparator<CashDetailEntity>() {
                    public int compare(CashDetailEntity o1, CashDetailEntity o2) {
                        return o1.getCashDetailId().compareTo(o2.getCashDetailId());
                    }
        });

        //CashMaster的status
        CashMasterEntity masterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
        Integer masterStatus = masterEntity.getStatus(); //1.生效 2.作廢 3.出帳 4.入帳 5.佣金

        List<CashDetailVO> list = new ArrayList();
        for(int i=0; i<cashDetailList.size(); i++){
            CashDetailEntity cashDetailEntity = (CashDetailEntity)cashDetailList.get(i);
            CashDetailVO cashDetailVO = new CashDetailVO();
            BeanUtils.copyProperties(cashDetailVO, cashDetailEntity);

            //公司名稱
            Integer companyId = cashDetailEntity.getCompanyId();
            CompanyEntity companyEntity = (CompanyEntity)getEntity(CompanyEntity.class, companyId);
            cashDetailVO.setCompanyName(companyEntity.getName());

            //CashMaster的status  1.生效 2.作廢 3.出帳 4.入帳 5.佣金
            cashDetailVO.setMasterStatus(masterStatus);

            //cashDetail狀態
            Integer status = cashDetailEntity.getStatus();
            switch(status){
                case 1:
                    cashDetailVO.setDetailStatus("生效");
                    break;
                case 2:
                    cashDetailVO.setDetailStatus("作廢");
                    break;
                default:
                    cashDetailVO.setDetailStatus("未知");
                    break;
            }

            //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值
            Integer cashType = cashDetailEntity.getCashType();
            switch (cashType) {
                case 1:
                    cashDetailVO.setCashTypeName("月租預繳");
                    break;
                case 2:
                    cashDetailVO.setCashTypeName("超額");
                    break;
                case 3:
                    cashDetailVO.setCashTypeName("代印代計");
                    break;
                case 4:
                    cashDetailVO.setCashTypeName("加值型服務");
                    break;
                case 5:
                    cashDetailVO.setCashTypeName("扣抵預繳");
                    break;
            }


            //方案名稱
            Integer packageId = cashDetailEntity.getPackageId();
            if(null != packageId){
                PackageModeEntity packageModeEntity = (PackageModeEntity)getEntity(PackageModeEntity.class, packageId);
                Integer packageType = packageModeEntity.getPackageType(); //月租(1) 級距(2)
                Integer chargeId = packageModeEntity.getChargeId();
                String packageName = null;
                if(1 == packageType){
                    //月租
                    ChargeModeCycleEntity chargeModeCycleEntity = (ChargeModeCycleEntity)getEntity(ChargeModeCycleEntity.class, chargeId);
                    packageName = chargeModeCycleEntity.getPackageName();
                } else if(2 == packageType){
                    //級距
                    ChargeModeGradeEntity chargeModeGradeEntity = (ChargeModeGradeEntity)getEntity(ChargeModeGradeEntity.class, chargeId);
                    packageName = chargeModeGradeEntity.getPackageName();
                }
                cashDetailVO.setPackageName(packageName);
                //計費區間
                Integer addId = packageModeEntity.getAdditionId();
                ChargeModeCycleAddEntity chargeCycleAdd = (ChargeModeCycleAddEntity)getEntity(ChargeModeCycleAddEntity.class, addId);
                Date realStarte = chargeCycleAdd.getRealStartDate();
                Date realEnd = chargeCycleAdd.getRealEndDate();
                String period = "(" + parseDate(realStarte)+"  ~ <br> " + parseDate(realEnd) + ")";
                cashDetailVO.setPeriod(period);

                cashDetailVO.setTaxTypeName("營業稅");
            } else { //某些情況下，cash_detail的package_id欄位不會有值

            }

            list.add(cashDetailVO);
        }

        return list;
    }

    /**
     * 找出該筆cashDetail的超額對應到的billCycle List
     * @param cashDetailId
     * @return
     * @throws Exception
     */
    public List<BillCycleEntity> getOverListByDetailId(Integer cashDetailId)throws Exception{
        BillCycleEntity searchBillCycle = new BillCycleEntity();
        searchBillCycle.setCashOutOverId(cashDetailId);
        List billCycleList =  getSearchEntity(BillCycleEntity.class, searchBillCycle);

        //billCycleList依bill_id排序
        Collections.sort(billCycleList,
                new Comparator<BillCycleEntity>() {
                    public int compare(BillCycleEntity o1, BillCycleEntity o2) {
                        return o1.getBillId().compareTo(o2.getBillId());
                    }
        });

        //把null的number換成0
        for(int i=0; i<billCycleList.size(); i++){
            BillCycleEntity entity = (BillCycleEntity)billCycleList.get(i);
            entity.setCnt(nullConstants.getNotNull(entity.getCnt()));
            entity.setCntGift(nullConstants.getNotNull(entity.getCntGift()));
            entity.setCntOver(nullConstants.getNotNull(entity.getCntOver()));
            entity.setPriceOver(nullConstants.getNotNull(entity.getPriceOver()));
            entity.setPayOver(nullConstants.getNotNull(entity.getPayOver()));
        }

        return billCycleList;
    }


    public CashMasterVO getCashMasterByMasterId(Integer cashMasterId) throws Exception{
        CashMasterEntity cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
        CashMasterVO cashMasterVO = new CashMasterVO();
        BeanUtils.copyProperties(cashMasterVO, cashMasterEntity);


        Map map = getCreatorAndModifier(cashMasterVO.getCreatorId(), cashMasterVO.getModifierId());
        cashMasterVO.setCreator((String) map.get("creator"));
        cashMasterVO.setModifier((String) map.get("modifier"));

        return cashMasterVO;
    }

    public String parseDate(Date date){
        SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy/MM/dd");
        return parserSDF.format(date);
    }

    /**
     * 修改帳單明細的差額，並重計detail和master
     * @param cashDetailId
     * @param diffPrice
     * @param diffPriceNote
     * @return
     * @throws Exception
     */
    public boolean transactionUpdateCashDetail(Integer cashDetailId, Double diffPrice, String diffPriceNote) throws Exception{
        //找到該筆cashDetail
        CashDetailEntity cashDetailEntity = (CashDetailEntity)getEntity(CashDetailEntity.class, cashDetailId);
        //更新該筆cashDetail的差價和差備備註(需重算該筆cashdetail的錢
        BigDecimal diffPrice_ = new BigDecimal(diffPrice);
        cashDetailEntity.setDiffPrice(diffPrice_);

        BigDecimal orgPrice = cashDetailEntity.getOrgPrice();
        cashDetailEntity = calPriceTax(cashDetailEntity, orgPrice, diffPrice_, diffPriceNote);

        saveOrUpdateEntity(cashDetailEntity, cashDetailEntity.getCashDetailId());

        //重新加總cashMaster的值
        Integer cashMasterId = cashDetailEntity.getCashMasterId();
        CashMasterEntity cashMasterEntity = sumCashMaster(cashMasterId);
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());

        return true;
    }

    public CashDetailEntity calPriceTax(CashDetailEntity cashDetailEntity, BigDecimal noTaxPrice, BigDecimal diffPrice, String diffPriceNote){
        cashDetailEntity.setOrgPrice(noTaxPrice); //原價

        cashDetailEntity.setDiffPrice(diffPrice); //差價

        //差價備註
        if(null != diffPriceNote){
            cashDetailEntity.setDiffPriceNote(diffPriceNote);
        }
        BigDecimal NoTaxInclusivePrice = noTaxPrice.subtract(diffPrice);
        cashDetailEntity.setNoTaxInclusivePrice(NoTaxInclusivePrice.setScale(2, BigDecimal.ROUND_HALF_UP)); //未稅價
        cashDetailEntity.setTaxType("1"); //稅別
        cashDetailEntity.setTaxRate((5 / 100f)); //稅率

        Double taxPrice = (Double)NoTaxInclusivePrice.doubleValue() * (Double)(5d/100d); //稅額
        Double taxInclusivePrice = NoTaxInclusivePrice.doubleValue() + taxPrice; //含稅價
        cashDetailEntity.setTaxPrice(new BigDecimal(taxPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
        cashDetailEntity.setTaxInclusivePrice(new BigDecimal(taxInclusivePrice).setScale(2, BigDecimal.ROUND_HALF_UP));

        return cashDetailEntity;
    }

    /**
     * cash_master是否有這個計算月份的資料，有的話，則回傳master_id，否則建一個新的master，傳回master_id
     * @param outYm //出帳年月
     * @param companyId //用戶名稱
     * @return
     * @throws Exception
     */
    public CashMasterEntity isHaveCashMaster(String outYm, Integer companyId, Integer modifierId) throws Exception {
        String sql = " select * from cash_master where out_ym = ? and company_id= ? ";
        List parameterList = new ArrayList();
        parameterList.add(outYm);
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);
        List list = query.list();
        CashMasterEntity cashMasterEntity = null;
        if(list != null && list.size() > 0){ //已有master_id了，取得master_id
            Map map = (Map)list.get(0);
            Integer cashMasterId = (Integer)map.get("cash_master_id");
            cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);

            //出帳後，不可再更改cash_master
            if(!isCashMasterOut(cashMasterEntity)){
                logger.info("出帳後，不可再更改cash_master。 cashMaster.BankYm="+cashMasterEntity.getBankYm()+", cashMaster.OutYm="+cashMasterEntity.getOutYm()+", cashMaster.companyId="+cashMasterEntity.getCompanyId());
                throw new Exception();
            }
        }else{//還沒有master_id，新增一個cash_master
            CashMasterEntity cashMaster = new CashMasterEntity();
            cashMaster.setOutYm(outYm);
            cashMaster.setCompanyId(companyId);
            cashMaster.setModifierId(modifierId);
            cashMaster.setModifyDate(Timestamp.from(new Date().toInstant()));
            cashMaster.setCreatorId(modifierId);
            cashMaster.setCreateDate(Timestamp.from(new Date().toInstant()));

            //欲轉換的日期字串
            String dateString = outYm + "01";
            //設定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //進行轉換
            Date date = sdf.parse(dateString);

            //出帳上海銀行的帳單年月=出帳年月的下個月
            Calendar bankCal = Calendar.getInstance();
            bankCal.setTime(date);
            bankCal.add(Calendar.MONTH, 1);
            cashMaster.setBankYm(timeUtils.getYearMonth2(bankCal.getTime())); //出帳上海銀行的帳單年月
            cashMaster.setStatus(1); //1.生效 2.作廢

            saveEntity(cashMaster);
            cashMasterEntity = cashMaster;
        }
        return cashMasterEntity;
    }

    //出帳後，不可再更改cash_master
    public boolean isCashMasterOut(CashMasterEntity master) throws Exception{
        int status = master.getStatus(); //1.生效 2.作廢 3.出帳 4.入帳 5.佣金
        if(status >= 3){ //出帳後，不可再更改cash_master
            return false;
        }
        return true;
    }

    //尋找要匯入上海銀行excel的資料-批次(by 年月)
    public List getCashMasterDetail(String outYm) throws Exception{
        return getCashMasterDetailList(outYm, null);
    }

    //尋找要匯入上海銀行excel的資料-多筆
    public List getCashMasterDetail(String outYm, String destJson) throws Exception{

        List<CashMasterEntity> selectOutList = new ArrayList<CashMasterEntity>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(destJson, collectionType);

        for(CashMasterBean masterBean:cashMasterList){
            Integer masterId = masterBean.getCashMasterId();
            CashMasterEntity masterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, masterId);
            selectOutList.add(masterEntity);
        }

        return getCashMasterDetailList(outYm, selectOutList);
    }

    //Todo:將會移至CashServiceImp，轉為使用CashMasterIdList做為傳入參數，並且返回設定好的bean，如果是動態，就返回map。
    public List getCashMasterDetailList(String outYm, List selectOutList) throws Exception{
        List list = new ArrayList();

        CashMasterEntity searchMaster = new CashMasterEntity();
        searchMaster.setOutYm(outYm);
        List masterList =  getSearchEntity(CashMasterEntity.class, searchMaster);

        //masterList依master_id排序
        Collections.sort(masterList,
                new Comparator<CashMasterEntity>() {
                    public int compare(CashMasterEntity o1, CashMasterEntity o2) {
                        return o2.getCashMasterId().compareTo(o1.getCashMasterId());
                    }
                });

        //移除沒有選到的筆數
        if(null != selectOutList && selectOutList.size()>0){
            for(int i=(masterList.size()-1); i >-1; i--){
                CashMasterEntity master = (CashMasterEntity)masterList.get(i);
                if(!selectOutList.contains(master)){
                    masterList.remove(master);
                }
            }
        }

        for(int i=0; i<masterList.size(); i++){
            CashMasterBean masterBean = new CashMasterBean();
            CashMasterEntity master = (CashMasterEntity)masterList.get(i);
            Integer masterId = master.getCashMasterId();
            BeanUtils.copyProperties(masterBean, master);

            if("1".equals(masterBean.getIsFirst())){ //用戶第一次綁合約(首次)，不匯出excel
                continue;
            }

            //如果用戶該月要繳的錢為0元，就不匯出到excel了。
            //masterBean.getTaxInclusiveAmout 有可能是null，不能直接相比
            //PK 2017/09/26
            if(masterBean != null && masterBean.getTaxInclusiveAmount() != null && (masterBean.getTaxInclusiveAmount().equals(new BigDecimal(0) ) )){
                continue;
            }
            //undo 用友x匯出上銀excel會有error

            //此master的companyId的companyName和businessNo和codeName是什麼?
            Integer cpId = masterBean.getCompanyId();
            CompanyEntity cpEntity = (CompanyEntity)getEntity(CompanyEntity.class, cpId);
            String companyName = cpEntity.getName();
            String businessNo = cpEntity.getBusinessNo();
            String codeName = cpEntity.getCodeName();
            masterBean.setCompanyName(companyName);
            masterBean.setBusinessNo(businessNo);
            masterBean.setCodeName(codeName);

            //此master有哪些detail
            CashDetailEntity searchDetail = new CashDetailEntity();
            searchDetail.setCashMasterId(masterId);
            List detailList =  getSearchEntity(CashDetailEntity.class, searchDetail);
            List detailofMasterList = new ArrayList();
            for(int k=0; k<detailList.size(); k++){
                CashDetailEntity detail = (CashDetailEntity)detailList.get(k);
                if(2 == detail.getStatus()){ //0.未生效 1.生效 2.作廢 3.出帳 4.入帳 5.佣金
                    continue;
                }
                if("1".equals(detail.getIsFirst())){ //用戶第一次綁合約(首次)，不匯出excel
                    continue;
                }
                CashDetailBean detailBean = new CashDetailBean();
                BeanUtils.copyProperties(detailBean, detail);

                //找出package name
                Integer packageId = detail.getPackageId();
                PackageModeEntity packageModeEntity = (PackageModeEntity)getEntity(PackageModeEntity.class, packageId);
                Integer chargeId = packageModeEntity.getChargeId();
                Integer pacakgeType = packageModeEntity.getPackageType(); //1.月租 2.級距
                detailBean.setChargeId(chargeId);
                if(1 == pacakgeType){ //月租型
                    ChargeModeCycleEntity cycleEntity = (ChargeModeCycleEntity)getEntity(ChargeModeCycleEntity.class, chargeId);
                    detailBean.setPackageName(cycleEntity.getPackageName());
                } else if(2 == pacakgeType){ //級距型
                    ChargeModeGradeEntity gradeEntity = (ChargeModeGradeEntity)getEntity(ChargeModeGradeEntity.class, chargeId);
                    detailBean.setPackageName(gradeEntity.getPackageName());
                }

                detailofMasterList.add(detailBean);
            }

            masterBean.setCashDetailList(detailofMasterList);
            list.add(masterBean);
        }
        return list;
    }

    public boolean transactionCancelOver(Integer cashDetailId) throws Exception{ //取消計算
        //1.找出bill_cycle的over_id有cash_detail_id的資料
        BillCycleEntity searchBillCycleEntity = new BillCycleEntity();
        searchBillCycleEntity.setCashOutOverId(cashDetailId);
        List billCycleList = getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);

        //2.把這些bill_cycle計算超額的值清掉(包括over_id)
        for(int i=0; i<billCycleList.size(); i++){
            BillCycleEntity billCycleEntity = (BillCycleEntity)billCycleList.get(i);
            billCycleEntity.setCnt(null);
            billCycleEntity.setCntGift(null);
            billCycleEntity.setCntOver(null);
            billCycleEntity.setPriceOver(null);
            billCycleEntity.setPayOver(null);
            billCycleEntity.setCashOutOverId(null);
            billCycleEntity.setCashInOverId(null);
            updateEntityIncludeNULL(billCycleEntity, billCycleEntity.getBillId());

        }

        //3.把這筆cash_detail刪掉
        CashDetailEntity cashDetailEntity = (CashDetailEntity)getEntity(CashDetailEntity.class, cashDetailId);
        //BigDecimal noTaxInclusivePrice = cashDetailEntity.getNoTaxInclusivePrice();
        //undo 2017/12/1 robinson edit
        //假如有公司預佣金部分須把code加回去
        Integer companyId = cashDetailEntity.getCompanyId();
        String calYM = cashDetailEntity.getCalYm();
        deleteEntity(cashDetailEntity);

        //4.把這筆cash_detail對應的扣抵的資料也刪掉
        CashDetailEntity searchDeductCashDetailEntity = new CashDetailEntity();
        //不可加金額條件，因為該筆帳單有可能用完最後的預用金，所以超額不會全扣抵
        //searchDeductCashDetailEntity.setNoTaxInclusivePrice(new BigDecimal(0).subtract(noTaxInclusivePrice));
        searchDeductCashDetailEntity.setCompanyId(companyId);
        searchDeductCashDetailEntity.setCalYm(calYM);
        searchDeductCashDetailEntity.setCashType(7);
        List<CashDetailEntity> deductCashDetailList= getSearchEntity(CashDetailEntity.class, searchDeductCashDetailEntity);
        if(null == deductCashDetailList || deductCashDetailList.size()==0){
            throw new Exception();
        }
        CashDetailEntity deductCashDetailEntity = deductCashDetailList.get(0);
        Integer deductMoney = 0-deductCashDetailEntity.getNoTaxInclusivePrice().intValue();
        deleteEntity(deductCashDetailEntity);

        //5.在prepay_deduct_master把錢加回去
        PrepayDeductMasterEntity searchPrepayDeductMaster = new PrepayDeductMasterEntity();
        searchPrepayDeductMaster.setCompanyId(companyId);
        List<PrepayDeductMasterEntity> prepayDeductMasterList= getSearchEntity(PrepayDeductMasterEntity.class, searchPrepayDeductMaster);
        if(null == prepayDeductMasterList || prepayDeductMasterList.size()==0){
            throw new Exception();
        }
        PrepayDeductMasterEntity prepayDeductMasterEntity = prepayDeductMasterList.get(0);
        Integer prepayDeductMasterId = prepayDeductMasterEntity.getPrepayDeductMasterId();
        Integer amount = prepayDeductMasterEntity.getAmount();
        amount = amount + deductMoney;
        prepayDeductMasterEntity.setAmount(amount);
        saveOrUpdateEntity(prepayDeductMasterEntity, prepayDeductMasterEntity.getPrepayDeductMasterId());

        //6.在deduct_detail增加一筆還原(加回去)的紀錄
        DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
        deductDetailEntity.setPrepayDeductMasterId(prepayDeductMasterId);
        deductDetailEntity.setCashDetailId(0);
        deductDetailEntity.setCompanyId(companyId);
        deductDetailEntity.setCalYm(calYM);
        deductDetailEntity.setDeductType(6);
        deductDetailEntity.setMoney(deductMoney);
        saveEntity(deductDetailEntity);

        return false;
    }

    //批次取消出帳
    public Integer transactionCancelOutYM(String outYM) throws Exception {
        //1.找出該outYm裡 outDate非空值的cash_master_id
        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
        searchCashMasterEntity.setOutYm(outYM);
        List<CashMasterEntity> cashMasterList = getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);

        int exeCnt = 0;
        //2.把outDate清空
        for(CashMasterEntity master: cashMasterList){
            boolean isCancelOut = cancelOut(master);
            if(isCancelOut){ //是否已取消出帳
                exeCnt++; //已取消出帳
            }
        }
        return exeCnt;
    }

    //多筆取消出帳
    public Integer transactionCancelOut(String masterIdAry) throws Exception{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(masterIdAry, collectionType);

        int exeCnt = 0;
        //找出所有要計算的MasterId
        for(int i=0; i<cashMasterList.size(); i++) {
            CashMasterBean bean = (CashMasterBean)cashMasterList.get(i);

            Integer cashMasterId = (null == bean.getCashMasterId())?0:bean.getCashMasterId();
            CashMasterEntity  cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
            boolean isCancelOut = cancelOut(cashMasterEntity);
            if(isCancelOut){ //是否已取消出帳
                exeCnt++; //已取消出帳
            }
        }
        return exeCnt;
    }

    public boolean cancelOut(CashMasterEntity cashMasterEntity) throws Exception{
        //已入帳後，不能在取消出帳
        if(cashMasterEntity.getStatus()>3){
            return false;
        }

        cashMasterEntity.setOutDate(null);
        cashMasterEntity.setStatus(1); //1.生效 3.出帳 4.入帳 5.佣金
        updateEntityIncludeNULL(cashMasterEntity, cashMasterEntity.getCashMasterId());

        return true;
    }

    //批次-寄帳單明細表
    public Integer transactionSendBillMailYM(String outYM) throws Exception{
        //1.找出該outYm裡 outDate非空值的cash_master_id
        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
        searchCashMasterEntity.setOutYm(outYM);
        List<CashMasterEntity> cashMasterList = getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);

        int exeCnt = 0;
        //2.把outDate清空
        for(CashMasterEntity master: cashMasterList){
            boolean isSend = sendMail(master);
            if(isSend){ //是否寄出
                exeCnt++;//寄出
                updateEmailDate(master); //更新cash_master的email_sent_date(寄送email日期)
            }
        }
        return exeCnt;
    }

    //多筆-寄帳單明細表
    public Integer transactionSendBillMail(String masterIdAry) throws Exception{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(masterIdAry, collectionType);

        int exeCnt = 0;
        //找出所有要計算的MasterId
        for(int i=0; i<cashMasterList.size(); i++) {
            CashMasterBean bean = (CashMasterBean)cashMasterList.get(i);
            Integer cashMasterId = (null == bean.getCashMasterId())?0:bean.getCashMasterId();
            CashMasterEntity  cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
            boolean isSend = sendMail(cashMasterEntity);
            if(isSend){ //是否寄出
                exeCnt++;//寄出
                updateEmailDate(cashMasterEntity); //更新cash_master的email_sent_date(寄送email日期)
            }
        }
        return exeCnt;
    }

    /**
     * 寄帳單明細
     * 需先出帳，才可以寄帳單。
     */

    public boolean sendMail(CashMasterEntity  cashMasterEntity) throws Exception{
        return sendMail(cashMasterEntity, null);
    }

    //寄帳單明細
    public boolean sendMail(CashMasterEntity  cashMasterEntity, String reEmail) throws Exception{
        //0.未生效 1.生效 2.作廢(master不會有此狀態) 3.出帳 4.入帳 5.佣金 : 出帳之後才可以寄email
        // &&用戶第一次綁合約(首次)的帳單，不寄email
        if(3 <= cashMasterEntity.getStatus() &&  (!"1".equals(cashMasterEntity.getIsFirst()))){
            //需先出帳，才可以寄帳單。

            //找出cash_master裡cash_detail的資訊(status不為2(作廢))
            CashDetailEntity searchCashDetail = new CashDetailEntity();
            searchCashDetail.setCashMasterId(cashMasterEntity.getCashMasterId());
            List cashDetailList = getSearchEntity(CashDetailEntity.class, searchCashDetail);
            //如果是cash_detail為空，就不要寄email出來了。
            if(null == cashDetailList || cashDetailList.size() == 0){
                return false;
            } else{
                //準備寄email.....

                Integer cpId = cashMasterEntity.getCompanyId();
                CompanyEntity cpEntity = (CompanyEntity)getEntity(CompanyEntity.class, cpId);
                String email = cpEntity.getEmail1();
                String cpName = cpEntity.getName();
                String bankYm = cashMasterEntity.getBankYm();

                String subject="【帳單明細通知】關網電子發票服務費用，請詳內文。";
                StringBuffer content = new StringBuffer();
                content.append("請注意：本信件是由「關網資訊雲端電子發票系統」自動產生與發送，請勿直接回覆。")
                        .append("\n\n敬愛的 <font color=\"#FF0000\">" + cpName + "</font>您好：")
                        .append("\n\n以下資訊為貴公司"+ bankYm +"月份繳款帳單之明細，請查收。")
                        .append("\n<font color=\"#FF0000\">★近期將由<font face=\"標楷體\"><b><u>上海銀行</font></b></u>寄發繳款通知予貴公司，請留意email信箱，並惠予繳費。★</font>\n")
                        .append("\n<font color=\"#FF0000\">★請於<font face=\"標楷體\"><b>編號處請輸入公司統編</font></b>即可進行費用繳納事宜。★</font>\n")
                        .append("\n<font color=\"FF0000\">★【詳細操作，請詳附件】★</font>\n");

                content.append(" <table  style=\"border:2px black solid; border-collapse: collapse\" cellpadding=\"5\" border='2' > ")
                        .append(" <tr>")
                        .append(" <th>繳費類型</th><th>方案名稱</th><th>發票額度</th><th>含稅費用</th>")
                        .append(" </tr>");

                int overIndex = -1;
                int hasContext = 0; //如果一封email裡只有作廢的內容，那麼這封email不會被寄出去

                for(int i=0; i<cashDetailList.size(); i++){
                    CashDetailEntity cashDetailEntity = (CashDetailEntity)cashDetailList.get(i);
                    Integer intCashType = cashDetailEntity.getCashType();
                    if(2 == cashDetailEntity.getStatus()){ //作廢的不理
                        continue;
                    }
                    if(2 == intCashType){ //超額
                        overIndex++;
                    }

                    //繳費類型
                    String cashType = parseCashType(intCashType); //1.月租2.月租超額3.代印代計4.加值型服務5.儲值

                    //含稅費用
                    BigDecimal taxInclusivePrice = cashDetailEntity.getTaxInclusivePrice().setScale(2, BigDecimal.ROUND_HALF_UP);

                    if(7 == intCashType || 6 == intCashType){ //7.扣抵費用 6.扣抵預繳費用
                        content.append(" <tr>")
                                .append(" <td>"+cashType+"</td><td></td><td></td><td>" + taxInclusivePrice + "</td>")
                                .append(" </tr>");
                    }else{
                        //方案名稱
                        Integer packageId= cashDetailEntity.getPackageId();
                        PackageModeEntity packageModeEntity = (PackageModeEntity)getEntity(PackageModeEntity.class, packageId);
                        Integer chargeId = packageModeEntity.getChargeId();
                        Integer packageType = packageModeEntity.getPackageType();

                        Integer baseQuantity = 0; //發票額度
                        String PakcageName = ""; //專案名稱
                        if(1 == packageType){
                            //月租型
                            ChargeModeCycleEntity chargeModeCycleEntity = (ChargeModeCycleEntity)getEntity(ChargeModeCycleEntity.class, chargeId);
                            PakcageName = chargeModeCycleEntity.getPackageName();
                            baseQuantity = chargeModeCycleEntity.getBaseQuantity();
                        } else if( 2 == packageType){
                            //級距型
                            ChargeModeGradeEntity chargeModeGradeEntity = (ChargeModeGradeEntity)getEntity(ChargeModeGradeEntity.class, chargeId);
                            PakcageName = chargeModeGradeEntity.getPackageName();
                            baseQuantity = chargeModeGradeEntity.getBaseQuantity();
                        }

                        if(1 == cashDetailEntity.getCashType()){ //月租的方案名稱要有該合約的時間起迄
                            //計費區間
                            Integer addId = packageModeEntity.getAdditionId();
                            ChargeModeCycleAddEntity chargeCycleAdd = (ChargeModeCycleAddEntity)getEntity(ChargeModeCycleAddEntity.class, addId);
                            Date realStarte = chargeCycleAdd.getRealStartDate();
                            Date realEnd = chargeCycleAdd.getRealEndDate();
                            String period = "(" + parseDate(realStarte)+"  ~  " + parseDate(realEnd) + ")";
                            PakcageName += "<br>" +  period;
                        }
                        content.append(" <tr>")
                                .append(" <td>"+cashType+"</td><td>" + PakcageName + "</td><td>" + baseQuantity + "</td><td>" + taxInclusivePrice + "</td>")
                                .append(" </tr>");
                    }

                    hasContext++;
                }

                if(hasContext==0){ //email裡只有作廢的內容，那麼這封email不會被寄出去
                    return false;
                }

                //合計
                BigDecimal taxInclusiveAmount = cashMasterEntity.getTaxInclusiveAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
                content.append(" <tr>")
                        .append(" <td colspan=2></td><td>合計</td><td>" + taxInclusiveAmount + "</td>")
                        .append(" </tr>");
                content.append(" </table>");

                if(overIndex > -1){
                    content.append("\n\n超額明細如下，請參考。");
                    content.append(" <table  style=\"border:2px black solid; border-collapse: collapse\" cellpadding=\"5\" border='2' > ");

                    boolean isGrade = false; //是否需要顯示級距表
                    Integer chargeIdforGrade = 0; //級距表id

                    for(int i=0; i<cashDetailList.size(); i++){
                        CashDetailEntity cashDetailEntity = (CashDetailEntity)cashDetailList.get(i);

                        if(i == 0 && 1 == cashDetailEntity.getBillType()){ //帳單類型 1.月租
                            content.append(" <tr>")
                                    .append(" <th>計算月份</th><th>發票額度</th><th>使用張數</th><th>超出額度</th><th>贈送張數</th><th>單張超額費用</th>")
                                    .append(" </tr>");
                        } else if(i == 0 && 2 == cashDetailEntity.getBillType()){ //帳單類型 2.級距
                            content.append(" <tr>")
                                    .append(" <th>計算月份</th><th>發票額度</th><th>使用張數</th><th>贈送張數</th>")
                                    .append(" </tr>");
                        } else if(i == 0 && 3 == cashDetailEntity.getBillType()){ //帳單類型 3.預繳
                            continue;
                        }

                        if(2 == cashDetailEntity.getStatus()){ //作廢的不理
                            continue;
                        }
                        if(2 != cashDetailEntity.getCashType()){ //非超額的不理
                            continue;
                        }

                        BillCycleEntity searchBillCycle = new BillCycleEntity();
                        searchBillCycle.setCashOutOverId(cashDetailEntity.getCashDetailId());
                        List<BillCycleEntity> billCycleList = getSearchEntity(BillCycleEntity.class, searchBillCycle, "yearMonth");
                        for(BillCycleEntity billCycle:billCycleList){
                            String yearMonth = billCycle.getYearMonth(); //計算月份
                            Integer cntLimit = (null == billCycle.getCntLimit())?0:billCycle.getCntLimit(); //發票額度
                            Integer cnt = (null == billCycle.getCnt())?0:billCycle.getCnt(); //使用張數
                            Integer cntOver = (null == billCycle.getCntOver())?0:billCycle.getCntOver(); //超出額度
                            Integer cntGift = (null == billCycle.getCntGift())?0:billCycle.getCntGift(); //贈送張數
                            BigDecimal singlePrice = (null == billCycle.getSinglePrice())?new BigDecimal(0):billCycle.getSinglePrice(); //超過使用量後單一張發票收費價格

                            if(1 == cashDetailEntity.getBillType()) { //帳單類型 1.月租
                                content.append(" <tr>")
                                        .append(" <td>"+yearMonth+"</td><td>" + cntLimit + "</td><td>" + cnt + "</td><td>" + cntOver + "</td><td>" + cntGift + "</td>")
                                        .append(" <td>"+singlePrice+"</td>")
                                        .append(" </tr>");
                            } else if(2 == cashDetailEntity.getBillType()) { //帳單類型 2.級距
                                content.append(" <tr>")
                                        .append(" <td>"+yearMonth+"</td><td>" + cntLimit + "</td><td>" + cnt + "</td><td>" + cntGift + "</td>")
                                        .append(" </tr>");
                                isGrade = true;
                                Integer packageId = cashDetailEntity.getPackageId();
                                PackageModeEntity packageModeEntity = (PackageModeEntity)getEntity(PackageModeEntity.class, packageId);
                                chargeIdforGrade = packageModeEntity.getChargeId();
                            }
                        }
                    }
                    content.append(" </table>");

                    if(isGrade){
                        //有級距型的超額，所以需呈現級距表
                        ChargeModeGradeEntity chargeModeGradeEntity = (ChargeModeGradeEntity)getEntity(ChargeModeGradeEntity.class, chargeIdforGrade);
                        String hasGrade = chargeModeGradeEntity.getHasGrade();
                        content.append("\n\n級距表如下，請參考。");
                        content.append(" <table  style=\"border:2px black solid; border-collapse: collapse\" cellpadding=\"5\" border='2' > ");
                        if(null != hasGrade && "Y".equals(hasGrade)){
                            content.append(" <tr>")
                                    .append(" <th>張數起</th><th>張數迄</th><th>價格(未稅)</th>")
                                    .append(" </tr>");
                            GradeEntity searchGradeEntity = new GradeEntity();
                            searchGradeEntity.setChargeId(chargeIdforGrade);
                            List gradeList = getSearchEntity(GradeEntity.class, searchGradeEntity);
                            for(int gradeInx = 0; gradeInx<gradeList.size(); gradeInx++){
                                GradeEntity gradeEntity = (GradeEntity)gradeList.get(gradeInx);
                                content.append(" <tr>")
                                        .append(" <td>" + gradeEntity.getCntStart() + "</td><td>" + gradeEntity.getCntEnd() + "</td><td>" + gradeEntity.getPrice() + "</td>")
                                        .append(" </tr>");
                            }
                        } else {
                            content.append(" <tr>")
                                    .append(" <th>級距區間張數</th><th>級距區間費用(未稅)</th>")
                                    .append(" </tr>");
                            content.append(" <tr>")
                                    .append(" <td>").append(chargeModeGradeEntity.getGradeCnt()).append("</td>")
                                    .append(" <td>").append(chargeModeGradeEntity.getGradePrice()).append("</td>")
                                    .append(" </tr>");
                        }
                        content.append(" </table>");
                    }
                }

                content.append("\n\n如對帳單資訊有疑問，請儘速致電關網資訊詢問，謝謝！")
                        .append("\n<a title=\"關網資訊股份有限公司\" href=\"http://www.gateweb.com.tw\">www.gateweb.com.tw</a>")
                        .append("\n客服時間：一般上班日09:30～12:00 , 13:30～18:00")
                        .append("\n客服專線：02-77183770");

//            SendEmailFileUtils.sendEmail(new String[]{email}, subject, content.toString());
                if(null != reEmail){
                    String[] reEmailList = new String[]{reEmail};
                    String path = this.getClass().getResource("/").getPath()+"/tempFile"+"/scsbPayBillTutorial.pdf";
                    SendEmailFileUtils.sendEmail(reEmailList, subject, content.toString(),path,"scsbPayBillTutorial.pdf");
                }else{
                    SendEmailFileUtils.sendEmail(reEmail, cpName , subject, content.toString());
                }
            }
            return true;
        }
        return false;
    }


    public boolean updateEmailDate(CashMasterEntity master) throws Exception{
        //更新cash_master的email_sent_date(寄送email日期)
        Integer cashMasterId = master.getCashMasterId();
        CashMasterEntity cashMasterEntity = (CashMasterEntity)getEntity(CashMasterEntity.class, cashMasterId);
        cashMasterEntity.setEmailSentDate(new java.sql.Timestamp((new Date()).getTime()));
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
        return true;
    }

    public String parseCashType(Integer cashType) throws Exception{
        //1.月租2.月租超額3.代印代計4.加值型服務5.儲值
        String str = "";
        switch(cashType){
            case 1:
                str = "月租型預繳";
                break;
            case 2:
                str = "超額費用";
                break;
            case 6:
                str = "扣抵預繳";
                break;
            case 7:
                str = "扣抵費用";
                break;
            default:
                str = "請洽客服人員";
                break;
        }
        return str;
    }

    //取消預繳
    public boolean transactionCancelPrepay(Integer cashDetailId) throws Exception{
        //0.需檢查狀態，是否可作「取消預繳」。註:未出帳前，才可以作「取消預繳」
        CashDetailEntity cashDetailEntity = (CashDetailEntity) getEntity(CashDetailEntity.class, cashDetailId);
        CashMasterEntity cashMasterEntity = (CashMasterEntity) getEntity(CashMasterEntity.class, cashDetailEntity.getCashMasterId());
        Integer cashMasterStatus = cashMasterEntity.getStatus(); //狀態 1.生效 2.作廢 3.出帳 4.入帳 5.佣金
        if(cashMasterStatus >=3){
            //註:未出帳前，才可以作「取消預繳」
            return false;
        }

        //1.找出prepay_detail的cash_detail_id有cash_detail_id的資料
        PrepayDetailEntity searchPrepayDetailEntity = new PrepayDetailEntity();
        searchPrepayDetailEntity.setCashDetailId(cashDetailId);
        List prepayDetailList = getSearchEntity(PrepayDetailEntity.class, searchPrepayDetailEntity);

        //2.把這些prepay_detail的cash_detail_id的值清掉(該筆預繳資料不出帳，但也不刪除，當作歷史資料來參考)
        for(int i=0; i<prepayDetailList.size(); i++){
            PrepayDetailEntity prepayDetailEntity = (PrepayDetailEntity)prepayDetailList.get(i);
            prepayDetailEntity.setCashDetailId(null);
            updateEntityIncludeNULL(prepayDetailEntity, prepayDetailEntity.getPrepayDetailId());
        }

        //3.把這筆cash_detail刪掉
        deleteEntity(cashDetailEntity);

        //4.重新加總cashMaster的值
        cashMasterEntity = sumCashMaster(cashMasterEntity.getCashMasterId());
        saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());

        return true;
    }

    //刪除帳單(帳單裡沒有任何明細，則可刪除)
    public boolean delCashMaster(Integer cashMasterId)throws Exception{
        //先檢查帳單裡是否沒有任何明細
        boolean isCashMasterEmpty = isCashMasterEmpty(cashMasterId);

        if(isCashMasterEmpty){
            //刪除帳單
            CashMasterEntity cashMasterEntity = (CashMasterEntity) getEntity(CashMasterEntity.class, cashMasterId);
            deleteEntity(cashMasterEntity);
            return true;
        }else{
            return false;
        }
    }

    //檢查帳單裡是否沒有任何明細
    public boolean isCashMasterEmpty(Integer cashMasterId)throws Exception{
        CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
        searchCashDetailEntity.setCashMasterId(cashMasterId);
        List cashDetailList = getSearchEntity(CashDetailEntity.class, searchCashDetailEntity);
        if(null == cashDetailList || cashDetailList.size() == 0){
            return true; //沒有任何明細
        } else {
            return false; //有明細
        }
    }
}

