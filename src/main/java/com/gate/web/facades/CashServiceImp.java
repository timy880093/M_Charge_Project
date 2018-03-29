package com.gate.web.facades;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gate.utils.ExcelPoiWrapper;
import com.gate.utils.FieldUtils;
import com.gate.utils.TimeUtils;
import com.gate.web.beans.CashDetailBean;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.*;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.reportModel.InvoiceBatchRecord;
import com.gateweb.reportModel.OrderCsv;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;

import dao.CashDAO;

@Service("cashService")
public class CashServiceImp implements CashService {

    private Logger logger = LogManager.getLogger(this.getClass().getName());

	@Autowired
    CashDAO cashDAO;

    @Autowired
    TimeUtils timeUtils;

    @Autowired
    FieldUtils fieldUtils;

    @Autowired
    CashMasterRepository cashMasterRepository;

    @Autowired
    CashDetailRepository cashDetailRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    PackageModeRepository packageModeRepository;

    @Autowired
    ChargeModeCycleRepository chargeModeCycleRepository;

    @Autowired
    ChargeModeGradeRepository chargeModeGradeRepository;

    @Autowired
    BillCycleRepository billCycleRepository;

    @Autowired
    PrepayDeductMasterRepository prepayDeductMasterRepository;

    @Autowired
    DeductDetailRepository deductDetailRepository;

    public Map getCashMaster(QuerySettingVO querySettingVO) throws Exception {
        Map returnMap = cashDAO.getCashMaster(querySettingVO);
        return returnMap;
    }

    public List getYM() throws Exception {
        return cashDAO.getYM();
    }

    public List getCompany() throws Exception {
        return cashDAO.getCompnay();
    }

    public Integer out(String masterIdAry) throws Exception{
        return cashDAO.transactionSumOut(masterIdAry);
    }

    public Integer outYM(String outYM, Integer userCompanyId) throws Exception{
        return cashDAO.transactionSumOutYM(outYM, userCompanyId);
    }

    public String excelSumIn(String businesscode, String inDate, String bankYM, Double inMoney) throws Exception{
        return cashDAO.transactionExcelSumIn(businesscode, inDate, bankYM, inMoney);
    }

    public List<CashDetailVO> getCashDetailListByMasterId(Integer cashMasterId) throws Exception{
        return cashDAO.getCashDetailListByMasterId(cashMasterId);
    }

    public List<BillCycleEntity> getOverListByDetailId(Integer cashDetailId) throws Exception{
        return cashDAO.getOverListByDetailId(cashDetailId);
    }

    public CashMasterVO getCashMasterByMasterId(Integer cashMasterId) throws  Exception{
        return cashDAO.getCashMasterByMasterId(cashMasterId);
    }

    public boolean updateCashDetail(Integer cashDetailId, Double diffPrice, String diffPriceNote) throws Exception{
        return cashDAO.transactionUpdateCashDetail(cashDetailId, diffPrice, diffPriceNote);
    }

    public boolean in(Integer cashMasterId, Double inAmount, String inDate, String inNote) throws Exception{
        return cashDAO.transactionIn(cashMasterId, inAmount, inDate, inNote);
    }

    public List getCashMasterDetail(String ym) throws Exception{
        return cashDAO.getCashMasterDetail(ym);
    }

    public List getCashMasterDetail(String ym, String destJson) throws Exception{
        return cashDAO.getCashMasterDetail(ym, destJson);
    }

