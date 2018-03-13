package dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gateweb.utils.BeanConverterUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.ContinuePackage;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CashMasterEntity;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository("companyChargeDAO")
public class CompanyChargeDAO extends BaseDAO {

	@Autowired
    CashDAO cashDAO;

    @Autowired
    CalCycleDAO calCycleDAO;

    @Autowired
    BeanConverterUtils beanConverterUtils;

    public List getChargeMonthList() throws Exception {
        String sql = "select charge_id,package_name from charge_mode_cycle where status = 1 ";
//        String sql = "select '1' as charge_type,charge_id,package_name from charge_mode_cycle where status = 1 ";
//                " union all " +
//                " select '2' as charge_type, charge_id,package_name from charge_mode_grade where status = 1 ";
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    public List getChargeGradeList() throws Exception {
        String sql = "select charge_id,package_name from charge_mode_grade where status = 1" ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    /**
     * 撈經銷商業務的list
     * @param dealerComplayId
     * @return
     * @throws Exception
     */
    public List getDealerList(Integer dealerComplayId) throws Exception {
        String sql = " select * from dealer where dealer_company_id=? ";
        List parameterList = new ArrayList();
        parameterList.add(dealerComplayId);
        Query query = createQuery(sql, parameterList, null);
        return query.list();

    }

    /**
     * 新增月租/級距合約(用戶綁合約)
     * @param bean
     * @throws Exception
     */
    public void transactionInsertCompanyChargeCycle(CompanyChargeCycleBean bean, Integer modifierId) throws Exception {
        Integer chargeType = bean.getPackageType(); //收費方式 1.月租 2.級距

        //ChargeModeCycleEntity cycleEntity = (ChargeModeCycleEntity) getEntity(ChargeModeCycleEntity.class, bean.getChargeId());
        ChargeModeCycleEntity cycleEntity = null;
        ChargeModeGradeEntity gradeEntity = null;
        if(1 == chargeType){
            //月租型
            cycleEntity = (ChargeModeCycleEntity) getEntity(ChargeModeCycleEntity.class, bean.getChargeId());
        }else if(2 == chargeType){
            //級距型
            gradeEntity = (ChargeModeGradeEntity) getEntity(ChargeModeGradeEntity.class, bean.getChargeId());
        }

        //新增charge_mode_cycle_add
        ChargeModeCycleAddEntity addEntity = beanConverterUtils.companyChargeCycleBeanToChargeModeCycleEntity(bean);
        java.sql.Date sqlStartDate = new java.sql.Date(timeUtils.getMonthOneDay(addEntity.getRealStartDate()).getTime()); //實際計算日起的當月的第一日，就是計算日起的日期

        java.util.Date sqlEndDate = addEntity.getRealEndDate();
        Calendar calEndDate = Calendar.getInstance();
        calEndDate.setTime(sqlEndDate);
        Date monthLastDay = timeUtils.getMonthLastDay(calEndDate.getTime());
        Integer realEndDateMaxDay = monthLastDay.getDate();
        if(calEndDate.get(Calendar.DATE) != realEndDateMaxDay){ //如果(實際計算日迄)的當月最後一天不等於(實際計算日迄)日期，那麼(計算日迄日期)應該是(實際計算日迄)的上個月的最後一天
            calEndDate.add(Calendar.MONTH, -1);
        }
        calEndDate.getTime();
        java.sql.Date endDate = new java.sql.Date(timeUtils.getMonthLastDay(calEndDate.getTime()).getTime());

        addEntity.setStartDate(sqlStartDate); //計算日起(某月的第一日)
        addEntity.setEndDate(endDate); //計算日迄(某月的最後一日)
        saveEntity(addEntity);

        //新增package_mode
        PackageModeEntity packageModeEntity = new PackageModeEntity();
        BeanUtils.copyProperties(packageModeEntity, bean);
        packageModeEntity.setAdditionId(addEntity.getAdditionId());
        packageModeEntity.setPackageType(chargeType);
        packageModeEntity.setStatus(String.valueOf(1) ); //0.未生效 1.生效 2.作廢
        saveEntity(packageModeEntity);

        //計算....................................................................
        //月租收費區間 1年繳 2.季繳 3.月繳.
        //合約生效後，產生月租帳單(bill_cycle和cash_flow的資料)
        //int feePeriod = cycleEntity.getFeePeriod();
        int feePeriod = 0;
        if(1 == chargeType){
            //月租型
            feePeriod = cycleEntity.getFeePeriod();
        } else if(2 == chargeType){
            //級距型
            feePeriod = gradeEntity.getFeePeriod();
        }

        if(1 == feePeriod){ //1.年繳
            genBillCashData(true, 0, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }else if(2 == feePeriod){ //2.季繳
            genBillCashData(false, 3, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }else if(3 == feePeriod){ //3.月繳
            genBillCashData(false, 1, -3, cycleEntity, packageModeEntity, bean, addEntity, false, true, chargeType, gradeEntity, modifierId);
        }

    }

    /**
     * * 合約生效後，產生月租帳單(bill_cycle和cash_flow的資料)
     * @param isFullPay 是否一次全部月租都預繳
     * @param seasonUnit 幾個月繳一次錢?
     * @param earlyMonth 提早幾個月出帳單(請輸入提早幾個月計算，註:計算年月會比帳單年月少一個月，出帳上海銀行年月會比帳單年月少一個月)
     * @param cycleEntity
     * @param packageModeEntity
     * @param bean
     * @param addEntity
     * @param isContinuePackage 是否為續約的case (如果為續約，則不再加贈合約的免費月份)
     * @param isFirst 用戶是否第一次(首次)綁合約(如果第一次，則要對cash_master和cash_detail的isFirst欄位設定為1)，第一次綁合約出帳不匯出Excel，也不寄帳單明細的email通知
     * @throws Exception
     */
    public void genBillCashData(boolean isFullPay, int seasonUnit, int earlyMonth, ChargeModeCycleEntity cycleEntity,
            PackageModeEntity packageModeEntity, CompanyChargeCycleBean bean,ChargeModeCycleAddEntity addEntity,
                                boolean isContinuePackage, boolean isFirst, int chargeType,
                                ChargeModeGradeEntity gradeEntity, Integer modifierId) throws Exception{

        //1.(方案的每月贈送張數)和(合約的每月贈送張數)目前只有DB的TABLE有此欄位，UI沒有，程式也不處理這類的贈送張數邏輯

        Calendar realStartCal = Calendar.getInstance();
        realStartCal.setTime(addEntity.getRealStartDate());

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(addEntity.getStartDate());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(addEntity.getEndDate());
        //用計算起迄日算出要計算幾個月
        Integer overMonths = (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 +       //結束-起始經過幾個月
                (endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH) + 1) ;

        int totalFreeMonth = 0; //總共的免費月份
        if(true == isContinuePackage){
            totalFreeMonth = addEntity.getFreeMonth(); //續約，不再加贈合約的免費月份
        } else {
            //totalFreeMonth = addEntity.getFreeMonth() + cycleEntity.getFreeMonth(); //總共的免費月份
            if(1 == chargeType){
                //月租型
                totalFreeMonth = addEntity.getFreeMonth() + cycleEntity.getFreeMonth(); //總共的免費月份
            }else if(2 == chargeType){
                //級距型
                totalFreeMonth = addEntity.getFreeMonth() + gradeEntity.getFreeMonth(); //總共的免費月份
            }
        }

        //目前沒有硇月或足月之分
//        if (cycleEntity.getChargeCycle() == 2) {
//            if (realStartCal.get(Calendar.DATE) > 15) {
//                //破月成立,多加一個免費月份
//                totalFreeMonth++;
//            }
//        }
//        int leftPrePayment = cycleEntity.getPrePayment();  //預付金額

        if(isFullPay == true){ //年繳 (一次全繳，不分期繳)
            seasonUnit = overMonths - totalFreeMonth ;
        }

        int giftPrice = (null==addEntity.getGiftPrice())?0:addEntity.getGiftPrice().intValue(); //加贈金額
//        int seasonUnit = 3; //一期3個月(多久算一次錢)
        int seasonSeq = 0; //第幾期(第幾次算錢)
        int seasonCnt = 0; //某期的第幾個月
//        int earlyMonth = -2;
        double noTaxPrice = 0;

        //出帳上海銀行的帳單年月=出帳年月的下個月
        Calendar bankCal = Calendar.getInstance();
        bankCal.setTime(startCal.getTime());
        bankCal.add(Calendar.MONTH, 1);

        CashDetailEntity cashDetailEntity = new CashDetailEntity();
        cashDetailEntity.setCompanyId(bean.getCompanyId()); //公司名稱
        cashDetailEntity.setCalYm(timeUtils.getYearMonth2(timeUtils.addMonth(startCal.getTime(), earlyMonth))); //計算年月 預繳(提早出帳)
        cashDetailEntity.setOutYm(timeUtils.getYearMonth2(timeUtils.addMonth(startCal.getTime(), earlyMonth+1))); //帳單年月 預繳
        CashMasterEntity cashMasterEntity = cashDAO.isHaveCashMaster(cashDetailEntity.getOutYm(), bean.getCompanyId(), modifierId);
        cashDetailEntity.setCashMasterId(cashMasterEntity.getCashMasterId()); //cash_master_id
        cashDetailEntity.setCashType(1); //月租
        cashDetailEntity.setBillType(chargeType); //帳單類型　1.月租 2.級距
        cashDetailEntity.setPackageId(packageModeEntity.getPackageId());
        cashDetailEntity.setStatus(1); //1.生效 2.作廢
        if(isFirst){
            //用戶是否第一次(首次)綁合約(如果第一次，則要對cash_master和cash_detail的isFirst欄位設定為1)，第一次綁合約出帳不匯出Excel，也不寄帳單明細的email通知
            String userChoseIsFirst = bean.getIsFirst(); //這個是Jia在「用戶綁合約畫面」選是否首次的值。
            if(null != userChoseIsFirst){
                if("1".equals(userChoseIsFirst)){
                    cashDetailEntity.setIsFirst("1"); //第一次使用
                    cashMasterEntity.setIsFirst("1"); //第一次使用
                    updateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
                }
            }
        }

        for (int i = 0; i < overMonths; i++) {
            if(null == cashDetailEntity.getCashDetailId()){
//                String calYM = cashDetailEntity.getCalYm();
                String outYm = cashDetailEntity.getOutYm();
                cashMasterEntity = cashDAO.isHaveCashMaster(outYm, bean.getCompanyId(), modifierId);
                cashDetailEntity.setCashMasterId(cashMasterEntity.getCashMasterId()); //cash_master_id
                saveEntity(cashDetailEntity);
            }

            Date account_month = startCal.getTime();
            java.sql.Date sqlAccountMonth = new java.sql.Date(account_month.getTime());


            //bill_Cycle寫入
            BillCycleEntity billCycleEntity = new BillCycleEntity();
            billCycleEntity.setPackageId(packageModeEntity.getPackageId());
            billCycleEntity.setStatus(String.valueOf(1)); //1.生效 2.作廢
            billCycleEntity.setYearMonth(timeUtils.getYearMonth2(account_month));
            billCycleEntity.setBillType(chargeType);  //合約類型 1.月租 2.級距
            billCycleEntity.setCompanyId(bean.getCompanyId());
            double salesPrice = 0; //月租金額

            if(1 == chargeType){
                //月租型
                billCycleEntity.setPriceMax(new BigDecimal(cycleEntity.getMaxPrice().doubleValue())); //當月最大收費價格
                billCycleEntity.setCntLimit(cycleEntity.getBaseQuantity()); //當月基本使用量(張)
                billCycleEntity.setSinglePrice(cycleEntity.getSinglePrice());
                billCycleEntity.setPrice(new BigDecimal(cycleEntity.getSalesPrice())); //月租金額
                salesPrice = cycleEntity.getSalesPrice().doubleValue();
            } else if(2 == chargeType){
                billCycleEntity.setCntLimit(gradeEntity.getBaseQuantity()); //當月基本使用量(張)
                billCycleEntity.setPrice(new BigDecimal(gradeEntity.getSalesPrice())); //月租金額
                salesPrice = gradeEntity.getSalesPrice().doubleValue();
            }

            if(i >= totalFreeMonth){
                noTaxPrice = noTaxPrice + salesPrice;
                billCycleEntity.setPayMonth(new BigDecimal(salesPrice) ); //月租應繳
                billCycleEntity.setCashOutMonthId(cashDetailEntity.getCashDetailId()); //月租出帳id
                seasonCnt++;
            }else{
                billCycleEntity.setIsPriceFree("1"); //月租金額 (免費)
                billCycleEntity.setPayMonth(new BigDecimal(0d)); //月租應繳
                billCycleEntity.setCashOutMonthId(cashDetailEntity.getCashDetailId()); //月租出帳id
            }

            saveEntity(billCycleEntity);

            startCal.add(Calendar.MONTH, 1);

            if(seasonCnt==seasonUnit){

                if(seasonSeq == 0 && (giftPrice != 0) ){ //第一筆季繳帳單，有合約的加贈金額
                    cashDetailEntity = cashDAO.calPriceTax(cashDetailEntity, new BigDecimal(noTaxPrice), new BigDecimal(giftPrice), "合約加贈金額"); //差價
                } else {
                    cashDetailEntity = cashDAO.calPriceTax(cashDetailEntity, new BigDecimal(noTaxPrice), new BigDecimal(0), null); //無差價
                }

//                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()); //現在時間
//                cashDetailEntity.setCashOutDate(currentTimestamp);
                updateEntity(cashDetailEntity,cashDetailEntity.getCashDetailId());

                seasonCnt = 0;
                noTaxPrice = 0d;
                seasonSeq ++;

                //除了合約第一個月的出帳單月份之外，其他出帳單月份皆要提早N個月出(因為是預繳)
                //出帳上海銀行的帳單年月=出帳年月的下個月
                Calendar cashFlowMonth = Calendar.getInstance();
                cashFlowMonth.setTime(startCal.getTime());
                String calYM = timeUtils.getYearMonth2(cashFlowMonth.getTime()); //出帳帳單年月

                cashDetailEntity = new CashDetailEntity();
                cashDetailEntity.setCompanyId(bean.getCompanyId()); //公司名稱
                cashDetailEntity.setCalYm(timeUtils.getYearMonth2(timeUtils.addMonth(timeUtils.parseDate(calYM), earlyMonth))); //計算年月 預繳
                cashDetailEntity.setOutYm(timeUtils.getYearMonth2(timeUtils.addMonth(timeUtils.parseDate(calYM), earlyMonth+1))); //帳單年月 預繳
                cashDetailEntity.setCashType(1); //月租
                cashDetailEntity.setBillType(chargeType); //帳單類型　1.月租 2.級距
                cashDetailEntity.setPackageId(packageModeEntity.getPackageId());
                cashDetailEntity.setStatus(1); //1.生效 2.作廢
            }
        }
    }




    /**
     * charge_cycle歷史記錄BY companyId
     * @param companyId
     * @return
     * @throws Exception
     */
    public List<Map> getChargeCycleHisByCompany(Integer companyId) throws Exception {
        Timestamp evlS = timeUtils.getCurrentTimestamp();

        String sql = " select pm.company_id,pm.package_id ," +
                "  CASE WHEN cmc.package_name IS NULL THEN cmg.package_name ELSE cmc.package_name END," +
                " pm.package_type,cmca.start_date,cmca.end_date,cmca.real_start_date, " +
                " cmca.real_end_date, pm.status " + //,getusername(pm.broker_id) broker_name from package_mode pm " +
                " from package_mode pm left join charge_mode_cycle cmc on (pm.charge_id = cmc.charge_id and pm.package_type=1)" +
                " left join charge_mode_grade cmg on (pm.charge_id = cmg.charge_id and pm.package_type=2) "+
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " where  company_id=?  " + //" pm.package_type= and "<=月租/級距
                " order by cmca.start_date desc, pm.package_id desc ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);

        Timestamp evlE = timeUtils.getCurrentTimestamp();
        long difference= evlE.getTime() - evlS.getTime();
        logger.info("CompanyChargeDAO getChargeCycleHisByCompany 撈用戶方案歷史紀錄sql  difference="+difference+"ms");

        return query.list();
    }

    /**
     * 合約結清前，先作試算的試算資料
     * @param companyId
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getSettleInfo(Integer companyId, Integer packageId)throws Exception{
        //月租型
        String sql = " select pm.package_id, " +
                " case when cmc.package_name is null then cmg.package_name else cmc.package_name end," +
                " to_char(cmca.real_start_date,'YYYY-MM-DD') real_start_date,to_char(cmca.real_end_date,'YYYY-MM-DD') real_end_date " +
                " ,to_char(cmca.start_date,'YYYY-MM-DD') start_date, to_char(cmca.end_date,'YYYY-MM-DD') end_date " +
                " from package_mode pm left join charge_mode_cycle cmc on pm.charge_id = cmc.charge_id and pm.package_type=1 " +
                " left join charge_mode_grade cmg on pm.charge_id = cmg.charge_id and pm.package_type=2 " +
                " left join charge_mode_cycle_add cmca on pm.addition_id = cmca.addition_id " +
                " where pm.company_id= ?  and pm.package_id = ? ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map)query.uniqueResult();
    }

    /**
     * 合約結清前，先作試算的試算資料
     * @param packageId
     * @param endDate
     * @return
     * @throws Exception
     */
    public Map getCycleTryCalSettle(Integer packageId, String endDate)throws Exception{
        String year = endDate.substring(0, 4);
        String month = endDate.substring(5,7);
        String packageEndDate = year + month;

        //已繳月租費
        String sql = " select coalesce(sum(coalesce(pay_month,0)),0) paymonth from bill_cycle where package_id= ? and cash_in_month_id is not null ";
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        Map paidMonthMap = (Map)query.uniqueResult();
        BigDecimal paidMonth = (BigDecimal)paidMonthMap.get("paymonth");

        //已使用月租費
        sql = " select coalesce(sum(coalesce(price,0)),0) usedMonth from bill_cycle where package_id=? " +
                "and bill_id<= (select bill_id from bill_cycle where package_id= ? and year_month=?) and is_price_free is null  ";
        parameterList = new ArrayList();
        parameterList.add(packageId);
        parameterList.add(packageId);
        parameterList.add(packageEndDate);
        query = createQuery(sql, parameterList, null);
        Map usedMonthMap = (Map)query.uniqueResult();
        BigDecimal usedMonth = (BigDecimal)usedMonthMap.get("usedmonth");

        //remainderMonth =  paidMonth - usedMonth
        BigDecimal remainderMonth = paidMonth.subtract(usedMonth);

        Map resultMap = new HashMap();
        resultMap.put("paidMonth", paidMonth);
        resultMap.put("usedMonth", usedMonth);
        resultMap.put("remainderMonth", remainderMonth);

        return resultMap;
    }

    /**
     * 確認結清
     * @param packageId
     * @param endDate
     * @param realEndDate
     * @return
     * @throws Exception
     */
    public Map transactionDoSettle(Integer packageId, String endDate, String realEndDate)throws Exception{
        String year = endDate.substring(0, 4);
        String month = endDate.substring(5,7);
        String packageEndDate = year + month;

        String rYear = realEndDate.substring(0, 4);
        String rMonth = realEndDate.substring(5,7);
        String packageRealEndDate = rYear + rMonth;

        //結清:更新charge_mode_cycle_add的real_end_date和end_date，並且把bill_cylce裡end_date後的bill全部作廢，cash_detail裡的end_date後的cash也要全部作廢
        PackageModeEntity searchPackageModeEntity = new PackageModeEntity();
        searchPackageModeEntity.setPackageId(packageId);
        List packageModeList =  getSearchEntity(PackageModeEntity.class, searchPackageModeEntity);

        //更新此package_id的status=2(作廢)
        String sql = " update package_mode set status='2' where package_id=? " ;
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        executeSql(sql, parameterList);


        sql = " select addition_id from package_mode where package_id=? " ;
        parameterList.clear();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        Map packageModelMap = (Map)query.uniqueResult();
        Integer additionId = (Integer)packageModelMap.get("addition_id");

        //1.更新real_end_date和end_date
        ChargeModeCycleAddEntity chargeModeCycleAddEntity = new ChargeModeCycleAddEntity();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date enDateCal = simpleDateFormat.parse(endDate);
        Date realEndDateCal = simpleDateFormat.parse(realEndDate);
        chargeModeCycleAddEntity.setEndDate(new java.sql.Date(enDateCal.getTime()));
        chargeModeCycleAddEntity.setRealEndDate(new java.sql.Date(realEndDateCal.getTime()));
        saveOrUpdateEntity(chargeModeCycleAddEntity,additionId);

        //2.把bill_cylce裡end_date後的bill全部作廢
        BillCycleEntity searchBillCycleEntity = new BillCycleEntity();
        searchBillCycleEntity.setPackageId(packageId);
        List billCycleList =  getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);
        Integer billFailStartId = 0;
        for(int j=0; j<billCycleList.size(); j++){
            BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleList.get(j);
            String ym = billCycleEntity.getYearMonth();
            if(ym.equals(year + month)){
                billFailStartId = billCycleEntity.getBillId();
                break;
            }
        }
        for(int j=0; j<billCycleList.size(); j++){
            BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleList.get(j);
            Integer billId = billCycleEntity.getBillId();
            Integer companyId = billCycleEntity.getCompanyId();
            if(billId > billFailStartId){
                billCycleEntity.setStatus("2"); //1.生效 2.作廢
                saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());

                //3.cash_detail裡的end_date後的cash也要全部作廢
                //(未入帳的cash_detail全部要作廢)
                //3.1月租費
                Integer cashOutMonthId = billCycleEntity.getCashOutMonthId();
                if(null != cashOutMonthId){ //出帳的
                    Integer cashInMonthId = billCycleEntity.getCashInMonthId();
                    if(null == cashInMonthId){ //未入帳的 (要作廢)
                        //已使用月租費
                        sql = " select cash_master_id from cash_detail where cash_detail_id=? " ;
                        parameterList.clear();
                        parameterList.add(cashOutMonthId);
                        query = createQuery(sql, parameterList, null);
                        Map cashDetailMap = (Map)query.uniqueResult();
                        Integer cashMasterId = (Integer)cashDetailMap.get("cash_master_id");

                        CashDetailEntity cashDetail_status = new CashDetailEntity();
                        cashDetail_status.setStatus(2); //1.生效 2.作廢
                        saveOrUpdateEntity(cashDetail_status, cashOutMonthId);


                        //重算cashMaster
                        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
                        searchCashMasterEntity.setCashMasterId(cashMasterId);
                        searchCashMasterEntity.setCompanyId(companyId);
                        List cashMasterList =  getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);

                        for(int k=0; k<cashMasterList.size(); k++){
                            CashMasterEntity cashMasterEntity = (CashMasterEntity)cashMasterList.get(0);
                            Integer cashMasterId_m = cashMasterEntity.getCashMasterId();
                            if(cashMasterId_m.equals(cashMasterId)){
                                cashMasterEntity = cashDAO.sumCashMaster(cashMasterEntity);
                                saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
                            }
                        }
                    }
                }

                //3.2超額費
                Integer cashOutOverId = billCycleEntity.getCashOutOverId();
                if(null != cashOutOverId){ //有出帳的(要作廢)
                    Integer cashInOverId = billCycleEntity.getCashInOverId();
                    if(null == cashInOverId){ //未入帳的
                        sql = " select cash_master_id from cash_detail where cash_detail_id=? " ;
                        parameterList.clear();
                        parameterList.add(cashOutOverId);
                        query = createQuery(sql, parameterList, null);
                        Map cashDetailMap = (Map)query.uniqueResult();
                        Integer cashMasterId = (Integer)cashDetailMap.get("cash_master_id");

                        CashDetailEntity cashDetail_status = new CashDetailEntity();
                        cashDetail_status.setStatus(2); //1.生效 2.作廢
                        saveOrUpdateEntity(cashDetail_status, cashOutMonthId);

                        //重算cashMaster
                        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
                        searchCashMasterEntity.setCashMasterId(cashMasterId);
                        searchCashMasterEntity.setCompanyId(companyId);
                        List cashMasterList =  getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);
                        for(int k=0; k<cashMasterList.size(); k++){
                            CashMasterEntity cashMasterEntity = (CashMasterEntity)cashMasterList.get(0);
                            Integer cashMasterId_m = cashMasterEntity.getCashMasterId();
                            if(cashMasterId_m.equals(cashMasterId)){
                                cashMasterEntity = cashDAO.sumCashMaster(cashMasterEntity);
                                saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
                            }
                        }
                    }
                }
            }
        }


        Map resultMap = new HashMap();
        return resultMap;
    }

