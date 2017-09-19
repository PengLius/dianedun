package cn.dianedun.tools;

/**
 * Created by Administrator on 2017/9/2.
 */

public class AppConfig {
    //    public static final String PORTS = "http://192.168.0.11:8081";//线下
    public static final String PORTS = "http://47.92.155.108:8081";//线上

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

}
