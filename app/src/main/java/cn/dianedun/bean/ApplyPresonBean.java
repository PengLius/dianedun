package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ApplyPresonBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"result":[{"id":"8a8ab0b246dc81120146dc8181950052","username":"admin","realname":"管理员"},{"id":"402823815df2ea32015df2ede38b0003","username":"zlyou","realname":"张亮有权限"},{"id":"402823815df2ea32015df2f4c8b80011","username":"liupengyou","realname":"刘朋有权限"}]}
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
             * id : 8a8ab0b246dc81120146dc8181950052
             * username : admin
             * realname : 管理员
             */

            private String id;
            private String username;
            private String realname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }
        }
    }
}
