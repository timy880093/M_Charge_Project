package com.gateweb.charge.service;

import com.gateweb.charge.report.ctbc.CtbcVirtualAccountGenerator;
import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class CtbcVirtualAccountTest {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CtbcVirtualAccountGenerator ctbcVirtualAccountGenerator;

    @Test
    public void virtualAccountGen() throws IOException {
        List<Company> companyList = companyRepository.findAll();
        File resultFile = new File("D:\\ctbcVirtualAccountList.txt");
        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }
        PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
        companyList.stream().forEach(company -> {
            Optional<String> virtualAccountOpt = ctbcVirtualAccountGenerator.getVirtualAccount(company.getBusinessNo());
            if (virtualAccountOpt.isPresent()) {
                writer.println(ctbcVirtualAccountGenerator.customFormatVirtualAccount(virtualAccountOpt.get()));
            }
        });
        writer.close();
    }
}
