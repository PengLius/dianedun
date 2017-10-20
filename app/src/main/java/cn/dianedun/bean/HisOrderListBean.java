package cn.dianedun.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/10.
 */


public class HisOrderListBean implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * data : {"result":[{"id":"38cd626202bd4251be37956aa331a97b","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016021116296","urgency":1,"orderOwner":"liupengwu","createTime":1508134276000,"applyTime":1508134260000,"feedTime":"","handlePersion":"","address":"保定市莲池区A座204","beginTime":"","endTime":"","applyStatus":"","status":0,"rejectCause":"","feedCause":"","feedOption":"","cause":"出门维修","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":"","departName":"尚达豪庭1号配电室","delayTime":0},{"id":"1c4c53c086a54a02bf6fba06c7fb7ccb","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016015512670","urgency":1,"orderOwner":"liupengwu","createTime":1508133313000,"applyTime":1508133300000,"feedTime":"","handlePersion":"","address":"保定市莲池区A座204","beginTime":"","endTime":"","applyStatus":"","status":0,"rejectCause":"","feedCause":"","feedOption":"","cause":"出门维修","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":"","departName":"尚达豪庭1号配电室","delayTime":0},{"id":"622bae60572d4851a8027a1fd3099f1d","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016015132478","urgency":1,"orderOwner":"liupengwu","createTime":1508133093000,"applyTime":1508133060000,"feedTime":"","handlePersion":"","address":"保定市莲池区A座204","beginTime":"","endTime":"","applyStatus":"","status":0,"rejectCause":"","feedCause":"","feedOption":"","cause":"出门维修","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":"","departName":"尚达豪庭1号配电室","delayTime":0},{"id":"7d20b44ab9f049ab915be04bff7adff2","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016011221706","urgency":1,"orderOwner":"liupengwu","createTime":1508130742000,"applyTime":1508130720000,"feedTime":"","handlePersion":"","address":"保定市莲池区A座204","beginTime":1508130660000,"endTime":1508303460000,"applyStatus":"","status":4,"rejectCause":"","feedCause":"","feedOption":"","cause":"漏电","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":"","departName":"尚达豪庭1号配电室","delayTime":0},{"id":"ff96271344c948eab3f3a7c4739240c3","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016085256135","urgency":0,"orderOwner":"liupengwu","createTime":1508115177000,"applyTime":1508115120000,"feedTime":"","handlePersion":"","address":"保定市莲池区A座204","beginTime":1508115120000,"endTime":1508115180000,"applyStatus":"","status":0,"rejectCause":"","feedCause":"","feedOption":"","cause":"配电室异常","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":"","departName":"尚达豪庭1号配电室","delayTime":0}]}
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
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * id : 38cd626202bd4251be37956aa331a97b
             * departId : 8a8ab0b246dc81120146dc8180bd0018
             * orderNum : 01016021116296
             * urgency : 1
             * orderOwner : liupengwu
             * createTime : 1508134276000
             * applyTime : 1508134260000
             * feedTime :
             * handlePersion :
             * address : 保定市莲池区A座204
             * beginTime :
             * endTime :
             * applyStatus :
             * status : 0
             * rejectCause :
             * feedCause :
             * feedOption :
             * cause : 出门维修
             * remark :
             * createName :
             * createBy :
             * createDate :
             * updateName :
             * updateBy :
             * updateDate :
             * sysOrgCode :
             * sysCompanyCode :
             * bpmStatus :
             * alertOptionsArray :
             * persionArray :
             * departName : 尚达豪庭1号配电室
             * delayTime : 0
             */

            private String id;
            private String departId;
            private String orderNum;
            private int urgency;
            private String orderOwner;
            private long createTime;
            private long applyTime;
            private String feedTime;
            private String handlePersion;
            private String address;
            private String beginTime;
            private String endTime;
            private String applyStatus;
            private int status;
            private String rejectCause;
            private String feedCause;
            private String feedOption;
            private String cause;
            private String remark;
            private String createName;
            private String createBy;
            private String createDate;
            private String updateName;
            private String updateBy;
            private String updateDate;
            private String sysOrgCode;
            private String sysCompanyCode;
            private String bpmStatus;
            private String alertOptionsArray;
            private String persionArray;
            private String departName;
            private int delayTime;

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

            public String getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(String orderNum) {
                this.orderNum = orderNum;
            }

            public int getUrgency() {
                return urgency;
            }

            public void setUrgency(int urgency) {
                this.urgency = urgency;
            }

            public String getOrderOwner() {
                return orderOwner;
            }

            public void setOrderOwner(String orderOwner) {
                this.orderOwner = orderOwner;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getApplyTime() {
                return applyTime;
            }

            public void setApplyTime(long applyTime) {
                this.applyTime = applyTime;
            }

            public String getFeedTime() {
                return feedTime;
            }

            public void setFeedTime(String feedTime) {
                this.feedTime = feedTime;
            }

            public String getHandlePersion() {
                return handlePersion;
            }

            public void setHandlePersion(String handlePersion) {
                this.handlePersion = handlePersion;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(String beginTime) {
                this.beginTime = beginTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getApplyStatus() {
                return applyStatus;
            }

            public void setApplyStatus(String applyStatus) {
                this.applyStatus = applyStatus;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getRejectCause() {
                return rejectCause;
            }

            public void setRejectCause(String rejectCause) {
                this.rejectCause = rejectCause;
            }

            public String getFeedCause() {
                return feedCause;
            }

            public void setFeedCause(String feedCause) {
                this.feedCause = feedCause;
            }

            public String getFeedOption() {
                return feedOption;
            }

            public void setFeedOption(String feedOption) {
                this.feedOption = feedOption;
            }

            public String getCause() {
                return cause;
            }

            public void setCause(String cause) {
                this.cause = cause;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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

            public String getAlertOptionsArray() {
                return alertOptionsArray;
            }

            public void setAlertOptionsArray(String alertOptionsArray) {
                this.alertOptionsArray = alertOptionsArray;
            }

            public String getPersionArray() {
                return persionArray;
            }

            public void setPersionArray(String persionArray) {
                this.persionArray = persionArray;
            }

            public String getDepartName() {
                return departName;
            }

            public void setDepartName(String departName) {
                this.departName = departName;
            }

            public int getDelayTime() {
                return delayTime;
            }

            public void setDelayTime(int delayTime) {
                this.delayTime = delayTime;
            }
        }
    }
}
