package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MessageBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"result":[{"id":7,"textNotify":"","pushNotify":"","createTime":"","type":0,"contents":"","status":1}]}
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
             * id : 7
             * textNotify :
             * pushNotify :
             * createTime :
             * type : 0
             * contents :
             * status : 1
             */

            private int id;
            private String textNotify;
            private String pushNotify;
            private String createTime;
            private int type;
            private String contents;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTextNotify() {
                return textNotify;
            }

            public void setTextNotify(String textNotify) {
                this.textNotify = textNotify;
            }

            public String getPushNotify() {
                return pushNotify;
            }

            public void setPushNotify(String pushNotify) {
                this.pushNotify = pushNotify;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
