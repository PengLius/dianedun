package cn.dianedun.bean;

/**
 * Created by Administrator on 2017/9/8.
 */

public class ToJsonBean {

    /**
     * type : 2
     * optionType : 2
     * contents : https://i.ys7.com/streamer/alarm/url
     */

    private int type;
    private int optionType;
    private String contents;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
