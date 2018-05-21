package com.zhangling.bluetooth.model.UI;

public class OBDModel {

    private long date; //毫秒数
    private Integer id;
    private String msgType;
    private long counts;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getCounts() {
        return counts;
    }

    public void setCounts(long counts) {
        this.counts = counts;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private String data;
    private String carId = "";
    private double value = 0.0;
    public class Modle {
        private String unit = "";
        private String sgName = "";
        private double value = 0.0;
        private double sign = 1.0;
    }

}
