package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ImagBean {

    /**
     * code : 0
     * msg : 成功
     * data : ["http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977431052473.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977431363123.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977431665336.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977431967018.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977432314078.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977432677620.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977432949915.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977433103000.jpeg","http://dianedun.oss-cn-zhangjiakou.aliyuncs.com/image/1505977433282497.jpeg"]
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
