package dao;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.CommissionLog;
import com.gate.web.beans.QuerySettingVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emily on 2016/1/11.
 */
public class CommissionLogDAO extends BaseDAO {

    public Map getCommissionMasterList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from commission_log cl  where 1=1 ");
        dataSb.append(" select cl.commission_log_id, cl.commission_cp_id, cl.in_date_start, cl.in_date_end, cl.in_amount," );
        dataSb.append(" cl.commission_type, cl.main_percent, cl.commission_amount, cl.is_paid, cl.note, dc.dealer_company_name  ");
        dataSb.append(" from commission_log cl left join dealer_company dc on cl.commission_cp_id = dc.dealer_company_id ");
        dataSb.append(" where 1=1 ");

        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("dealerCompany")){
                if(StringUtils.isNotEmpty(searchMap.get("dealerCompany").toString())){
                    whereSb.append(" and cl.commission_cp_id = ?");
                    parameters.add(Integer.parseInt((String)searchMap.get("dealerCompany")));
                }
            }

            if(searchMap.containsKey("inDateS")){
                if(StringUtils.isNotEmpty(searchMap.get("inDateS").toString())){
                    whereSb.append(" and (cl.in_date_start>= to_date(?,'YYYY-MM-dd') ");
                    parameters.add(searchMap.get("inDateS"));
                }
                if(searchMap.containsKey("inDateE")) {
                    if (StringUtils.isNotEmpty(searchMap.get("inDateE").toString())) {
                        whereSb.append(" or cl.in_date_end >= to_date(?,'YYYY-MM-dd') )");
                        parameters.add(searchMap.get("inDateE"));
                    }
                }else{
                    whereSb.append(")");
                }
            } else if(searchMap.containsKey("inDateE")){
                if(StringUtils.isNotEmpty(searchMap.get("inDateE").toString())){
                    whereSb.append(" and cl.in_date_end >= to_date(?,'YYYY-MM-dd') ");
                    parameters.add(searchMap.get("inDateE"));
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

    public List getDealerCompanyList() throws Exception {
        String sql = "select * from dealer_company order by dealer_company_id ";
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    //計算佣金
    public boolean transactionCalCommission(String dealerCompany, String inDateS, String inDateE) throws Exception{
        Integer dealerCompanyId = Integer.parseInt(dealerCompany);
        //1.該經銷商在某時間起迄區間裡的已入帳月租費
        String sql = " select cm.in_date,dc.commission_type, dc.main_percent, cd.cash_detail_id " +
                " from cash_detail cd left join cash_master cm on cd.cash_master_id=cm.cash_master_id " +
                " left join package_mode pm on cd.package_id = pm.package_id " +
                " left join dealer_company dc on pm.dealer_company_id = dc.dealer_company_id "+
                //" where cd.status=1 and cd.cash_type=1 and cd.bill_type=1 and cm.in_date is not null and cd.commission_log_id is null " + //只有月租才算佣金，超額不算佣金；該筆算過佣金的話，不重新計算佣金
                //cd.cash_type: 1.月租2.月租超額(只有月租才算佣金，超額不算佣金；該筆算過佣金的話，不重新計算佣金)  cd.bill_type: 1.月租 2.級距
                " where cd.status=1 and cd.cash_type=1 and cm.in_date is not null and cd.commission_log_id is null " +
                " and cm.in_date between to_date( ? ,'YYYY-MM-dd') and  to_date( ? ,'YYYY-MM-dd')+1 " +
                " and cm.status >= 4 " + //出帳後，才可以算佣金
                " and pm.dealer_company_id=?  ";
        List parameterList = new ArrayList();
        parameterList.add(inDateS);
        parameterList.add(inDateE);
        parameterList.add(dealerCompanyId);
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();

        //經銷商計算佣金計錄
        CommissionLogEntity comLog = new CommissionLogEntity();

        String commissionType = "1"; //佣金類型0 固定金額, 1 固定比例, 2 經銷商
        BigDecimal mainPercent = new BigDecimal(0); //佣金比例
        if(dataList.size()>0){
            Map record = (Map)dataList.get(0);
            commissionType = ""+ (char)record.get("commission_type"); //佣金類型0 固定金額, 1 固定比例, 2 經銷商
            mainPercent = (BigDecimal)record.get("main_percent"); //佣金比例

            comLog.setCommissionCpId(dealerCompanyId); //經銷商id
            comLog.setCommissionYmd(TimeUtils.getYYYYMMDD(new java.util.Date())); //佣金計算年月日
            comLog.setInDateStart(new java.sql.Date(TimeUtils.parseDateYYYY_MM_DD(inDateS).getTime())); //入帳時間起
            comLog.setInDateEnd(new java.sql.Date(TimeUtils.parseDateYYYY_MM_DD(inDateE).getTime())); //入帳時間迄
            comLog.setCommissionType(commissionType);
            comLog.setMainPercent(mainPercent);
            saveEntity(comLog);
        }
        Integer comLogId = comLog.getCommissionLogId();

        BigDecimal sumPrice = new BigDecimal(0);
        BigDecimal sumComAmount = new BigDecimal(0);
        //2.每一筆細項的佣金金額
        for(int i=0; i<dataList.size(); i++){
            Map record = (Map)dataList.get(i);
            Integer cashDetailId = (Integer)record.get("cash_detail_id");
            CashDetailEntity cashDetailEntity = (CashDetailEntity)getEntity(CashDetailEntity.class, cashDetailId);

            //BigDecimal price = cashDetailEntity.getTaxInclusivePrice(); //計算金額
            BigDecimal price = cashDetailEntity.getTaxInclusivePrice(); //計算金額
            BigDecimal comAmount = price.multiply(mainPercent.divide(new BigDecimal(100))); //計算金額*佣金比例
            sumPrice = sumPrice.add(price);
            sumComAmount = sumComAmount.add(comAmount);
            cashDetailEntity.setCommissionLogId(comLogId); //佣金紀錄id
            cashDetailEntity.setCommissionAmount(comAmount); //佣金金額

            saveOrUpdateEntity(cashDetailEntity, cashDetailEntity.getCashDetailId());

        }
        if(dataList.size()>0){
            //佣金金額總計
            comLog.setInAmount(sumPrice);
            comLog.setCommissionAmount(sumComAmount);
            saveOrUpdateEntity(comLog, comLog.getCommissionLogId());
        }

        return true;
    }

    //檢視佣金明細
    public List getCommissionLogDetailList(String commissionLogId) throws Exception{

        String sql = " select cp.name, cd.cash_type, CASE WHEN cmc.package_name IS NULL THEN cmg.package_name ELSE cmc.package_name END, " +
                " cd.cal_ym, cd.out_ym, to_char(cm.in_date, 'YYYY-MM-DD') in_date,cd.tax_inclusive_price,   " +
                " cl.commission_type, cl.main_percent, cd.commission_amount, cm.is_inout_money_unmatch  " +
                " from cash_detail cd left join company cp on cd.company_id=cp.company_id " +
                " left join package_mode pm on cd.package_id = pm.package_id " +
                " left join charge_mode_cycle cmc on pm.charge_id=cmc.charge_id and pm.package_type=1 " + //月租型
                " left join charge_mode_grade cmg on pm.charge_id=cmg.charge_id and pm.package_type=2 " + //級距型
                " left join cash_master cm on cd.cash_master_id=cm.cash_master_id " +
                " left join commission_log cl on cd.commission_log_id = cl.commission_log_id " +
                " where cd.commission_log_id = ? ";
        List parameterList = new ArrayList();
        parameterList.add(Integer.parseInt(commissionLogId));
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();

        return dataList;
    }

    //修改備註
    public boolean updateNote(Integer commissionLogId, String note) throws Exception{
        CommissionLogEntity entity = (CommissionLogEntity)getEntity(CommissionLogEntity.class, commissionLogId);
        entity.setNote(note);
        saveOrUpdateEntity(entity, entity.getCommissionLogId());
        return true;
    }

    //佣金付款
    public boolean updatePayCommission(String commissionLog) throws Exception{
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CommissionLog>>(){}.getType();
        List<CommissionLog> comLogList = gson.fromJson(commissionLog, collectionType);

        //找出所有要付款的commission_log, 並付款
        for(int i=0; i<comLogList.size(); i++){
            CommissionLog bean = (CommissionLog)comLogList.get(i);
            Integer commissionLogId = bean.getCommissionLogId();

            CommissionLogEntity entity = (CommissionLogEntity)getEntity(CommissionLogEntity.class, commissionLogId);
            entity.setIsPaid("1"); //1.付款

            saveOrUpdateEntity(entity, entity.getCommissionLogId());
        }

        return true;
    }

    //匯出excel的資料
    public List<Map> exportCom(String commissionLog)throws Exception{
        List<Map> exportCommissionLogList = new ArrayList<Map>();

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<CommissionLog>>(){}.getType();
        List<CommissionLog> comLogList = gson.fromJson(commissionLog, collectionType);

        //找出所有要付款的commission_log, 並付款
        for(int i=0; i<comLogList.size(); i++){
            CommissionLog bean = (CommissionLog)comLogList.get(i);
            Integer commissionLogId = bean.getCommissionLogId();
            CommissionLogEntity entity = (CommissionLogEntity)getEntity(CommissionLogEntity.class, commissionLogId);
            BeanUtils.copyProperties(bean, entity);

            //經銷公司的名字
            Integer commissionCpId = bean.getCommissionCpId();
            DealerCompanyEntity dealerCompanyEntity = (DealerCompanyEntity)getEntity(DealerCompanyEntity.class, commissionCpId);
            String dealerCpName = dealerCompanyEntity.getDealerCompanyName();
            bean.setDealerCompanyName(dealerCpName);

            bean.setStrCommissionType(parseCommissionType(bean.getCommissionType())); //佣金類型說明
            bean.setStrIsPaid(parseIsPaid(bean.getIsPaid())); //是否付款說明
            bean.setStrMainPercent(((BigDecimal)bean.getMainPercent()).doubleValue()+"%");

            Map commissionLogMap = new HashMap();
            commissionLogMap.put("master", bean);

            List detailList = getCommissionLogDetailList(""+entity.getCommissionLogId()); //佣金明細
            commissionLogMap.put("detail", detailList);

            exportCommissionLogList.add(commissionLogMap);
        }

        return exportCommissionLogList;
    }

    //佣金類型0 固定金額, 1 固定比例, 2 經銷商
    public String parseCommissionType(String commissionType){
        String strCommissionType = "";

        if("0".equals(commissionType)){
            strCommissionType = "固定金額";
        }else if("1".equals(commissionType)){
            strCommissionType = "固定比例";
        }else if("2".equals(commissionType)){
            strCommissionType = "經銷商";
        }

        return strCommissionType;
    }

    //佣金付款狀態 1:付款
    public String parseIsPaid(String isPaid){
        String strIsPaid = "未付款";

        if("1".equals(isPaid)){
            strIsPaid = "已付款";
        }

        return strIsPaid;
    }
}
