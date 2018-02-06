package com.magiclon.unitstrokeproject.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.util.Log

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
            db.close()
            return list
        }
    //        db.execSQL("delete from unitstroke where type='1'");
    //            Log.e("--","select type,lat,lon from unitstroke where type="+i);
    //                Log.e("-**-",latlng.toString());
    val allInfoLatlng: List<List<LatLng>>
        get() {
            val db = mysqliteopenhelper!!.readableDatabase
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
            db.close()
            return list
        }

    fun someLatlng(type: Int): List<LatLng> {
        val db = mysqliteopenhelper!!.readableDatabase
        val latLngs = ArrayList<LatLng>()
        try {
            val cursor = db.rawQuery("select type,lat,lon from unitstroke where type=" + type, null)
            while (cursor.moveToNext()) {
                val type = cursor.getInt(0)
                val lat = cursor.getDouble(1)
                val lon = cursor.getDouble(2)
                val latlng = LatLng(lat, lon)
                latLngs.add(latlng)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return latLngs
    }

    init {
        if (mysqliteopenhelper == null) {
            mysqliteopenhelper = MySqliteOpenHelper(context)
        }
    }

    fun insertLatLon(type: String, typename: String, lat: String, lon: String) {
        val db = mysqliteopenhelper!!.writableDatabase
        db.beginTransaction()
        try {
            val sqlitestatement = db.compileStatement("insert into unitstroke values (?,?,?,?);")
            sqlitestatement.bindString(1, type)
            sqlitestatement.bindString(2, typename)
            sqlitestatement.bindString(3, lat)
            sqlitestatement.bindString(4, lon)
            sqlitestatement.executeInsert()
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Unit 1级市
     */
    fun getUnitFirst(): List<UnitInfoBean> {
        val list = ArrayList<UnitInfoBean>()
        // 通过管理对象获取数据库
        val db = mysqliteopenhelper!!.writableDatabase
        try {
            val cu = db.rawQuery("select unit_id,depname from UnitInfo where parentid='1501'", null)
            while (cu.moveToNext()) {
                val ub = UnitInfoBean()
                ub.unit_id = cu.getString(0)
                ub.depname = cu.getString(1)
                list.add(ub)
            }
            cu.close()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }finally {
            db.close()
        }
        return list
    }

    /**
     * Unit 2级旗县区 3级乡镇街道 4级村委会
     */
    fun getUnitNext(parentid: String): List<UnitInfoBean> {
        val list = ArrayList<UnitInfoBean>()
        // 通过管理对象获取数据库
        val db = mysqliteopenhelper!!.writableDatabase
        try {
            val cu = db.rawQuery("select unit_id,depname from UnitInfo where parentid=?", arrayOf(parentid))
            while (cu.moveToNext()) {
                val ub = UnitInfoBean()
                ub.unit_id = cu.getString(0)
                ub.depname = cu.getString(1)
                list.add(ub)
            }
            cu.close()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }finally {
            db.close()
        }
        return list
    }

    fun getSomePolygenInfo(polygenid: String): PolygenInfoBean {
        val db = mysqliteopenhelper!!.readableDatabase
        var polygeninfo = PolygenInfoBean()
        try {
            var cursor = db.rawQuery("select * from polygen where polygenid = '$polygenid'", null)
            while (cursor.moveToNext()) {
                var polygenid = cursor.getString(0)
                var unit_id = cursor.getString(1)
                var dpname = cursor.getString(2)
                var gender = cursor.getString(3)
                var hukou = cursor.getString(4)
                var xstype = cursor.getString(5)
                var country = cursor.getString(6)
                var city = cursor.getString(7)
                var total = cursor.getString(8)
                polygeninfo = PolygenInfoBean(polygenid, unit_id, dpname, gender, hukou, xstype, country, city, total)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return polygeninfo
    }

}
