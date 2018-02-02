package dao;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;

import com.gate.core.db.HibernateCoreUtils;
import com.gate.core.utils.CustomBeanUtilsBean;
import com.gate.utils.TimeUtils;
import com.gate.web.authority.UserInfo;
import com.gate.web.authority.UserInfoContext;

/**
 * Created by simon on 2014/6/26.
 */
public class BaseDAO {

    protected static Logger logger = null;
    //把flush size的大小，改成參數型態。
    protected static int FLUSH_SIZE = 100;
    protected static final String PAGE_QUERY_TOTAL_COUNT = "TOTAL_COUNT";
    protected static final String PAGE_QUERY_DATA_LIST = "DATA_LIST";

    public UserInfo userInfo = UserInfoContext.getUserInfo();

    public BaseDAO() {
        synchronized (this) {
            if (logger == null) {
                logger = Logger.getLogger(getClass().getName());
            }
        }
    }

    @PersistenceContext(unitName = "chargeFacade")
    protected EntityManager entityManager;

    /**
     * 抓取entity根據該物件的PK
     *
     * @param classObj
     * @return
     * @throws Exception
     */
    public Object getEntity(Class classObj, Serializable pk) throws Exception {
    		Session session = (Session) entityManager.getDelegate();
        Object returnObj = null;
        returnObj = session.get(classObj, pk);
        session.evict(returnObj);//切斷關係
        return returnObj;
    }

