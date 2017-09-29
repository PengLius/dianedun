package cn.dianedun.bean;

/**
 * Created by Administrator on 2017/9/28.
 */

public class VersionBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"ver_name":"1.0","ver_no":"1","download":"http://"}
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
         * ver_name : 1.0
         * ver_no : 1
         * download : http://
         */

        private String ver_name;
        private String ver_no;
        private String download;

        public String getVer_name() {
            return ver_name;
        }

        public void setVer_name(String ver_name) {
            this.ver_name = ver_name;
        }

        public String getVer_no() {
            return ver_no;
        }

        public void setVer_no(String ver_no) {
            this.ver_no = ver_no;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }
}
