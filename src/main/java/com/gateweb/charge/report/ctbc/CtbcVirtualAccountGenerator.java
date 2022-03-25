package com.gateweb.charge.report.ctbc;

import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@PropertySource("classpath:charge.custom.properties")
public class CtbcVirtualAccountGenerator {
    protected org.apache.log4j.Logger logger = LogManager.getLogger(CtbcVirtualAccountGenerator.class);

    @Autowired
    private Environment env;

    int[] multiplierArr = new int[]{3, 7, 1};

    protected Optional<String> getCtbcBusinessId() {
        String ctbcBusinessId = env.getProperty("ctbc.business.id");
        return Optional.ofNullable(ctbcBusinessId);
    }

    public Optional<Integer> getCheckSum(String businessId, String businessNo) {
        Optional<Integer> result = Optional.empty();
        try {
            String t1 = businessId + businessNo;
            Integer r = 0;
            char[] chars = t1.toCharArray();
            int cusor = 0;
            for (char c : chars) {
                Integer m1 = Integer.parseInt(String.valueOf(c)) * multiplierArr[cusor];
                String m1Str = m1.toString();
                r += Integer.parseInt(m1Str.substring(m1Str.length() - 1));
                if (cusor == multiplierArr.length - 1) {
                    cusor = 0;
                } else {
                    cusor++;
                }
            }
            String rStr = r.toString();
            Integer intResult = Integer.parseInt(rStr.substring(rStr.length() - 1));
            if (intResult == 0) {
                result = Optional.of(0);
            } else {
                result = Optional.of(10 - intResult);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public Optional<String> getVirtualAccount(String businessNo) {
        Optional<String> result = Optional.empty();
        Optional<String> businessIdOpt = getCtbcBusinessId();
        if (businessIdOpt.isPresent()) {
            Optional<Integer> checkSumOpt = getCheckSum(businessIdOpt.get(), businessNo);
            if (checkSumOpt.isPresent()) {
                result = Optional.of(businessIdOpt.get() + businessNo + checkSumOpt.get());
            }
        }
        return result;
    }

    public String customFormatVirtualAccount(String virtualAccount) {
        StringBuffer resultBuffer = new StringBuffer();
        char[] charArray = virtualAccount.toCharArray();
        if (virtualAccount.length() == 14) {
            for (int i = 0; i < charArray.length; i++) {
                resultBuffer.append(charArray[i]);
                if (i == 4 || i == 12) {
                    resultBuffer.append("-");
                }
            }
        }
        return resultBuffer.toString();
    }
}
