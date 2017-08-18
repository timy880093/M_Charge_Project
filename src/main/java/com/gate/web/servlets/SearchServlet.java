package com.gate.web.servlets;

import com.gate.web.beans.QuerySettingVO;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/7/16
 * Time: 上午 11:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class SearchServlet extends BaseServlet {

    public Map getData(Map requestParameterMap,QuerySettingVO querySettingVO, Map otherMap,String funcId) throws Exception {
        HttpServletRequest request = (HttpServletRequest)otherMap.get(REQUEST);
        QuerySettingVO vo = setQuerySettingVO(requestParameterMap,querySettingVO,request,funcId);
        Map data = doSearchData(vo,otherMap);
        Map gridData = setGrid(vo,data);
        return gridData;
    }

    public Map getDownloadData(Map requestParameterMap,QuerySettingVO querySettingVO, Map otherMap,String funcId) throws Exception {
        HttpServletRequest request = (HttpServletRequest)otherMap.get(REQUEST);
        QuerySettingVO vo = setQuerySettingVO(requestParameterMap,querySettingVO,request,funcId);
        Map data = doSearchDownloadData(vo,otherMap);
        return data;
    }



    public QuerySettingVO setQuerySettingVO(Map requestParameterMap, QuerySettingVO querySettingVO, HttpServletRequest request,String funcId) throws UnsupportedEncodingException {

        if(request.getSession().getAttribute(funcId)!=null && ((String[])requestParameterMap.get("_search"))[0].equals("false")){
            querySettingVO = (QuerySettingVO)request.getSession().getAttribute(funcId);
        } else{

            String searchField[]=(String[])requestParameterMap.get("searchField[]");
            String searchString[]=(String[])requestParameterMap.get("searchString[]");
            Map searchMap = new HashMap();
            if(searchField!=null){
                for(int i=0;i<searchField.length;i++){
                    searchMap.put(searchField[i],java.net.URLDecoder.decode(searchString[i], "UTF-8"));
                }
            }
            querySettingVO.setSearchMap(searchMap);
            String [] sidx = (String[]) requestParameterMap.get( "sidx" );
            querySettingVO.setSidx(String.valueOf(sidx[0]));
            String [] sord = (String[]) requestParameterMap.get( "sord" );
            querySettingVO.setSord(String.valueOf(sord[0]));
            String [] page = (String[]) requestParameterMap.get( "page" );
            querySettingVO.setPage(Integer.valueOf(page[0]));
            String [] rows = (String[]) requestParameterMap.get( "rows" );
            querySettingVO.setRows(Integer.valueOf(rows[0]));
            request.getSession().setAttribute(funcId,querySettingVO);

        }
        return querySettingVO;
    }

    public Map setGrid(QuerySettingVO querySettingVO,Map data){
        Integer size =Integer.valueOf( data.get("TOTAL_COUNT").toString());
        Map pageMap = new HashMap();
        Integer pages = null;
        if(size% querySettingVO.getRows()==0){
            pages = size/ querySettingVO.getRows();
        } else{
            pages = size/ querySettingVO.getRows()+1;
        }
        pageMap.put("total",pages);
        pageMap.put("page",querySettingVO.getPage());
        pageMap.put("records",size);
        pageMap.put("rows", data.get("DATA_LIST"));
        return pageMap;
    }

    public abstract Map doSearchData(QuerySettingVO querySettingVO, Map otherMap) throws Exception;

    public abstract Map doSearchDownloadData(QuerySettingVO querySettingVO, Map otherMap) throws Exception;
}
