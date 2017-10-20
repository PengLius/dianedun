package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DetailsBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"ff96271344c948eab3f3a7c4739240c3","departId":"8a8ab0b246dc81120146dc8180bd0018","orderNum":"01016085256135","urgency":0,"orderOwner":"liupengwu","createTime":1508115177000,"applyTime":1508115120000,"feedTime":null,"handlePersion":"杨阳,册世,张技术员,杨洋,李技术员","address":"保定市莲池区A座204","beginTime":1508115120000,"endTime":1508115180000,"applyStatus":0,"status":3,"rejectCause":"理由不充分","feedCause":null,"feedOption":null,"cause":"配电室异常","remark":null,"createName":"刘朋无","createBy":"402823815df2ea32015df2f5f0d60015","createDate":null,"updateName":null,"updateBy":null,"updateDate":null,"sysOrgCode":null,"sysCompanyCode":null,"bpmStatus":null,"alertOptionsArray":[{"id":3,"optionId":"402823815dcc085b015dcc0a7b750003","type":5,"optionType":1,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"}],"persionArray":["杨阳","册世","张技术员","杨洋","李技术员"],"departName":"尚达豪庭1号配电室","delayTime":0}
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
         * id : ff96271344c948eab3f3a7c4739240c3
         * departId : 8a8ab0b246dc81120146dc8180bd0018
         * orderNum : 01016085256135
         * urgency : 0
         * orderOwner : liupengwu
         * createTime : 1508115177000
         * applyTime : 1508115120000
         * feedTime : null
         * handlePersion : 杨阳,册世,张技术员,杨洋,李技术员
         * address : 保定市莲池区A座204
         * beginTime : 1508115120000
         * endTime : 1508115180000
         * applyStatus : 0
         * status : 3
         * rejectCause : 理由不充分
         * feedCause : null
         * feedOption : null
         * cause : 配电室异常
         * remark : null
         * createName : 刘朋无
         * createBy : 402823815df2ea32015df2f5f0d60015
         * createDate : null
         * updateName : null
         * updateBy : null
         * updateDate : null
         * sysOrgCode : null
         * sysCompanyCode : null
         * bpmStatus : null
         * alertOptionsArray : [{"id":3,"optionId":"402823815dcc085b015dcc0a7b750003","type":5,"optionType":1,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"}]
         * persionArray : ["杨阳","册世","张技术员","杨洋","李技术员"]
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
        private Object feedTime;
        private String handlePersion;
        private String address;
        private long beginTime;
        private long endTime;
        private int applyStatus;
        private int status;
        private String rejectCause;
        private Object feedCause;
        private Object feedOption;
        private String cause;
        private Object remark;
        private String createName;
        private String createBy;
        private Object createDate;
        private Object updateName;
        private Object updateBy;
        private Object updateDate;
        private Object sysOrgCode;
        private Object sysCompanyCode;
        private Object bpmStatus;
        private String departName;
        private int delayTime;
        private List<AlertOptionsArrayBean> alertOptionsArray;
        private List<String> persionArray;

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

        public Object getFeedTime() {
            return feedTime;
        }

        public void setFeedTime(Object feedTime) {
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

        public int getApplyStatus() {
            return applyStatus;
        }

        public void setApplyStatus(int applyStatus) {
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

        public Object getFeedCause() {
            return feedCause;
        }

        public void setFeedCause(Object feedCause) {
            this.feedCause = feedCause;
        }

        public Object getFeedOption() {
            return feedOption;
        }

        public void setFeedOption(Object feedOption) {
            this.feedOption = feedOption;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
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

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }

        public Object getUpdateName() {
            return updateName;
        }

        public void setUpdateName(Object updateName) {
            this.updateName = updateName;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public Object getSysOrgCode() {
            return sysOrgCode;
        }

        public void setSysOrgCode(Object sysOrgCode) {
            this.sysOrgCode = sysOrgCode;
        }

        public Object getSysCompanyCode() {
            return sysCompanyCode;
        }

        public void setSysCompanyCode(Object sysCompanyCode) {
            this.sysCompanyCode = sysCompanyCode;
        }

        public Object getBpmStatus() {
            return bpmStatus;
        }

        public void setBpmStatus(Object bpmStatus) {
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

        public List<String> getPersionArray() {
            return persionArray;
        }

        public void setPersionArray(List<String> persionArray) {
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
