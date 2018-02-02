package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.ChargeModeGradeVO;
import com.gate.web.formbeans.ChargeModeCycleBean;
import com.gate.web.formbeans.ChargeModeGradeBean;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.GradeEntity;

@Repository("chargeDAO")
public class ChargeDAO extends BaseDAO {

	@Autowired
    CashDAO cashDAO;
	
    public Map getChargeList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append(" select count(1) from ( ");
        if (querySettingVO.getSearchMap().size() > 0) {
            Map searchMap = querySettingVO.getSearchMap();
            if (searchMap.containsKey("charge_type")) {
                if (StringUtils.isNotEmpty(searchMap.get("charge_type").toString())) {
                    if ("1".equals(searchMap.get("charge_type").toString())) {
                        whereSb.append(" select charge_id,package_name,1 as charge_type, sales_price,status from charge_mode_cycle ");
                    } else if ("2".equals(searchMap.get("charge_type").toString())) {
                        whereSb.append(" select charge_id,package_name,2 as charge_type,sales_price,status from charge_mode_grade ");
                    }
                    whereSb.append(" where 1=1 ");
                    genWhereSql(querySettingVO, parameters, whereSb);
                } else {
                    genTypeAllSql(querySettingVO, parameters, whereSb);
                }
            }
        } else {
            genTypeAllSql(querySettingVO, parameters, whereSb);
        }
        dataSb.append(whereSb);
        countSb.append(whereSb);
        dataSb.append(" order by " + querySettingVO.getSidx() + " " + querySettingVO.getSord());
//        String datasb2 = " select charge_id,package_name,'2' as charge_type,sales_price,status from charge_mode_single  where 1=1  order by status asc";
        countSb.append(" ) charges ");
        int first = (querySettingVO.getPage() - 1) * querySettingVO.getRows();
        int end = querySettingVO.getRows();
        Map returnMap = queryForPageData(countSb.toString(), dataSb.toString(), parameters, first, end, -1, null);
        return returnMap;
    }

    private void genWhereSql(QuerySettingVO querySettingVO, List<Object> parameters, StringBuffer whereSb) {
        if (querySettingVO.getSearchMap().size() > 0) {
            Map searchMap = querySettingVO.getSearchMap();
            if (searchMap.containsKey("package_name")) {
                if (StringUtils.isNotEmpty(searchMap.get("package_name").toString())) {
                    whereSb.append(" and package_name like ?");
                    parameters.add("%" + searchMap.get("package_name") + "%");
                }

            }
            if (searchMap.containsKey("status")) {
                if (StringUtils.isNotEmpty(searchMap.get("status").toString())) {
                    whereSb.append(" and status = ?");
                    parameters.add(Integer.parseInt((String) searchMap.get("status")));
                }
            }

        }
    }

    private void genTypeAllSql(QuerySettingVO querySettingVO, List<Object> parameters, StringBuffer whereSb) {
//        whereSb.append(" select charge_id,package_name,'1' as charge_type,sales_price,status from charge_mode_cycle");
        whereSb.append(" select charge_id,package_name,'1' as charge_type, sales_price,status from charge_mode_cycle ");
        whereSb.append(" where 1=1 ");
        genWhereSql(querySettingVO, parameters, whereSb);

        whereSb.append(" union all");
        whereSb.append(" select charge_id,package_name,'2' as charge_type,sales_price,status from charge_mode_grade ");
        whereSb.append(" where 1=1 ");
        genWhereSql(querySettingVO, parameters, whereSb);


//        whereSb.append(" union all");
//        whereSb.append(" select charge_id,package_name,'2' as charge_type,sales_price,status from charge_mode_single ");
//        whereSb.append(" where 1=1 ");
//        genWhereSql(querySettingVO, parameters, whereSb);
    }

    //修改月租型方案狀態
    public void changeChargeModeCycleStatus(Integer chargeId, Integer status) throws Exception {
        ChargeModeCycleEntity entity = new ChargeModeCycleEntity();
        entity.setStatus(status);
        updateEntity(entity, chargeId);
    }

    //修改級距型方案狀態
    public void changeChargeModeGradeStatus(Integer chargeId, Integer status) throws Exception {
        ChargeModeGradeEntity entity = new ChargeModeGradeEntity();
        entity.setStatus(status);
        updateEntity(entity, chargeId);
    }

    //找到級距型方案的資料
    public ChargeModeGradeVO findChargeModeGradeByChargeId(Integer chargeId) throws Exception {
        ChargeModeGradeEntity chargeEntity = (ChargeModeGradeEntity) getEntity(ChargeModeGradeEntity.class,chargeId);
        ChargeModeGradeVO chargeVO = new ChargeModeGradeVO();
        BeanUtils.copyProperties(chargeVO,chargeEntity);
        Map map = getCreatorAndModifier(chargeVO.getCreatorId(),chargeVO.getModifierId());
        chargeVO.setCreator((String) map.get("creator"));
        chargeVO.setModifier((String) map.get("modifier"));
        return chargeVO;
    }

    //取得某級距方案的級距清單
    public List<GradeEntity> getGradeList(Integer chargeId) throws Exception {
        GradeEntity searchGradeEntity = new GradeEntity();
        searchGradeEntity.setChargeId(chargeId);
        List queryList = getSearchEntity(GradeEntity.class, searchGradeEntity);

        List list = new ArrayList();
        for(int i=0; i<queryList.size(); i++){
            GradeEntity entity = (GradeEntity)queryList.get(i);
            list.add(entity);
        }
        return list;
    }

    //新增月租型方案
    public Integer insertChargeModeCycle(ChargeModeCycleBean bean) throws Exception {
        ChargeModeCycleEntity entity = new ChargeModeCycleEntity();
        BeanUtils.copyProperties(entity, bean);
        saveEntity(entity);
        return null;
    }



