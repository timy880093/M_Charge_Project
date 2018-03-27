package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.DealerCompanyVO;
import com.gate.web.displaybeans.DealerVO;
import com.gate.web.formbeans.DealerCompanyBean;
import com.gateweb.charge.model.DealerCompanyEntity;
import com.gateweb.charge.model.DealerEntity;

@Repository("commissionDAO")
public class CommissionDAO extends BaseDAO {

    public Map getDealerCompanyList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from dealer_company dc  where 1=1 ");
        dataSb.append(" select *  ");
        dataSb.append(" from dealer_company dc ");
        dataSb.append(" where 1=1 ");

        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("dealer_company_name")){
                if(StringUtils.isNotEmpty(searchMap.get("dealer_company_name").toString())){
                    whereSb.append(" and dc.dealer_company_name like ?");
                    parameters.add("%"+searchMap.get("dealer_company_name")+"%");
                }
            }
            if(searchMap.containsKey("commission_type")){
                if(StringUtils.isNotEmpty(searchMap.get("commission_type").toString())){
                    whereSb.append(" and dc.commission_type = ?");
//                    parameters.add(Integer.parseInt((String) searchMap.get("commission_type")));
                    parameters.add( searchMap.get("commission_type"));
                }
            }

            if(searchMap.containsKey("status")){
                if(StringUtils.isNotEmpty(searchMap.get("status").toString())){
                    whereSb.append(" and dc.status = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("status")));
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

    //取得某經銷商資訊
    public DealerCompanyVO getDealerCompanyByDealerCompanyId(Integer dealerCompanyId) throws Exception {
        DealerCompanyEntity entity = (DealerCompanyEntity) getEntity(DealerCompanyEntity.class, dealerCompanyId);
        if(null == entity){
            return null;
        }
        DealerCompanyVO vo = new DealerCompanyVO();
        BeanUtils.copyProperties(vo,entity);
        return vo;
    }

    //取得某經銷商的業務員清單
    public List<DealerVO> getDealerByDealerCompanyId(Integer dealerCompanyId) throws Exception {
        DealerEntity searchDealerEntity = new DealerEntity();
        searchDealerEntity.setDealerCompanyId(dealerCompanyId);

        List queryList = getSearchEntity(DealerEntity.class, searchDealerEntity);

        List list = new ArrayList();
        for(int i=0; i<queryList.size(); i++){
            DealerEntity entity = (DealerEntity)queryList.get(i);
            DealerVO vo = new DealerVO();
            BeanUtils.copyProperties(vo,entity);
            list.add(vo);
        }
        return list;
    }

//    //取得經銷商清單(下拉選單顯示經銷商列表)
//    public List<DealerCompanyEntity> getDealerCompanyListForDropBox() throws Exception {
//        List parameters = new ArrayList();
//        StringBuffer sql = new StringBuffer();
//        sql.append("SELECT * FROM dealer_company dcp where status=1  ");
////        getRoleCompany(sql,parameters,"cp.");
//        return createQueryForList(sql.toString(),parameters,DealerCompanyEntity.class);
//    }

    //新增或修改經銷商和經銷商業務員資訊
    public boolean transactionInsertDealerCompany(DealerCompanyBean bean,Long userId)throws Exception{
        DealerCompanyEntity entity = new DealerCompanyEntity();
        BeanUtils.copyProperties(entity, bean);
        Integer dealerCompanyId = entity.getDealerCompanyId();
        entity.setCreatorId(userId.intValue());

        //新增或修改 經銷商資訊(dealer_company)
        if(null == entity.getDealerCompanyId()){
            entity.setStatus(1);
            saveEntity(entity);
            dealerCompanyId = entity.getDealerCompanyId();
        }else{
            saveOrUpdateEntity(entity, entity.getDealerCompanyId());
        }

        //新增或修改 經銷商的業務員資訊(dealer)
        for(int i=0; i<bean.getDealerId().length; i++){
            Integer dealerId = bean.getDealerId()[i];
            String dealerName = bean.getDealerName()[i];
            String dealerPhone = bean.getDealerPhone()[i];
            String dealerEmail = bean.getDealerEmail()[i];
            if(StringUtils.isEmpty(dealerName)){
                continue;
            }
            if(null == dealerId){
                DealerEntity dealerEntity = new DealerEntity();
                dealerEntity.setDealerCompanyId(dealerCompanyId);
                dealerEntity.setDealerName(dealerName);
                dealerEntity.setDealerPhone(dealerPhone);
                dealerEntity.setDealerEmail(dealerEmail);
                dealerEntity.setCreatorId(userId.intValue());
                saveEntity(dealerEntity);
            }else{
                DealerEntity dealerEntity = (DealerEntity)getEntity(DealerEntity.class, dealerId);
                dealerEntity.setDealerName(dealerName);
                dealerEntity.setDealerPhone(dealerPhone);
                dealerEntity.setDealerEmail(dealerEmail);
                saveOrUpdateEntity(dealerEntity, dealerEntity.getDealerId());
            }
        }
        return true;
    }

    //變更經銷商狀態(1:open, 2:stop)
    public void updateCommissionStatus(Integer dealerCompanyId,Integer status) throws Exception {
        DealerCompanyEntity entity = new DealerCompanyEntity();
        entity.setStatus(status);
        updateEntity(entity, dealerCompanyId);
    }

}