    /**
     * 檢視合約明細(月租型)
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getCyclePackageInfoByPackageId(Integer packageId) throws Exception {
        String sql = "select cp.name company_name,cp.business_no,cmc.package_name,cmc.charge_cycle," +
                " cmc.base_quantity,cmc.single_price,cmc.max_price,cmc.free_quantity,cmc.free_month free_month_base, " +
                " cmc.pre_payment,cmc.sales_price,cmc.contract_limit,cmca.* " +
                " ,  pm.dealer_company_id, dcp.dealer_company_name, der.dealer_name, pm.dealer_id, pm.broker_cp2, pm.broker2, pm.broker_cp3, pm.broker3  " +
                " from package_mode pm left join company cp on (pm.company_id = cp.company_id) " +
                " left join charge_mode_cycle cmc on (pm.charge_id = cmc.charge_id) " +
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " left join dealer_company dcp on pm.dealer_company_id = dcp.dealer_company_id  " +
                " left join dealer der on pm.dealer_id = der.dealer_id  " +
                " where pm.package_id=? "; //pm.package_type=1 and
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map) query.uniqueResult();
    }

    /**
     * 檢視合約明細(級距型)
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getGradePackageInfoByPackageId(Integer packageId) throws Exception {
        String sql = "select cp.name company_name,cp.business_no, cmg.package_name," +
                " cmg.charge_cycle,cmg.base_quantity,cmg.free_month free_month_base, " +
                " cmg.pre_payment,cmg.sales_price,cmg.contract_limit," +
                " cmg.has_grade, cmg.grade_price, cmg.grade_cnt,  " +
                " cmca.* ,  pm.dealer_company_id, dcp.dealer_company_name, der.dealer_name, pm.dealer_id, pm.broker_cp2, pm.broker2, pm.broker_cp3, pm.broker3  " +
                " from package_mode pm left join company cp on (pm.company_id = cp.company_id) " +
                " left join charge_mode_grade cmg on (pm.charge_id = cmg.charge_id) " +
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " left join dealer_company dcp on pm.dealer_company_id = dcp.dealer_company_id  " +
                " left join dealer der on pm.dealer_id = der.dealer_id  " +
                " where pm.package_type= '2' and pm.package_id=? ";
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map) query.uniqueResult();
    }

    /**
     * 快過期合約續約
     * @param almostOut
     * @return
     * @throws Exception
     */
    public Map transactionContinuePackage(String almostOut, Integer modifierId) throws Exception{
        logger.info("transactionContinuePackage start = " + new java.util.Date());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<ContinuePackage>>(){}.getType();
        List<ContinuePackage> conPkgList = gson.fromJson(almostOut, collectionType);

        //找出所有要續約的合約(by almostOut (YYYYMM))
        for(int i=0; i<conPkgList.size(); i++){
            ContinuePackage continuePackage = conPkgList.get(i);
//            Integer packageId = Integer.parseInt(continuePackage.getPackageId());
            Integer companyId = Integer.parseInt(continuePackage.getCompanyId());

            //找出所有即將到期的合約(by almostOut (YYYYMM))
            List parameters = new ArrayList();
            StringBuffer sql = new StringBuffer();
            sql.append(" select pm.*, cmca.real_end_date, cmca.addition_quantity,cmca.free_month,cmca.gift_price,cmca.end_date  ");
            sql.append(" from  charge_mode_cycle_add cmca left join package_mode pm on pm.addition_id=cmca.addition_id ");
            sql.append(" where pm.company_id=? ");
            //只撈該用戶最新的一筆方案資料，作續約
            sql.append(" and pm.package_id = (select max(package_id) from package_mode where company_id=?) ");
            sql.append(" order by package_id desc ");
//            parameters.add(packageId);
            parameters.add(companyId);
            parameters.add(companyId);
            List packageList = createQuery(sql.toString(), parameters, null).list();

            java.sql.Date endDate = null;

            //將原合約續約
            for(int j=0; j<packageList.size(); j++){
                Map map = (Map)packageList.get(j);
                Integer packageType = (Integer)map.get("package_type");
                Integer chargeId = (Integer)map.get("charge_id");
                Integer dealerCompanyId = (Integer)map.get("dealer_company_id");
                Integer dealerId = (Integer)map.get("dealer_id");
                java.sql.Date realEndDate = (java.sql.Date)map.get("real_end_date");
                Integer additionQuantity = (Integer)map.get("addition_quantity");
                Integer freeMonth = (Integer)map.get("free_month");
                BigDecimal giftPrice = (BigDecimal)map.get("giftPrice");
                endDate = (java.sql.Date)map.get("end_date");
                String status = ((Character)map.get("status")).toString();

                //如果該用戶最新一筆方案的狀態不是生效中，就不續約
                if(!"1".equals(status)){
                    //status: 0=未生效，1=生效，2=作廢
                    continue;
                }

                //1.charge_mode_cycle_add
                ChargeModeCycleAddEntity chargeModeCycleAddEntity = new ChargeModeCycleAddEntity();
                java.util.Date _RealEndDate = new java.util.Date(realEndDate.getTime());
                Calendar newCal = Calendar.getInstance();
                newCal.setTime(_RealEndDate);
                newCal.add(Calendar.DATE, 1);
                Integer firstDay = newCal.get(Calendar.DAY_OF_MONTH);
                chargeModeCycleAddEntity.setRealStartDate(new java.sql.Date(newCal.getTimeInMillis())); //實際開始時間
                chargeModeCycleAddEntity.setStartDate(new java.sql.Date(timeUtils.getMonthOneDay(newCal.getTime()).getTime())); //計算開始時間
                newCal.setTime(_RealEndDate);
                newCal.add(Calendar.MONTH, 12);
                chargeModeCycleAddEntity.setRealEndDate(new java.sql.Date(newCal.getTimeInMillis())); //實際結束時間
                if(firstDay != 1){
                    newCal.add(Calendar.MONTH, -1);
                }
                chargeModeCycleAddEntity.setEndDate(new java.sql.Date(timeUtils.getMonthLastDay(newCal.getTime()).getTime())); //計算結束時間
                chargeModeCycleAddEntity.setAdditionId(0);
                chargeModeCycleAddEntity.setFreeMonth(0);
                chargeModeCycleAddEntity.setGiftPrice(new BigDecimal(0));
                saveEntity(chargeModeCycleAddEntity);
                Integer newAdditionId=chargeModeCycleAddEntity.getAdditionId();

                //2.package_mode
                PackageModeEntity packageModeEntity = new PackageModeEntity();
                packageModeEntity.setPackageType(packageType);
                packageModeEntity.setAdditionId(newAdditionId);
                packageModeEntity.setChargeId(chargeId);
                packageModeEntity.setCompanyId(companyId);
                packageModeEntity.setDealerCompanyId(dealerCompanyId);
                packageModeEntity.setDealerId(dealerId);
                packageModeEntity.setStatus("0"); //續約-未生效，要等入帳後才生效 (0.未生效 1.生效 2.作廢)
                saveEntity(packageModeEntity);
                Integer newPackageId = packageModeEntity.getPackageId();

                //3.bill_cycle, cash_detail, cash_master
                ChargeModeCycleEntity cycleEntity = null;
                ChargeModeGradeEntity gradeEntity = null;
                if(1 == packageType){
                    //月租型
                    cycleEntity = (ChargeModeCycleEntity) getEntity(ChargeModeCycleEntity.class, chargeId);
                } else if(2 == packageType){
                    //級距型
                    gradeEntity = (ChargeModeGradeEntity) getEntity(ChargeModeGradeEntity.class, chargeId);
                }
                CompanyChargeCycleBean bean = new CompanyChargeCycleBean();
                bean.setCompanyId(companyId);

                /**
                                * 計算
                                */
                //月租收費區間 1年繳 2.季繳 3.月繳.
                //合約生效後，產生月租帳單(bill_cycle和cash_flow的資料)
                int feePeriod = 0;
                if(1 == packageType){ //月租型
                    feePeriod = cycleEntity.getFeePeriod();
                } else if(2 == packageType){ //級距型
                    feePeriod = gradeEntity.getFeePeriod();
                }
                if(1 == feePeriod){ //1.年繳
                    genBillCashData(true, 0, -3, cycleEntity, packageModeEntity, bean, chargeModeCycleAddEntity, true, false, packageType, gradeEntity, modifierId);
                }else if(2 == feePeriod){ //2.季繳
                    genBillCashData(false, 3, -3, cycleEntity, packageModeEntity, bean, chargeModeCycleAddEntity, true, false, packageType, gradeEntity, modifierId);
                }else if(3 == feePeriod){ //3.月繳
                    genBillCashData(false, 1, -3, cycleEntity, packageModeEntity, bean, chargeModeCycleAddEntity, true, false, packageType, gradeEntity, modifierId);
                }

                //將上一份合約的超額結清
                //j把上一份合約最後前三個月的超額全部結清
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.MONTH, -2);
                String calYM = timeUtils.getYearMonth2(calendar.getTime());
//                new CalCycleDAO().CalOverIsEnd(companyId, calYM);

                break; //續約最後一筆的合約就好<==這行可以拿掉了
            }


        }
        logger.info("transactionContinuePackage end = " + new java.util.Date());
        return null;
    }

}
