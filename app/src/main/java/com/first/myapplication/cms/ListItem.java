package com.first.myapplication.cms;

public class ListItem {

    private String time;
    private String desc;
    private String endtime;

    public ListItem(String time, String endtime, String desc) {
        this.time = time;
        this.endtime = endtime;
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
