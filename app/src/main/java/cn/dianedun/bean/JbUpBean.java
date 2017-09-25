package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class JbUpBean {

    /**
     * code : 0
     * msg : 成功
     * data : ["http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/test/skttest"]
     */

    private int code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
