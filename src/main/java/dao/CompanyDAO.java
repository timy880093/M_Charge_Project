package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.QuerySettingVO;
import com.gateweb.charge.model.CompanyEntity;

@Repository("companyDAO")
public class CompanyDAO extends BaseDAO {

    public Boolean checkBusinessNo(String businessNo) throws Exception {
        String sql = " select * from company where business_no = ? ";
        List parameterList = new ArrayList();
        parameterList.add(businessNo);
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList.size()>0;
    }

    public Boolean checkBusinessNo(String businessNo,String companyId) throws Exception {
        String sql = " select * from company where business_no = ? ";
        List parameterList = new ArrayList();
        parameterList.add(businessNo);
        if(StringUtils.isNotEmpty(companyId)){
            sql = sql +" and company_id != ? ";
            parameterList.add(Integer.parseInt(companyId));
        }

        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList.size()>0;
    }

    public Map getCompanyList(QuerySettingVO querySettingVO) throws Exception {
        Timestamp evlS = TimeUtils.getCurrentTimestamp();

        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from company cp ");
        countSb.append(" left join (select pm.* from (select max(package_id) package_id,company_id from package_mode group by company_id) p left join package_mode pm ");
        countSb.append(" on p.package_id=pm.package_id) pm2  ");
        countSb.append("  on (cp.company_id = pm2.company_id)   left join charge_mode_cycle_add ad on pm2.addition_id = ad.addition_id  where 1=1 and (cp.verify_status=3 or cp.verify_status=9) and cp.company_type=1 ");

        dataSb.append(" select cp.transfer_type,cp.company_id,cp.name,cp.business_no,cp.verify_status,cp.phone,cp.contact1,cp.parent_id,pm2.package_type, pm2.status as pkg_status, ad.real_start_date, ad.real_end_date, pm2.package_id " );
        dataSb.append("  from company as cp ");
        dataSb.append(" left join (select pm.* from (select max(package_id) package_id,company_id from package_mode group by company_id) p left join package_mode pm ");
        dataSb.append(" on p.package_id=pm.package_id ) pm2  ");
        dataSb.append("  on (cp.company_id = pm2.company_id)   left join charge_mode_cycle_add ad on pm2.addition_id = ad.addition_id  ");
        dataSb.append(" where 1=1 and (cp.verify_status=3 or cp.verify_status=9) and cp.company_type=1 ");
        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("name")){
                if(StringUtils.isNotEmpty(searchMap.get("name").toString())){
                    whereSb.append(" and cp.name like ?");
                    parameters.add("%"+searchMap.get("name")+"%");
                }

            }
            if(searchMap.containsKey("business_no")){
                if(StringUtils.isNotEmpty(searchMap.get("business_no").toString())){
                    whereSb.append(" and cp.business_no like ?");
                    parameters.add("%"+searchMap.get("business_no")+"%");
                }
            }
            if(searchMap.containsKey("almost_out")){
                if(StringUtils.isNotEmpty(searchMap.get("almost_out").toString())){
                whereSb.append(" and to_char(ad.real_end_date, 'YYYYMM') <= ?  ");
                parameters.add(searchMap.get("almost_out").toString());
                }
            }
            if(searchMap.containsKey("pkg_status")){
                if(StringUtils.isNotEmpty(searchMap.get("pkg_status").toString())){
                    if(searchMap.get("pkg_status").toString().equals("4")){
                        //0000285: Jia說:「用戶費率管理頁面」希望可以在搜尋條件裡可選擇顯示未綁約用戶
                        whereSb.append(" and (pm2.status is null or pm2.status not in ('0','1','2','3')) ");
                    }else{
                        whereSb.append(" and pm2.status = ?  ");
                        parameters.add(searchMap.get("pkg_status").toString());
                    }
                }
            }
        }
//        getRoleCompany(whereSb,parameters,"cp.");
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by "+querySettingVO.getSidx()+" "+querySettingVO.getSord());
        int first = (querySettingVO.getPage()-1)*querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);

        Timestamp evlE = TimeUtils.getCurrentTimestamp();
        long difference= evlE.getTime() - evlS.getTime();
        logger.info("CompanyDAO getCompanyList 撈用戶費率管理畫面清單sql difference="+difference+"ms");

        return returnMap;
    }

    public List getCompanyLevelByBusinessNo(String businessNo) throws Exception {
        String sql =
                "WITH RECURSIVE\n" +
                "        company_level AS\n" +
                "        (\n" +
                "        SELECT  company.*\n" +
                "        FROM    company\n" +
                "        WHERE   business_no = ? and verify_status = 3 \n" +
                "        UNION ALL\n" +
                "        SELECT  c2.*\n" +
                "        FROM    company c2\n" +
                "        JOIN    company_level\n" +
                "        ON      c2.parent_id = company_level.company_id AND c2.verify_status = 3 \n" +
                "        )\n" +
                "SELECT  *\n" +
                "FROM company_level";

        List parameterList = new ArrayList();
        parameterList.add(businessNo);
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    public Map getCompanyByBusinesNo(String businessNo) throws Exception {
        List parameterList = new ArrayList();
        String sql = "SELECT * FROM company WHERE business_no = ?";
        parameterList.add(businessNo);
        Query query = createQuery(sql, parameterList, null);
        return (Map)query.uniqueResult();

    }

    public List getChildCompanyList(Integer companyId) throws Exception {
        List parameterList = new ArrayList();
        String sql = "SELECT * FROM company WHERE parent_id = ? AND verify_status = 3 order by company_id";
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);
        return query.list();
    }

    public Map getCompanyInfo(Integer companyId) throws Exception {
        List parameterList = new ArrayList();
        String sql = "SELECT company_id,business_no,name company_name FROM company WHERE company_id = ? ";
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);
        return (Map)query.uniqueResult();
    }

    public List getCompanyListByType(Integer type) throws Exception {
        List parameterList = new ArrayList();
        String sql = "SELECT company_id,name company_name,business_no FROM company WHERE company_type = ? and verify_status = 3";
        parameterList.add(type);
        Query query = createQuery(sql, parameterList, null);
        return query.list();
    }

    public List<CompanyEntity> getCompanyList(Boolean parent) throws Exception {
        String sql = "SELECT * FROM company WHERE company_type = 1 or company_type=4";
        if(parent.equals(true)){
            sql +=" AND parent_id is null ";
        }
        return createQueryForList(sql,null,CompanyEntity.class);
    }

    public List getCommissionCompanyList(Integer packageType) throws Exception {
        String whereType = "";
        if(packageType==2){
             whereType = " package_type = 2 ";
        } else{
             whereType = " (package_type = 0 or package_type=1) ";
        }
        String sql = "select company.company_id,company.name company_name,company.business_no " +
                " from commission" +
                " left join \"user\"" +
                " on commission.broker_id = \"user\".user_id" +
                " left join company" +
                " on \"user\".company_id = company.company_id" +
                " left join commission_package" +
                " on commission.package_id = commission_package.package_id" +
                " where " +whereType+
                " GROUP BY company.company_id,company.name,company.business_no";

        Query query = createQuery(sql, null);
        return query.list();
    }

    public List<CompanyEntity> getCompanyListByRole() throws Exception {
        List parameters = new ArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM company cp WHERE company_type = 1  ");
//        getRoleCompany(sql,parameters,"cp.");
        return createQueryForList(sql.toString(),parameters,CompanyEntity.class);
    }


    public static void main(String... args) {
        CompanyDAO dao =  new CompanyDAO();

        try {
//            Map entity = companyChargeDAO.getCompanyByBusinesNo("27748439");
//
//            System.out.println(entity.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
