package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class PeiDSBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"switchRoomList":[{"id":"402823815d68419b015d684a10160001","departname":"炫彩SOHO1号配电室"},{"id":"402823815d68419b015d684a10160002","departname":"炫彩SOHO2号配电室"}]}
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
        private List<SwitchRoomListBean> switchRoomList;

        public List<SwitchRoomListBean> getSwitchRoomList() {
            return switchRoomList;
        }

        public void setSwitchRoomList(List<SwitchRoomListBean> switchRoomList) {
            this.switchRoomList = switchRoomList;
        }

        public static class SwitchRoomListBean {
            /**
             * id : 402823815d68419b015d684a10160001
             * departname : 炫彩SOHO1号配电室
             */

            private String id;
            private String departname;

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
        }
    }
}
