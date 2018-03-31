package dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;
import java.util.Date;

import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.BillCycleRepository;
import com.gateweb.charge.repository.CashDetailRepository;
import com.gateweb.charge.repository.CompanyRepository;
import com.gateweb.charge.repository.InvoiceAmountSummaryReportRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.NullConstants;
import com.gate.utils.SendEmailFileUtils;
import com.gate.utils.TimeUtils;
import com.gate.web.beans.CalOver;
import com.gate.web.beans.QuerySettingVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository("calCycleDAO")
public class CalCycleDAO extends BaseDAO {

    @Autowired
    NullConstants nullConstants;
	
	@Autowired
    CashDAO cashDAO;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    InvoiceAmountSummaryReportRepository invoiceAmountSummaryReportRepository;

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    CashDetailRepository cashDetailRepository;

    public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception {
        Timestamp evlS = timeUtils.getCurrentTimestamp();

        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from bill_cycle bc left join company cp on bc.company_id= cp.company_id where 1=1 ");
        dataSb.append(" select bc.bill_id,bc.bill_type,bc.package_id,bc.company_id,cp.name,bc.year_month, price,  " );
        //dataSb.append(" case(bc.is_price_free) when '1' THEN 0 ELSE bc.price END price, " );
        dataSb.append(" bc.cnt, bc.cnt_limit, bc.cnt_gift, bc.cnt_over, bc.single_price,bc.price_over, bc.price_max,  " );
        dataSb.append(" bc.pay_over, bc.pay_month, bc.cash_out_over_id, bc.cash_out_month_id,bc.cash_in_over_id, bc.cash_in_month_id,bc.status bc_status" );
        dataSb.append(" from bill_cycle bc left join company cp on bc.company_id= cp.company_id " );
        dataSb.append(" where (bill_type=1 or bill_type=2) ");
        

        if (querySettingVO.getSearchMap().size() > 0) {
            Map searchMap = querySettingVO.getSearchMap();
            if (searchMap.containsKey("calYM")) {
                if (StringUtils.isNotEmpty(searchMap.get("calYM").toString())) {
                    whereSb.append(" and bc.year_month = ? ") ;
                    parameters.add(searchMap.get("calYM"));
                }
            }

            if (searchMap.containsKey("userCompanyId")) {
                if (StringUtils.isNotEmpty(searchMap.get("userCompanyId").toString())) {
                    whereSb.append(" and bc.company_id = ? ");
                    parameters.add(Integer.parseInt((String)searchMap.get("userCompanyId")));
                }
            }
        }
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by " + querySettingVO.getSidx() + " " + querySettingVO.getSord() + " , bc.bill_id desc");
        int first = (querySettingVO.getPage() - 1) * querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);

        Timestamp evlE = timeUtils.getCurrentTimestamp();
        long difference = evlE.getTime() - evlS.getTime();
        logger.info("calCycleDAO getBillCycleList 撈月租超額計算畫面清單sql difference="+difference+"ms");

