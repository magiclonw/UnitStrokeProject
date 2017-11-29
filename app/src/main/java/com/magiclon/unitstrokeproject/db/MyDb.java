package com.magiclon.unitstrokeproject.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：MagicLon
 * 时间：2017/11/28 028
 * 邮箱：1348149485@qq.com
 * 描述：
 */

public class MyDb {
    private MySqliteOpenHelper mysqliteopenhelper;

    public MyDb(Context context) {
        if (mysqliteopenhelper == null) {
            mysqliteopenhelper = new MySqliteOpenHelper(context);
        }
    }

    public void insertLatLon(String type, String typename, String lat, String lon) {
        SQLiteDatabase db = mysqliteopenhelper.getWritableDatabase();
        db.execSQL("insert into unitstroke values (?,?,?,?);", new String[]{type, typename, lat, lon});
    }

    public List<List<UnitStrokeBean>> getAllInfo() {
        SQLiteDatabase db = mysqliteopenhelper.getReadableDatabase();
        db.beginTransaction();
//        db.execSQL("delete from unitstroke where type='1'");
        List<List<UnitStrokeBean>> list=new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            Cursor cursor = db.rawQuery("select type,lat,lon from unitstroke where type="+i, null);
//            Log.e("--","select type,lat,lon from unitstroke where type="+i);
            List<UnitStrokeBean> ulist = new ArrayList<>();
            while (cursor.moveToNext()) {
                int type = cursor.getInt(0);
                double lat = cursor.getDouble(1);
                double lon = cursor.getDouble(2);
                UnitStrokeBean unitStrokeBean = new UnitStrokeBean(type, lat, lon);
//                Log.e("-**-",unitStrokeBean.toString());
                ulist.add(unitStrokeBean);
            }
            list.add(ulist);
            cursor.close();
        }
        db.endTransaction();
        db.close();
        return list;
    }
    public List<List<LatLng>> getAllInfoLatlng() {
        SQLiteDatabase db = mysqliteopenhelper.getReadableDatabase();
        db.beginTransaction();
//        db.execSQL("delete from unitstroke where type='1'");
        List<List<LatLng>> list=new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            Cursor cursor = db.rawQuery("select type,lat,lon from unitstroke where type="+i, null);
//            Log.e("--","select type,lat,lon from unitstroke where type="+i);
            List<LatLng> latLngs = new ArrayList<>();
            while (cursor.moveToNext()) {
                int type = cursor.getInt(0);
                double lat = cursor.getDouble(1);
                double lon = cursor.getDouble(2);
                LatLng latlng = new LatLng(lat, lon);
//                Log.e("-**-",latlng.toString());
                latLngs.add(latlng);
            }
            list.add(latLngs);
            cursor.close();
        }
        db.endTransaction();
        db.close();
        return list;
    }
}
