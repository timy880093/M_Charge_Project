package dao;

import com.gate.web.beans.QuerySettingVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/2/4.
 */
public class BrokerCompanyDAO extends BaseDAO{

    //畫面查詢的資料
    public Map getBrokerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = getBrokerCompanySql(false);
        StringBuffer countSb = getBrokerCompanySql(true);
        StringBuffer whereSb = new StringBuffer();

        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("2")){ //介紹公司
                if(StringUtils.isNotEmpty(searchMap.get("2").toString())){
                    whereSb.append(" and pm.broker_cp2 = ?");
                    parameters.add(searchMap.get("2"));
                }
            }
            if(searchMap.containsKey("3")){ //裝機公司
                if(StringUtils.isNotEmpty(searchMap.get("3").toString())){
                    whereSb.append(" and pm.broker_cp3 = ?");
                    parameters.add(searchMap.get("3"));
                }
            }
        }
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by "+querySettingVO.getSidx()+" "+querySettingVO.getSord());
        int first = (querySettingVO.getPage()-1)*querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);
        return returnMap;
    }

    //excel匯出的資料
    public List getExcelBrokerCompanyList(String brokerType, String brokerCompany) throws Exception{
        StringBuffer dataSb = getBrokerCompanySql(false);
        StringBuffer whereSb = new StringBuffer();
        List<Object> parameters = new ArrayList<Object>();
        String orderBy = "";

        if(StringUtils.isNotEmpty(brokerType)){
            if(brokerType.equals("2")){ //介紹公司
                if(StringUtils.isNotEmpty(brokerCompany)){
                    whereSb.append(" and pm.broker_cp2 = ?");
                    parameters.add(brokerCompany);
                }
                orderBy = " order by pm.broker_cp2, pm.broker2, cp.company_id ";
            }else if(brokerType.equals("3")){ //裝機公司
                if(StringUtils.isNotEmpty(brokerCompany)){
                    whereSb.append(" and pm.broker_cp3 = ?");
                    parameters.add(brokerCompany);
                }
                orderBy = " order by pm.broker_cp3, pm.broker3, cp.company_id ";
            }
        }
        dataSb.append(whereSb);
        dataSb.append(orderBy);
        Query query = createQuery(dataSb.toString(),parameters,null);
        return query.list();
    }

    //畫面搜尋和excel匯出的sql
    public StringBuffer getBrokerCompanySql(boolean isCount){
        StringBuffer sqlSb = new StringBuffer();
        if(isCount){ //分頁計數用sql
            sqlSb.append(" select count(1) ");
        }else{ //查詢sql
            sqlSb.append(" select pm.package_id, cp.name, cp.business_no, to_char(cmca.real_start_date,'YYYY/MM/DD') real_start_date, to_char(cmca.real_end_date,'YYYY/MM/DD') real_end_date, ");
            sqlSb.append(" cmc.package_name, dc.dealer_company_name, d.dealer_name, pm.broker_cp2, pm.broker2, ");
            sqlSb.append(" pm.broker_cp3, pm.broker3 ");
        }
        sqlSb.append(" from package_mode pm left join company cp on pm.company_id=cp.company_id ");
        sqlSb.append(" left join charge_mode_cycle_add cmca on pm.addition_id=cmca.addition_id ");
        sqlSb.append(" left join charge_mode_cycle cmc on pm.charge_id=cmc.charge_id ");
        sqlSb.append(" left join dealer_company dc on pm.dealer_company_id=dc.dealer_company_id ");
        sqlSb.append(" left join dealer d on pm.dealer_id=d.dealer_id where 1=1 ");
        sqlSb.append(" and (trim(pm.broker_cp2) <>'' or trim(pm.broker2) <>'' or trim(pm.broker_cp3) <>'' or trim(pm.broker3) <>'')  "); //只搜尋有介紹公司和裝機公司的資料

        return sqlSb;
    }



    //介紹人公司
    public List getBrokerCp2List() throws Exception {
        String sql = " select distinct broker_cp2 from package_mode where broker_cp2 is not null and broker_cp2<>'' " ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql,parameterList,null);
        return query.list();
    }

    //裝機公司
    public List getBrokerCp3List() throws Exception {
        String sql = " select distinct broker_cp3 from package_mode where broker_cp3 is not null and broker_cp3<>'' " ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql,parameterList,null);
        return query.list();
    }

}
