package dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gate.web.facades.CalCycleService;
import com.gateweb.utils.BeanConverterUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.ContinuePackage;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gateweb.charge.model.BillCycleEntity;
import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CashMasterEntity;
import com.gateweb.charge.model.ChargeModeCycleAddEntity;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.ChargeModeGradeEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository("companyChargeDAO")
public class CompanyChargeDAO extends BaseDAO {

	@Autowired
    CashDAO cashDAO;

    @Autowired
    CalCycleDAO calCycleDAO;

    @Autowired
    BeanConverterUtils beanConverterUtils;

    @Autowired
    CalCycleService calCycleService;

    public List getChargeMonthList() throws Exception {
        String sql = "select charge_id,package_name from charge_mode_cycle where status = 1 ";
//        String sql = "select '1' as charge_type,charge_id,package_name from charge_mode_cycle where status = 1 ";
//                " union all " +
//                " select '2' as charge_type, charge_id,package_name from charge_mode_grade where status = 1 ";
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    public List getChargeGradeList() throws Exception {
        String sql = "select charge_id,package_name from charge_mode_grade where status = 1" ;
        List parameterList = new ArrayList();
        Query query = createQuery(sql, parameterList, null);
        List dataList = query.list();
        return dataList;
    }

    /**
     * 撈經銷商業務的list
     * @param dealerComplayId
     * @return
     * @throws Exception
     */
    public List getDealerList(Integer dealerComplayId) throws Exception {
        String sql = " select * from dealer where dealer_company_id=? ";
        List parameterList = new ArrayList();
        parameterList.add(dealerComplayId);
        Query query = createQuery(sql, parameterList, null);
        return query.list();

    }

    /**
     * charge_cycle歷史記錄BY companyId
     * @param companyId
     * @return
     * @throws Exception
     */
    public List<Map> getChargeCycleHisByCompany(Integer companyId) throws Exception {
        Timestamp evlS = timeUtils.getCurrentTimestamp();

        String sql = " select pm.company_id,pm.package_id ," +
                "  CASE WHEN cmc.package_name IS NULL THEN cmg.package_name ELSE cmc.package_name END," +
                " pm.package_type,cmca.start_date,cmca.end_date,cmca.real_start_date, " +
                " cmca.real_end_date, pm.status " + //,getusername(pm.broker_id) broker_name from package_mode pm " +
                " from package_mode pm left join charge_mode_cycle cmc on (pm.charge_id = cmc.charge_id and pm.package_type=1)" +
                " left join charge_mode_grade cmg on (pm.charge_id = cmg.charge_id and pm.package_type=2) "+
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " where  company_id=?  " + //" pm.package_type= and "<=月租/級距
                " order by cmca.start_date desc, pm.package_id desc ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);

        Timestamp evlE = timeUtils.getCurrentTimestamp();
        long difference= evlE.getTime() - evlS.getTime();
        logger.info("CompanyChargeDAO getChargeCycleHisByCompany 撈用戶方案歷史紀錄sql  difference="+difference+"ms");

        return query.list();
    }

    /**
     * 合約結清前，先作試算的試算資料
     * @param companyId
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getSettleInfo(Integer companyId, Integer packageId)throws Exception{
        //月租型
        String sql = " select pm.package_id, " +
                " case when cmc.package_name is null then cmg.package_name else cmc.package_name end," +
                " to_char(cmca.real_start_date,'YYYY-MM-DD') real_start_date,to_char(cmca.real_end_date,'YYYY-MM-DD') real_end_date " +
                " ,to_char(cmca.start_date,'YYYY-MM-DD') start_date, to_char(cmca.end_date,'YYYY-MM-DD') end_date " +
                " from package_mode pm left join charge_mode_cycle cmc on pm.charge_id = cmc.charge_id and pm.package_type=1 " +
                " left join charge_mode_grade cmg on pm.charge_id = cmg.charge_id and pm.package_type=2 " +
                " left join charge_mode_cycle_add cmca on pm.addition_id = cmca.addition_id " +
                " where pm.company_id= ?  and pm.package_id = ? ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map)query.uniqueResult();
    }

    /**
     * 合約結清前，先作試算的試算資料
     * @param packageId
     * @param endDate
     * @return
     * @throws Exception
     */
    public Map getCycleTryCalSettle(Integer packageId, String endDate)throws Exception{
        String year = endDate.substring(0, 4);
        String month = endDate.substring(5,7);
        String packageEndDate = year + month;

        //已繳月租費
        String sql = " select coalesce(sum(coalesce(pay_month,0)),0) paymonth from bill_cycle where package_id= ? and cash_in_month_id is not null ";
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        Map paidMonthMap = (Map)query.uniqueResult();
        BigDecimal paidMonth = (BigDecimal)paidMonthMap.get("paymonth");

        //已使用月租費
        sql = " select coalesce(sum(coalesce(price,0)),0) usedMonth from bill_cycle where package_id=? " +
                "and bill_id<= (select bill_id from bill_cycle where package_id= ? and year_month=?) and is_price_free is null  ";
        parameterList = new ArrayList();
        parameterList.add(packageId);
        parameterList.add(packageId);
        parameterList.add(packageEndDate);
        query = createQuery(sql, parameterList, null);
        Map usedMonthMap = (Map)query.uniqueResult();
        BigDecimal usedMonth = (BigDecimal)usedMonthMap.get("usedmonth");

        //remainderMonth =  paidMonth - usedMonth
        BigDecimal remainderMonth = paidMonth.subtract(usedMonth);

        Map resultMap = new HashMap();
        resultMap.put("paidMonth", paidMonth);
        resultMap.put("usedMonth", usedMonth);
        resultMap.put("remainderMonth", remainderMonth);

        return resultMap;
    }

    /**
     * 確認結清
     * @param packageId
     * @param endDate
     * @param realEndDate
     * @return
     * @throws Exception
     */
    public Map transactionDoSettle(Integer packageId, String endDate, String realEndDate)throws Exception{
        String year = endDate.substring(0, 4);
        String month = endDate.substring(5,7);
        String packageEndDate = year + month;

        String rYear = realEndDate.substring(0, 4);
        String rMonth = realEndDate.substring(5,7);
        String packageRealEndDate = rYear + rMonth;

        //結清:更新charge_mode_cycle_add的real_end_date和end_date，並且把bill_cylce裡end_date後的bill全部作廢，cash_detail裡的end_date後的cash也要全部作廢
        PackageModeEntity searchPackageModeEntity = new PackageModeEntity();
        searchPackageModeEntity.setPackageId(packageId);
        List packageModeList =  getSearchEntity(PackageModeEntity.class, searchPackageModeEntity);

        //更新此package_id的status=2(作廢)
        String sql = " update package_mode set status='2' where package_id=? " ;
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        executeSql(sql, parameterList);


        sql = " select addition_id from package_mode where package_id=? " ;
        parameterList.clear();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        Map packageModelMap = (Map)query.uniqueResult();
        Integer additionId = (Integer)packageModelMap.get("addition_id");

        //1.更新real_end_date和end_date
        ChargeModeCycleAddEntity chargeModeCycleAddEntity = new ChargeModeCycleAddEntity();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date enDateCal = simpleDateFormat.parse(endDate);
        Date realEndDateCal = simpleDateFormat.parse(realEndDate);
        chargeModeCycleAddEntity.setEndDate(new java.sql.Date(enDateCal.getTime()));
        chargeModeCycleAddEntity.setRealEndDate(new java.sql.Date(realEndDateCal.getTime()));
        saveOrUpdateEntity(chargeModeCycleAddEntity,additionId);

        //2.把bill_cylce裡end_date後的bill全部作廢
        BillCycleEntity searchBillCycleEntity = new BillCycleEntity();
        searchBillCycleEntity.setPackageId(packageId);
        List billCycleList =  getSearchEntity(BillCycleEntity.class, searchBillCycleEntity);
        Integer billFailStartId = 0;
        for(int j=0; j<billCycleList.size(); j++){
            BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleList.get(j);
            String ym = billCycleEntity.getYearMonth();
            if(ym.equals(year + month)){
                billFailStartId = billCycleEntity.getBillId();
                break;
            }
        }
        for(int j=0; j<billCycleList.size(); j++){
            BillCycleEntity  billCycleEntity = (BillCycleEntity)billCycleList.get(j);
            Integer billId = billCycleEntity.getBillId();
            Integer companyId = billCycleEntity.getCompanyId();
            if(billId > billFailStartId){
                billCycleEntity.setStatus("2"); //1.生效 2.作廢
                saveOrUpdateEntity(billCycleEntity, billCycleEntity.getBillId());

                //3.cash_detail裡的end_date後的cash也要全部作廢
                //(未入帳的cash_detail全部要作廢)
                //3.1月租費
                Integer cashOutMonthId = billCycleEntity.getCashOutMonthId();
                if(null != cashOutMonthId){ //出帳的
                    Integer cashInMonthId = billCycleEntity.getCashInMonthId();
                    if(null == cashInMonthId){ //未入帳的 (要作廢)
                        //已使用月租費
                        sql = " select cash_master_id from cash_detail where cash_detail_id=? " ;
                        parameterList.clear();
                        parameterList.add(cashOutMonthId);
                        query = createQuery(sql, parameterList, null);
                        Map cashDetailMap = (Map)query.uniqueResult();
                        Integer cashMasterId = (Integer)cashDetailMap.get("cash_master_id");

                        CashDetailEntity cashDetail_status = new CashDetailEntity();
                        cashDetail_status.setStatus(2); //1.生效 2.作廢
                        saveOrUpdateEntity(cashDetail_status, cashOutMonthId);


                        //重算cashMaster
                        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
                        searchCashMasterEntity.setCashMasterId(cashMasterId);
                        searchCashMasterEntity.setCompanyId(companyId);
                        List cashMasterList =  getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);

                        for(int k=0; k<cashMasterList.size(); k++){
                            CashMasterEntity cashMasterEntity = (CashMasterEntity)cashMasterList.get(0);
                            Integer cashMasterId_m = cashMasterEntity.getCashMasterId();
                            if(cashMasterId_m.equals(cashMasterId)){
                                cashMasterEntity = cashDAO.sumCashMaster(cashMasterEntity);
                                saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
                            }
                        }
                    }
                }

                //3.2超額費
                Integer cashOutOverId = billCycleEntity.getCashOutOverId();
                if(null != cashOutOverId){ //有出帳的(要作廢)
                    Integer cashInOverId = billCycleEntity.getCashInOverId();
                    if(null == cashInOverId){ //未入帳的
                        sql = " select cash_master_id from cash_detail where cash_detail_id=? " ;
                        parameterList.clear();
                        parameterList.add(cashOutOverId);
                        query = createQuery(sql, parameterList, null);
                        Map cashDetailMap = (Map)query.uniqueResult();
                        Integer cashMasterId = (Integer)cashDetailMap.get("cash_master_id");

                        CashDetailEntity cashDetail_status = new CashDetailEntity();
                        cashDetail_status.setStatus(2); //1.生效 2.作廢
                        saveOrUpdateEntity(cashDetail_status, cashOutMonthId);

                        //重算cashMaster
                        CashMasterEntity searchCashMasterEntity = new CashMasterEntity();
                        searchCashMasterEntity.setCashMasterId(cashMasterId);
                        searchCashMasterEntity.setCompanyId(companyId);
                        List cashMasterList =  getSearchEntity(CashMasterEntity.class, searchCashMasterEntity);
                        for(int k=0; k<cashMasterList.size(); k++){
                            CashMasterEntity cashMasterEntity = (CashMasterEntity)cashMasterList.get(0);
                            Integer cashMasterId_m = cashMasterEntity.getCashMasterId();
                            if(cashMasterId_m.equals(cashMasterId)){
                                cashMasterEntity = cashDAO.sumCashMaster(cashMasterEntity);
                                saveOrUpdateEntity(cashMasterEntity, cashMasterEntity.getCashMasterId());
                            }
                        }
                    }
                }
            }
        }


        Map resultMap = new HashMap();
        return resultMap;
    }