        return returnMap;
    }

    public List getYM() throws Exception {
        List list = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -24);

        for(int i=0; i<37; i++){
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

    public List getUserCompanyList() throws Exception {
        String sql = "select company_id, name, business_no from company order by company_id " ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql,parameterList,null);
        return query.list();
    }

    /**
     * 計算該公司該期使用了幾張發票。
     * @param companyId
     * @param calYM
     * @return
     */
    public Integer calOverByCompany(Integer companyId,String calYM){
        Integer useCnt = 0;
        try{
            //1.找出所有用戶的當月張數：計算當期發票開立A0401/C0401張數
            List parameterList = new ArrayList();
            //取得發票期別(民國年+雙數月)
            String cYearMonth = timeUtils.getYearMonth(calYM+"01");
            parameterList.add(cYearMonth);
            String tempWhereSql = "";
            if(null != companyId ){
                tempWhereSql = " and cp.company_id = ? " ;
                parameterList.add(companyId);
            }
            parameterList.add(calYM); //超額計算年月

            String sql = "select im.seller,cp.company_id,name,count(1) " +
                    "from invoice_main im left join company cp on im.seller=cp.business_no " +
                    "where 1=1 and im.c_year_month = ? " + tempWhereSql +
                    " and im.invoice_date is not null and substring(im.invoice_date,1,6)= ?  " +
                    //只計算我們公司一般用戶使用張數，不計算樂天用戶使用張數
                    " and (im.sync_company_key is null or im.sync_company_key not in ('41112002-23dc-440e-944c-4fed3777a187')) ";

            sql += "group by im.seller,cp.company_id order by cp.company_id ";
            Query query = createQuery(sql, parameterList, null);
            List<Map> useCntList = query.list();

            for (int k = 0; k < useCntList.size(); k++) {
                //Integer cpId = (Integer)(useCntList.get(k).get("company_id"));
                //String cpName = (String)(useCntList.get(k).get("name"));
                useCnt = ((BigInteger) useCntList.get(k).get("count")).intValue(); //該公司某年月開的發票數量
            }
            System.out.println("cpId="+companyId+" useCnt="+useCnt+" calYM="+calYM + " cYearMonth="+cYearMonth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return useCnt;
        }
    }

    public Integer calOverByCompanyWithFromInvoiceAmountSummaryReport(CompanyEntity companyEntity, Date fromModifyDate, Date toModifyDate){
        List<InvoiceAmountSummaryReportEntity> invoiceAmountSummaryReportEntityList
            = invoiceAmountSummaryReportRepository.findBySellerIsAndModifyDateGreaterThanAndModifyDateLessThanAndClosedNot(
                    companyEntity.getBusinessNo()
                    , new java.sql.Date(fromModifyDate.getTime())
                    , new java.sql.Date(toModifyDate.getTime())
                    , true
        );
        return invoiceAmountSummaryReportEntityList.size();
    }

    //加總計算超額到cash_detail和cash_Master
    public void sumOverOut(
            Integer cpId
            , String calYM
            , Integer packageId
            , Integer cashMasterId
            , Integer cashDetailId
            , BigDecimal sumOfPayOver
            , List<BillCycleEntity> overList
            , boolean isConintueCal
            , Integer chargeType
            , Integer modifierId)throws Exception{
        logger.info("sumOverOut start = " + new java.util.Date());

        //update bill_cycle的值cash_out_over_id
        //舊的saveOrUpdateEntity沒有功能，而且他的isConintueCal只是用來看是否為多筆一起計算而已，所以合併方法。
//        List<BillCycleEntity> sumOfPayOverList = new ArrayList<BillCycleEntity>();
//        if(isConintueCal){
//            sumOfPayOverList= getSumOfPayOverList(cpId, calYM);
//        } else {
//            sumOfPayOverList = overList;
//        }

//        for(int m = 0; m<sumOfPayOverList.size(); m++){
//            BillCycleEntity updateBillCycleEntity = (BillCycleEntity)sumOfPayOverList.get(m);
//            updateBillCycleEntity.setCashOutOverId(cashDetailId);
//            saveOrUpdateEntity(updateBillCycleEntity, updateBillCycleEntity.getBillId());
//        }

        //於billCycle中寫入CashOutOverId
        for(BillCycleEntity billCycleEntity: overList){
            billCycleEntity.setCashOutOverId(cashDetailId);
            billCycleRepository.save(billCycleEntity);
        }

        /**如果有啟用扣抵，就作扣抵**/
        //該用戶是否有啟用扣抵?
        PrepayDeductMasterEntity searchPrepayDeductMasterEntity = new PrepayDeductMasterEntity();
        searchPrepayDeductMasterEntity.setCompanyId(cpId);
        List prepayDeductMasterList = getSearchEntity(PrepayDeductMasterEntity.class, searchPrepayDeductMasterEntity);
        for(int masterInx = 0; masterInx<prepayDeductMasterList.size(); masterInx++){
            PrepayDeductMasterEntity master = (PrepayDeductMasterEntity)prepayDeductMasterList.get(masterInx);
            String isEnableOver = master.getIsEnableOver(); //該用戶是否有啟用扣抵?
            if(null != isEnableOver && "Y".equals(isEnableOver)){
                //該用戶有啟用扣抵, 就作扣抵
                Integer amount = master.getAmount(); //剩餘可作扣抵的錢
                if(amount <= 0){ //如果沒錢可扣抵，就不作扣抵
                    continue;
                }
                Integer sumOver = sumOfPayOver.intValue(); //這次超額要繳的錢(註:只作整數的扣抵)

                CashDetailEntity cashDetailEntity_forDeduct = new CashDetailEntity();
                cashDetailEntity_forDeduct.setCompanyId(cpId); //公司名稱
                cashDetailEntity_forDeduct.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYM))); //計算年月
                cashDetailEntity_forDeduct.setOutYm(timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYM), 1))); //帳單年月
                cashDetailEntity_forDeduct.setCashMasterId(cashMasterId); //cash_master_id
                cashDetailEntity_forDeduct.setCashType(7); //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值 6.預繳 7.扣抵
                cashDetailEntity_forDeduct.setBillType(chargeType); //帳單類型　1.月租 2.級距
                cashDetailEntity_forDeduct.setPackageId(packageId); //超額的cashDetail不紀錄packageId(超額的cashDetail記的packageId只能參考，不是真正值)，因為可能跨兩種不同的package。
                cashDetailEntity_forDeduct.setStatus(1); //1.生效 2.作廢

                if(amount < sumOver){
                    //剩餘金額不夠扣抵，就先扣抵「剩餘可作扣抵的錢」
                    BigDecimal minusSumOfPayOver = new BigDecimal(0).subtract(new BigDecimal(amount));
                    cashDetailEntity_forDeduct = cashDAO.calPriceTax(cashDetailEntity_forDeduct, minusSumOfPayOver, new BigDecimal(0), "超額扣抵");
                    saveEntity(cashDetailEntity_forDeduct);

                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(master.getPrepayDeductMasterId());
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(cpId);
                    deductDetailEntity.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYM)));
                    deductDetailEntity.setDeductType(2); //1.月租 2.超額(由此可知到抵扣抵了哪類金額(可搭prepay_deduct_master的is_enable_over、is_enable_cycle使用))
                    deductDetailEntity.setMoney(-amount);
                    saveEntity(deductDetailEntity);

                    master.setAmount(0);
                    saveOrUpdateEntity(master, master.getPrepayDeductMasterId());

                } else {
                    //剩餘金額夠扣抵，就作扣抵
                    Integer leftAmount = amount-sumOver;

                    BigDecimal minusSumOfPayOver = new BigDecimal(0).subtract(sumOfPayOver);
                    cashDetailEntity_forDeduct = cashDAO.calPriceTax(cashDetailEntity_forDeduct, minusSumOfPayOver, new BigDecimal(0), "超額扣抵");
                    saveEntity(cashDetailEntity_forDeduct);

                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(master.getPrepayDeductMasterId());
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(cpId);

                    deductDetailEntity.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYM)));
                    deductDetailEntity.setDeductType(2); //1.月租 2.超額(由此可知到抵扣抵了哪類金額(可搭prepay_deduct_master的is_enable_over、is_enable_cycle使用))
                    deductDetailEntity.setMoney(-sumOver);
                    saveEntity(deductDetailEntity);

                    master.setAmount(leftAmount);
                    saveOrUpdateEntity(master, master.getPrepayDeductMasterId());
                }
            }
            break;
        }

        logger.info("sumOverOut end = " + new java.util.Date());
    }


    /**計算未出帳超額的加總
     * @param cpId 計算該公司的超額
     * @param calYM 加總calYM之前的超額
     * @return
     * @throws Exception
     */
    public BigDecimal getSumOfPayOver(Integer cpId, String calYM) throws Exception{
//        String overSumSql = " select sum(pay_over) sum_over from bill_cycle where company_id=? and cash_out_over_id is null and to_date( ? ,'YYYYMM')>=to_date( year_month ,'YYYYMM') ";
        logger.info("getSumOfPayOver start = " + new java.util.Date());
        List<BillCycleEntity> resultList = getSumOfPayOverList(cpId, calYM);

        //計算未出帳超額的加總
        BigDecimal sumOfPayOver = new BigDecimal(0);
        for(int i=0; i<resultList.size(); i++){
            BillCycleEntity bc =(BillCycleEntity)resultList.get(i);
            BigDecimal payover = nullConstants.getNotNull(bc.getPayOver());
            sumOfPayOver = sumOfPayOver.add(payover);
        }
        logger.info("getSumOfPayOver end = " + new java.util.Date());
        return sumOfPayOver;
    }

    /**只計算overList裡的超額加總值
     * @param cpId 計算該公司的超額
     * @param overList 只計算overList裡的超額加總值
     * @return
     * @throws Exception
     */
    public BigDecimal getSumOfPayOver(Integer cpId, List<BillCycleEntity> overList) throws Exception{
        //List<BillCycleEntity> resultList = getSumOfPayOverList(cpId, overList);

        //計算未出帳超額的加總
        BigDecimal sumOfPayOver = new BigDecimal(0);
        for(int i=0; i<overList.size(); i++){
            BillCycleEntity bc =(BillCycleEntity)overList.get(i);
            BigDecimal payover = nullConstants.getNotNull(bc.getPayOver());
            sumOfPayOver = sumOfPayOver.add(payover);
        }
        return sumOfPayOver;
    }

    /**
     * 找出.....該公司之計算年月的bill_cycle，且....
     * 1.)超額未出帳到cash_detail (所以cash_out_over_id is null)
     * 2.)在該計算年月之前的bill_cycle (to_date( ? ,'YYYYMM')>=to_date( year_month ,'YYYYMM') )
     * 的bill_cycle，
     * @param cpId
     * @param calYM
     * @return
     * @throws Exception
     */
    public List<BillCycleEntity> getSumOfPayOverList(Integer cpId, String calYM) throws Exception {
        //String overSumSql = " select sum(pay_over) sum_over from bill_cycle where company_id=? and cash_out_over_id is null and to_date( ? ,'YYYYMM')>=to_date( year_month ,'YYYYMM') ";
        logger.info("getSumOfPayOverList start = " + new java.util.Date());
        //找出該公司之計算年月的那筆bill_cycle，取得那筆bill_cycle的bill_id和package_Id
        BillCycleEntity searchBC = new BillCycleEntity();
        searchBC.setCompanyId(cpId);
        searchBC.setYearMonth(calYM);
        searchBC.setStatus("1");
        List searchBCList =  getSearchEntity(BillCycleEntity.class, searchBC);
        BillCycleEntity BC = (BillCycleEntity)searchBCList.get(0);
        Integer billId = BC.getBillId();

        //找出在那筆計算年月之前且超額未出帳到cash_detail的bill_cycle
        BillCycleEntity searchBC2 = new BillCycleEntity();
        searchBC2.setCompanyId(cpId);
        searchBC2.setStatus("1");
        List searchBC2List =  getSearchEntity(BillCycleEntity.class, searchBC2);
        List<BillCycleEntity> resultList = new ArrayList();
        for(int w=0; w<searchBC2List.size(); w++){
            BillCycleEntity bc =(BillCycleEntity)searchBC2List.get(w);
            Integer billId_tmp = bc.getBillId();
            if(null == bc.getCashOutOverId()){ //未出帳到cash_detail (cash_out_over_id is null))
                if(billId >= billId_tmp){ //在該計算年月之前的bill_cycle
                    resultList.add(bc);
                }
            }
        }
        logger.info("getSumOfPayOverList end = " + new java.util.Date());
        return resultList;
    }

    /**
     * /寄超額email通知(by年月)
     * @param calYM
     * @return
     * @throws Exception
     */
    public Integer sendOverMailYM(String calYM) throws Exception{ //寄超額email通知(批次)
        BillCycleEntity searchBillCycleEntity = new BillCycleEntity();
        searchBillCycleEntity.setYearMonth(calYM);
        List billCycleList = getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);

        int sendCnt = 0;
        for(int i=0; i<billCycleList.size(); i++){
            BillCycleEntity billCycleEntity = (BillCycleEntity)billCycleList.get(i);
            boolean isSend = OverMailContext(billCycleEntity); //寄信
            if(isSend){
                sendCnt ++;
            }
        }
        return sendCnt;
    }

    /**
     * 寄超額email通知(選擇某幾筆)
     * @param calOverAry 寄這幾筆的超額通知
     * @return
     * @throws Exception
     */
    public Integer sendOverMail(String calOverAry) throws Exception{ //寄超額email通知(選擇某幾筆)
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CalOver>>(){}.getType();
        List<CalOver> calOverList = gson.fromJson(calOverAry, collectionType);

        int sendCnt = 0;
        //找出所有要email的超額
        for(int i=0; i<calOverList.size(); i++) {
            CalOver calOver = (CalOver)calOverList.get(i);
            Integer billId = Integer.parseInt(calOver.getBillId());
            BillCycleEntity billCycleEntity = (BillCycleEntity)getEntity(BillCycleEntity.class, billId);
            boolean isSend = OverMailContext(billCycleEntity); //寄信
            if(isSend){
                sendCnt ++;
            }
        }
        return sendCnt;
    }

    //寄送email通知
    public boolean OverMailContext(BillCycleEntity billCycleEntity)throws Exception {
        String status = billCycleEntity.getStatus();

        if("2".equals(status)){ //作廢的資料不寄信
            return false;
        }

        Integer cpId = billCycleEntity.getCompanyId();
        CompanyEntity cpEntity = (CompanyEntity)getEntity(CompanyEntity.class, cpId);
        String email = cpEntity.getEmail1();
        String cpName = cpEntity.getName();
        String yearMonth = billCycleEntity.getYearMonth();
        //方案名稱
        Integer packageId= billCycleEntity.getPackageId();
        PackageModeEntity packageModeEntity = (PackageModeEntity)getEntity(PackageModeEntity.class, packageId);
        Integer packageType = packageModeEntity.getPackageType();
        Integer chargeId = packageModeEntity.getChargeId();
        String PakcageName = "";
        if(1 == packageType){ //月租型
            ChargeModeCycleEntity chargeModeCycleEntity = (ChargeModeCycleEntity)getEntity(ChargeModeCycleEntity.class, chargeId);
            PakcageName = chargeModeCycleEntity.getPackageName();
        } else if(2 ==  packageType){ //級距型
            ChargeModeGradeEntity chargeModeGradeEntity = (ChargeModeGradeEntity)getEntity(ChargeModeGradeEntity.class, chargeId);
            PakcageName = chargeModeGradeEntity.getPackageName();
        }


        Integer cntLimit= (null == billCycleEntity.getCntLimit())?0:billCycleEntity.getCntLimit(); //發票額度
        Integer cnt = (null == billCycleEntity.getCnt())?0:billCycleEntity.getCnt();  //使用張數
        Integer cntOver = (null == billCycleEntity.getCntOver())?0:billCycleEntity.getCntOver(); //超出額度
        Integer cntGift = (null == billCycleEntity.getCntGift())?0:billCycleEntity.getCntGift(); //贈送額度

        String subject="【超額通知】關網電子發票超額明細，請詳內文。";
        StringBuffer content = new StringBuffer();
        content.append("請注意：本信件是由「關網資訊雲端電子發票系統」自動產生與發送，請勿直接回覆。")
                .append("\n\n敬愛的 " + cpName + " 您好：")
                .append("\n\n以下資訊為貴公司" + yearMonth + "月份超額發票之明細，請查收。\n\n");

        content.append(" <table  style=\"border:2px black solid; border-collapse: collapse\" cellpadding=\"5\" border='2' > ")
                .append(" <tr>")
                .append(" <th>計算類型</th><th>方案名稱</th><th>發票額度</th><th>使用張數</th><th>超出額度</th><th>贈送額度</th>")
                .append(" </tr>")
                .append(" <tr>")
                .append(" <td>超額</td><td>"+PakcageName+"</td><td>"+cntLimit+"</td><td>"+cnt+"</td><td>"+cntOver+"</td><td>"+cntGift+"</td>")
                .append(" </tr>")
                .append(" </table>");

        content.append("\n\n累計超額費用至500元後，將另出繳款帳單，或於下一年度合約一併收款。")
                .append("\n\n如有疑問，請儘速致電關網資訊詢問，謝謝！")
                .append("\n<a title=\"關網資訊股份有限公司\" href=\"http://www.gateweb.com.tw\">www.gateweb.com.tw</a>")
                .append("\n客服時間：一般上班日09:30～12:00 , 13:30～18:00")
                .append("\n客服專線：02-77183770");


//        SendEmailFileUtils.sendEmail(new String[]{email},  subject, content.toString());
        SendEmailFileUtils.sendEmail(email, cpName, subject, content.toString());

        return true;
    }

}

