package freeMarker;

import com.gateweb.charge.model.UserEntity;
import freemarker.template.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eason on 4/19/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/gateweb/einv/spring/spring_freemarker.xml" })
public class freeMarkerTest {

    @Autowired
    public Configuration freemarkerConfiguration;

    public static final String TEMPLATE = "test.ftl";

    @Test
    public void composeHtml() throws Exception {
        UserEntity user = new UserEntity();
        user.setName("test1");
        user.setEmail("test@example.com");
        user.setCompanyId(new Long(123));

        Map<String, Object> freeMarkerTemplateMap = new HashMap<String, Object>();
        freeMarkerTemplateMap.put("user", user);
        Writer fileWriter = new FileWriter(new File("output.html"));
        //String resultString =  FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(TEMPLATE), freeMarkderTemplateMap);
        freemarkerConfiguration.getTemplate(TEMPLATE).process(freeMarkerTemplateMap, fileWriter);

        //fileWriter.write(resultString);

        //System.out.println("result: "+resultString);
    }


}
