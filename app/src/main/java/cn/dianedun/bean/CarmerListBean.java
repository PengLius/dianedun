package cn.dianedun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class CarmerListBean {

    /**
     * code : 0
     * msg : success
     * data : {"page":{"total":2,"page":0,"size":10},"result":[{"deviceSerial":"427734444","channelNo":1,"channelName":"C1(427734444)","status":1,"isShared":"1","picUrl":"http://img.ys7.com/group1/M00/02/B4/CmGCA1dRGyuAdJ_RAABJBCB_Re4796.jpg","isEncrypt":1,"videoLevel":2,"encryptCode":"MDWBOZ"},{"deviceSerial":"519544444","channelNo":1,"channelName":"C2C(519544444)","status":0,"isShared":"2","picUrl":"https://i.ys7.com/assets/imgs/public/homeDevice.jpeg","isEncrypt":0,"videoLevel":2}]}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
         * page : {"total":2,"page":0,"size":10}
         * result : [{"deviceSerial":"427734444","channelNo":1,"channelName":"C1(427734444)","status":1,"isShared":"1","picUrl":"http://img.ys7.com/group1/M00/02/B4/CmGCA1dRGyuAdJ_RAABJBCB_Re4796.jpg","isEncrypt":1,"videoLevel":2,"encryptCode":"MDWBOZ"},{"deviceSerial":"519544444","channelNo":1,"channelName":"C2C(519544444)","status":0,"isShared":"2","picUrl":"https://i.ys7.com/assets/imgs/public/homeDevice.jpeg","isEncrypt":0,"videoLevel":2}]
         */

        private PageBean page;
        private List<ResultBean> result;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class PageBean {
            /**
             * total : 2
             * page : 0
             * size : 10
             */

            private int total;
            private int page;
            private int size;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }
        }

        public static class ResultBean {
            /**
             * deviceSerial : 427734444
             * channelNo : 1
             * channelName : C1(427734444)
             * status : 1
             * isShared : 1
             * picUrl : http://img.ys7.com/group1/M00/02/B4/CmGCA1dRGyuAdJ_RAABJBCB_Re4796.jpg
             * isEncrypt : 1
             * videoLevel : 2
             * encryptCode : MDWBOZ
             */

            private String deviceSerial;
            private int channelNo;
            private String channelName;
            private int status;
            private String isShared;
            private String picUrl;
            private int isEncrypt;
            private int videoLevel;
            private String encryptCode;

            public String getDeviceSerial() {
                return deviceSerial;
            }

            public void setDeviceSerial(String deviceSerial) {
                this.deviceSerial = deviceSerial;
            }

            public int getChannelNo() {
                return channelNo;
            }

            public void setChannelNo(int channelNo) {
                this.channelNo = channelNo;
            }

            public String getChannelName() {
                return channelName;
            }

            public void setChannelName(String channelName) {
                this.channelName = channelName;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getIsShared() {
                return isShared;
            }

            public void setIsShared(String isShared) {
                this.isShared = isShared;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public int getIsEncrypt() {
                return isEncrypt;
            }

            public void setIsEncrypt(int isEncrypt) {
                this.isEncrypt = isEncrypt;
            }

            public int getVideoLevel() {
                return videoLevel;
            }

            public void setVideoLevel(int videoLevel) {
                this.videoLevel = videoLevel;
            }

            public String getEncryptCode() {
                return encryptCode;
            }

            public void setEncryptCode(String encryptCode) {
                this.encryptCode = encryptCode;
            }
        }
    }
}
