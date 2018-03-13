package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.formbeans.WarrantyBean;
import com.gateweb.charge.model.WarrantyEntity;

@Repository("warrantyDAO")
public class WarrantyDAO extends BaseDAO {

    public Map getWarrantyList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from warranty w left join company cp on w.company_id=cp.company_id  left join dealer_company dcp on w.dealer_company_id=dcp.dealer_company_id where 1=1 ");
        dataSb.append(" select w.*, cp.name, cp.business_no, dcp.dealer_company_name ");
        dataSb.append(" from warranty w left join company cp on w.company_id=cp.company_id ");
        dataSb.append(" left join dealer_company dcp on w.dealer_company_id=dcp.dealer_company_id where 1=1 ");

        if (querySettingVO.getSearchMap().size() > 0) {
            Map searchMap = querySettingVO.getSearchMap();
            if (searchMap.containsKey("userCompanyId")) {
                if (StringUtils.isNotEmpty(searchMap.get("userCompanyId").toString())) {
                    whereSb.append(" and w.company_id = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("userCompanyId")));
                }
            }
            if (searchMap.containsKey("userDealerCompanyId")) {
                if (StringUtils.isNotEmpty(searchMap.get("userDealerCompanyId").toString())) {
                    whereSb.append(" and dcp.dealer_company_id = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("userDealerCompanyId")));
                }
            }

            if (searchMap.containsKey("onlyShipForSearch")) {
                if (StringUtils.isNotEmpty(searchMap.get("onlyShipForSearch").toString())) {
                    whereSb.append(" and w.only_ship = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("onlyShipForSearch")));
                }
            }
            if (searchMap.containsKey("statusForSearch")) {
                if (StringUtils.isNotEmpty(searchMap.get("statusForSearch").toString())) {
                    whereSb.append(" and w.status = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("statusForSearch")));
                }
            }
        }

        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by " + querySettingVO.getSidx() + " " + querySettingVO.getSord() + " , w.warranty_id desc ");
        int first = (querySettingVO.getPage() - 1) * querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);
        return returnMap;
    }

    public List getWarrantyList(String warrantyId)throws Exception{
        String sql ="select count(1) from warranty w left join company cp on w.company_id=cp.company_id  left join dealer_company dcp on w.dealer_company_id=dcp.dealer_company_id where 1=1 "+
     " select w.*, cp.name, cp.business_no, dcp.dealer_company_name "+
        " from warranty w left join company cp on w.company_id=cp.company_id "+
      " left join dealer_company dcp on w.dealer_company_id=dcp.dealer_company_id where 1=1 ";




        List parameterList = new ArrayList();
      parameterList.add(Integer.parseInt(warrantyId));
      Query query = createQuery(sql,parameterList);
      List dataList= query.list();

      return dataList;



    }







    public Integer updateWarranty(WarrantyBean warrantyBean) throws Exception {
        WarrantyEntity entity = new WarrantyEntity();
        BeanUtils.copyProperties(entity, warrantyBean);

        if (null == entity.getCompanyId()) {
            entity.setCompanyId(0);
        }
        if (null == entity.getStartDate()) {
            entity.setStartDate(timeUtils.parseDateYYYYMMDD("1000101"));
        }
        if (null == entity.getEndDate()) {
            entity.setEndDate(timeUtils.parseDateYYYYMMDD("1000101"));
        }
        if (null == entity.getOnlyShip()) {
            entity.setOnlyShip(2);
        }
        if (null == entity.getDealerCompanyId()) {
            entity.setDealerCompanyId(0);
        }

//        saveOrUpdateEntity(entity, entity.getWarrantyId());
        if (StringUtils.isEmpty(warrantyBean.getWarrantyId())) {
            //新增
            saveEntity(entity);
        } else {
            //更新
            updateEntity(entity, entity.getWarrantyId());
        }
        return 1;
    }

    //經銷商清單
    public List getUserDealerCompanyList() throws Exception {
        String sql = " select dealer_company_id, dealer_company_name, business_no from dealer_company order by dealer_company_id ";
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        return query.list();
    }
   //匯出excel資料
//    public List<Map> exportWar(String warranty) throws Exception {
//        List<Map> exportWarrantyList = new ArrayList<Map>();
//
//        Gson gson = new Gson();
//        Type collectionType = new TypeToken<List<Warranty>>() {
//        }.getType();
//        List<Warranty> warrantyList= gson.fromJson(warranty, collectionType);
//
//        for (int i = 0; i < warrantyList.size(); i++) {
//              Warranty bean = (Warranty)warrantyList.get(i);
//             Integer warrantyId = bean.getWarrantyId();
//            WarrantyEntity entity = (WarrantyEntity) getEntity(WarrantyEntity.class, warrantyId);
//            BeanUtils.copyProperties(bean, entity);
//
//
//
//
//            Map warrantyMap = new HashMap();
//            warrantyMap.put("master",bean);
//
//            List detailList = getWarrantyList("" + entity.getWarrantyId());
//            warrantyMap.put("detail", detailList);
//
//
//            exportWarrantyList.add(warrantyMap);
//        }
//        return exportWarrantyList;
//    }
//








        }









