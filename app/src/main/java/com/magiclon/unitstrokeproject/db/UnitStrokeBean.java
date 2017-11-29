package com.magiclon.unitstrokeproject.db;

/**
 * 作者：MagicLon
 * 时间：2017/11/28 028
 * 邮箱：1348149485@qq.com
 * 描述：
 */

public class UnitStrokeBean {
    int type;
    String typename;
    double lat;
    double lon;

    public UnitStrokeBean(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public UnitStrokeBean(int type, double lat, double lon) {
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public UnitStrokeBean(int type, String typename, double lat, double lon) {
        this.type = type;
        this.typename = typename;
        this.lat = lat;
        this.lon = lon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "UnitStrokeBean{" +
                "type=" + type +
                ", typename='" + typename + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
