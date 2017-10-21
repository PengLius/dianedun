package cn.dianedun.bean;


import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class RealTimeConBean {


    /**
     * code : 0
     * msg : 成功
     * data : {"iaList":["50","50","10","10","10","10","20","20","80","80","30","30","0","10","10","80","0","0","0","0","10","10","0","10","30","30","10","10","0","40","20","40","70","70","30","30"],"ibList":["50","50","10","10","10","10","20","20","80","80","30","30","10","10","10","80","80","80","10","10","10","10","10","30","30","30","10","10","10","90","20","40","70","70","30","30"],"icList":["0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"],"vaList":["50","50","10","10","10","10","20","20","80","80","30","30","10","10","10","80","80","80","10","10","10","10","0","10","30","30","10","10","0","40","20","40","70","70","30","30"],"vbList":["50","50","10","10","10","10","20","20","80","80","30","30","10","10","10","80","80","80","10","10","10","10","0","10","30","30","10","10","0","40","20","40","70","70","30","30"],"vcList":["50","50","10","10","10","10","20","20","10","10","20","20","50","50","10","40","0","0","70","70","30","30","0","80","60","60","40","40","0","80","10","50","30","30","20","20"],"vabList":["50","50","10","10","10","10","20","20","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"],"vbcList":["80","80","10","10","60","60","40","40","10","10","20","20","50","50","10","40","0","0","70","70","30","30","0","80","60","60","40","40","0","80","10","50","30","30","20","20"],"vcaList":["0","0","0","0","0","0","0","0","10","10","20","20","50","50","10","40","0","0","70","70","30","30","0","80","60","60","40","40","0","80","10","50","30","30","20","20"],"xList":["2017-09-26 03:30","2017-09-26 03:45","2017-09-26 04:30","2017-09-26 04:45","2017-09-26 06:30","2017-09-26 06:45","2017-09-26 07:00","2017-09-26 07:15","2017-09-26 08:00","2017-09-26 08:15","2017-09-26 09:30","2017-09-26 09:45","2017-09-26 11:00","2017-09-26 11:15","2017-09-26 11:30","2017-09-26 11:45","2017-09-26 13:00","2017-09-26 13:15","2017-09-26 14:00","2017-09-26 14:15","2017-09-26 15:30","2017-09-26 15:45","2017-09-26 16:30","2017-09-26 16:45","2017-09-26 17:30","2017-09-26 17:45","2017-09-26 18:00","2017-09-26 18:15","2017-09-26 18:30","2017-09-26 18:45","2017-09-26 20:00","2017-09-26 20:15","2017-09-26 21:30","2017-09-26 21:45","2017-09-26 22:30","2017-09-26 22:45"]}
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
        private List<String> iaList;
        private List<String> ibList;
        private List<String> icList;
        private List<String> vaList;
        private List<String> vbList;
        private List<String> vcList;
        private List<String> vabList;
        private List<String> vbcList;
        private List<String> vcaList;
        private List<String> xList;

        public List<String> getIaList() {
            return iaList;
        }

        public void setIaList(List<String> iaList) {
            this.iaList = iaList;
        }

        public List<String> getIbList() {
            return ibList;
        }

        public void setIbList(List<String> ibList) {
            this.ibList = ibList;
        }

        public List<String> getIcList() {
            return icList;
        }

        public void setIcList(List<String> icList) {
            this.icList = icList;
        }

        public List<String> getVaList() {
            return vaList;
        }

        public void setVaList(List<String> vaList) {
            this.vaList = vaList;
        }

        public List<String> getVbList() {
            return vbList;
        }

        public void setVbList(List<String> vbList) {
            this.vbList = vbList;
        }

        public List<String> getVcList() {
            return vcList;
        }

        public void setVcList(List<String> vcList) {
            this.vcList = vcList;
        }

        public List<String> getVabList() {
            return vabList;
        }

        public void setVabList(List<String> vabList) {
            this.vabList = vabList;
        }

        public List<String> getVbcList() {
            return vbcList;
        }

        public void setVbcList(List<String> vbcList) {
            this.vbcList = vbcList;
        }

        public List<String> getVcaList() {
            return vcaList;
        }

        public void setVcaList(List<String> vcaList) {
            this.vcaList = vcaList;
        }

        public List<String> getXList() {
            return xList;
        }

        public void setXList(List<String> xList) {
            this.xList = xList;
        }
    }
}
