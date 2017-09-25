package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DisposeJBBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"402823815dc47a36015dc47b4ed90000","departId":"8a8ab0b246dc81120146dc8180ba0017","departName":"炫彩SOHO2号配电室","alertDetails":"高压侧电流a异常","type":1,"address":"保定市炫彩SOHO B座704","result":"配电室线路故障","status":1,"createTime":"2017-08-09 08:52:52","options":[{"id":5,"optionId":"402823815dc47a36015dc47b4ed90000","optionType":0,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"},{"id":6,"optionId":"402823815dc47a36015dc47b4ed90000","optionType":0,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/20170822143004ArpDFjG7.jpg"}]}
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
         * id : 402823815dc47a36015dc47b4ed90000
         * departId : 8a8ab0b246dc81120146dc8180ba0017
         * departName : 炫彩SOHO2号配电室
         * alertDetails : 高压侧电流a异常
         * type : 1
         * address : 保定市炫彩SOHO B座704
         * result : 配电室线路故障
         * status : 1
         * createTime : 2017-08-09 08:52:52
         * options : [{"id":5,"optionId":"402823815dc47a36015dc47b4ed90000","optionType":0,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"},{"id":6,"optionId":"402823815dc47a36015dc47b4ed90000","optionType":0,"contents":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/20170822143004ArpDFjG7.jpg"}]
         */

        private String id;
        private String departId;
        private String departName;
        private String alertDetails;
        private int type;
        private String address;
        private String result;
        private int status;
        private String createTime;
        private List<OptionsBean> options;

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

        public List<OptionsBean> getOptions() {
            return options;
        }

        public void setOptions(List<OptionsBean> options) {
            this.options = options;
        }

        public static class OptionsBean {
            /**
             * id : 5
             * optionId : 402823815dc47a36015dc47b4ed90000
             * optionType : 0
             * contents : http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest
             */

            private int id;
            private String optionId;
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
