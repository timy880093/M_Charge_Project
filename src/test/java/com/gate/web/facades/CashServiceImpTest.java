package com.gate.web.facades;

import com.gate.utils.CsvUtils;
import com.gate.utils.FileUtils;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.reportModel.OrderCsv;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/13/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class CashServiceImpTest {
    Gson gson = new Gson();

    @Autowired
    CashService cashService;

    @Autowired
    CsvUtils csvUtils;

    @Autowired
    FileUtils fileUtils;

    //測試抓取CashVO
    @Test
    public void CashVOFindByOutYmTest(){
        List<CashVO> cashVOList = cashService.findCashVoByOutYm("201712");
        for(CashVO cashVO : cashVOList){
            System.out.println(gson.toJson(cashVO));
        }
    }

    @Test
    public void CashVoToOrderVoTest(){
        List<CashVO> cashVoList = cashService.findCashVoByOutYm("201712");
        List<OrderCsv> orderCsvList = new ArrayList<>();
        for(CashVO cashVo : cashVoList){
            orderCsvList.addAll(cashService.genOrderCsvListByCashVO(new Long(2),cashVo));
        }
    }

    @Test
    public void CashVoToSingleOrderCsvTest() throws IOException {
        //一筆一筆處理
        List<CashVO> cashVoList = cashService.findCashVoByOutYm("201712");
        for(CashVO cashVo : cashVoList){
            List<OrderCsv> orderCsvList = cashService.genOrderCsvListByCashVO(new Long(2),cashVo);
            //第一行欄位資料
            List<String> headerValueList = csvUtils.genBeanHeaderData(OrderCsv.class,new ArrayList<>());
            String headerStringData = csvUtils.dataListToCsvLineData(headerValueList,",");
            List<String> detailStringDataList = new ArrayList<>();
            //後續明細資料
            for(OrderCsv orderCsv: orderCsvList){
                List<Object> beanDataObjectList = csvUtils.genBeanValueData(orderCsv,new ArrayList<>());
                List<String> beanDataArrayList = csvUtils.objectListToStringList(beanDataObjectList);
                String detailStringData = csvUtils.dataListToCsvLineData(beanDataArrayList,",");
                detailStringDataList.add(detailStringData);
            }
            List<String> summaryStringDataList = new ArrayList<>();
            summaryStringDataList.add(headerStringData);
            summaryStringDataList.addAll(detailStringDataList);
            if(orderCsvList.size()>0){
                File csvFile = new File("C:/Users/Eason/Desktop/20180307Problem/"+cashVo.getCashMasterEntity().getCashMasterId()+".csv");
                fileUtils.writeTextFile(csvFile,summaryStringDataList);
            }
        }
    }

    public List<String> ignoreColumnFromOrderMain(){
        //設定產生csv資料時要忽略的欄位名稱。
        List<String> ignoreColumnFromOrderMain = new ArrayList<>();
        ignoreColumnFromOrderMain.add("closeDate");
        ignoreColumnFromOrderMain.add("discountAmount");
        ignoreColumnFromOrderMain.add("mainRemark");
        ignoreColumnFromOrderMain.add("carrierId1");
        ignoreColumnFromOrderMain.add("carrierId2");
        ignoreColumnFromOrderMain.add("orderStatus");
        ignoreColumnFromOrderMain.add("allowanceCount");
        ignoreColumnFromOrderMain.add("npoban");
        ignoreColumnFromOrderMain.add("carrierType");
        ignoreColumnFromOrderMain.add("freeTaxSalesAmount");
        ignoreColumnFromOrderMain.add("CPrinterId");
        ignoreColumnFromOrderMain.add("CPrinterNo");
        ignoreColumnFromOrderMain.add("acceptStatus");
        ignoreColumnFromOrderMain.add("id");
        ignoreColumnFromOrderMain.add("createDate");
        ignoreColumnFromOrderMain.add("donateMark");
        ignoreColumnFromOrderMain.add("CPosRemark1");
        ignoreColumnFromOrderMain.add("CPosRemark2");
        ignoreColumnFromOrderMain.add("storeIdentifier");
        ignoreColumnFromOrderMain.add("deliveryStartDate");
        ignoreColumnFromOrderMain.add("randomNumber");
        ignoreColumnFromOrderMain.add("creatorId");
        ignoreColumnFromOrderMain.add("customsClearanceMark");
        ignoreColumnFromOrderMain.add("sellerEmailAddress");
        ignoreColumnFromOrderMain.add("CInvoiceStatus");
        ignoreColumnFromOrderMain.add("modifierId");
        ignoreColumnFromOrderMain.add("modifyDate");
        ignoreColumnFromOrderMain.add("uploadStatus");
        ignoreColumnFromOrderMain.add("confirmStatus");
        ignoreColumnFromOrderMain.add("deliveryEndDate");
        return ignoreColumnFromOrderMain;
    }

    public List<String> ignoreColumnFromOrderDetail(){
        //設定產生csv資料時要忽略的欄位名稱
        List<String> ignoreColumnFromOrderDetail = new ArrayList<>();
        ignoreColumnFromOrderDetail.add("freeGoods");
        ignoreColumnFromOrderDetail.add("unit");
        ignoreColumnFromOrderDetail.add("spec");
        ignoreColumnFromOrderDetail.add("packBox");
        ignoreColumnFromOrderDetail.add("unitNetPrice");
        ignoreColumnFromOrderDetail.add("unitTaxPrice");
        ignoreColumnFromOrderDetail.add("unitSalesPrice");
        ignoreColumnFromOrderDetail.add("bulkPrice");
        ignoreColumnFromOrderDetail.add("remark");
        ignoreColumnFromOrderDetail.add("creatorId");
        ignoreColumnFromOrderDetail.add("createDate");
        ignoreColumnFromOrderDetail.add("modifierId");
        ignoreColumnFromOrderDetail.add("modifyDate");
        ignoreColumnFromOrderDetail.add("orderMainId");
        ignoreColumnFromOrderDetail.add("cRemark1");
        ignoreColumnFromOrderDetail.add("itemNo");
        ignoreColumnFromOrderDetail.add("taxRate");
        ignoreColumnFromOrderDetail.add("taxType");
        ignoreColumnFromOrderDetail.add("CRemark1");
        ignoreColumnFromOrderDetail.add("id");
        ignoreColumnFromOrderDetail.add("invoiceNumber");
        ignoreColumnFromOrderDetail.add("orderNumber");
        ignoreColumnFromOrderDetail.add("relateNumber");
        ignoreColumnFromOrderDetail.add("yearMonth");
        return ignoreColumnFromOrderDetail;
    }

}
