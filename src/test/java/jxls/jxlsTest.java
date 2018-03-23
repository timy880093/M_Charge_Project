package jxls;

import com.gate.utils.JxlsUtils;
import com.gate.web.facades.CashService;
import com.gate.web.facades.CommissionLogService;
import com.gateweb.reportModel.CommissionRecord;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;


/**
 * Created by Eason on 3/23/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/gateweb/einv/spring/spring_web.xml"})
public class jxlsTest {

    private String COMMISSION_JXLS_TEMPLATE = "jxls/commission_jxls_template.xls";

    private String DYNAMIC_COLUMN_CONFIGURATION = "jxls/dynamic_columns_demo.xml";

    private String OUT_JXLS_TEMPLATE = "jxls/out_jxls_template.xls";

    private String OUTPUT_FILE = "jxls/result.xls";

    @Autowired
    CommissionLogService commissionLogService;

    @Autowired
    CashService cashService;

    @Autowired
    JxlsUtils jxlsUtils;

    @Test
    /**
     * using exists object as datasource
     */
    public void CommissionSimpleTest(){
        try {
            FileInputStream templateInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+COMMISSION_JXLS_TEMPLATE);
            FileOutputStream resultOutputStream = new FileOutputStream(this.getClass().getResource("/").getPath()+OUTPUT_FILE);
            // AJAX 資料來源
            List<Map> excelList = commissionLogService.exportCom(new Integer[]{1,2,3});
            List<CommissionRecord> commissionRecordList = commissionLogService.genCommissionRecordList(excelList);
            Map<String,Object> contextMap = new HashMap<>();
            contextMap.put("commissionRecordList",commissionRecordList);
            jxlsUtils.processTemplate(contextMap,templateInputStream,resultOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * using list variables as datasource
     * 詳情請見：https://bitbucket.org/leonate/jxls-demo 官網有寫簡單範例，但如果要複雜的對應要直接看project.
     */
    public void ComplicatedMappingTest(){
        try{
            List cashMasterList =  cashService.getCashMasterDetail("201802");
            Map<String,Object> dataMap = cashService.genCashDataExcelDataMap(cashMasterList);
            FileInputStream fileInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+OUT_JXLS_TEMPLATE);
            FileOutputStream resultOutputStream = new FileOutputStream(this.getClass().getResource("/").getPath()+OUTPUT_FILE);
            InputStream configInputStream = new FileInputStream(this.getClass().getResource("/").getPath()+DYNAMIC_COLUMN_CONFIGURATION);
            Map<String,Object> contextMap = new HashMap<>();
            contextMap.put("headers",dataMap.get("header"));
            contextMap.put("rows", dataMap.get("data"));
            jxlsUtils.processTemplate(contextMap,fileInputStream,resultOutputStream,configInputStream,new CellRef("Template!A1"));
            // saving the results to file
            System.out.println("Complete");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
