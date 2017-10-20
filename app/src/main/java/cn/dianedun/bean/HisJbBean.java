package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class HisJbBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"list":[{"id":"09848f14fbdb493e8031a1bdc86270d6","createTime":"2017-10-12 09:23:00","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"高压侧A2电流A120A","type":1},{"id":"c795637330af442aa6508c31fdc74f44","createTime":"2017-10-10 16:39:00","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"湿度侧室内湿度异常","type":2},{"id":"c795637330af442aa6508c31fdb74e4b","createTime":"2017-09-27 15:54:46","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"低压侧A1设备电流a异常","type":1},{"id":"a9c45925abae46cca37b9977d7893ef5","createTime":"2017-09-29 12:40:49","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"温度侧30异常","type":1},{"id":"a381917ae7c241b5b06977e7f43e4c32","createTime":"2017-09-29 12:41:45","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"温度侧29异常","type":2},{"id":"c795637330af442aa6508c31fdc74f43","createTime":"2017-10-10 10:24:00","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"温度侧29异常","type":2},{"id":"632334ad5148469588c67ee3c642b79d","createTime":"2017-09-29 12:21:12","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"温度侧33异常","type":2},{"id":"226de1b9d5064cba91a1a8bc5b3d4c80","createTime":"2017-09-29 12:47:00","departName":"尚达豪庭1号配电室","address":"保定市莲池区A座204","departId":"8a8ab0b246dc81120146dc8180bd0018","alertDetails":"温度侧36异常","type":2}]}
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
             * id : 09848f14fbdb493e8031a1bdc86270d6
             * createTime : 2017-10-12 09:23:00
             * departName : 尚达豪庭1号配电室
             * address : 保定市莲池区A座204
             * departId : 8a8ab0b246dc81120146dc8180bd0018
             * alertDetails : 高压侧A2电流A120A
             * type : 1
             */

            private String id;
            private String createTime;
            private String departName;
            private String address;
            private String departId;
            private String alertDetails;
            private int type;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
