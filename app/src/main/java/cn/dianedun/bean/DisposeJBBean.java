package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DisposeJBBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"402823815dc47a36015dc47b4ed90071","departId":"8a8ab0b246dc81120146dc8180bd0018","pointId":16,"departName":"尚达豪庭1号配电室","alertDetails":"温度6号异常","type":2,"address":"保定市炫彩SOHO B座704","result":"柯南加的","status":0,"createTime":"2017-08-09 08:52:50","confirmTime":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","options":[]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 402823815dc47a36015dc47b4ed90071
         * departId : 8a8ab0b246dc81120146dc8180bd0018
         * pointId : 16
         * departName : 尚达豪庭1号配电室
         * alertDetails : 温度6号异常
         * type : 2
         * address : 保定市炫彩SOHO B座704
         * result : 柯南加的
         * status : 0
         * createTime : 2017-08-09 08:52:50
         * confirmTime :
         * createName :
         * createBy :
         * createDate :
         * updateName :
         * updateBy :
         * updateDate :
         * sysOrgCode :
         * sysCompanyCode :
         * bpmStatus :
         * options : []
         */

        private String id;
        private String departId;
        private int pointId;
        private String departName;
        private String alertDetails;
        private int type;
        private String address;
        private String result;
        private int status;
        private String createTime;
        private String confirmTime;
        private String createName;
        private String createBy;
        private String createDate;
        private String updateName;
        private String updateBy;
        private String updateDate;
        private String sysOrgCode;
        private String sysCompanyCode;
        private String bpmStatus;
        private List<?> options;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDepartId() {
            return departId;
        }

        public void setDepartId(String departId) {
            this.departId = departId;
        }

        public int getPointId() {
            return pointId;
        }

        public void setPointId(int pointId) {
            this.pointId = pointId;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public String getAlertDetails() {
            return alertDetails;
        }

        public void setAlertDetails(String alertDetails) {
            this.alertDetails = alertDetails;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getConfirmTime() {
            return confirmTime;
        }

        public void setConfirmTime(String confirmTime) {
            this.confirmTime = confirmTime;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateName() {
            return updateName;
        }

        public void setUpdateName(String updateName) {
            this.updateName = updateName;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getSysOrgCode() {
            return sysOrgCode;
        }

        public void setSysOrgCode(String sysOrgCode) {
            this.sysOrgCode = sysOrgCode;
        }

        public String getSysCompanyCode() {
            return sysCompanyCode;
        }

        public void setSysCompanyCode(String sysCompanyCode) {
            this.sysCompanyCode = sysCompanyCode;
        }

        public String getBpmStatus() {
            return bpmStatus;
        }

        public void setBpmStatus(String bpmStatus) {
            this.bpmStatus = bpmStatus;
        }

        public List<?> getOptions() {
            return options;
        }

        public void setOptions(List<?> options) {
            this.options = options;
        }
    }
}
