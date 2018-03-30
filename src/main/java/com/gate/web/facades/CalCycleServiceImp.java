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
    GradeRepository gradeRepository;

    @Autowired
    BillCycleService billCycleService;

    @Autowired
    CashDAO cashDAO;

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    CashMasterRepository cashMasterRepository;

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
            String yearMonth = entity.getYearMonth();
            packageId = entity.getPackageId();
            chargeType = entity.getBillType();
            CalOver(yearMonth, companyId, false, modifierId);
        }
        BigDecimal sumOfPayOver = calCycleDAO.getSumOfPayOver(companyId, calYM);
        CashMasterEntity  cashMasterEntity = isHaveCashMaster(timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYM), 1)), companyId, modifierId);
        calCycleDAO.sumOverOut(companyId, calYM, packageId,cashMasterEntity.getCashMasterId(), sumOfPayOver, null, true, chargeType, modifierId);

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
            String billYearMonth = billCycleEntity.getYearMonth();
            CalOver(billYearMonth, billCpId, false, modifierId); //計算當個月的超額，先不判斷(累計超額是否超過某個值，需出帳)
            BillCycleEntity resultEntity = billCycleRepository.findByBillId(billId);
            if(null == resultEntity.getCashOutOverId()){
                cpOverList.add(resultEntity);
            }
        }
        BigDecimal sumOfPayOver = calCycleDAO.getSumOfPayOver(companyId, cpOverList);
        CashMasterEntity  cashMasterEntity = isHaveCashMaster(timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(calYM), 1)), companyId, modifierId);
        calCycleDAO.sumOverOut(companyId, calYM, packageId,cashMasterEntity.getCashMasterId(), sumOfPayOver, cpOverList, false, chargeType, modifierId);

        return true;
    }

    /**計算超額
     * @param isSum 判斷是否累計超過某個值，要作加總
     * @return
     * @throws Exception
     */
    public boolean transactionCalOver(BillCycleEntity billCycleEntity, Integer modifierId,boolean isSum) {
        boolean result = false;
        try{
            prepareBillCycleData(billCycleEntity);
            writeBillCycleData(billCycleEntity,isSum,modifierId);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    //計算超額-by年月
    @Override
    public Integer batchCalOverByYearMonth(String yearMonth,Integer modifierId){
        Integer cnt = 0;
        List<BillCycleEntity> billCycleEntityList = billCycleService.getAllCompaniesBillCycle(yearMonth);
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            boolean result = transactionCalOver(billCycleEntity,modifierId,true);
            if(result){
                cnt++;
            }
        }
        return cnt;
    }

    //計算超額-多筆
    @Override
    public Integer batchCalOver(List<Integer> billIdList, Integer modifierId) throws Exception{
        Collections.sort(billIdList);
        List<BillCycleEntity> billCycleEntityList = new ArrayList<>();
        for(Integer billId: billIdList){
            billCycleEntityList.add(billCycleRepository.findByBillId(billId));
        }
        Integer cnt = 0;
        for(BillCycleEntity billCycleEntity: billCycleEntityList){
            //多筆是true
            boolean result = transactionCalOver(billCycleEntity,modifierId,true);
            if(result){
                cnt++;
            }
        }
        return cnt;
    }

    public Integer CalOver(String calYM, Integer companyId, boolean isSum, Integer modifierId) throws Exception{
        logger.info("CalOver start = " + new java.util.Date());
        Integer cnt = 0;  //計算了幾筆

        //找出所有的公司-by年月時，會找出所有的公司
        List<CompanyEntity> companyEntityList = new ArrayList<>();
        if(null != companyId){
            companyEntityList.add(companyRepository.findByCompanyId(companyId));
        }else{
            companyEntityList.addAll(companyRepository.findAll());
        }

        for(CompanyEntity companyEntity: companyEntityList){
            List<BillCycleEntity> billCycleList = billCycleRepository.findByYearMonthIsAndCompanyIdIs(calYM,companyEntity.getCompanyId());

            //該公司在某年月的那一筆billCycle
            for(BillCycleEntity billCycleEntity: billCycleList){
                prepareBillCycleData(billCycleEntity);
                writeBillCycleData(billCycleEntity,isSum,modifierId);
            }
        }

        logger.info("CalOver end = " + new java.util.Date());
        return cnt;  //計算了幾筆
    }

    public void prepareBillCycleData(BillCycleEntity billCycleEntity) throws Exception {
        //如果bill_cycle的Cash_Out_Over_Id不是null，則不作超額計算(已被計算過了(算到cash_detail裡))
        if(null != billCycleEntity.getCashOutOverId()){
            return;
        }
        //作廢的不計算
        if("2".equals(billCycleEntity.getStatus())){
            return;
        }
        Integer useCnt = calCycleDAO.calOverByCompany(billCycleEntity.getCompanyId(),billCycleEntity.getYearMonth());
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

    public void writeBillCycleData(BillCycleEntity billCycleEntity,boolean isSum,Integer modifierId) throws Exception {
        //開始作超額計算...........，end
        calCycleDAO.saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());
        if(isSum == true){ //isSum=true: 每月計算超額 isSum=false: 續約，結清之前的超額(先不在此作加總)
            //3.如果超額達300元，將資料寫cash_flow table
            BigDecimal sumOfPayOver = calCycleDAO.getSumOfPayOver(billCycleEntity.getCompanyId(), billCycleEntity.getYearMonth());
            double sumOver = sumOfPayOver.doubleValue();
            //該超額總額大於500元
            if(sumOver > 500d) {
                CashMasterEntity cashMasterEntity = isHaveCashMaster(
                        timeUtils.getYYYYMM(timeUtils.addMonth(timeUtils.parseDate(billCycleEntity.getYearMonth()), 1))
                        , billCycleEntity.getCompanyId()
                        , modifierId
                );
                calCycleDAO.sumOverOut(
                        billCycleEntity.getCompanyId()
                        , billCycleEntity.getYearMonth()
                        , billCycleEntity.getPackageId()
                        , cashMasterEntity.getCashMasterId()
                        , sumOfPayOver
                        , null
                        , true
                        , billCycleEntity.getBillType()
                        , modifierId
                );
            }
        }
    }

    public boolean calOverToCash(String calYM, Integer companyId, String calOverAry, Integer modifierId) throws Exception {
        return transactionCalOverToCash(calYM, companyId, calOverAry, modifierId);
    }

//    public List calOver(String calYM, String companyId) throws Exception {
//        return dao.transactionCalOver(calYM, companyId);
//    }

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
