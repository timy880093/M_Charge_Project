package com.gateweb.utils;

import org.springframework.stereotype.Component;

/**
 * Created by Eason on 3/19/2018.
 */
@Component
public class CommissionLogReportUtils {
    //佣金類型0 固定金額, 1 固定比例, 2 經銷商
    public String parseCommissionType(String commissionType){
        String strCommissionType = "";

        if("0".equals(commissionType)){
            strCommissionType = "固定金額";
        }else if("1".equals(commissionType)){
            strCommissionType = "固定比例";
        }else if("2".equals(commissionType)){
            strCommissionType = "經銷商";
        }

        return strCommissionType;
    }

    //佣金付款狀態 1:付款
    public String parseIsPaid(String isPaid){
        String strIsPaid = "未付款";

        if("1".equals(isPaid)){
            strIsPaid = "已付款";
        }

        return strIsPaid;
    }

    //cash_master的出入帳金額是否相同
    public String isInoutMoneyUnmatch(String value){
        String result = "相同";
        if("1".equals(value)){
            result = "不相同";
        }
        return result;
    }

    public String parseIsInoutMoneyUnmatch(String isInOutMoneyUnMatch){
        String strIsInOutMoneyUnMatch = "否";
        if("1".equals(isInOutMoneyUnMatch)){
            strIsInOutMoneyUnMatch = "是";
        }
        return strIsInOutMoneyUnMatch;
    }

    public String parseIsFirst(String isFirst){
        String strIsFirst = "否";
        if("1".equals(isFirst)){
            strIsFirst = "是";
        }
        return strIsFirst;
    }

    //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值
    public String formatCashType(Integer cashType){
        String result = "";
        switch (cashType) {
            case 1:
                result = "月租預繳";
                break;
            case 2:
                result = "超額";
                break;
            case 3:
                result = "代印代計";
                break;
            case 4:
                result = "加值型服務";
                break;
        }
        return result;
    }
}
