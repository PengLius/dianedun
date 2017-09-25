package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class DepartPlacesListBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"result":[{"id":"8a8ab0b246dc81120146dc8180a20016","departname":"新大长远","address":"gfg"},{"id":"402823815d68419b015d684a10160002","departname":"炫彩SOHO2号配电室","address":"保定市炫彩SOHO B座704"},{"id":"402823815d68419b015d684a10160001","departname":"炫彩SOHO1号配电室","address":"保定市炫彩SOHO B座704"},{"id":"8a8ab0b246dc81120146dc8180bd0018","departname":"尚达豪庭1号配电室","address":"保定市莲池区A座204"}]}
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
             * id : 8a8ab0b246dc81120146dc8180a20016
             * departname : 新大长远
             * address : gfg
             */

            private String id;
            private String departname;
            private String address;

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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
