package dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Good688 on 2014/8/26.
 */
@Repository("logParameterDAO")
public class LogParameterDAO extends BaseDAO {
    public List getLogParameterList() throws Exception {
        String sql = "select * from log_parameter  ";
        Query query = createQuery(sql, null);
        List dataList = query.list();
        return dataList;
    }
}
