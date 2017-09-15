package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DetailsBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"43c99b18ecbf4351916578c35eea193c","departId":"8a8ab0b246dc81120146dc8180a20016","orderNum":"00906023026760","urgency":2,"orderOwner":"liupengyou","createTime":"","applyTime":1504679400000,"feedTime":"","handlePersion":"管理员,刘朋有权限,张亮有权限","address":"看看","beginTime":1504679340000,"endTime":1504679340000,"applyStatus":"","status":4,"rejectCause":"","feedCause":"","feedOption":"","cause":"看见了","remark":"","createName":"刘朋有权限","createBy":"402823815df2ea32015df2f4c8b80011","createDate":"","updateName":"","updateBy":"","updateDate":"","sysOrgCode":"","sysCompanyCode":"","bpmStatus":"","alertOptionsArray":[{"id":3,"optionId":"402823815dcc085b015dcc0a7b750003","type":5,"optionType":1,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"}],"persionArray":[],"departName":"新大长远","delayTime":0}
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
         * id : 43c99b18ecbf4351916578c35eea193c
         * departId : 8a8ab0b246dc81120146dc8180a20016
         * orderNum : 00906023026760
         * urgency : 2
         * orderOwner : liupengyou
         * createTime :
         * applyTime : 1504679400000
         * feedTime :
         * handlePersion : 管理员,刘朋有权限,张亮有权限
         * address : 看看
         * beginTime : 1504679340000
         * endTime : 1504679340000
         * applyStatus :
         * status : 4
         * rejectCause :
         * feedCause :
         * feedOption :
         * cause : 看见了
         * remark :
         * createName : 刘朋有权限
         * createBy : 402823815df2ea32015df2f4c8b80011
         * createDate :
         * updateName :
         * updateBy :
         * updateDate :
         * sysOrgCode :
         * sysCompanyCode :
         * bpmStatus :
         * alertOptionsArray : [{"id":3,"optionId":"402823815dcc085b015dcc0a7b750003","type":5,"optionType":1,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"}]
         * persionArray : []
         * departName : 新大长远
         * delayTime : 0
         */

        private String id;
        private String departId;
        private String orderNum;
        private int urgency;
        private String orderOwner;
        private String createTime;
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
        private String departName;
        private int delayTime;
        private List<AlertOptionsArrayBean> alertOptionsArray;
        private List<?> persionArray;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
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

        public List<AlertOptionsArrayBean> getAlertOptionsArray() {
            return alertOptionsArray;
        }

        public void setAlertOptionsArray(List<AlertOptionsArrayBean> alertOptionsArray) {
            this.alertOptionsArray = alertOptionsArray;
        }

        public List<?> getPersionArray() {
            return persionArray;
        }

        public void setPersionArray(List<?> persionArray) {
            this.persionArray = persionArray;
        }

        public static class AlertOptionsArrayBean {
            /**
             * id : 3
             * optionId : 402823815dcc085b015dcc0a7b750003
             * type : 5
             * optionType : 1
             * contents : http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest
             */

            private int id;
            private String optionId;
            private int type;
            private int optionType;
            private String contents;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOptionId() {
                return optionId;
            }

            public void setOptionId(String optionId) {
                this.optionId = optionId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getOptionType() {
                return optionType;
            }

            public void setOptionType(int optionType) {
                this.optionType = optionType;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }
        }
    }
}
