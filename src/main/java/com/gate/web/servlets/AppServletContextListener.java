package com.gate.web.servlets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.gate.config.SystemConfigLoader;
import com.gate.core.utils.CustomBeanUtilsBean;
import com.gate.web.facades.CompanyService;

@WebListener
public class AppServletContextListener implements ServletContextListener {

	protected static final Logger logger = LogManager.getLogger(AppServletContextListener.class);
	
	@Autowired
	CompanyService companyService;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("ServletContextListener destroyed");

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		try {
            SystemConfigLoader.instance().load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        CustomBeanUtilsBean.register();
        logger.info("init BeanUtil!!!!!");
		
		
		String licenseString = "<License>\n" +
				"  <Data>\n" +
				"    <LicensedTo>Gate Web</LicensedTo>\n" +
				"    <EmailTo>se03@gateweb.com.tw</EmailTo>\n" +
				"    <LicenseType>Developer OEM</LicenseType>\n" +
				"    <LicenseNote>Limited to 1 developer, unlimited physical locations</LicenseNote>\n" +
				"    <OrderID>170518042408</OrderID>\n" +
				"    <UserID>401727</UserID>\n" +
				"    <OEM>This is a redistributable license</OEM>\n" +
				"    <Products>\n" +
				"    <Product>Aspose.Cells for Java</Product>\n" +
				"    </Products>\n" +
				"    <EditionType>Enterprise</EditionType>\n" +
				"    <SerialNumber>202e4a19-c0f2-4bf8-8130-21cc1cc58570</SerialNumber>\n" +
				"    <SubscriptionExpiry>20180518</SubscriptionExpiry>\n" +
				"    <LicenseVersion>3.0</LicenseVersion>\n" +
				"    <LicenseInstructions>http://www.aspose.com/corporate/purchase/license-instructions.aspx</LicenseInstructions>\n" +
				"  </Data>\n" +
				"  <Signature>e6KsjBs2Ya3QO83MR5Bevgyam8DZAn1H+qRmxXotM/HIOtDBiB1Fe5r/Db4VwbiAgPwEqOKaUK26Y9J1w8kYSYhOrlvYXFSI0U1S/CzJSs75i3U18hD9FpE4cRCAnMj8XpYIk/IAd40e/5ovfWHVNMuVIoxZAMhRqRBav7Imd6k=</Signature>\n" +
				"</License>";

		InputStream inputStream =
				new ByteArrayInputStream(licenseString.getBytes());
		com.aspose.cells.License license = new com.aspose.cells.License();
		license.setLicense(inputStream);

		logger.info("Aspose license: " +com.aspose.cells.License.isLicenseSet());

	}



}
