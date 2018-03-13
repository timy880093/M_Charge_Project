package dao;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gate.utils.TimeUtils;
import com.gate.web.beans.FirstCompanyPackageBean;
import com.gate.web.formbeans.CompanyChargeCycleBean;
import com.gate.web.formbeans.WarrantyBean;
import com.gateweb.charge.model.ChargeModeCycleEntity;
import com.gateweb.charge.model.CompanyEntity;
import com.gateweb.charge.model.DealerCompanyEntity;
import com.gateweb.charge.model.DealerEntity;
import com.gateweb.charge.model.PackageModeEntity;
import com.gateweb.charge.model.WarrantyEntity;

/**
 * Created by emily on 2016/4/7.
 */
@Repository("firstCompanyPackageDAO")
public class FirstCompanyPackageDAO extends BaseDAO {
    String result = "";
    String successResult = "";
    String failResult = "";
    
    @Autowired
    CompanyChargeDAO companyChargeDAO;

    //執行批次建立第一次的用戶綁合約的資料
    public String executionFirstCmpPkg( List<FirstCompanyPackageBean> excelDataList) throws Exception{

        for(FirstCompanyPackageBean firstBean: excelDataList){
            String businesscode = null ; //統編
            String companyName = null; //公司名稱
            String dealerCmpName = null; //經銷商
            String dealerName = null; //經銷商業務
            String realStartDate = null; //起帳日
            String chargeName = null; //計費型態
            Integer freeMonth = 0; //客製化:加贈免費月份
            Integer giftPrice = 0; //客製化:贈送金額
            String broker2CpName = null; //介紹公司
            String broker2Name = null; //介紹公司的介紹人
            String broker3CpName = null; //裝機公司
            String broker3Name = null; //裝機公司的裝機人

            businesscode = firstBean.getBusinesscode(); //統編
            dealerCmpName = firstBean.getDealerCmpName(); //經銷商
            dealerName = firstBean.getDealerName(); //經銷商業務
            realStartDate = firstBean.getRealStartDate(); //起帳日
            chargeName = firstBean.getChargeName(); //計費型態
            freeMonth = (null == firstBean.getFreeMonth())?0:(firstBean.getFreeMonth()); //客製化:加贈免費月份
            giftPrice = (null == firstBean.getGiftPrice())?0:(firstBean.getGiftPrice().intValue()); //客製化:贈送金額
            broker2CpName = firstBean.getBroker2CpName(); //介紹公司
            broker2Name = firstBean.getBroker2Name(); //介紹公司的介紹人
            broker3CpName = firstBean.getBroker3CpName(); //裝機公司
            broker3Name = firstBean.getBroker3Name(); //裝機公司的裝機人

            //原本用戶綁合約UI設定的資料
            CompanyChargeCycleBean bean = new CompanyChargeCycleBean();

            //1.找出companyId
            Integer companyId = null;
            CompanyEntity searchCompany = new CompanyEntity();
            searchCompany.setBusinessNo(businesscode);
            List<CompanyEntity> companyList =  getSearchEntity(CompanyEntity.class, searchCompany);
            for(CompanyEntity entity : companyList){
                companyId = entity.getCompanyId();
                companyName = entity.getName();
                break;
            }

            if(null == companyId){
                failResult += "<br>" + companyName + " " + businesscode + " companyId == null!!! ";
                continue;
            }

            //如果該用戶己經綁合約了，就不再建立了.....
            PackageModeEntity searchPackageMode = new PackageModeEntity();
            searchPackageMode.setCompanyId(companyId);
            List<PackageModeEntity> packageModeEntityList = getSearchEntity(PackageModeEntity.class, searchPackageMode);
            if(null != packageModeEntityList && packageModeEntityList.size() > 0){
                failResult += "<br>" + companyName + " " + businesscode + " 該用戶己經綁合約了，就不再建立了........ ";
                continue;
            }

            //如果realStartDate長度不為8，不處理
            if(null == realStartDate || realStartDate.length() != 8){
                failResult += "<br>" + companyName + " " + businesscode + " 起帳日長度不為8，不處理........ ";
                continue;
            }
            //轉換格式內容 原:20170104 新:2017-01-04
            String tmp_realStartDate = "";
            tmp_realStartDate += realStartDate.substring(0, 4);
            tmp_realStartDate += "-";
            tmp_realStartDate += realStartDate.substring(4, 6);
            tmp_realStartDate += "-";
            tmp_realStartDate += realStartDate.substring(6, 8);
            realStartDate = tmp_realStartDate;

            //2.找出方案合約id
            Integer chargeId = null;
            ChargeModeCycleEntity searchCharge = new ChargeModeCycleEntity();
            searchCharge.setPackageName(chargeName);
            List<ChargeModeCycleEntity> chargeList =  getSearchEntity(ChargeModeCycleEntity.class, searchCharge);
            for(ChargeModeCycleEntity entity : chargeList){
                chargeId = entity.getChargeId();
                break;
            }

            if(null == chargeId){
                failResult += "<br>" + companyName + " " + businesscode + "," + chargeName + " chargeId == null!!! ";
                continue;
            }

            //3.找出經銷商公司
            Integer dealerCompanyId = null;
            Integer dealerId = null;

            DealerCompanyEntity searchDealerCompany = new DealerCompanyEntity();
            searchDealerCompany.setDealerCompanyName(dealerCmpName);
            List<DealerCompanyEntity> dealerCompanyList =  getSearchEntity(DealerCompanyEntity.class, searchDealerCompany);
            for(DealerCompanyEntity entity : dealerCompanyList){
                dealerCompanyId = entity.getDealerCompanyId();

                //4.找出經銷商公司的業務員
                DealerEntity searchDealer = new DealerEntity();
                searchDealer.setDealerCompanyId(dealerCompanyId);
                searchDealer.setDealerName(dealerName);
                List<DealerEntity> dealerList =  getSearchEntity(DealerEntity.class, searchDealer);
                for(DealerEntity entitychild : dealerList){
                    dealerId = entitychild.getDealerId();
                    break;
                }

                break;
            }


            //5.算出實際迄日
            ChargeModeCycleEntity chargeModeCycleEntity = (ChargeModeCycleEntity)getEntity(ChargeModeCycleEntity.class, chargeId);
            Integer contractLimit = chargeModeCycleEntity.getContractLimit(); //綁約幾個月
            Integer chargeFreeMonth = chargeModeCycleEntity.getFreeMonth(); //合約的免費月份  註:freeMonth;<=客製化的免費用份
            //註:realStartDate 起帳日
            Calendar cal_realStartDate = timeUtils.string2Calendar("yyyy-MM-dd", realStartDate);
            Integer allAddMonth = contractLimit + chargeFreeMonth + freeMonth; //從起日到迄日一共幾個月
            cal_realStartDate.add(Calendar.MONTH, allAddMonth);
            cal_realStartDate.add(Calendar.DATE, -1);

//            String realEndDate = TimeUtils.date2String("YYYY-MM-DD", cal_realStartDate,"");
            String realEndDate = timeUtils.calendar2String("yyyy-MM-dd", cal_realStartDate);

            bean.setCompanyId(companyId); //公司id
            bean.setChargeId(chargeId); //方案合約id
            bean.setAdditionQuantity(0); //客製化:額外贈送張數(每月) 目前這個功能沒有
            bean.setFreeMonth(freeMonth); //客製化:免費月份
            bean.setGiftPrice(giftPrice); //客製化:加贈金額
            bean.setRealStartDate(realStartDate); //實際起日
            bean.setRealEndDate(realEndDate); //實際迄日
            bean.setDealerCompanyId(dealerCompanyId); //經銷商公司
            bean.setDealerId(dealerId); //經銷商公司業務員
            bean.setBrokerCp2(broker2CpName); //介紹公司
            bean.setBroker2(broker2Name); //介紹公司的介紹人
            bean.setBrokerCp3(broker3CpName); //裝機公司
            bean.setBroker3(broker3Name); //裝機公司的裝機人

            //6.原本用戶綁合約UI按下確定新增後，作的事情
            try{
            		companyChargeDAO.transactionInsertCompanyChargeCycle(bean);
                successResult += "<br>" + companyName + " " + businesscode + ",companyId=" + companyId + ",chargeId=" + chargeId +",freeMonth=" + freeMonth +
                        ",giftPrice=" + giftPrice +",realStartDate=" + realStartDate +",realEndDate=" + realEndDate +
                        ",dealerCompanyId=" + dealerCompanyId +",dealerId=" + dealerId + ",broker2CpName=" +
                        broker2CpName +",broker2Name=" + broker2Name +",broker3CpName=" + broker3CpName +",broker3Name=" + broker3Name +"  ok!";
            }catch(Exception e){
                failResult += "<br>" + companyName + ", " + businesscode + "  " + e.toString();
            }

            //機器保固
            try{
                WarrantyBean warrantyBean = new WarrantyBean();
                BeanUtils.copyProperties(warrantyBean, firstBean);
                warrantyBean.setCompanyId(String.valueOf(companyId));
                warrantyBean.setStatus("1");

                //有填保固起日，才需要建立機器保固(有些公司沒有買機器)
                //算出保固迄日
                String warratyStartDate = warrantyBean.getStartDate();
                if(!StringUtils.isEmpty(warratyStartDate)){

                    //轉換格式內容 原:20170104 新:2017-01-04
                    String tmp_warratyStartDate = "";
                    tmp_warratyStartDate += warratyStartDate.substring(0, 4);
                    tmp_warratyStartDate += "-";
                    tmp_warratyStartDate += warratyStartDate.substring(4, 6);
                    tmp_warratyStartDate += "-";
                    tmp_warratyStartDate += warratyStartDate.substring(6, 8);
                    warratyStartDate = tmp_warratyStartDate;
                    Calendar cal_WarrantyStartDate = timeUtils.string2Calendar("yyyy-MM-dd", warratyStartDate);
                    Integer warrantyMonth = 12;
                    if("true".equals(warrantyBean.getExtend())){
                        warrantyMonth = 36;
                    }
                    cal_WarrantyStartDate.add(Calendar.MONTH, warrantyMonth);
                    cal_WarrantyStartDate.add(Calendar.DATE, -1);
                    String warratyEndDate = timeUtils.calendar2String("yyyy-MM-dd", cal_WarrantyStartDate);
                    warrantyBean.setEndDate(warratyEndDate);
                    warrantyBean.setStartDate(tmp_warratyStartDate); //資料格式yyyy-mm-dd 才可存到db

                    if(StringUtils.isEmpty(warrantyBean.getExtend())){
                        warrantyBean.setExtend("false");
                    }

                    WarrantyEntity  warrantyEntity = new WarrantyEntity();
                    BeanUtils.copyProperties(warrantyEntity, warrantyBean);
                    saveEntity(warrantyEntity);
                }

            }catch(Exception e){
                failResult += "<br>Insert Warranty Error " + companyName + ", " + businesscode + "  " + e.toString();
            }
        }

        result = "建立成功的用戶清單<br>" +successResult + "<br><hr>建立失敗的用戶清單<br>" + failResult;
        return result;
    }
}
