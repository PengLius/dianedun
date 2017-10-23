package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */

public class WaterChartBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"waterList":["0","0","0","0","0","0","1","1","0","0","0","0","1","1","0","0","0","0","1","1","1","1","0","0","0","0","0","0","1","1","0","0","1","1","0","0","0","0","1","1","0","0","1","1","0","0","0","0","1","1","1","1","0","1","1","1","0","0","1","1","0","0","0","0","0","0","0","0","0","0","0","0","1","1","0","0","0","1","0","0","0","0","1","1","0","0","0","0","0","0","0","0","0","0"],"xList":["2017-09-26 00:45","2017-09-26 00:48","2017-09-26 01:15","2017-09-26 01:18","2017-09-26 01:45","2017-09-26 01:48","2017-09-26 02:15","2017-09-26 02:18","2017-09-26 02:45","2017-09-26 02:48","2017-09-26 03:15","2017-09-26 03:18","2017-09-26 03:45","2017-09-26 03:48","2017-09-26 04:15","2017-09-26 04:18","2017-09-26 04:45","2017-09-26 04:48","2017-09-26 05:15","2017-09-26 05:18","2017-09-26 06:15","2017-09-26 06:18","2017-09-26 06:45","2017-09-26 06:48","2017-09-26 07:15","2017-09-26 07:18","2017-09-26 07:45","2017-09-26 07:48","2017-09-26 08:15","2017-09-26 08:18","2017-09-26 08:45","2017-09-26 08:48","2017-09-26 09:15","2017-09-26 09:18","2017-09-26 09:45","2017-09-26 09:48","2017-09-26 10:15","2017-09-26 10:18","2017-09-26 10:45","2017-09-26 10:48","2017-09-26 11:15","2017-09-26 11:18","2017-09-26 11:45","2017-09-26 11:48","2017-09-26 12:15","2017-09-26 12:18","2017-09-26 12:45","2017-09-26 12:48","2017-09-26 13:15","2017-09-26 13:18","2017-09-26 13:45","2017-09-26 13:48","2017-09-26 14:15","2017-09-26 14:18","2017-09-26 14:45","2017-09-26 14:48","2017-09-26 15:15","2017-09-26 15:18","2017-09-26 15:45","2017-09-26 15:48","2017-09-26 16:15","2017-09-26 16:18","2017-09-26 16:45","2017-09-26 16:48","2017-09-26 17:15","2017-09-26 17:18","2017-09-26 17:45","2017-09-26 17:48","2017-09-26 18:15","2017-09-26 18:18","2017-09-26 18:45","2017-09-26 18:48","2017-09-26 19:15","2017-09-26 19:18","2017-09-26 19:45","2017-09-26 19:48","2017-09-26 20:15","2017-09-26 20:18","2017-09-26 20:45","2017-09-26 20:48","2017-09-26 21:15","2017-09-26 21:18","2017-09-26 21:45","2017-09-26 21:48","2017-09-26 22:15","2017-09-26 22:18","2017-09-26 22:45","2017-09-26 22:48","2017-09-26 23:15","2017-09-26 23:18","2017-09-26 23:45","2017-09-26 23:48","2017-09-27 00:15","2017-09-27 00:18"]}
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
        private List<String> waterList;
        private List<String> xList;

        public List<String> getWaterList() {
            return waterList;
        }

        public void setWaterList(List<String> waterList) {
            this.waterList = waterList;
        }

        public List<String> getXList() {
            return xList;
        }

        public void setXList(List<String> xList) {
            this.xList = xList;
        }
    }
}