    /**
     * 取消計算
     * @param cashDetailId
     * @return
     * @throws Exception
     */
    @Override
    public boolean transactionCancelOver(Integer cashMasterId, Integer cashDetailId) throws Exception{
        //1.找出bill_cycle的over_id有cash_detail_id的資料
        List<BillCycleEntity> billCycleEntityList = billCycleRepository.findByCashOutOverId(cashDetailId);

        //2.把這些bill_cycle計算超額的值清掉(包括over_id)
        for(BillCycleEntity billCycleEntity : billCycleEntityList){
            billCycleEntity.setCnt(null);
            billCycleEntity.setCntGift(null);
            billCycleEntity.setCntOver(null);
            billCycleEntity.setPriceOver(null);
            billCycleEntity.setPayOver(null);
            billCycleEntity.setCashOutOverId(null);
            billCycleEntity.setCashInOverId(null);
//            billCycleRepository.save(billCycleEntity);
            billCycleRepository.delete(billCycleEntity);
        }

        //3.把這筆cash_detail刪掉
        CashDetailEntity cashDetailEntity = cashDetailRepository.findByCashDetailId(cashDetailId);
        cashDetailRepository.delete(cashDetailEntity);
        //BigDecimal noTaxInclusivePrice = cashDetailEntity.getNoTaxInclusivePrice();
        //undo 2017/12/1 robinson edit
        //假如有公司預佣金部分須把code加回去
        Integer companyId = cashDetailEntity.getCompanyId();
        String calYM = cashDetailEntity.getCalYm();

        //4.把這筆cash_detail對應的扣抵的資料也刪掉
        //不可加金額條件，因為該筆帳單有可能用完最後的預用金，所以超額不會全扣抵
        //searchDeductCashDetailEntity.setNoTaxInclusivePrice(new BigDecimal(0).subtract(noTaxInclusivePrice));

        //先查詢他有沒有預用金
        //5.在prepay_deduct_master把錢加回去
        List<PrepayDeductMasterEntity> prepayDeductMasterList = prepayDeductMasterRepository.findByCompanyId(companyId);

        if(prepayDeductMasterList.size()!=0){
            //根據PrepayDeductMaster查出為扣抵的DeductDetail
            for(PrepayDeductMasterEntity prepayDeductMasterEntity:prepayDeductMasterList){
                Integer deductMoney = 0;
                List<DeductDetailEntity> deductDetailEntityList
                        = deductDetailRepository.findByPrepayDeductMasterIdIsAndCalYmIsAndDeductTypeIs(
                            prepayDeductMasterEntity.getPrepayDeductMasterId()
                            ,calYM
                            ,2
                );
                //根據DeductDetail查出產生的cashDetail
                for(DeductDetailEntity deductDetailEntity: deductDetailEntityList){
                    CashDetailEntity deductCashDetailEntity
                            = cashDetailRepository.findByCashDetailIdIsAndCashTypeIs(deductDetailEntity.getCashDetailId(),7);
                    //刪除cashDetail及deductDetail
                    cashDetailRepository.delete(deductCashDetailEntity);
                    deductDetailRepository.delete(deductDetailEntity);
                    deductMoney = 0-deductCashDetailEntity.getNoTaxInclusivePrice().intValue();
                }
                Integer amount = prepayDeductMasterEntity.getAmount();
                amount = amount + deductMoney;
                prepayDeductMasterEntity.setAmount(amount);
                prepayDeductMasterRepository.save(prepayDeductMasterEntity);
            }
        }

//        //6.在deduct_detail增加一筆還原(加回去)的紀錄
//        DeductDetailEntity deductDetailEntity = new DeductDetailEntity();
//        deductDetailEntity.setPrepayDeductMasterId(prepayDeductMasterId);
//        deductDetailEntity.setCashDetailId(0);
//        deductDetailEntity.setCompanyId(companyId);
//        deductDetailEntity.setCalYm(calYM);
//        deductDetailEntity.setDeductType(6);
//        deductDetailEntity.setMoney(deductMoney);
//        deductDetailRepository.save(deductDetailEntity);

        return false;
    }

    public Integer cancelOutYM(String outYM) throws Exception {
        return cashDAO.transactionCancelOutYM(outYM);
    }

    public Integer cancelOut(String masterIdAry) throws Exception {
        return cashDAO.transactionCancelOut(masterIdAry);
    }

    public Integer sendBillMailYM(String outYM) throws Exception{
        return cashDAO.transactionSendBillMailYM(outYM);
    }

    public Integer sendBillMail(String masterIdAry) throws Exception{
        return cashDAO.transactionSendBillMail(masterIdAry);
    }

    public Integer transactionCancelIn(String strCashMasterId) throws Exception{
        return cashDAO.transactionCancelIn(strCashMasterId);
    }

    public boolean cancelPrepay(Integer cashDetailId) throws Exception{
        return cashDAO.transactionCancelPrepay(cashDetailId);
    }

