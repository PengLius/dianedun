package cn.dianedun.tools;

/**
 * Created by Administrator on 2017/9/2.
 */

public class AppConfig {
    public static final String PORTS = "http://192.168.0.11:8081";//线下

    public static final String LOGIN = PORTS + "/user/login";//登陆接口
    public static final String NOTECODE = PORTS + "/user/getCode";//用户名获取验证码
    public static final String RESETPWD = PORTS + "/user/resetPwd";//重置密码
    public static final String GETDEPARTBYUSER = PORTS + "/handleorder/getDepartByUser";//获得工单申请地点
    public static final String GETUSERBYDEPARTID = PORTS + "/handleorder/getUserByDepartId";//获得工单申请人
    public static final String APPLYHANDLEORDER = PORTS + "/handleorder/applyHandleOrder";//工单申请
    public static final String GETMESSAGELIST = PORTS + "/user/getMessageList";//我的消息全部
    public static final String UPLOADFILE = PORTS + "/handleorder/uploadFile";//上传文件

}
