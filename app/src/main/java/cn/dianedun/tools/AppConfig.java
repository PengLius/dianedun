package cn.dianedun.tools;

/**
 * Created by Administrator on 2017/9/2.
 */

public class AppConfig {
//        public static final String PORTS = "http://192.168.0.11:8080";//线下
//    public static final String PORTS = "http://192.168.0.13:8080";//线下2
    public static final String PORTS = "http://47.92.155.108:8081";//线上
//    public static final String PORTS = "http://192.168.1.2:8080/dyd-app";//局域网

    public static final String LOGIN = PORTS + "/user/login";//登陆接口
    public static final String NOTECODE = PORTS + "/user/getCode";//用户名获取验证码
    public static final String RESETPWD = PORTS + "/user/resetPwd";//重置密码
    public static final String GETDEPARTBYUSER = PORTS + "/handleorder/getDepartByUser";//获得工单申请地点
    public static final String GETUSERBYDEPARTID = PORTS + "/handleorder/getUserByDepartId";//获得工单申请人
    public static final String APPLYHANDLEORDER = PORTS + "/handleorder/applyHandleOrder";//工单申请
    public static final String GETMESSAGELIST = PORTS + "/user/getMessageList";//我的消息全部
    public static final String UPLOADFILE = PORTS + "/handleorder/uploadFile";//上传文件
    public static final String APPROVALHANDLEORDER = PORTS + "/handleorder/approvalHandleOrder";//工单审批
    public static final String GETHANDLEORDERBYSTATS = PORTS + "/handleorder/getHandleOrderByStats";//历史工单
    public static final String GETHANDLEORDERBYNUM = PORTS + "/handleorder/getHandleOrderByNum";//工单详情
    public static final String SHOWINDEX = PORTS + "/index/showIndex";// 首页列表
    public static final String REVOKEDHANDLERORDER = PORTS + "/handleorder/revokedHandleOrder";// 工单撤销
    public static final String FINDALARMBYID = PORTS + "/alarm/findAlarmById";//警报详情
    public static final String HISTORYALARM = PORTS + "/alarm/historyAlarm";//历史警报
    public static final String UPDATEALARMBYID = PORTS + "/alarm/updateAlarmById";//警报处理
    public static final String FINDSWITCHROOMBYID = PORTS + "/monitor/findSwitchRoomByid";// 监测页_下拉框展示
    public static final String FINDALLLATEST = PORTS + "/monitor/findAllLatest";// 监测页_配电室数据
    public static final String INDEX = PORTS + "/monitor/index";// 概览(管理员)
    public static final String JBUPLOADFILE = PORTS + "/alarm/upLoadFile";//  附件上传警报
    public static final String FEEDBACKHANDLEORDER = PORTS + "/handleorder/feedbackHandleOrder";//工单反馈
    public static final String GETUSERINFO = PORTS + "/user/getUserInfo";//个人资料
    public static final String LOGINOUT = PORTS + "/user/logout";// 退出登录
    public static final String MONDIFYUSERHEADIMG = PORTS + "/user/modifyUserHeadImg";// 修改头像和手机号
    public static final String MODIFYMESSAGESTATSALL = PORTS + "/user/modifyMessageStatsAll";// 更新所有消息状态为已读
    public static final String MODIFYMESSAGESTATS = PORTS + "/user/modifyMessageStats";//根据message id 修改消息读取状态
    public static final String ANDROIDVERSION = PORTS + "/index/androidVersion";//安卓版本信息
    public static final String FINDABOUTUS = PORTS + "/aboutus/findAboutUs";//关于我们
    public static final String MODIFYHANDLEORDER = PORTS + "/handleorder/modifyHandleOrder";//工单修改
    public static final String STATSHDEVICE = PORTS + "/count/statsHDevice";//高压数据统计
    public static final String STATSLDEVICE = PORTS + "/count/statsLDevice";//低压数据统计
    public static final String STATSTEMP = PORTS + "/count/statsTemp";//温度数据统计
    public static final String STATSHUMIDITY = PORTS + "/count/statsHumidity";//湿度数据统计
    public static final String STATSWATER = PORTS + "/count/statsWater";//水浸数据统计


    public static final String WEB_CARMERA_PORTS = "http://47.92.155.108:8082";//线上WEB
    public static final String APP_CARMERA_PORTS = "http://47.92.155.108:8081";//线上APP
//    public static final String WEB_CARMERA_PORTS = "http://192.168.1.2:8080/dyd-pc";//线上WEB
//    public static final String APP_CARMERA_PORTS = "http://192.168.1.2:8080/dyd-app";//线上APP

    public static final String GETACCESSTOKEN = WEB_CARMERA_PORTS + "/cameraController/getAccessTokenByDepartId";//获取token
    public static final String GET_CARMERLIST = WEB_CARMERA_PORTS + "/cameraController/getCameraList";//获取摄像头列表
    public static final String CONTROLLIGHT = WEB_CARMERA_PORTS + "/cameraController/onlight";//开关灯
    public static final String GETDEPARTPLACES = APP_CARMERA_PORTS + "/handleorder/getDepartByUser";//获取配电室地址列表

    public static final String GETTOKENKEY = "https://open.ys7.com/api/lapp/token/get";//通过萤石获取token
}

