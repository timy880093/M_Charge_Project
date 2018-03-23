package com.gate.web.facades;

import com.gate.utils.CsvUtils;
import com.gate.utils.FileUtils;
import com.gate.utils.JxlsUtils;
import com.gateweb.charge.vo.CashVO;
import com.gateweb.reportModel.OrderCsv;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.*;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

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

    @Autowired
    JxlsUtils jxlsUtils;

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
    public void postMessageToEinvTest(){
        String uploadFilePath = "orderCsv/2899.csv";
        org.springframework.util.MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("file", new FileSystemResource(this.getClass().getResource("/").getPath()+uploadFilePath));
        String uri = "http://localhost:8080/backendAdmin/batchcsv/invoiceCsvUpload?method=uploadInvoiceTxt";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String auth = "pkliu" + ":" + "!QAZ2wsx";
        byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
        String authHeader = "Basic " + new String(encodedAuth);
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");
        //headers.set("Accept", "text/plain");
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        HttpEntity<org.springframework.util.MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
        } catch (ResourceAccessException rae) {
            System.out.println(rae.getMessage());
        } catch (HttpClientErrorException hee) {
            System.out.println(hee.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
