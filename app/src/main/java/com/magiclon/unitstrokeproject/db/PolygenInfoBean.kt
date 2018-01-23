package com.magiclon.unitstrokeproject.db

/**
 * 作者：MagicLon
 * 时间：2018/1/18 018
 * 邮箱：1348149485@qq.com
 * 描述：
 */
class PolygenInfoBean {
    var polygenid = ""
    var unit_id = ""
    var dpname = ""
    var gender = ""
    var hukou = ""
    var xstype = ""
    var country = ""
    var city = ""
    var total = ""


    constructor()
    constructor(polygenid: String, unit_id: String, dpname: String, gender: String, hukou: String, xstype: String, country: String, city: String, total: String) {
        this.polygenid = polygenid
        this.unit_id = unit_id
        this.dpname = dpname
        this.gender = gender
        this.hukou = hukou
        this.xstype = xstype
        this.country = country
        this.city = city
        this.total = total
    }

    override fun toString(): String {
        return "PolygenInfoBean(polygenid='$polygenid', unit_id='$unit_id', dpname='$dpname', gender='$gender', hukou='$hukou', xstype='$xstype', country='$country', city='$city', total='$total')"
    }


}