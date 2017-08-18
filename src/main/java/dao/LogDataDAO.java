package dao;

import com.gate.web.beans.QuerySettingVO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Good688 on 2014/8/26.
 */
public class LogDataDAO extends BaseDAO {
    public Map getLogList (QuerySettingVO querySettingVO) throws Exception{
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer countSb = new StringBuffer();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from log_data join log_parameter on log_data.parameter_id = log_parameter.parameter_id where 1=1 ");
        dataSb.append("SELECT ldata.log_id,ldata.log_url,ldata.access_time,ldata.user_name,ldata.request_obj,ldata.session_obj,lparameter.action_name  ");
        dataSb.append("FROM log_data ldata JOIN log_parameter lparameter on ldata.parameter_id = lparameter.parameter_id where 1=1 ");
        if (querySettingVO.getSearchMap().size()>0){
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("user_name")){
                if(StringUtils.isNotEmpty(searchMap.get("user_name").toString())){
                    whereSb.append(" and user_name like ?");
                    parameters.add("%"+searchMap.get("user_name")+"%");
                }
            }
            if(searchMap.containsKey("action_name")){
                if(StringUtils.isNotEmpty(searchMap.get("action_name").toString())){
                    whereSb.append(" and action_name like ?");
                    parameters.add("%"+searchMap.get("action_name")+"%");
                }
            }
            if(searchMap.containsKey("searchStart")&&searchMap.containsKey("searchEnd")){
                if(StringUtils.isNotEmpty(searchMap.get("searchStart").toString())&& StringUtils.isNotEmpty(searchMap.get("searchEnd").toString())){
                    whereSb.append(" and access_time >  '"+searchMap.get("searchStart")+"' ");
                    //parameters.add(searchMap.get("searchStart"));
                    whereSb.append(" and access_time < '"+searchMap.get("searchEnd")+"' " );
                    //parameters.add(searchMap.get("searchEnd"));
                }
            }
        }
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by "+querySettingVO.getSidx()+" "+querySettingVO.getSord());
        int first = (querySettingVO.getPage()-1)*querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(),dataSb.toString(),parameters,first,end,-1,null);
        return returnMap;
    }

}
