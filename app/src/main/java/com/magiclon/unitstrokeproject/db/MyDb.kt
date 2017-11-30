package com.magiclon.unitstrokeproject.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.amap.api.maps.model.LatLng

import java.util.ArrayList

/**
 * 作者：MagicLon
 * 时间：2017/11/28 028
 * 邮箱：1348149485@qq.com
 * 描述：
 */

class MyDb(context: Context) {
    private var mysqliteopenhelper: MySqliteOpenHelper? = null

    //        db.execSQL("delete from unitstroke where type='1'");
    //            Log.e("--","select type,lat,lon from unitstroke where type="+i);
    //                Log.e("-**-",unitStrokeBean.toString());
    val allInfo: List<List<UnitStrokeBean>>
        get() {
            val db = mysqliteopenhelper!!.readableDatabase
            db.beginTransaction()
            val list = ArrayList<List<UnitStrokeBean>>()
            for (i in 1..12) {
                val cursor = db.rawQuery("select type,lat,lon from unitstroke where type=" + i, null)
                val ulist = ArrayList<UnitStrokeBean>()
                while (cursor.moveToNext()) {
                    val type = cursor.getInt(0)
                    val lat = cursor.getDouble(1)
                    val lon = cursor.getDouble(2)
                    val unitStrokeBean = UnitStrokeBean(type, lat, lon)
                    ulist.add(unitStrokeBean)
                }
                list.add(ulist)
                cursor.close()
            }
            db.endTransaction()
            db.close()
            return list
        }
    //        db.execSQL("delete from unitstroke where type='1'");
    //            Log.e("--","select type,lat,lon from unitstroke where type="+i);
    //                Log.e("-**-",latlng.toString());
    val allInfoLatlng: List<List<LatLng>>
        get() {
            val db = mysqliteopenhelper!!.readableDatabase
            db.beginTransaction()
            val list = ArrayList<List<LatLng>>()
            for (i in 1..12) {
                val cursor = db.rawQuery("select type,lat,lon from unitstroke where type=" + i, null)
                val latLngs = ArrayList<LatLng>()
                while (cursor.moveToNext()) {
                    val type = cursor.getInt(0)
                    val lat = cursor.getDouble(1)
                    val lon = cursor.getDouble(2)
                    val latlng = LatLng(lat, lon)
                    latLngs.add(latlng)
                }
                list.add(latLngs)
                cursor.close()
            }
            db.endTransaction()
            db.close()
            return list
        }

    init {
        if (mysqliteopenhelper == null) {
            mysqliteopenhelper = MySqliteOpenHelper(context)
        }
    }

    fun insertLatLon(type: String, typename: String, lat: String, lon: String) {
        val db = mysqliteopenhelper!!.writableDatabase
        db.execSQL("insert into unitstroke values (?,?,?,?);", arrayOf(type, typename, lat, lon))
    }
}