    public boolean delCashMaster(Integer cashMasterId)throws Exception{
        //刪除帳單(帳單裡沒有任何明細，則可刪除)
        //先檢查帳單裡是否沒有任何明細
        boolean isCashMasterEmpty = isCashMasterEmpty(cashMasterId);

        if(isCashMasterEmpty){
            //刪除帳單
            CashMasterEntity cashMasterEntity = cashMasterRepository.findByCashMasterId(cashMasterId);
            cashMasterRepository.delete(cashMasterEntity);
            return true;
        }else{
            return false;
        }
    }

    //檢查帳單裡是否沒有任何明細
    public boolean isCashMasterEmpty(Integer cashMasterId)throws Exception{
        CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
        searchCashDetailEntity.setCashMasterId(cashMasterId);
        List<CashDetailEntity> cashDetailEntityList = cashDetailRepository.findByCashMasterId(cashMasterId);
        if(null == cashDetailEntityList || cashDetailEntityList.size() == 0){
            return true; //沒有任何明細
        } else {
            return false; //有明細
        }
    }

    //使用CashVO的資料建立訂單。
    @Override
    public List<OrderCsv> genOrderCsvListByCashVO(Long sellerCompanyId, CashVO cashVO){
        CompanyEntity sellerCompany = companyRepository.findByCompanyId(sellerCompanyId.intValue());
        CompanyEntity buyerCompany = companyRepository.findByCompanyId(cashVO.getCashMasterEntity().getCompanyId());
        return genOrderCsvList(sellerCompany,buyerCompany,cashVO);
    }

    //匯出發票Excel的資料-批次(by年月)
    public List getInvoiceItem(String ym) throws Exception{
        CashMasterEntity searchMaster = new CashMasterEntity();
        searchMaster.setOutYm(ym);
        List masterList =  cashDAO.getSearchEntity(CashMasterEntity.class, searchMaster);
        return getInvoiceItemList(masterList);
    }

    //取得CashMasterEntityList
    public List<CashVO> getCashMasterEntityList(String yearMonth) throws Exception{
        List<CashVO> resultList = new ArrayList<>();
        CashMasterEntity conditionEntity = new CashMasterEntity();
        conditionEntity.setOutYm(yearMonth);
        List<CashMasterEntity> cashMasterEntityList = cashMasterRepository.searchLikeVo(conditionEntity);
        for(CashMasterEntity cashMasterEntity: cashMasterEntityList){
            CashDetailEntity cashDetailEntity = new CashDetailEntity();
            cashDetailEntity.setCashMasterId(cashMasterEntity.getCashMasterId());
            List<CashDetailEntity> cashDetailEntityList = cashDetailRepository.searchLikeVo(cashDetailEntity);
            CashVO cashVO = new CashVO();
            cashVO.setCashMasterEntity(cashMasterEntity);
            cashVO.setCashDetailEntityList(cashDetailEntityList);
            resultList.add(cashVO);
        }
        return resultList;
    }

    //取得CashMasterEntityListById
    @Override
    public CashVO findCashVoById(Integer cashMasterId) throws Exception{
        CashVO cashVO = new CashVO();
        CashMasterEntity cashMasterEntity = cashMasterRepository.findByCashMasterId(cashMasterId);
        List<CashDetailEntity> cashDetailEntityList
                = cashDetailRepository.findByCashMasterId(cashMasterId.intValue());
        if(cashMasterEntity!=null){
            cashVO.setCashMasterEntity(cashMasterEntity);
        }
        if(cashDetailEntityList.size()!=0){
            cashVO.setCashDetailEntityList(cashDetailEntityList);
        }
        return cashVO;
    }

    //匯出發票Excel的資料-多筆
    public List getInvoiceItem(String ym, String destJson) throws Exception{
        List<CashMasterEntity> selectOutList = new ArrayList<CashMasterEntity>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(destJson, collectionType);

        for(CashMasterBean masterBean:cashMasterList){
            Integer masterId = masterBean.getCashMasterId();
            CashMasterEntity masterEntity = (CashMasterEntity)cashDAO.getEntity(CashMasterEntity.class, masterId);
            selectOutList.add(masterEntity);
        }

        return getInvoiceItemList(selectOutList);
    }

