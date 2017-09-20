package cn.dianedun.tools;

/**
 * Created by Administrator on 2017/9/2.
 */

public class AppConfig {
    public static final String PORTS = "http://47.92.155.108:8081";//线下

    public static final String LOGIN = PORTS + "/user/login";//登陆接口
    public static final String NOTECODE = PORTS + "/user/getCode";//用户名获取验证码
    public static final String RESETPWD = PORTS + "/user/resetPwd";//重置密码
    public static final String GETDEPARTBYUSER = PORTS + "/handleorder/getDepartByUser";//获得工单申请地点
    public static final String GETUSERBYDEPARTID = PORTS + "/handleorder/getUserByDepartId";//获得工单申请人
    public static final String APPLYHANDLEORDER = PORTS + "/handleorder/applyHandleOrder";//工单申请
    public static final String GETMESSAGELIST = PORTS + "/user/getMessageList";//我的消息全部
    public static final String UPLOADFILE = PORTS + "/handleorder/uploadFile";//上传文件


    public static final String WEB_CARMERA_PORTS = "http://47.92.155.108:8082";//线上WEB
    public static final String APP_CARMERA_PORTS = "http://47.92.155.108:8081";//线上APP
    public static final String GETACCESSTOKEN = WEB_CARMERA_PORTS + "/cameraController/getAccessTokenByDepartId";//获取token
    public static final String GETDEPARTPLACES = APP_CARMERA_PORTS + "/handleorder/getDepartByUser";//获取配电室地址列表
}

