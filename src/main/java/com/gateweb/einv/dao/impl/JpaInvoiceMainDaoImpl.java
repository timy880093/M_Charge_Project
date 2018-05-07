package com.gateweb.einv.dao.impl;

import com.gateweb.einv.dao.InvoiceMainDao;
import com.gateweb.einv.model.InvoiceAmountSummaryReportEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 5/7/2018.
 */
@Repository("einvInvoiceMainDao")
public class JpaInvoiceMainDaoImpl extends EinvJpaGenericDaoImpl<com.gateweb.einv.model.InvoiceMain,        Long      > implements InvoiceMainDao {

    public JpaInvoiceMainDaoImpl(){

    }

    @Override
    /**
     * 看起來大於包含小於不包含。
     */
    public List<InvoiceAmountSummaryReportEntity> getAmountSummaryReport(Timestamp from, Timestamp to){
        String selectSql =
                "SELECT invoiceDate " +
                        " ,seller " +
                        " ,buyer " +
                        " ,cInvoiceStatus " +
                        " ,count(cInvoiceStatus) as amount " +
                        " ,sum(totalAmount) as  total  " +
                        " FROM InvoiceMain im " +
                        " WHERE EXISTS ( " +
                        "  SELECT businessNo " +
                        "  FROM Company c " +
                        "  WHERE verifyStatus = '3' " +
                        "   AND companyType = '1' " +
                        "   AND businessNo = seller " +
                        "  ) " +
                        " AND cInvoiceStatus != 1 " +
                        " AND createDate > :fromTimestamp " +
                        " AND createDate < :toTimestamp " +
                        " GROUP BY " +
                        " invoiceDate " +
                        " ,seller " +
                        " ,buyer " +
                        " ,cInvoiceStatus ORDER BY seller " +
                        " ,invoiceDate";

        Query query = entityManager.createQuery(selectSql);
        query.setParameter("fromTimestamp", from);
        query.setParameter("toTimestamp", to);
        List<InvoiceAmountSummaryReportEntity> resultList = new ArrayList<>();
        for(Object arrayObject : query.getResultList()){
            Object[] valueArray = (Object[])arrayObject;
            InvoiceAmountSummaryReportEntity invoiceAmountSummaryReportEntity = new InvoiceAmountSummaryReportEntity();
            invoiceAmountSummaryReportEntity.setInvoiceDate(valueArray[0].toString());
            invoiceAmountSummaryReportEntity.setSeller(valueArray[1].toString());
            invoiceAmountSummaryReportEntity.setBuyer(valueArray[2].toString());
            invoiceAmountSummaryReportEntity.setInvoiceStatus(new Integer(valueArray[3].toString()));
            invoiceAmountSummaryReportEntity.setAmount(new Integer(valueArray[4].toString()));
            invoiceAmountSummaryReportEntity.setTotal(new BigDecimal(valueArray[5].toString()));
            //設定預設值
            //為了之後方便，做了一個小操作
            invoiceAmountSummaryReportEntity.setCreateDate(from);
            invoiceAmountSummaryReportEntity.setModifyDate(to);
            invoiceAmountSummaryReportEntity.setCreatorId(7);
            invoiceAmountSummaryReportEntity.setModifierId(7);
            resultList.add(invoiceAmountSummaryReportEntity);
        }
        return resultList;
    }
}