    public boolean exportCashMasterBusinessLogicContinueFilter(CashMasterEntity cashMasterEntity){
        boolean result = false;
        Integer status = cashMasterEntity.getStatus(); //1.生效 3.出帳 4.入帳 5.佣金
        if(status < 3){ //未出帳的帳單，不能開發票
            result = true;
        }

        //如果用戶該月要繳的錢為0元，就不匯出到excel了。
        if(cashMasterEntity.getTaxInclusiveAmount()==null
                || cashMasterEntity.getTaxInclusiveAmount().compareTo(BigDecimal.ZERO)==0){
            result = true;
        }

        //0000301: 首次不匯出到excel(因為首次是先開發票，在由業務帶機器去裝機，所以不需要再匯出發票了。)
        String isFirst = cashMasterEntity.getIsFirst();
        if(null!=isFirst && "1".equals(isFirst)){
            result = true;
        }
        return result;
    }

    public boolean exportCashDetailBusinessLogicContinueFilter(CashDetailEntity cashDetailEntity){
        boolean result = false;
        //如果該筆detail是作廢，就不要匯出至excel裡
        if(null != cashDetailEntity.getStatus() && cashDetailEntity.getStatus() != 1){
            result = true;
        }
        return result;
    }

    public String getPackageDescriptionByCashType(CashDetailEntity cashDetailEntity){
        String itemName = "";
        try{
            //找出package name
            Integer feePeriod = 0;
            String packageName = "";

            //只有「月租型」和「級距型」，才會有方案名稱(不管是月租or超額)。
            Integer billType = cashDetailEntity.getBillType(); //1.月租 2.級距 3.預繳
            if(1 == billType || 2 == billType) { //也可用cashDetail的cashType來判斷，不過這裡用billType來作判斷。
                Integer packageId = cashDetailEntity.getPackageId();
                PackageModeEntity packageModeEntity = packageModeRepository.findByPackageId(packageId);
                Integer packageType = packageModeEntity.getPackageType();
                Integer chargeId = packageModeEntity.getChargeId();

                if(1 == packageType){//月租型
                    ChargeModeCycleEntity cycleEntity = chargeModeCycleRepository.findByChargeId(chargeId);
                    feePeriod = cycleEntity.getFeePeriod(); //1.年繳 2.季繳
                    packageName = cycleEntity.getPackageName();
                }else if( 2 == packageType){ //級距型
                    ChargeModeGradeEntity gradeEntity = chargeModeGradeRepository.findByChargeId(chargeId);
                    feePeriod = gradeEntity.getFeePeriod();
                    packageName = gradeEntity.getPackageName();
                }
            }

            //1.月租or級距 2.超額 6.預繳 7.扣抵
            switch(cashDetailEntity.getCashType()){
                case 1: //月租預繳
                    if(1 == feePeriod){
                        itemName = packageName + "預繳"; //年繳
                    }else{
                        itemName = packageName + "預繳"; //季繳
                    }
                    break;
                case 2: //超額
                    itemName = packageName + "超額";
                    break;
                case 6: //預繳
                    itemName = "預繳";
                    break;
                case 7: //扣抵
                    itemName = "扣抵";
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return itemName;
        }
    }

    public BigDecimal getTaxInclusivePrice(CashDetailEntity cashDetailEntity){
        BigDecimal taxInclusivePrice = cashDetailEntity.getTaxInclusivePrice().setScale(2, BigDecimal.ROUND_HALF_UP);
        if((null == taxInclusivePrice) || (0 ==taxInclusivePrice.compareTo(new BigDecimal(0)))){
            taxInclusivePrice = new BigDecimal(0);
        }
        return taxInclusivePrice;
    }

    public List<OrderCsv> genOrderCsvList(
            CompanyEntity sellerCompany
            , CompanyEntity buyerCompany
            , CashVO cashVO){
        List<OrderCsv> orderCsvList = new ArrayList<>();
        if(!exportCashMasterBusinessLogicContinueFilter(cashVO.getCashMasterEntity())){
            //取得相關資料
            orderCsvList = genOrderCsvEntityByCashMaster(sellerCompany,buyerCompany,cashVO);
        }
        return orderCsvList;
    }

    //匯出發票Excel的資料
    public List getInvoiceItemList(List masterList) throws Exception{
        List list = new ArrayList();

        //masterList依master_id排序
        Collections.sort(masterList,
                new Comparator<CashMasterEntity>() {
                    public int compare(CashMasterEntity o1, CashMasterEntity o2) {
                        return o2.getCashMasterId().compareTo(o1.getCashMasterId());
                    }
                });

        int invoiceIndex = 1;
        for(int i=0; i<masterList.size(); i++){
            CashMasterEntity master = (CashMasterEntity)masterList.get(i);

            //判斷跳過條件
            if(exportCashMasterBusinessLogicContinueFilter(master)){
                continue;
            }

            Integer masterId = master.getCashMasterId();
            CashDetailEntity searchCashDetailEntity = new CashDetailEntity();
            searchCashDetailEntity.setCashMasterId(masterId);

            CompanyEntity companyEntity = (CompanyEntity)cashDAO.getEntity(CompanyEntity.class, master.getCompanyId());
            String businessNo = companyEntity.getBusinessNo();

            List<CashDetailEntity> cashDetailList = cashDAO.getSearchEntity(CashDetailEntity.class, searchCashDetailEntity);

            int itemIndex = 1;
            for(CashDetailEntity detail:cashDetailList){

                //判斷跳過條件
                if(exportCashDetailBusinessLogicContinueFilter(detail)){
                    continue;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                String invoiceDate = sdf.format(new Date());

                //必須放在feePeriod的結果之後。
                String itemName = getPackageDescriptionByCashType(detail);
                BigDecimal taxInclusivePrice = getTaxInclusivePrice(detail);
                InvoiceExcelBean bean = new InvoiceExcelBean();
                bean.setInvoiceIndex(invoiceIndex); //發票張數
                bean.setInvoiceDate(invoiceDate); //發票日期
                bean.setItemIndex(itemIndex); //品序號
                bean.setItemName(itemName); //發票品名
                bean.setItemCnt(1); //數量
                bean.setUnitPrice(taxInclusivePrice); //單價
                bean.setTaxType(1); //課稅別
                bean.setTax(0.05d); //稅率
                bean.setBusinessNo(businessNo); //買方統編
                list.add(bean);
                itemIndex++;
            }
            invoiceIndex++;
        }

        return list;
    }

    //多筆-寄帳單明細表
    @Override
    public Integer transactionSendBillMail1(String masterIdAry) throws Exception{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CashMasterBean>>(){}.getType();
        List<CashMasterBean> cashMasterList = gson.fromJson(masterIdAry, collectionType);

        int exeCnt = 0;
        //找出所有要計算的MasterId
        for(int i=0; i<cashMasterList.size(); i++) {
            CashMasterBean bean = (CashMasterBean)cashMasterList.get(i);
            Integer cashMasterId = (null == bean.getCashMasterId())?0:bean.getCashMasterId();
            CashMasterEntity cashMasterEntity = (CashMasterEntity)cashDAO.getEntity(CashMasterEntity.class, cashMasterId);
            boolean isSend = cashDAO.sendMail(cashMasterEntity);
            if(isSend){ //是否寄出
                exeCnt++;//寄出
                cashDAO.updateEmailDate(cashMasterEntity); //更新cash_master的email_sent_date(寄送email日期)
            }
        }
        return exeCnt;
    }

    //輸入自行要重寄的Email(帳單明細表)
    @Override
    public Integer reSendBillEmail(String strCashMasterId, String strReSendBillMail) throws Exception{
        int exeCnt = 0;
        //找出所有要計算的MasterId
        Integer cashMasterId = (null == strCashMasterId)?0:Integer.parseInt(strCashMasterId);
        CashMasterEntity  cashMasterEntity = (CashMasterEntity)cashDAO.getEntity(CashMasterEntity.class, cashMasterId);
        boolean isSend = cashDAO.sendMail(cashMasterEntity, strReSendBillMail);
        if(isSend){ //是否寄出
            exeCnt++;//寄出
            cashDAO.updateEmailDate(cashMasterEntity); //更新cash_master的email_sent_date(寄送email日期)
        }
        return exeCnt;
    }

    @Override
    public List<CashVO> findCashVoByOutYm(String yearMonth){
        List<CashVO> cashVOList = new ArrayList<>();
        List<CashMasterEntity> cashMasterEntityList = cashMasterRepository.findByOutYm(yearMonth);
        for(CashMasterEntity cashMasterEntity : cashMasterEntityList){
            List<CashDetailEntity> cashDetailEntityList
                    = cashDetailRepository.findByCashMasterId(cashMasterEntity.getCashMasterId());
            if(cashDetailEntityList!=null && cashDetailEntityList.size()>0){
                CashVO cashVO = new CashVO();
                cashVO.setCashMasterEntity(cashMasterEntity);
                cashVO.setCashDetailEntityList(cashDetailEntityList);
                cashVOList.add(cashVO);
            }
        }
        return cashVOList;
    }

    @Override
    public Map<String,Object> genCashDataExcelDataMap(List<CashMasterBean> cashMasterBeanList){
        Map<String,Object> resultMap = new HashMap<>();
        List<String> headerList = new ArrayList<>();
        List<List<Object>> dataList = new ArrayList<>();

        //寫死固定欄位
        headerList.add("*編號");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("");
        headerList.add("姓名");
        headerList.add("客戶辨識碼");

        //加入動態欄位
        //divide and conquer
        //先跑header的部份
        List<String> packageList = new ArrayList<>();
        for(CashMasterBean cashMasterBean : cashMasterBeanList){
            for(CashDetailBean cashDetailBean: cashMasterBean.getCashDetailList()){
                Integer chargeId = cashDetailBean.getChargeId();
                Integer cashType = cashDetailBean.getCashType(); //1.月租 2.超額 3.代印代寄 4.加值型 5.儲值 6.預繳
                Integer billType = cashDetailBean.getBillType(); //1.月租 2.級距

                //分兩次，先加入欄位的map
                String packageName = getPackageName(cashType,cashDetailBean.getPackageName());
                if(!packageList.contains(packageName)){
                    packageList.add(packageName);
                    //寫入header
                    headerList.add(packageName);
                }
            }
        }

        for(CashMasterBean cashMasterBean : cashMasterBeanList){
            List<Object> detailValueList = new ArrayList<>();
            detailValueList.add(cashMasterBean.getBusinessNo());
            detailValueList.add(cashMasterBean.getInAmount());
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add("");
            detailValueList.add(cashMasterBean.getCompanyName());
            detailValueList.add(cashMasterBean.getBusinessNo());
            //寫value
            //就以前的情況，一定只會有一個。
            CashDetailBean cashDetailBean = cashMasterBean.getCashDetailList().get(0);
            for(String packageName: packageList){
                if(cashDetailBean.getPackageName().equals(packageName)){
                    detailValueList.add(cashDetailBean.getTaxInclusivePrice());
                }else{
                    detailValueList.add(0);
                }
            }
            dataList.add(detailValueList);
        }
        resultMap.put("header",headerList);
        resultMap.put("data",dataList);
        return resultMap;
    }

    public String getPackageName(
            Integer cashType
            , String originalPackageName){
        String packageName = "";
        if(cashType == 1){ //月租
            packageName = originalPackageName;
        }else if(cashType == 2){ //超額
            packageName = originalPackageName+"(超額)";
        }else  if(cashType == 6){ //預繳
            packageName = originalPackageName+"預繳";
        }else if(cashType == 7){ //7.扣抵
            packageName = originalPackageName + "扣抵";
        }
        return packageName;
    }

    /**
     *  產生查詢下載的Excel報表
     * @param cashMasterList
     * @param tempPath
     * @return
     * @throws Exception
     */
    @Override
    public ExcelPoiWrapper genCashDataToExcel(List<CashMasterBean> cashMasterList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);

        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=2;
        int index = 0;
        int packageIndex = 8;
        for(CashMasterBean masterBean:cashMasterList){
            excel.copyRows(2, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, masterBean.getBusinessNo());
            excel.setValue(baseRow, index + 2, masterBean.getInAmount());
            excel.setValue(baseRow, index + 6, masterBean.getCompanyName());
            excel.setValue(baseRow, index + 7, masterBean.getBusinessNo());

            List<CashDetailBean> cashDetailList = masterBean.getCashDetailList();
            for(CashDetailBean detail: cashDetailList){
                Integer chargeId = detail.getChargeId();
                Integer cashType = detail.getCashType(); //1.月租 2.超額 3.代印代寄 4.加值型 5.儲值 6.預繳
                Integer billType = detail.getBillType(); //1.月租 2.級距

                //要繳的金額(月租預繳和超額分開)
                BigDecimal taxInclusivePrice_ = detail.getTaxInclusivePrice();
                Integer taxInclusivePrice = 0;
                if(taxInclusivePrice_.intValue() == 0){
                    taxInclusivePrice = 0; //不然excel顯示的數字會0E-9
                }else{
                    taxInclusivePrice = taxInclusivePrice_.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
                }

                if(null == packageMap.get(chargeId+","+cashType+","+billType)){
                    packageMap.put(chargeId+","+cashType+","+billType, packageIndex);
                    excel.setValue(baseRow, index + packageIndex, taxInclusivePrice);
                    if(cashType == 1){ //月租
                        excel.setValue(1, index + packageIndex, detail.getPackageName());
                    }else if(cashType == 2){ //超額
                        excel.setValue(1, index + packageIndex, detail.getPackageName()+"(超額)");
                    }else  if(cashType == 6){ //預繳
                        excel.setValue(1, index + packageIndex, "預繳");
                    }else if(cashType == 7){ //7.扣抵
                        excel.setValue(1, index + packageIndex, "扣抵");
                    }
                    packageIndex++;
                }else{
                    Integer packageIndexOld = (Integer)packageMap.get(chargeId + ","+cashType+","+billType);
                    excel.setValue(baseRow, index + packageIndexOld, taxInclusivePrice);
                }
            }
            baseRow++;
        }

        //把沒有packagename的cell填0
        HSSFSheet sheet = excel.getSheet();
        for(int i=2; i<baseRow; i++){
            Row row =sheet.getRow(i-1);
            for(int j=8; j<packageIndex; j++){
                Cell cell = row.getCell(j-1);
                if(!fieldUtils.isNotEmptyCell(cell)){
                    excel.setValue(i, j, 0);
                }
            }
        }

        return excel;

    }

    //匯出發票資料Excel
    @Override
    public ExcelPoiWrapper genInvoiceItemToExcel(List<InvoiceExcelBean> InvoiceExcelList, String tempPath) throws Exception {
        ExcelPoiWrapper excel = new ExcelPoiWrapper(tempPath);
        HashMap packageMap = new HashMap();
        excel.setWorkSheet(1);
        int baseRow=3;
        int index = 0;
        int packageIndex = 8;
        for(InvoiceExcelBean bean:InvoiceExcelList){
            excel.copyRows(3, 21, 1, baseRow);

            excel.setValue(baseRow, index + 1, bean.getInvoiceIndex()); //發票張數
            excel.setValue(baseRow, index + 2, bean.getInvoiceDate()); //發票日期
            excel.setValue(baseRow, index + 3, bean.getItemIndex()); //品名序號
            excel.setValue(baseRow, index + 4, bean.getItemName()); //發票品名
            excel.setValue(baseRow, index + 5, bean.getItemCnt()); //數量
            excel.setValue(baseRow, index + 6, bean.getUnitPrice()); //單價
            excel.setValue(baseRow, index + 7, bean.getTaxType()); //課稅別
            excel.setValue(baseRow, index + 8, bean.getTax()); //稅率
            excel.setValue(baseRow, index + 10, bean.getBusinessNo()); //買方統編
            excel.setValue(baseRow, index + 11, 2); //1.列印 2.列印+email

            baseRow++;
        }
        return excel;
    }

    @Override
    public List<InvoiceBatchRecord> genInvoiceBatchRecordList(List<InvoiceExcelBean> invoiceExcelBeanList){
        List<InvoiceBatchRecord> resultList = new ArrayList<>();
        for(InvoiceExcelBean invoiceExcelBean:invoiceExcelBeanList){
            InvoiceBatchRecord invoiceBatchRecord = new InvoiceBatchRecord();
            invoiceBatchRecord.setInvoiceSequence(invoiceExcelBean.getInvoiceIndex());
            invoiceBatchRecord.setInvoiceDate(invoiceExcelBean.getInvoiceDate());
            invoiceBatchRecord.setProductItemSequence(invoiceExcelBean.getItemIndex());
            invoiceBatchRecord.setProductItemDescription(invoiceExcelBean.getItemName());
            invoiceBatchRecord.setProductItemQuantity(invoiceExcelBean.getItemCnt());
            invoiceBatchRecord.setProductItemUnitPrice(invoiceExcelBean.getUnitPrice());
            invoiceBatchRecord.setTaxType(invoiceExcelBean.getTaxType());
            invoiceBatchRecord.setTaxRate(invoiceExcelBean.getTax());
            invoiceBatchRecord.setBuyerIdentifier(invoiceExcelBean.getBusinessNo());
            invoiceBatchRecord.setPrintOrEmailRemark(2);
            resultList.add(invoiceBatchRecord);
        }
        return resultList;
    }

    /**
     *
     * @param sellerCompany
     * @param buyerCompany
     * @param cashVO
     * @return
     */
    public List<OrderCsv> genOrderCsvEntityByCashMaster(
            CompanyEntity sellerCompany
            , CompanyEntity buyerCompany
            , CashVO cashVO){
        List<OrderCsv> orderCsvList = new ArrayList<>();
        //產生明細項目的部份。
        for(int i = 0; i<cashVO.getCashDetailEntityList().size();i++){
            if(exportCashDetailBusinessLogicContinueFilter(cashVO.getCashDetailEntityList().get(i))){
                continue;
            }

            OrderCsv orderCsv = new OrderCsv();
            orderCsv.setOrderNumber(cashVO.getCashMasterEntity().getCashMasterId().toString());

            //seller
            orderCsv.setBuyerIdentifier(buyerCompany.getBusinessNo());
            orderCsv.setBuyerAddress(buyerCompany.getCompanyAddress());
            orderCsv.setBuyerEmailAddress(buyerCompany.getEmail1());
            orderCsv.setBuyerName(buyerCompany.getName());
            orderCsv.setBuyerPersonInCharge(buyerCompany.getContact1());

            //buyer
            orderCsv.setSellerIdentifier(sellerCompany.getBusinessNo());
            orderCsv.setSellerName(sellerCompany.getName());
            orderCsv.setSellerAddress(sellerCompany.getCompanyAddress());
            orderCsv.setSellerEmailAddress(sellerCompany.getEmail1());
            orderCsv.setSellerPersonInCharge(sellerCompany.getContact1());

            //預設資料
            orderCsv.setTaxType("1");
            orderCsv.setTaxRate(new Float(0.05));

            //訂單基本資料
            orderCsv.setYearMonth(timeUtils.getYearMonth(timeUtils.getCurrentDateString("yyyyMMdd")));

            String description = getPackageDescriptionByCashType(cashVO.getCashDetailEntityList().get(i));
            orderCsv.setDetailDescription(description);
            BigDecimal taxInclusivePrice = getTaxInclusivePrice(cashVO.getCashDetailEntityList().get(i));
            orderCsv.setDetailUnitPrice(taxInclusivePrice);

            //基本設定
            orderCsv.setDetailSequenceNumber(i+1);
            orderCsv.setDetailQuantity(new BigDecimal(1));
            orderCsv.setTaxRate(new Float(0.05));
            orderCsv.setDetailAmount(
                    orderCsv.getDetailQuantity().multiply(taxInclusivePrice)
            );
            orderCsv.setTaxAmount(
                    orderCsv.getDetailAmount().subtract(
                            orderCsv.getDetailAmount().divide(
                                    new BigDecimal(1).add(new BigDecimal(0.05)),0,BigDecimal.ROUND_HALF_UP
                            )
                    )
            );
            orderCsv.setTotalAmount(orderCsv.getDetailAmount().add(orderCsv.getTaxAmount()));

            //明細基本資料
            orderCsv.setYearMonth(timeUtils.getYearMonth(timeUtils.getCurrentDateString("yyyyMMdd")));
            orderCsvList.add(orderCsv);
        }
        return orderCsvList;
    }

}
