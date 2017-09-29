package cn.dianedun.bean;

/**
 * Created by Administrator on 2017/9/22.
 */

public class MineBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"id":"402823815df2ea32015df2f4c8b80011","activitisync":"","browser":"","password":"","realname":"刘朋有权限","signature":"","status":"","userkey":"","username":"liupengyou","departid":"","deleteFlag":"","token":"","mobilephone":"15933227792","headimg":"http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1506333173776688.jpg","logtime":"2017-09-26 08:59:11.0","unreadCount":0}
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
         * id : 402823815df2ea32015df2f4c8b80011
         * activitisync :
         * browser :
         * password :
         * realname : 刘朋有权限
         * signature :
         * status :
         * userkey :
         * username : liupengyou
         * departid :
         * deleteFlag :
         * token :
         * mobilephone : 15933227792
         * headimg : http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1506333173776688.jpg
         * logtime : 2017-09-26 08:59:11.0
         * unreadCount : 0
         */

        private String id;
        private String activitisync;
        private String browser;
        private String password;
        private String realname;
        private String signature;
        private String status;
        private String userkey;
        private String username;
        private String departid;
        private String deleteFlag;
        private String token;
        private String mobilephone;
        private String headimg;
        private String logtime;
        private int unreadCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActivitisync() {
            return activitisync;
        }

        public void setActivitisync(String activitisync) {
            this.activitisync = activitisync;
        }

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(String browser) {
            this.browser = browser;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserkey() {
            return userkey;
        }

        public void setUserkey(String userkey) {
            this.userkey = userkey;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDepartid() {
            return departid;
        }

        public void setDepartid(String departid) {
            this.departid = departid;
        }

        public String getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(String deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getLogtime() {
            return logtime;
        }

        public void setLogtime(String logtime) {
            this.logtime = logtime;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }
    }
}
