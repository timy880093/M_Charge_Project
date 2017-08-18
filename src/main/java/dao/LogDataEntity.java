package dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Good688 on 2014/8/26.
 */
public class LogDataEntity implements Serializable {
    private Integer logId;
    private String logUrl;
    private Integer parameterId;
    private Timestamp accessTime;
    private String userName;
    private String requestObj;
    private String sessionObj;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Timestamp getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Timestamp accessTime) {
        this.accessTime = accessTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestObj() {
        return requestObj;
    }

    public void setRequestObj(String requestObj) {
        this.requestObj = requestObj;
    }

    public String getSessionObj() {
        return sessionObj;
    }

    public void setSessionObj(String sessionObj) {
        this.sessionObj = sessionObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogDataEntity that = (LogDataEntity) o;

        if (accessTime != null ? !accessTime.equals(that.accessTime) : that.accessTime != null) return false;
        if (logId != null ? !logId.equals(that.logId) : that.logId != null) return false;
        if (logUrl != null ? !logUrl.equals(that.logUrl) : that.logUrl != null) return false;
        if (parameterId != null ? !parameterId.equals(that.parameterId) : that.parameterId != null) return false;
        if (requestObj != null ? !requestObj.equals(that.requestObj) : that.requestObj != null) return false;
        if (sessionObj != null ? !sessionObj.equals(that.sessionObj) : that.sessionObj != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = logId != null ? logId.hashCode() : 0;
        result = 31 * result + (logUrl != null ? logUrl.hashCode() : 0);
        result = 31 * result + (parameterId != null ? parameterId.hashCode() : 0);
        result = 31 * result + (accessTime != null ? accessTime.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (requestObj != null ? requestObj.hashCode() : 0);
        result = 31 * result + (sessionObj != null ? sessionObj.hashCode() : 0);
        return result;
    }
}