    /**
     * 取得entity列表
     * @param classObj 要查詢物件藍圖
     * @param entity 查尋條件
     * @return
     * @throws Exception
     */
    public List getSearchEntity(Class classObj,Serializable entity) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(classObj);
        criteria.add(Example.create(entity));
        return criteria.list();
    }

    public List getSearchEntity(Class classObj,Serializable entity, String orderby) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(classObj);
        criteria.add(Example.create(entity));
        criteria.addOrder(Order.asc(orderby));
        return criteria.list();
    }



    /**
     * default insert object
     *
     * @param entity
     * @throws Exception
     */
    public void saveEntity(Object entity) throws Exception {

        Class c = entity.getClass();
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("setCreatorId")) {
                Class[] integer = {Integer.class};
                //判斷是否已經有外部設定值，如果有則不再複寫
                boolean isSet = false;
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals("getCreatorId")) {
                        Object valueObject = methods[i].invoke(entity);
                        if (valueObject != null) {
                            isSet = true;
                        }
                        break;
                    }
                }
                if (isSet == false) {
                    String userId = UserInfoContext.getUserInfo().getUserId();
                    Method creatorId = c.getMethod("setCreatorId", integer);
                    creatorId.invoke(entity, new Object[]{Integer.parseInt(userId)});
                    Method modifierId = c.getMethod("setModifierId", integer);
                    modifierId.invoke(entity, new Object[]{Integer.parseInt(userId)});
                }
            }
            if (method.getName().equals("setCreateDate")) {
                Class[] timestamp = {Timestamp.class};
                boolean isSet = false;
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals("getCreateDate")) {
                        Object valueObject = methods[i].invoke(entity);
                        if (valueObject != null) {
                            isSet = true;
                        }
                        break;
                    }
                }
                if (isSet == false) {
                    Method createDate = c.getMethod("setCreateDate", timestamp);
                    createDate.invoke(entity, new Object[]{TimeUtils.getCurrentTimestamp()});
                    Method modifyDate = c.getMethod("setModifyDate", timestamp);
                    modifyDate.invoke(entity, new Object[]{TimeUtils.getCurrentTimestamp()});
                }
            }
        }

        Session session = (Session) entityManager.getDelegate();
        session.save(entity);
        session.flush();
        session.clear();
    }

    public void deleteEntity(Object entity) throws Exception {
        Class c = entity.getClass();
        Session session = (Session) entityManager.getDelegate();
        session.delete(entity);
    }


    /**
     * 自動判斷資料是否有value如果有value則改用update
     *
     * @param obj
     * @throws Exception
     */
    public void saveOrUpdateEntity(Object obj, Serializable pk) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Object beforeObj = SerializationUtils.clone((Serializable) obj); //beanUtils無法clone Timestamp所以改用SerializationUtils
        Object dbObj = session.get(obj.getClass(), (Serializable) pk);//這邊是從根據object 的pk去load ,object，所以本身是有在控管內的
        if (dbObj == null) {//新增
            session.save(beforeObj);
        } else {
            merge(beforeObj, dbObj);//這邊merge 兩個bean，只有不為NULL的才會寫入原先的obj
            copyProperties(dbObj, beforeObj);
            session.update(dbObj);
        }
    }


    /**
     * 單純更新資料
     *
     * @param obj
     * @param pk
     * @throws Exception
     */
    public void updateEntity(Object obj, Serializable pk) throws Exception {
        Class c = obj.getClass();
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("setCreatorId")) {
                Class[] timestamp = {Timestamp.class};
                Class[] integer = {Integer.class};
                String userId = UserInfoContext.getUserInfo().getUserId();
                Method modifierId = c.getMethod("setModifierId", integer);
                modifierId.invoke(obj, new Object[]{Integer.parseInt(userId)});
                Method modifyDate = c.getMethod("setModifyDate", timestamp);
                modifyDate.invoke(obj, new Object[]{TimeUtils.getCurrentTimestamp()});
            }
        }

        Session session = (Session) entityManager.getDelegate();
        Object beforeObj = SerializationUtils.clone((Serializable) obj);
        Object dbObj = session.get(obj.getClass(), (Serializable) pk);
        if (dbObj == null) {
            throw new Exception("Can't find DB Object~~~ ");
        } else {
            merge(beforeObj, dbObj);//這邊merge 兩個bean，只有不為NULL的才會寫入原先的obj
            copyProperties(dbObj, beforeObj);
            session.update(dbObj);
            session.flush();
            session.clear();
        }
    }

    /**
     * default update object 這個method會自動過濾null的value，如果沒有設定的話，他會以之前db的value寫入。
     *
     * @param obj
     * @throws Exception
     */
    public void updateEntityIncludeNULL(Object obj, Serializable pk) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Object beforeObj = SerializationUtils.clone((Serializable) obj);
        Object dbObj = session.get(obj.getClass(), (Serializable) pk);
        if (dbObj == null) {
            throw new Exception("Can't find DB Object~~~ ");
        } else {
            copyProperties(dbObj, beforeObj);
            session.update(dbObj);
        }
    }

    /**
     * query with range for (paging)
     *
     * @param sql
     * @param firstRowNum
     * @param endRowNum
     * @param fetchSize
     * @return
     */
    public Query createQueryForPage(String sql, int firstRowNum, int endRowNum,
                                    int fetchSize, Object resultObj) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Query query = session.createSQLQuery(sql);
        if (firstRowNum != -1) {
            query.setFirstResult(firstRowNum);
        }
        if (endRowNum != -1) {
            query.setMaxResults(endRowNum);
        }
        if (fetchSize != -1) {
            query.setFetchSize(fetchSize);
        }
        if (resultObj != null) { //這邊順便處理return回來的物件的mapping
            query.setResultTransformer(Transformers.aliasToBean(resultObj.getClass()));
        } else {
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        }
        return query;
    }

    public <T> List<T> createQueryForList(String sql ,List parameterList, Class<T> object) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        SQLQuery q = session.createSQLQuery(sql);

        if (parameterList != null) {
            for (int i = 0; i < parameterList.size(); i++) {
                Object obj = parameterList.get(i);
                q.setParameter(i, obj);
            }
        }
        q.addEntity(object);
        List<T> entities = q.list();
        return entities;
    }


    public Query createQueryForPage(String sql, List parameterList, int firstRowNum, int endRowNum,
                                    int fetchSize, Object resultObj) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Query query = session.createSQLQuery(sql);
        if (parameterList != null) {
            for (int i = 0; i < parameterList.size(); i++) {
                Object obj = parameterList.get(i);
                query.setParameter(i, obj);
            }
        }
        if (firstRowNum != -1) {
            query.setFirstResult(firstRowNum);
        }
        if (endRowNum != -1) {
            query.setMaxResults(endRowNum);
        }
        if (fetchSize != -1) {
            query.setFetchSize(fetchSize);
        }
        if (resultObj != null) { //這邊順便處理return回來的物件的mapping
            query.setResultTransformer(Transformers.aliasToBean(resultObj.getClass()));
        } else {
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        }
        return query;
    }


    public Map queryForPageData(String getTotalCountSQL, String dataListSQL, List parameterList, int firstRowNum,
                                int endRowNum, int fetchSize, Object resultObj) throws Exception {
        Map returnMap = new HashMap();
        Query countQuery = this.createQuery(getTotalCountSQL, parameterList, null);
        if (countQuery.list().size() > 1) {
            throw new Exception("getTotalCountSQL 用法錯誤[回傳只能一筆]");
        }
        List countList = countQuery.list();
        Map<String, String> tempMap = (Map) countList.get(0);
        Set<String> keySet = tempMap.keySet();
        int columnCount = 0;
        String countValue = "";
        for (String key : keySet) {
            countValue = String.valueOf(tempMap.get(key));
            columnCount = columnCount + 1;
        }
        if (columnCount > 1) {
            throw new Exception("getTotalCountSQL 用法錯誤[只能抓取一個欄位]");
        }

        Query dataListQuery = this.createQueryForPage(dataListSQL, parameterList, firstRowNum, endRowNum, fetchSize, resultObj);
        returnMap.put(PAGE_QUERY_TOTAL_COUNT, countValue);
        returnMap.put(PAGE_QUERY_DATA_LIST, dataListQuery.list());
        return returnMap;
    }

    /**
     * 處理map裡面放key,value，value可以單筆值或是陣列
     *
     * @param sql
     * @param parameterMap
     * @param firstRowNum
     * @param endRowNum
     * @param fetchSize
     * @param resultObj
     * @return
     * @throws Exception
     */
    public Query createQueryForPage(String sql, Map parameterMap, int firstRowNum, int endRowNum,
                                    int fetchSize, Object resultObj) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Query query = session.createSQLQuery(sql);
        if (parameterMap != null) {
            Set<String> keySet = parameterMap.keySet();
            for (String key : keySet) {
                Object object = parameterMap.get(key);
                if (object instanceof List) {
                    query.setParameterList(key, ((List) object).toArray());
                } else {
                    query.setParameter(key, object);
                }
            }
        }
        if (firstRowNum != -1) {
            query.setFirstResult(firstRowNum);
        }
        if (endRowNum != -1) {
            query.setMaxResults(endRowNum);
        }
        if (fetchSize != -1) {
            query.setFetchSize(fetchSize);
        }
        if (resultObj != null) { //這邊順便處理return回來的物件的mapping
            query.setResultTransformer(Transformers.aliasToBean(resultObj.getClass()));
        } else {
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        }
        return query;
    }

    /**
     * default query no range(paging)
     *
     * @param sql
     * @return
     */
    public Query createQuery(String sql, Object resultObj) throws Exception {
        return createQueryForPage(sql, -1, -1, -1, resultObj);
    }


    public Query createQuery(String sql, List parameterList, Object resultObj) throws Exception {
        return createQueryForPage(sql, parameterList, -1, -1, -1, resultObj);
    }

    public Query createQuery(String sql, Map parameterMap, Object returnObj) throws Exception {
        return createQueryForPage(sql, parameterMap, -1, -1, -1, returnObj);
    }

    public List getTableList(String tableName) throws Exception {
        String sql = "select * from " + tableName;
        Query query = createQuery(sql, null);
        return query.list();
    }

    //for Examples transaction。
    public void transactionBU() throws Exception {
        List dataList = getTableList("testing_tb");
        System.out.println("in transactionBU");
    }


    private <M> void merge(M target, M destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            // Only copy writable attributes
            if (descriptor.getWriteMethod() != null) {
                Object targetValue = descriptor.getReadMethod()
                        .invoke(target);
                // Only copy values values where the destination values is null
                if (targetValue == null) {
                    Object destinationValue = descriptor.getReadMethod().invoke(
                            destination);
                    descriptor.getWriteMethod().invoke(target, destinationValue);
                }
            }
        }
    }


    /**
     * 使用自訂coverter來處理date跟timestamp的問題。
     *
     * @param destination
     * @param source
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void copyProperties(Object destination, Object source) throws IntrospectionException,
            InvocationTargetException, IllegalAccessException {
        //註冊BeanUtils的Mapping。
        CustomBeanUtilsBean.register();
        BeanInfo beanInfo = Introspector.getBeanInfo(destination.getClass());
        PropertyDescriptor[] propertyDescriptors =
                beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            if (propertyDescriptors[i].getWriteMethod() != null) {
                String propertyName = propertyDescriptors[i].getName();
                Object sourceValue = propertyDescriptors[i].getReadMethod().invoke(source);
                //當sourceValue=null時，BeanUtils會參考CustomBeanUtilsBean所註冊的型別default值，給值
                //若有新增新的型別，且default值與官方不同時，請在CustomBeanUtilsBean自行新增default
                BeanUtils.copyProperty(destination, propertyName, sourceValue);
            }
        }
    }

    //單純執行sql
    public void executeSql(String sql, List parameterList) throws Exception {
        Session session = (Session) entityManager.getDelegate();
        Query query = session.createSQLQuery(sql);
        if (parameterList != null) {
            for (int i = 0; i < parameterList.size(); i++) {
                Object obj = parameterList.get(i);
                query.setParameter(i, obj);
            }
        }
        query.executeUpdate();

    }

    /**
     * 產生有權限查看的公司sql
     * @param sql
     * @param parameterList
     * @param prefix
     */
    public void getRoleCompany(StringBuffer sql, List parameterList, String prefix) {
        UserInfo userInfo = UserInfoContext.getUserInfo();
        if (!userInfo.getRoleId().equals("600")) {
//            String[] companys = userInfo.getReferenceCompanyId().split(",");
//            Integer[] intCompanys = new Integer[companys.length];
//
//            for (int n = 0; n < companys.length; n++) {
//                intCompanys[n] = Integer.parseInt(companys[n]);
//            }
//            sql.append(" and " + prefix + "company_id in (");
//            for (int j = 0; j < intCompanys.length; j++) {
//                if (j == companys.length - 1)
//                    sql.append("?) ");
//                else
//                    sql.append("?,");
//            }
//            for (Integer company : intCompanys) {
//                parameterList.add(company);
//            }
        }

    }

    //取得creator,modifier
    public Map getCreatorAndModifier(Integer creatorId, Integer modifierId) throws Exception {
        List parameterList = new ArrayList();
        String sql = "select getUserName(?) as creator,getUserName(?) as modifier";
        parameterList.add(creatorId);
        parameterList.add(modifierId);
        Query query = createQuery(sql, parameterList, null);
        return (Map) query.uniqueResult();

    }

}
