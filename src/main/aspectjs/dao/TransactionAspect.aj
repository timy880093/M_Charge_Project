package dao;

import com.gate.core.db.HibernateCoreUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by simon on 2014/6/25.
 */


public aspect TransactionAspect {
    protected static ThreadLocal transactionLocal = new ThreadLocal();
    protected static ThreadLocal transactionSaveOrUpdate = new ThreadLocal();
    //一般交易使用
    pointcut saveOrUpdateMethod(): execution(* dao.*DAO.save*(..))||execution(* dao.*DAO.update*(..))
    ||execution(* dao.*DAO.delete*(..))||execution(* dao.*DAO.execute*(..));
    //跨DAO的save，update，delete，get
    pointcut transactionMethod(): execution(* dao.*DAO.transaction*(..));
    //一般查詢方式
    pointcut getMethod(): execution(* dao.*DAO.get*(..));
    //單存自訂SQL，此種方式不會Close Connection.
    pointcut createQueryMethod(): execution(* dao.*DAO.createQuery(..));
    private Logger logger = Logger.getLogger(TransactionAspect.class);

    before(): createQueryMethod(){
        try {
            HibernateCoreUtils.getSession(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    before(): getMethod(){
        try {
            HibernateCoreUtils.getSession(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    after() returning: getMethod(){
        if (transactionLocal.get() != null) { //表示外面已經開啟Transaction
            //外面已經有開啟TX不需要自動關閉交易
        } else {
            HibernateCoreUtils.closeSession();
        }
    }

    after() throwing (Exception e):getMethod(){
        if (transactionLocal.get() != null) { //表示外面已經開啟Transaction
            //外面已經有開啟TX不需要自動關閉交易
        } else {
            HibernateCoreUtils.closeSession();
        }
    }

    before(): transactionMethod(){
        try {
            Session session = HibernateCoreUtils.getSession(null);
            Transaction transaction = session.beginTransaction(); //在4.x中這邊已表示開啟TX了
//            transaction.begin();
            if (transactionLocal.get() != null) {
                throw new Exception("can't start two transaction !!!");
            } else {
                transactionLocal.set(transaction);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    after() returning: transactionMethod() {
        try {
            Session session = HibernateCoreUtils.getSession(null);
            Transaction transaction =(Transaction)transactionLocal.get();
            if( transaction != null){
                session.flush();
                session.clear();
                transaction.commit();
                HibernateCoreUtils.closeSession();
            }
            transactionLocal.set(null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    after() throwing (Exception e):transactionMethod(){
        try {
            e.printStackTrace();
            Session session = HibernateCoreUtils.getSession(null);

            Transaction transaction =(Transaction)transactionLocal.get();
            if( transaction != null){
//                session.flush();
                session.clear();
                transaction.rollback();
                HibernateCoreUtils.closeSession();
            }
            transactionLocal.set(null);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    before(): saveOrUpdateMethod(){
        try {
            if (transactionLocal.get() != null) { //表示外面已經開啟Transaction
                //外面已經有開啟TX不需要再開啟TX
            } else {
                Session session = HibernateCoreUtils.getSession(null);
                Transaction transaction = session.getTransaction(); //在4.x中這邊已表示開啟TX了
                transaction.begin();
                transactionSaveOrUpdate.set(transaction);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    after() returning: saveOrUpdateMethod(){
        try {
            if (transactionLocal.get() != null) { //表示外面已經開啟Transaction
                //外面已經有開啟TX，裡面不需要自動關閉交易
            } else {
                Session session = HibernateCoreUtils.getSession(null);
                Transaction transaction = (Transaction)transactionSaveOrUpdate.get();
                if(transaction!=null){
                    session.flush();
                    session.clear();
                    transaction.commit();
                }
                transactionSaveOrUpdate.set(null);
                HibernateCoreUtils.closeSession();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    after() throwing (Exception e):saveOrUpdateMethod(){
        try {
            if (transactionLocal.get() != null) { //表示外面已經開啟Transaction
                //外面已經有開啟TX，裡面不需要自動復原交易
            } else {
                Session session = HibernateCoreUtils.getSession(null);
                Transaction transaction = (Transaction)transactionSaveOrUpdate.get();
                if(transaction!=null){
//                    session.flush();
                    session.clear();
                    transaction.rollback();
                }
                transactionSaveOrUpdate.set(null);
                HibernateCoreUtils.closeSession();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

}