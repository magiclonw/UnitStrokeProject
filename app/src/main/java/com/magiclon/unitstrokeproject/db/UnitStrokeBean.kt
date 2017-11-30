package com.magiclon.unitstrokeproject.db

/**
 * 作者：MagicLon
 * 时间：2017/11/28 028
 * 邮箱：1348149485@qq.com
 * 描述：
 */

class UnitStrokeBean {
    var type: Int = 0
    var typename: String=""
    var lat: Double = 0.toDouble()
    var lon: Double = 0.toDouble()

    constructor(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
    }

    constructor(type: Int, lat: Double, lon: Double) {
        this.type = type
        this.lat = lat
        this.lon = lon
    }

    constructor(type: Int, typename: String, lat: Double, lon: Double) {
        this.type = type
        this.typename = typename
        this.lat = lat
        this.lon = lon
    }

    override fun toString(): String {
        return "UnitStrokeBean{" +
                "type=" + type +
                ", typename='" + typename + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}'
    }
}
