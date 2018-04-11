package com.gate.web.facades;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.CalOver;
import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.CashDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.GiftVO;
import com.gate.web.formbeans.GiftBean;

import dao.CalCycleDAO;

@Service("calCycleService")
public class CalCycleServiceImp implements CalCycleService {
    protected static Logger logger = LogManager.getLogger(CalCycleServiceImp.class.getName());
	
	@Autowired
    CalCycleDAO calCycleDAO;

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    ChargeModeGradeRepository chargeModeGradeRepository;

    @Autowired
    ChargeModeCycleAddRepository chargeModeCycleAddRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    BillCycleService billCycleService;

    @Autowired
    CashDAO cashDAO;

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    CashMasterRepository cashMasterRepository;

    @Autowired
    CashDetailRepository cashDetailRepository;

    @Autowired
    DeductDetailRepository deductDetailRepository;

    @Autowired
    PrepayDeductMasterRepository prepayDeductMasterRepository;

    @Autowired
    PackageService packageService;

    public Map getBillCycleList(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = calCycleDAO.getBillCycleList(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return calCycleDAO.getYM();
    }

    public List getUserCompanyList() throws Exception{
        return calCycleDAO.getUserCompanyList();
    }

    /**
     * cash_master是否有這個計算月份的資料，有的話，則回傳master_id，否則建一個新的master，傳回master_id
     * @param outYm //出帳年月
     * @param companyId //用戶名稱
     * @return
     * @throws Exception
     */
    @Override
    public CashMasterEntity isHaveCashMaster(String outYm, Integer companyId, Integer modifierId) throws Exception {
        List<CashMasterEntity> cashMasterEntityList = cashMasterRepository.findByOutYmIsAndCompanyIdIs(outYm,companyId);
        CashMasterEntity cashMasterEntity = null;
        if(cashMasterEntityList.size() > 0){ //已有master_id了，取得master_id
            cashMasterEntity = cashMasterEntityList.get(0);

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

            cashMasterRepository.save(cashMaster);
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

    public CashDetailEntity createCashDetailEntityForCalOver(
            Integer companyId
            , String calYm
            , Integer cashMasterId
            , Integer billType
            , Integer packageId
            , Integer modifierId
            , BigDecimal sumOfPayOver ){
        //insert 一筆新的cash_detail'、cash_master
        CashDetailEntity cashDetailEntity = new CashDetailEntity();
        cashDetailEntity.setCompanyId(companyId); //公司名稱
        cashDetailEntity.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYm))); //計算年月
        cashDetailEntity.setOutYm(timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYm), 1))); //帳單年月
        cashDetailEntity.setCashMasterId(cashMasterId); //cash_master_id
        cashDetailEntity.setCashType(2); //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值
        cashDetailEntity.setBillType(billType); //帳單類型　1.月租 2.級距
        cashDetailEntity.setPackageId(packageId); //超額的cashDetail不紀錄packageId(超額的cashDetail記的packageId只能參考，不是真正值)，因為可能跨兩種不同的package。
        cashDetailEntity.setStatus(1); //1.生效 2.作廢
        cashDetailEntity.setModifierId(modifierId);
        cashDetailEntity.setCreatorId(modifierId);
        cashDetailEntity.setCreateDate(new Timestamp(new Date().getTime()));
        cashDetailEntity.setModifyDate(new Timestamp(new Date().getTime()));
        cashDetailEntity = cashDAO.calPriceTax(cashDetailEntity, sumOfPayOver, new BigDecimal(0), null);

        cashDetailRepository.save(cashDetailEntity);
        return cashDetailEntity;
    }

    //計算超額-某年月之前全部計算，並一次結清
    public void CalOverIsEnd(Integer companyId, String calYM, Integer modifierId) throws Exception{
        logger.info("CalOverIsEnd start = " + new java.util.Date());
        //1.找出該用戶在calYM之前的所有資料
        List<BillCycleEntity> beforeBillCycleList = calCycleDAO.getSumOfPayOverList(companyId, calYM);

        Integer packageId = null; //因為累計超額可能跟合約，所以超額cash_detail的package_id沒用
        Integer chargeType = null;
        //2.將這些筆數全部加總
        //isSum=false: 續約，結清之前的超額(先不在此作加總)
        for(int i=0; i<beforeBillCycleList.size(); i++){
            BillCycleEntity entity = (BillCycleEntity)beforeBillCycleList.get(i);
            List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
            billCycleEntityList.add(entity);
            packageId = entity.getPackageId();
            chargeType = entity.getBillType();
            calOverByCompany(companyId,modifierId,false,billCycleEntityList);
        }
        BigDecimal sumOfPayOver = calCycleDAO.getSumOfPayOver(companyId, calYM);
        CashMasterEntity  cashMasterEntity = isHaveCashMaster(timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYM), 1)), companyId, modifierId);
        CashDetailEntity cashDetailEntity = createCashDetailEntityForCalOver(
                companyId
                ,calYM
                ,cashMasterEntity.getCashMasterId()
                ,chargeType
                ,packageId
                ,modifierId
                ,sumOfPayOver
        );

        //計算預繳扣抵的部份
        sumOverOut(
                companyId
                , calYM
                , packageId
                , cashMasterEntity.getCashMasterId()
                , cashDetailEntity.getCashDetailId()
                , sumOfPayOver
                , null
                , true
                , chargeType
                , modifierId
        );
        logger.info("CalOverIsEnd end = " + new java.util.Date());
    }

    //計算超額-可選擇某幾筆計算且出帳
    // (需先選擇某用戶名稱,在js擋)
    // 某用戶的超額任選幾筆，合併出一筆帳單
    //calYM不代表計算那個年月，而是指:出帳時顯示的計算年月
    public boolean transactionCalOverToCash(String calYM, Integer companyId, String calOverAry, Integer modifierId) throws Exception {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CalOver>>(){}.getType();
        List<CalOver> calOverList = gson.fromJson(calOverAry, collectionType);

        //排序calOverList
        Collections.sort(calOverList);

        Integer packageId = null; //因為累計超額可能跟合約，所以超額cash_detail的package_id沒用
        Integer chargeType = null; //1.月租 2.級距
        Integer cnt = 0; //某公司共有幾筆的超額
        List<BillCycleEntity> cpOverList = new ArrayList<BillCycleEntity>(); //哪些bill_cycle的資料會被作計算
        //找出所有要計算的超額帳單by CalOver (YYYYMM))
        for(int i=0; i<calOverList.size(); i++) {
            CalOver calOver = calOverList.get(i);
            Integer billId = Integer.parseInt(calOver.getBillId());
            BillCycleEntity billCycleEntity = billCycleRepository.findByBillId(billId);
            Integer billCpId = billCycleEntity.getCompanyId();
            if(billCpId.intValue() != companyId.intValue()){
                continue; //如果該筆選擇的不是畫面選擇的用戶名稱，則不處理
            }
            packageId = billCycleEntity.getPackageId();
            chargeType = billCycleEntity.getBillType();
            List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
            billCycleEntityList.add(billCycleEntity);
            calOverByCompany(billCpId,modifierId,false, billCycleEntityList); //計算當個月的超額，先不判斷(累計超額是否超過某個值，需出帳)
            BillCycleEntity resultEntity = billCycleRepository.findByBillId(billId);
            if(null == resultEntity.getCashOutOverId()){
                cpOverList.add(resultEntity);
            }
        }
        BigDecimal sumOfPayOver = calCycleDAO.getSumOfPayOver(companyId, cpOverList);
        CashMasterEntity  cashMasterEntity = isHaveCashMaster(
                timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYM), 1))
                , companyId
                , modifierId
        );
        CashDetailEntity cashDetailEntity = createCashDetailEntityForCalOver(
                companyId
                ,calYM
                ,cashMasterEntity.getCashMasterId()
                ,chargeType
                ,packageId
                ,modifierId
                ,sumOfPayOver
        );
        sumOverOut(
                companyId
                , calYM
                , packageId
                , cashMasterEntity.getCashMasterId()
                , cashDetailEntity.getCashDetailId()
                , sumOfPayOver
                , cpOverList
                , false
                , chargeType
                , modifierId
        );

        return true;
    }

    //計算超額-by年月
    @Override
    public Integer batchCalOverByYearMonthAndCompanyId(Integer companyId, String yearMonth, Integer modifierId){
        Integer cnt = 0;
        List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByYearMonthIsAndCompanyIdIs(yearMonth,companyId);
        try{
            PackageModeEntity packageModeEntity = packageService.getCurrentPackageModeByYearMonth(companyId,yearMonth);
            if(packageModeEntity!=null){
                Date lastYearMonthDate = findLastYearMonthDateByBillCycleList(billCycleEntityList);
                billCycleEntityList = collectBillCycle(companyId,lastYearMonthDate,billCycleEntityList);
                billCycleListDataFilter(billCycleEntityList);
                prepareBillCycleListData(billCycleEntityList);
                writeBillCycleOverData(
                        billCycleEntityList
                        , true
                        , packageModeEntity
                        , modifierId
                );
                cnt++;
            }else{
                logger.info("CompanyId:" + companyId + " doesn't have enabled package mode");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return cnt;
    }

    /**
     * 合併計算
     * @param companyId
     * @return
     */
    public List<BillCycleEntity> collectBillCycle(Integer companyId,Date lastYearMonthDate,List<BillCycleEntity> billCycleEntityList){

        //曾經計算過但未出帳的記錄要一併併入計算，但年月不能大於選中的最大年月。
        List<BillCycleEntity> notYetOutBillCycleEntityList
                = billCycleRepository.findByCntOverIsNotNullAndPriceOverIsNotNullAndPayOverIsNotNullAndCashOutOverIdIsNullAndCompanyIdIsAndStatusIsNot(companyId,"2");
        //移除已經在清單中的資料
        for(BillCycleEntity notYetOutBillCycleEntity:notYetOutBillCycleEntityList){
            Date tempYearMonthDate = timeUtils.stringToDate(notYetOutBillCycleEntity.getYearMonth(),"yyyyMM");
            if(!billCycleEntityList.contains(notYetOutBillCycleEntity.getBillId())){
                if(tempYearMonthDate.before(lastYearMonthDate)){
                    billCycleEntityList.add(notYetOutBillCycleEntity);
                }
            }
        }
        return billCycleEntityList;
    }

    /**
     * 找出最大的年月
     * @param billCycleEntityList
     * @return
     */
    public Date findLastYearMonthDateByBillCycleList(List<BillCycleEntity> billCycleEntityList){
        //因為不會影響任何資料，直接回今天日期
        if(billCycleEntityList.size()==0){
            return new Date();
        }
        //找出最大的年月
        Date lastYearMonthDate = timeUtils.stringToDate(
                billCycleEntityList.get(billCycleEntityList.size()-1).getYearMonth()
                , "yyyyMM"
        );
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            Date tempYearMonthDate = timeUtils.stringToDate(
                    billCycleEntity.getYearMonth()
                    , "yyyyMM"
            );
            if(lastYearMonthDate.before(tempYearMonthDate)){
                lastYearMonthDate = tempYearMonthDate;
            }
        }
        return lastYearMonthDate;
    }

    //計算超額-多筆
    @Override
    /**
     * 計算超額
     * isSum 判斷是否累計超過某個值，要作加總
     * 帳單年月為billCycle記錄的最後一個年月。
     * @param billIdList
     * @param modifierId
     * @return
     * @throws Exception
     */
    public Integer batchCalOver(List<Integer> billIdList,Integer modifierId) throws Exception{
        Collections.sort(billIdList);
        //查出選擇的billCycle
        //根據公司類型分類
        Map<Integer,List<BillCycleEntity>> billCycleByCompanyMap = new HashMap<>();
        for(Integer billId: billIdList){
            BillCycleEntity billCycleEntity = billCycleRepository.findByBillId(billId);
            if(billCycleByCompanyMap.containsKey(billCycleEntity.getCompanyId())){
                billCycleByCompanyMap.get(billCycleEntity.getCompanyId()).add(billCycleEntity);
            }else{
                List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
                billCycleEntityList.add(billCycleEntity);
                billCycleByCompanyMap.put(billCycleEntity.getCompanyId(),billCycleEntityList);
            }
        }
        int cnt = 0;
        try {
            for(Integer companyId: billCycleByCompanyMap.keySet()) {
                Date lastYearMonthDate = findLastYearMonthDateByBillCycleList(billCycleByCompanyMap.get(companyId));
                List<BillCycleEntity> billCycleEntityList
                        = collectBillCycle(companyId, lastYearMonthDate,billCycleByCompanyMap.get(companyId));
                cnt+=calOverByCompany(companyId,modifierId,true,billCycleEntityList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return cnt;
        }
    }

    public Integer calOverByCompany(
            Integer companyId
            , Integer modifierId
            , Boolean isSum
            , List<BillCycleEntity> billCycleEntityList) {
        logger.info("CalOver start = " + new java.util.Date());
        Integer cnt = 0;  //計算了幾筆
        try{
            Date lastYearMonthDate = findLastYearMonthDateByBillCycleList(billCycleEntityList);
            collectBillCycle(companyId, lastYearMonthDate, billCycleEntityList);
            PackageModeEntity packageModeEntity = packageService.getCurrentPackageModeByYearMonth(companyId,timeUtils.getCurrentDateString("yyyyMM"));
            //應計算完所有的billCycleEntity後再決定超額資料。
            billCycleListDataFilter(billCycleEntityList);
            prepareBillCycleListData(billCycleEntityList);
            //該公司在某年月的那一筆billCycle
            writeBillCycleOverData(
                    billCycleEntityList
                    , isSum
                    , packageModeEntity
                    , modifierId
            );
            cnt = billCycleEntityList.size();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("CalOver end = " + new java.util.Date());
            return cnt;  //計算了幾筆
        }
    }

    /**
     *
     * @param billCycleEntityList
     * @throws Exception
     */
    public void billCycleListDataFilter(List<BillCycleEntity> billCycleEntityList) throws Exception {
        //移除狀態為作廢的筆數
        List<BillCycleEntity> removedBillCycleEntityList = new ArrayList<>();
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            //如果bill_cycle的Cash_Out_Over_Id不是null，則不作超額計算(已被計算過了(算到cash_detail裡))
            if(null != billCycleEntity.getCashOutOverId()){
                removedBillCycleEntityList.add(billCycleEntity);
                continue;
            }else if("2".equals(billCycleEntity.getStatus())){
                //作廢的不計算
                removedBillCycleEntityList.add(billCycleEntity);
                continue;
            }
        }
        billCycleEntityList.removeAll(removedBillCycleEntityList);
    }

    public void prepareBillCycleListData(List<BillCycleEntity> billCycleEntityList) throws Exception {
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            prepareBillCycleData(billCycleEntity);
        }
    }

    /**
     * 若要直接使用這個function，請注意要過瀘狀態為作廢及CashOutOverId不是null的資料，這些不應該被計入超額。
     * @param billCycleEntity
     * @throws Exception
     */
    public void prepareBillCycleData(BillCycleEntity billCycleEntity) throws Exception {
//        Integer useCnt
//                = calCycleDAO.calOverByCompany(billCycleEntity.getCompanyId(),billCycleEntity.getYearMonth());
        Integer useCnt
                = calCycleDAO.calOverByCompanyWithFromInvoiceAmountSummaryReport(billCycleEntity.getCompanyId(),billCycleEntity.getYearMonth());
        billCycleEntity.setCnt(useCnt); //當月使用張數
        Integer cntGift = (null == billCycleEntity.getCntGift() )?0:(billCycleEntity.getCntGift()); //贈送張數
        Integer cntLimit = billCycleEntity.getCntLimit(); //當月免費張數上限
        BigDecimal singlePrice = billCycleEntity.getSinglePrice(); //單一張發票收費價格
        BigDecimal priceMax = billCycleEntity.getPriceMax(); //當月繳款金額上限
        BigDecimal price = billCycleEntity.getPrice(); //月租金額
        //開始作超額計算...........，start
        //超限張數
        Integer cntOverTemp = useCnt - cntGift - cntLimit;
        BigDecimal cntOver = new BigDecimal(cntOverTemp);

        Integer billType = billCycleEntity.getBillType(); //合約類型 1.月租 2.級距
        Integer packageId = billCycleEntity.getPackageId();
        if(1 == billType){
            //月租型
            if(cntOverTemp < 0){
                billCycleEntity.setCntOver(0); //超限張數
                billCycleEntity.setPriceOver(new BigDecimal(0)); //超限金額
                billCycleEntity.setPayOver(new BigDecimal(0)); //超額金額
            }else{
                billCycleEntity.setCntOver(cntOverTemp); //超限張數

                //超限金額 = 超限張數*單一張發票收費價格
                BigDecimal priceOver = cntOver.multiply(singlePrice) ; //超限金額
                billCycleEntity.setPriceOver(priceOver); //超限金額

                BigDecimal priceMaxTemp = priceMax.subtract(price);

                //超額金額= 超限金額，如果超額金額大於最大繳款上限，則繳繳款金額上限
                if(priceMaxTemp.compareTo(priceOver) == -1){  //-1、0、1，分别表示小於、等於、大於
                    billCycleEntity.setPayOver(priceMaxTemp); //超額金額
                }else{
                    billCycleEntity.setPayOver(priceOver); //超額金額
                }
            }
        } else if(2 == billType){
            //級距型
            if(cntOverTemp < 0){
                billCycleEntity.setCntOver(0); //超限張數
                billCycleEntity.setPriceOver(new BigDecimal(0)); //超限金額
                billCycleEntity.setPayOver(new BigDecimal(0)); //超額金額
            }else {
                billCycleEntity.setCntOver(cntOverTemp); //超限張數
                //找出該合約的方案
                PackageModeEntity packageModeEntity = packageModeRepository.findByPackageId(packageId);
                ChargeModeGradeEntity chargeModeGradeEntity = chargeModeGradeRepository.findByChargeId(packageModeEntity.getChargeId());
                String hasGrade = chargeModeGradeEntity.getHasGrade(); //是否有級距表?
                if("N".equals(hasGrade)){
                    //不使用級距表
                    Integer gradeCnt = chargeModeGradeEntity.getGradeCnt(); //級距區間張數
                    Integer gradePrice = chargeModeGradeEntity.getGradePrice(); //級距區間費用
                    boolean isDivisible = (cntOverTemp%gradeCnt) == 0 ;
                    Integer grades = cntOverTemp/gradeCnt;
                    if(!isDivisible){grades = grades + 1;}

                    BigDecimal priceOver = new BigDecimal(grades*gradePrice); //超限金額
                    billCycleEntity.setPriceOver(priceOver); //超限金額
                    billCycleEntity.setPayOver(priceOver); //超額金額
                } else if("Y".equals(hasGrade)){
                    //使用級距表
                    List<GradeEntity> gradeList = gradeRepository.findByChargeId(chargeModeGradeEntity.getChargeId());
                    BigDecimal priceOver = new BigDecimal(0);
                    for(int gradeIndex=0; gradeIndex<gradeList.size(); gradeIndex++){
                        GradeEntity gradeEntity = (GradeEntity) gradeList.get(gradeIndex);
                        Integer cntStart = gradeEntity.getCntStart();
                        Integer cntEnd = gradeEntity.getCntEnd();
                        Integer gradePrice = gradeEntity.getPrice();
                        if(cntOverTemp>=cntStart && cntOverTemp<=cntEnd){
                            priceOver = priceOver.add(new BigDecimal(gradePrice));
                        } else if(cntOverTemp>=cntEnd){
                            priceOver = priceOver.add(new BigDecimal(gradePrice));
                        }
                    }
                    billCycleEntity.setPriceOver(priceOver); //超限金額
                    billCycleEntity.setPayOver(priceOver); //超額金額
                }
            }
        }
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
        List prepayDeductMasterList = calCycleDAO.getSearchEntity(PrepayDeductMasterEntity.class, searchPrepayDeductMasterEntity);
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
                cashDetailEntity_forDeduct.setModifierId(modifierId);
                cashDetailEntity_forDeduct.setCreatorId(modifierId);
                cashDetailEntity_forDeduct.setCreateDate(new Timestamp(new Date().getTime()));
                cashDetailEntity_forDeduct.setModifyDate(new Timestamp(new Date().getTime()));

                if(amount < sumOver){
                    //剩餘金額不夠扣抵，就先扣抵「剩餘可作扣抵的錢」
                    BigDecimal minusSumOfPayOver = new BigDecimal(0).subtract(new BigDecimal(amount));
                    cashDetailEntity_forDeduct = cashDAO.calPriceTax(cashDetailEntity_forDeduct, minusSumOfPayOver, new BigDecimal(0), "超額扣抵");
                    cashDetailRepository.save(cashDetailEntity_forDeduct);

                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(master.getPrepayDeductMasterId());
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(cpId);
                    deductDetailEntity.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYM)));
                    deductDetailEntity.setDeductType(2); //1.月租 2.超額(由此可知到抵扣抵了哪類金額(可搭prepay_deduct_master的is_enable_over、is_enable_cycle使用))
                    deductDetailEntity.setMoney(-amount);
                    deductDetailRepository.save(deductDetailEntity);

                    master.setAmount(0);
                    prepayDeductMasterRepository.save(master);

                } else {
                    //剩餘金額夠扣抵，就作扣抵
                    Integer leftAmount = amount-sumOver;

                    BigDecimal minusSumOfPayOver = new BigDecimal(0).subtract(sumOfPayOver);
                    cashDetailEntity_forDeduct = cashDAO.calPriceTax(cashDetailEntity_forDeduct, minusSumOfPayOver, new BigDecimal(0), "超額扣抵");
                    cashDetailRepository.save(cashDetailEntity_forDeduct);

                    DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
                    deductDetailEntity.setPrepayDeductMasterId(master.getPrepayDeductMasterId());
                    deductDetailEntity.setCashDetailId(cashDetailId);
                    deductDetailEntity.setCompanyId(cpId);

                    deductDetailEntity.setCalYm(timeUtils.getYYYYMM(timeUtils.parseDate(calYM)));
                    deductDetailEntity.setDeductType(2); //1.月租 2.超額(由此可知到抵扣抵了哪類金額(可搭prepay_deduct_master的is_enable_over、is_enable_cycle使用))
                    deductDetailEntity.setMoney(-sumOver);
                    deductDetailRepository.save(deductDetailEntity);

                    master.setAmount(leftAmount);
                    prepayDeductMasterRepository.save(master);
                }
            }
            break;
        }

        logger.info("sumOverOut end = " + new java.util.Date());
    }

    /**
     * 若該次的billCycle加總大於500，應於超額明細中每項超額的資料都寫入cashDetailId
     * isSum=true: 每月計算超額 isSum=false: 續約，結清之前的超額(先不在此作加總)
     * @param billCycleEntityList
     * @param isSum
     * @param modifierId
     * @throws Exception
     */
    public void writeBillCycleOverData(
            List<BillCycleEntity> billCycleEntityList
            , boolean isSum
            , PackageModeEntity packageModeEntity
            , Integer modifierId) throws Exception {
        BigDecimal sumOver = BigDecimal.ZERO;
        //要使用已經存在的CashMaster進行更新。
        Date billYearMonth= null;
        for(BillCycleEntity billCycleEntity : billCycleEntityList){
            //開始作超額計算...........，end
            calCycleDAO.saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());
            //3.如果超額達500元，將資料寫cash_flow table
            //使用最後超額的年月作為帳單年月
            Date otherBillCycleDate = timeUtils.parseDate(billCycleEntity.getYearMonth());
            if(billYearMonth==null || billYearMonth.before(otherBillCycleDate)){
                billYearMonth = otherBillCycleDate;
            }
            BigDecimal sumOfPayOver = billCycleEntity.getPayOver();
            sumOver = sumOver.add(sumOfPayOver);
        }
        if(isSum == true){
            Date lastYearMonthDate = findLastYearMonthDateByBillCycleList(billCycleEntityList);
            //若最後一個billCycleEntity的年月為奇數則不寫出帳單。
            Calendar lastYearMonthCalendar = Calendar.getInstance();
            lastYearMonthCalendar.setTime(lastYearMonthDate);
            //陣列月份，要加一。
            Integer lastMonth = lastYearMonthCalendar.get(Calendar.MONTH)+1;
            //該超額總額大於500元，且非奇數月
            if(sumOver.doubleValue() > 500d
                    && lastMonth%2==0) {
                //只有大於五百的時候要出帳。
                CashMasterEntity cashMasterEntity = isHaveCashMaster(
                        timeUtils.getYYYYMM(timeUtils.addMonth(billYearMonth, 1))
                        , packageModeEntity.getCompanyId()
                        , modifierId
                );
                CashDetailEntity cashDetailEntity = createCashDetailEntityForCalOver(
                        cashMasterEntity.getCompanyId()
                        , timeUtils.getYYYYMM(billYearMonth)
                        , cashMasterEntity.getCashMasterId()
                        , packageModeEntity.getPackageType()
                        , packageModeEntity.getPackageId()
                        , modifierId
                        , sumOver
                );
                sumOverOut(
                        packageModeEntity.getCompanyId()
                        , timeUtils.getCurrentDateString("yyyyMM")
                        , packageModeEntity.getPackageId()
                        , cashMasterEntity.getCashMasterId()
                        , cashDetailEntity.getCashDetailId()
                        , sumOver
                        , billCycleEntityList
                        , true
                        , packageModeEntity.getPackageType()
                        , modifierId
                );
            }
        }
    }

    public boolean calOverToCash(String calYM, Integer companyId, String calOverAry, Integer modifierId) throws Exception {
        return transactionCalOverToCash(calYM, companyId, calOverAry, modifierId);
    }

    public void updateGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(entity, bean);
        calCycleDAO.updateEntity(entity, entity.getGiftId());
    }

    public Integer insertGift(GiftBean bean) throws Exception {
        GiftEntity entity = new GiftEntity();
        BeanUtils.copyProperties(entity, bean);
        calCycleDAO.saveEntity(entity);
        return null;
    }

    public GiftVO findGiftByBillId(Integer billId) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)calCycleDAO.getEntity(BillCycleEntity.class, billId);
        GiftVO giftVO = new GiftVO();
        giftVO.setBillId(billCycleEntity.getBillId());
        giftVO.setCompanyId(billCycleEntity.getCompanyId());
        giftVO.setCalYM(billCycleEntity.getYearMonth());
        giftVO.setCntGift(billCycleEntity.getCntGift());
        giftVO.setIsCalculated(true); //超額已經計算的話，不能再修改贈送點數了。
        if(null == billCycleEntity.getCashOutOverId()){
            giftVO.setIsCalculated(false); //超額已經計算的話，不能再修改贈送點數了。
        }
        CompanyEntity cpEntity = (CompanyEntity)calCycleDAO.getEntity(CompanyEntity.class, billCycleEntity.getCompanyId());
        giftVO.setCompanyName(cpEntity.getName());

        return giftVO;
    }

    public boolean updateCntGiftByBillId(Integer billId, Integer cntGift) throws Exception {
        BillCycleEntity billCycleEntity = (BillCycleEntity)calCycleDAO.getEntity(BillCycleEntity.class, billId);
        billCycleEntity.setCntGift(cntGift);
        calCycleDAO.saveOrUpdateEntity(billCycleEntity, billId);
        return true;
    }

    public boolean deleteGiftByGiftId(Integer giftId) throws Exception {
        GiftEntity giftEntity = (GiftEntity) calCycleDAO.getEntity(GiftEntity.class,giftId);
        calCycleDAO.deleteEntity(giftEntity);
        return true;
    }

    public Integer sendOverMailYM(String calYM) throws Exception{
        return calCycleDAO.sendOverMailYM(calYM);
    }

    public Integer sendOverMail(String calOverAry) throws Exception{
        return calCycleDAO.sendOverMail(calOverAry);
    }
}
