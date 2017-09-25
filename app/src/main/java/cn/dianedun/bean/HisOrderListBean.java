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
     * data : {"result":[{"id":"078bf059079144edbcfeeeb926c25bde","departId":"","orderNum":"00909025141643","urgency":2,"orderOwner":"liupengyou","createTime":1504939901523,"applyTime":1504939860000,"feedTime":"","handlePersion":"","address":"看见了","beginTime":1504939680000,"endTime":1504939680000,"applyStatus":"","status":1,"rejectCause":"","feedCause":"","feedOption":"","cause":"看见了","remark":"","createName":"","createBy":"","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":"","persionArray":""}]}
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

    public static class DataBean implements Serializable {
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean implements Serializable {
            /**
             * id : 078bf059079144edbcfeeeb926c25bde
             * departId :
             * orderNum : 00909025141643
             * urgency : 2
             * orderOwner : liupengyou
             * createTime : 1504939901523
             * applyTime : 1504939860000
             * feedTime :
             * handlePersion :
             * address : 看见了
             * beginTime : 1504939680000
             * endTime : 1504939680000
             * applyStatus :
             * status : 1
             * rejectCause :
             * feedCause :
             * feedOption :
             * cause : 看见了
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
            private long beginTime;
            private long endTime;
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

            public long getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(long beginTime) {
                this.beginTime = beginTime;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
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
        }
    }
}