    /**
     * 檢視合約明細(月租型)
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getCyclePackageInfoByPackageId(Integer packageId) throws Exception {
        String sql = "select cp.name company_name,cp.business_no,cmc.package_name,cmc.charge_cycle," +
                " cmc.base_quantity,cmc.single_price,cmc.max_price,cmc.free_quantity,cmc.free_month free_month_base, " +
                " cmc.pre_payment,cmc.sales_price,cmc.contract_limit,cmca.* " +
                " ,  pm.dealer_company_id, dcp.dealer_company_name, der.dealer_name, pm.dealer_id, pm.broker_cp2, pm.broker2, pm.broker_cp3, pm.broker3  " +
                " from package_mode pm left join company cp on (pm.company_id = cp.company_id) " +
                " left join charge_mode_cycle cmc on (pm.charge_id = cmc.charge_id) " +
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " left join dealer_company dcp on pm.dealer_company_id = dcp.dealer_company_id  " +
                " left join dealer der on pm.dealer_id = der.dealer_id  " +
                " where pm.package_id=? "; //pm.package_type=1 and
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map) query.uniqueResult();
    }

    /**
     * 檢視合約明細(級距型)
     * @param packageId
     * @return
     * @throws Exception
     */
    public Map getGradePackageInfoByPackageId(Integer packageId) throws Exception {
        String sql = "select cp.name company_name,cp.business_no, cmg.package_name," +
                " cmg.charge_cycle,cmg.base_quantity,cmg.free_month free_month_base, " +
                " cmg.pre_payment,cmg.sales_price,cmg.contract_limit," +
                " cmg.has_grade, cmg.grade_price, cmg.grade_cnt,  " +
                " cmca.* ,  pm.dealer_company_id, dcp.dealer_company_name, der.dealer_name, pm.dealer_id, pm.broker_cp2, pm.broker2, pm.broker_cp3, pm.broker3  " +
                " from package_mode pm left join company cp on (pm.company_id = cp.company_id) " +
                " left join charge_mode_grade cmg on (pm.charge_id = cmg.charge_id) " +
                " left join charge_mode_cycle_add cmca on (pm.addition_id = cmca.addition_id) " +
                " left join dealer_company dcp on pm.dealer_company_id = dcp.dealer_company_id  " +
                " left join dealer der on pm.dealer_id = der.dealer_id  " +
                " where pm.package_type= '2' and pm.package_id=? ";
        List parameterList = new ArrayList();
        parameterList.add(packageId);
        Query query = createQuery(sql, parameterList, null);
        return (Map) query.uniqueResult();
    }

}
