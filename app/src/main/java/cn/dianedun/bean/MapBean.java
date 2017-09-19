package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class MapBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"departList":[{"id":"402823815d549509015d5497206e0001","departname":"炫彩SOHO","description":"","picture":"","parentdepartid":"","orgCode":"","orgType":"","lng":"115.575949","lat":"38.878506","mobile":"","dbName":"","ip":"","fax":"","address":"","departOrder":"","alarmLevel":"2","alertdetail":"0"},{"id":"8a8ab0b246dc81120146dc8180ba0017","departname":"尚达豪庭","description":"","picture":"","parentdepartid":"","orgCode":"","orgType":"","lng":"115.515579","lat":"38.879798","mobile":"","dbName":"","ip":"","fax":"","address":"","departOrder":"","alarmLevel":"2","alertdetail":"0"}]}
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
        private List<DepartListBean> departList;

        public List<DepartListBean> getDepartList() {
            return departList;
        }

        public void setDepartList(List<DepartListBean> departList) {
            this.departList = departList;
        }

        public static class DepartListBean {
            /**
             * id : 402823815d549509015d5497206e0001
             * departname : 炫彩SOHO
             * description :
             * picture :
             * parentdepartid :
             * orgCode :
             * orgType :
             * lng : 115.575949
             * lat : 38.878506
             * mobile :
             * dbName :
             * ip :
             * fax :
             * address :
             * departOrder :
             * alarmLevel : 2
             * alertdetail : 0
             */

            private String id;
            private String departname;
            private String description;
            private String picture;
            private String parentdepartid;
            private String orgCode;
            private String orgType;
            private String lng;
            private String lat;
            private String mobile;
            private String dbName;
            private String ip;
            private String fax;
            private String address;
            private String departOrder;
            private String alarmLevel;
            private String alertdetail;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDepartname() {
                return departname;
            }

            public void setDepartname(String departname) {
                this.departname = departname;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getParentdepartid() {
                return parentdepartid;
            }

            public void setParentdepartid(String parentdepartid) {
                this.parentdepartid = parentdepartid;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public String getOrgType() {
                return orgType;
            }

            public void setOrgType(String orgType) {
                this.orgType = orgType;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getDbName() {
                return dbName;
            }

            public void setDbName(String dbName) {
                this.dbName = dbName;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public String getFax() {
                return fax;
            }

            public void setFax(String fax) {
                this.fax = fax;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDepartOrder() {
                return departOrder;
            }

            public void setDepartOrder(String departOrder) {
                this.departOrder = departOrder;
            }

            public String getAlarmLevel() {
                return alarmLevel;
            }

            public void setAlarmLevel(String alarmLevel) {
                this.alarmLevel = alarmLevel;
            }

            public String getAlertdetail() {
                return alertdetail;
            }

            public void setAlertdetail(String alertdetail) {
                this.alertdetail = alertdetail;
            }
        }
    }
}
