package com.gate.web.facades;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.CashMasterBean;
import com.gate.web.beans.InvoiceExcelBean;
import com.gateweb.charge.model.*;
import com.gateweb.charge.repository.*;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.einv.model.OrderDetailsEntity;
import com.gateweb.einv.model.OrderMainEntity;
import com.gateweb.einv.vo.OrderVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.CashDetailVO;
import com.gate.web.displaybeans.CashMasterVO;

import dao.CashDAO;

@Service("cashService")
public class CashServiceImp implements CashService {

    private Log logger = LogFactory.getLog(this.getClass().getName());

	@Autowired
    CashDAO cashDAO;

    @Autowired
    TimeUtils timeUtils;

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

    public boolean cancelOver(Integer cashDetailId) throws Exception{
        return cashDAO.transactionCancelOver(cashDetailId);
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
        return cashDAO.delCashMaster(cashMasterId);
    }

    //使用CashVO的資料建立訂單。
    @Override
    public OrderVO genOrderByCashRecord(Long sellerCompanyId, CashVO cashVO){
        CompanyEntity sellerCompany = companyRepository.findByCompanyId(sellerCompanyId.intValue());
        CompanyEntity buyerCompany = companyRepository.findByCompanyId(cashVO.getCashMasterEntity().getCompanyId());
        return genOrder(sellerCompany,buyerCompany,cashVO);
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
        if(cashMasterEntity.getTaxInclusiveAmount().compareTo(new BigDecimal(0)) == 0){
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

    public OrderVO genOrder(
            CompanyEntity sellerCompany
            , CompanyEntity buyerCompany
            , CashVO cashVO){
        if(exportCashMasterBusinessLogicContinueFilter(cashVO.getCashMasterEntity())){
            return null;
        }

        //取得相關資料
        OrderVO orderVO = new OrderVO();
        OrderMainEntity orderMainEntity
                = genOrderMainEntityByCashMaster(sellerCompany,buyerCompany,cashVO.getCashMasterEntity());
        List<OrderDetailsEntity> orderDetailsEntityList = genOrderDetailsEntityByCashDetail(cashVO);
        orderVO.setOrderMainEntity(orderMainEntity);
        orderVO.setOrderDetailsEntityList(orderDetailsEntityList);
        return orderVO;
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

    /**
     *
     * @param sellerCompany
     * @param buyerCompany
     * @param cashMasterEntity
     * @return
     */
    public OrderMainEntity genOrderMainEntityByCashMaster(
            CompanyEntity sellerCompany
            , CompanyEntity buyerCompany
            , CashMasterEntity cashMasterEntity){

        OrderMainEntity orderMainEntity = new OrderMainEntity();
        orderMainEntity.setOrderNumber(cashMasterEntity.getCashMasterId().toString());

        //seller
        orderMainEntity.setBuyer(buyerCompany.getBusinessNo());
        orderMainEntity.setBuyerAddress(buyerCompany.getCompanyAddress());
        orderMainEntity.setBuyerEmailAddress(buyerCompany.getEmail1());
        orderMainEntity.setBuyerName(buyerCompany.getName());
        orderMainEntity.setBuyerPersonInCharge(buyerCompany.getContact1());

        //buyer
        orderMainEntity.setSeller(sellerCompany.getBusinessNo());
        orderMainEntity.setSellerName(sellerCompany.getName());
        orderMainEntity.setSellerAddress(sellerCompany.getCompanyAddress());
        orderMainEntity.setSellerEmailAddress(sellerCompany.getEmail1());
        orderMainEntity.setSellerPersonInCharge(sellerCompany.getContact1());

        //預設資料
        orderMainEntity.setTaxType("1");
        orderMainEntity.setTaxRate(new Float(0.05));

        //訂單基本資料
        orderMainEntity.setYearMonth(timeUtils.getYearMonth(timeUtils.getCurrentDateString("yyyyMMdd")));
        orderMainEntity.setCreateDate(timeUtils.getCurrentTimestamp());
        orderMainEntity.setModifyDate(timeUtils.getCurrentTimestamp());

        return orderMainEntity;
    }

    public List<OrderDetailsEntity> genOrderDetailsEntityByCashDetail(CashVO cashVO){
        List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();

        for(int i = 0; i<cashVO.getCashDetailEntityList().size();i++){
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            String description = getPackageDescriptionByCashType(cashVO.getCashDetailEntityList().get(i));
            orderDetailsEntity.setDescription(description);
            BigDecimal taxInclusivePrice = getTaxInclusivePrice(cashVO.getCashDetailEntityList().get(i));
            orderDetailsEntity.setUnitPrice(taxInclusivePrice);

            //基本設定
            orderDetailsEntity.setSequenceNumber(i+1);
            orderDetailsEntity.setQuantity(new BigDecimal(1));
            orderDetailsEntity.setTaxType("1");
            orderDetailsEntity.setTaxRate(new Float(0.05));
            orderDetailsEntity.setAmount(
                    orderDetailsEntity.getQuantity().multiply(taxInclusivePrice)
            );

            //明細基本資料
            orderDetailsEntity.setYearMonth(timeUtils.getYearMonth(timeUtils.getCurrentDateString("yyyyMMdd")));
            orderDetailsEntity.setCreateDate(timeUtils.getCurrentTimestamp());
            orderDetailsEntity.setModifyDate(timeUtils.getCurrentTimestamp());
            orderDetailsEntityList.add(orderDetailsEntity);
        }
        return orderDetailsEntityList;
    }


}
