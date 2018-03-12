package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.QuerySettingVO;
import com.gate.web.displaybeans.PrepayDetailVO;
import com.gateweb.charge.model.CashDetailEntity;
import com.gateweb.charge.model.CashMasterEntity;
import com.gateweb.charge.model.PrepayDeductMasterEntity;
import com.gateweb.charge.model.PrepayDetailEntity;

/**
 * Created by emily on 2017/5/19.
 */
@Repository("prepayDeductDAO")
public class PrepayDeductDAO extends BaseDAO{
	
	@Autowired
    CashDAO cashDAO;

    public Map getPrepayDeductCompanyList(QuerySettingVO querySettingVO) throws Exception {
        List<Object> parameters = new ArrayList<Object>();
        StringBuffer dataSb = new StringBuffer();
        StringBuffer countSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();
        countSb.append("select count(1) from prepay_deduct_master dc left join company cp on dc.company_id=cp.company_id where 1=1 ");
        dataSb.append(" select dc.*, cp.name  ");
        dataSb.append(" from prepay_deduct_master dc left join company cp on dc.company_id=cp.company_id ");
        dataSb.append(" where 1=1 ");

        if (querySettingVO.getSearchMap().size()>0) {
            Map searchMap = querySettingVO.getSearchMap();
            if(searchMap.containsKey("companyId")){
                if(StringUtils.isNotEmpty(searchMap.get("companyId").toString())){
                    whereSb.append(" and dc.company_id = ?");
                    parameters.add(Integer.parseInt((String)searchMap.get("companyId")));
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

    //新增使用預用金的用戶
    public Integer transactionCreatePdm(Integer companyId) throws Exception {
        Integer cnt = 0;  //計算了幾筆
       
        PrepayDeductMasterEntity entity = new PrepayDeductMasterEntity();
        entity.setCompanyId(companyId);
        entity.setAmount(0); //總預用金
        entity.setIsEnableOver("N"); //是否啟用超額預繳扣抵
        saveEntity(entity);
        cnt++;

        return cnt;
    }


    //檢視預繳扣抵主表
    public PrepayDeductMasterEntity getPrepayDeductMaster(Integer masterId) throws Exception{
        PrepayDeductMasterEntity masterEntity = (PrepayDeductMasterEntity)getEntity(PrepayDeductMasterEntity.class, masterId);
        return masterEntity;
    }

    //檢視預繳清單
    public List<PrepayDetailVO> getPrepayDetail(Integer companyId) throws Exception{


        return null;
    }

    //變更用戶預用金是否啟用超額狀態(Y/N)
    public void updateMasterStatus(Integer masterId,String status) throws Exception {
        PrepayDeductMasterEntity entity = new PrepayDeductMasterEntity();
        entity.setIsEnableOver(status);
        updateEntity(entity, masterId);
    }

    //新增一筆預繳清單
    public Integer transactionInsertPrepayDetail(PrepayDetailEntity entity) throws Exception {
        int cnt = 0;
        String calYM = entity.getCalYm();
        Integer cpId = entity.getCompanyId();
        BigDecimal money = new BigDecimal(entity.getMoney());

        //新增一筆預繳帳單
        //insert 一筆新的cash_detail'、cash_master
        CashDetailEntity cashDetailEntity = new CashDetailEntity();
        cashDetailEntity.setCompanyId(cpId); //公司名稱
        cashDetailEntity.setCalYm(TimeUtils.getYYYYMM(TimeUtils.parseDate(calYM))); //計算年月
        cashDetailEntity.setOutYm(TimeUtils.getYYYYMM(TimeUtils.addMonth(TimeUtils.parseDate(calYM), 1))); //帳單年月
        CashMasterEntity  cashMasterEntity = cashDAO.isHaveCashMaster(TimeUtils.getYYYYMM(TimeUtils.addMonth(TimeUtils.parseDate(calYM), 1)), cpId);
        cashDetailEntity.setCashMasterId(cashMasterEntity.getCashMasterId()); //cash_master_id
        cashDetailEntity.setCashType(6); //計費類型 1.月租2.月租超額3.代印代計4.加值型服務5.儲值 6.預繳
        cashDetailEntity.setBillType(3); //帳單類型 1.月租 2.級距 3.預繳
        cashDetailEntity.setPackageId(null); //註:預繳類型不紀錄packageId  超額的cashdetail也不紀錄packageId(超額的cashdetail記的packageId只能參考，不是真正值)，因為可能跨兩種不同的package(超額可以跨合約出帳)。
        cashDetailEntity.setStatus(1); //1.生效 2.作廢
        cashDetailEntity = cashDAO.calPriceTax(cashDetailEntity, money, new BigDecimal(0), null);
        saveEntity(cashDetailEntity);
        Integer cashDetailId = cashDetailEntity.getCashDetailId();

        //新增一筆預繳清單
        entity.setCashDetailId(cashDetailId);
        saveEntity(entity);

        return cnt;
    }

    public List<Map> getPrepayDetailHisByCompany(Integer companyId) throws Exception {
        String sql = " select cp.name, cm.status , pd.* " +
                " from prepay_detail pd left join company cp on pd.company_id=cp.company_id " +
                " left join cash_detail cd on pd.cash_detail_id=cd.cash_detail_id " +
                " left join cash_master cm on cd.cash_master_id=cm.cash_master_id " +
                " where pd.company_id=? " +
                " order by pd.cal_ym desc ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);
        return query.list();
    }

    public List<Map> getDeductDetailHisByCompany(Integer companyId) throws Exception {
        String sql = " select cp.name, cm.status , dd.* " +
                " from deduct_detail dd left join company cp on dd.company_id=cp.company_id " +
                " left join cash_detail cd on dd.cash_detail_id=cd.cash_detail_id " +
                " left join cash_master cm on cd.cash_master_id=cm.cash_master_id " +
                " where dd.company_id=? " +
                " order by dd.cal_ym desc ";
        List parameterList = new ArrayList();
        parameterList.add(companyId);
        Query query = createQuery(sql, parameterList, null);
        return query.list();
    }
}
