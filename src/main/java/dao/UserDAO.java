package dao;

import com.gate.web.beans.QuerySettingVO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("userDAO")
public class UserDAO extends BaseDAO {

    public Map getUserList(QuerySettingVO querySettingVO,Integer companyId) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from \"user\" u left join company c on (u.company_id = c.company_id) where 1=1 ");
        dataSb.append(" select u.*,c.name company_name ");
        dataSb.append(" from \"user\" u left join company c on (u.company_id = c.company_id) ");
        dataSb.append(" where 1=1 ");
        if(companyId!=null){           //一般公司帳號登入
            whereSb.append(" and u.company_id = ?");
            parameters.add(companyId);
        }
        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("company_id")&&!searchMap.get("company_id").equals("0")){
                whereSb.append(" and c.company_id = ?");
                parameters.add(Integer.valueOf(searchMap.get("company_id").toString()));
            }
            if(searchMap.containsKey("user_name")){
                if(StringUtils.isNotEmpty(searchMap.get("user_name").toString())){
                    whereSb.append(" and u.name like ?");
                    parameters.add("%"+searchMap.get("user_name")+"%");
                }
            }
            if(searchMap.containsKey("account")){
                if(StringUtils.isNotEmpty(searchMap.get("account").toString())){
                    whereSb.append(" and u.account like ?");
                    parameters.add("%"+searchMap.get("account")+"%");
                }
            }
            if(searchMap.containsKey("company_name")){
                if(StringUtils.isNotEmpty(searchMap.get("company_name").toString())){
                    whereSb.append(" and c.name like ?");
                    parameters.add("%"+searchMap.get("company_name")+"%");
                }
            }
        }
//        getRoleCompany(whereSb,parameters,"u.");
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by "+querySettingVO.getSidx()+" "+querySettingVO.getSord());
        int first = (querySettingVO.getPage()-1)*querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);
        return returnMap;
    }

    public Boolean checkAccount(String account,String userId) throws Exception {
        String sql = " select * from \"user\" where account = ? ";
        List parameterList = new ArrayList();
        parameterList.add(account);
        if(userId!=null){
            sql = sql +" and user_id != ? ";
            parameterList.add(Integer.parseInt(userId));
        }

        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList.size()>0;
    }


}
