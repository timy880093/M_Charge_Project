package com.gate.web.servlets.backend.cash;

import com.gate.web.facades.CashServiceImp;
import com.gate.web.servlets.BaseServlet;
import dao.CashDAO;
import org.apache.commons.mail.EmailException;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/1/21.
 */
@WebServlet(urlPatterns = "/backendAdmin/cashAjaxServlet")
public class CashAjaxServlet extends BaseServlet {

    @Override
    public String[] serviceBU(Map requestParameterMap, Map requestAttMap, Map sessionMap, Map otherMap) throws Exception {
        CashServiceImp cashServiceImp = new CashServiceImp();
        Object methodObj = requestParameterMap.get("method");
        String method = "";
        if(methodObj!=null) method = ((String[])requestParameterMap.get("method"))[0];
        if(method.equals("in")){ //入帳-單筆
            String data="success!!";
            try {
                Integer cashMasterId = null;
                if (requestParameterMap.get("cashMasterId") != null) {
                    cashMasterId = Integer.parseInt(((String[]) requestParameterMap.get("cashMasterId"))[0]);
                }
                Double inAmount = null;
                if (requestParameterMap.get("inAmount") != null) {
                    inAmount = Double.parseDouble(((String[]) requestParameterMap.get("inAmount"))[0]);
                }
                String inDate = null;
                if (requestParameterMap.get("inDate") != null) {
                    inDate = ((String[]) requestParameterMap.get("inDate"))[0];
                }
                String inNote = null;
                if (requestParameterMap.get("inNote") != null) {
                    inNote = ((String[]) requestParameterMap.get("inNote"))[0];
                }

                cashServiceImp.in(cashMasterId, inAmount, inDate, inNote);

            } catch (Exception e) {
                e.printStackTrace();
                data = "error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("reSendEmail")){ //輸入自行要重寄的Email
            String data = "success!!";
            try {
                String cashMasterId = ((String[]) requestParameterMap.get("cashMasterId"))[0];
                String email =  ((String[]) requestParameterMap.get("email"))[0];
                int exeCnt = new CashDAO().reSendBillEmail(cashMasterId, email);
                if(exeCnt == 0){
                    data = "error";
                }
            }catch (EmailException e) {
                e.printStackTrace();
                data = "error";
                otherMap.put(AJAX_JSON_OBJECT, data);
                return null;
            }
            }else if(method.equals("reSendEmail1")){//輸入自行重計mail
                String data = "success!!";
                try {
                    String cashMasterId = ((String[]) requestParameterMap.get("cashMasterId"))[0];
                    String email =  ((String[]) requestParameterMap.get("email"))[0];
                    int exeCnt = new CashDAO().reSendBillEmail(cashMasterId, email);
                    if(exeCnt == 0){
                        data = "error";
                    }
                }catch (EmailException e){
                    e.printStackTrace();
                    data = "error";
                    otherMap.put(AJAX_JSON_OBJECT, data);
                    return null;


            }










            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        } else if(method.equals("cancelIn")){ //取消入帳
            String data = "success!!";
            try {
                String cashMasterId = ((String[]) requestParameterMap.get("cashMasterId"))[0];
                int exeCnt = cashServiceImp.transactionCancelIn(cashMasterId);
                if(exeCnt == 0){
                    data = "error";
                }
            }catch (EmailException e){
                e.printStackTrace();
                data = "error";
            }
            otherMap.put(AJAX_JSON_OBJECT, data);
            return null;
        }
        return new String[0];
    }
}