//    //修改月租型方案
    public void updateChargeModeCycle(ChargeModeCycleBean bean) throws Exception {
        ChargeModeCycleEntity entity = new ChargeModeCycleEntity();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy/MM/dd");
        BeanUtils.copyProperties(entity,bean);
        updateEntity(entity, entity.getChargeId());
    }




    //新增或修改級距型方案
    public Integer insertChargeModeGrade(ChargeModeGradeBean bean) throws Exception {
        ChargeModeGradeEntity entity = new ChargeModeGradeEntity();
        BeanUtils.copyProperties(entity,bean);

        Integer chargeModeGradeId = entity.getChargeId();

        //新增或修改 經銷商資訊(dealer_company)
        if(null == chargeModeGradeId){
            entity.setStatus(1);
            saveEntity(entity);
            chargeModeGradeId = entity.getChargeId();
        }else{
            saveOrUpdateEntity(entity, chargeModeGradeId);
        }

        //如果是修改資料的話，把原本的grade找出來比較
        List tempGradeIdList = new ArrayList();
        GradeEntity searchGradeEntity = new GradeEntity();
        searchGradeEntity.setChargeId(chargeModeGradeId);
        List orgGradeList = getSearchEntity(GradeEntity.class, searchGradeEntity);
        for(int i=0; i<orgGradeList.size(); i++){
            GradeEntity orgGradeEntity = (GradeEntity)orgGradeList.get(i);
            Integer gradeId = orgGradeEntity.getGradeId();
            tempGradeIdList.add((int)gradeId);
        }


        //新增或修改 級距表(grade)資料
        for(int i=0; i<bean.getCntStart().length; i++){
            Integer gradeId = bean.getGradeId()[i];
            Integer cntStart = bean.getCntStart()[i];
            Integer cntEnd = bean.getCntEnd()[i];
            Integer price = bean.getPrice()[i];
            if(null == cntStart || null == cntEnd || null == price){
                continue;
            }
            if(null == gradeId){
                //新增
                GradeEntity gradeEntity = new GradeEntity();
                gradeEntity.setChargeId(entity.getChargeId());
                gradeEntity.setCntStart(cntStart);
                gradeEntity.setCntEnd(cntEnd);
                gradeEntity.setPrice(price);
                saveEntity(gradeEntity);

            }else{
                //修改

                if(tempGradeIdList.contains((int)gradeId)){
                    //如果gradeId不在原本的grade清單裡，就要刪除
                    tempGradeIdList.remove((int)gradeId);
                }

                GradeEntity gradeEntity = (GradeEntity)getEntity(GradeEntity.class, gradeId);
                gradeEntity.setCntStart(cntStart);
                gradeEntity.setCntEnd(cntEnd);
                gradeEntity.setPrice(price);
                saveOrUpdateEntity(gradeEntity, gradeEntity.getGradeId());
            }
        }

        for(int i=0; i<tempGradeIdList.size(); i++){
            Integer gradeId = (Integer)tempGradeIdList.get(i);
            GradeEntity gradeEntity = (GradeEntity)getEntity(GradeEntity.class, gradeId);
            deleteEntity(gradeEntity);
        }
        return null;
    }


}
