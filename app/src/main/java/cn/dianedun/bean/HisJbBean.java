package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class HisJbBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"list":[{"id":"402823815dab213e015dab22ce7d0001","createTime":"2017-08-08 21:49:48","departName":"炫彩SOHO2号配电室","address":"保定市炫彩SOHO B座704","departId":"402823815d68419b015d684a10160002","alertDetails":"高压侧1号柜电压异常"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 402823815dab213e015dab22ce7d0001
             * createTime : 2017-08-08 21:49:48
             * departName : 炫彩SOHO2号配电室
             * address : 保定市炫彩SOHO B座704
             * departId : 402823815d68419b015d684a10160002
             * alertDetails : 高压侧1号柜电压异常
             */

            private String id;
            private String createTime;
            private String departName;
            private String address;
            private String departId;
            private String alertDetails;
            private int type;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getDepartName() {
                return departName;
            }

            public void setDepartName(String departName) {
                this.departName = departName;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDepartId() {
                return departId;
            }

            public void setDepartId(String departId) {
                this.departId = departId;
            }

            public String getAlertDetails() {
                return alertDetails;
            }

            public void setAlertDetails(String alertDetails) {
                this.alertDetails = alertDetails;
            }
        }
    }
}
