package com.magiclon.unitstrokeproject

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.PolygonOptions
import com.gyf.barlibrary.ImmersionBar
import com.magiclon.unitstrokeproject.db.MyDb
import kotlinx.android.synthetic.main.activity_main.*
import com.amap.api.maps.model.LatLngBounds


class MainActivity : AppCompatActivity() {
    private var db: MyDb? = null
    private var mAMap: AMap? = null
    private var latlngs: List<List<LatLng>>? = null
    private var colors = intArrayOf(Color.argb(0, 0, 0, 0), Color.argb(90, 250, 5, 22), Color.argb(90, 155, 5, 251), Color.argb(90, 155, 5, 251), Color.argb(90, 51, 5, 251), Color.argb(90, 5, 167, 251), Color.argb(90, 5, 237, 251), Color.argb(90, 5, 237, 251), Color.argb(90, 171, 249, 239), Color.argb(90, 5, 251, 28), Color.argb(90, 251, 248, 5), Color.argb(90, 251, 150, 5))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImmersionBar.with(this).titleBar(toolbar, false).transparentBar()?.fullScreen(false)?.navigationBarColor(R.color.white)?.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.statusBarDarkFont(true, 0.2f)?.init()
        map.onCreate(savedInstanceState)
        mAMap = map.map
        val b = LatLngBounds.builder()
//        var p1 = LatLng((40.488054 * 2) - 39.588911, (111.4149225 * 2) - 110.518197)
//        var p2 = LatLng((40.488054 * 2) - 39.588911, (111.4149225 * 2) - 112.311648)
//        var p3 = LatLng((40.488054 * 2) - 41.387197, (111.4149225 * 2) - 110.518197)
//        var p4 = LatLng((40.488054 * 2) - 41.387197, (111.4149225 * 2) - 112.311648)

        var p5 = LatLng(39.588911, 110.518197)
        var p6 = LatLng(39.588911, 112.311648)
        var p7 = LatLng(41.387197, 110.518197)
        var p8 = LatLng(41.387197, 112.311648)

//        b.include(p1)
//        b.include(p2)
//        b.include(p3)
//        b.include(p4)
        b.include(p5)
        b.include(p6)
        b.include(p7)
        b.include(p8)
        mAMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 50))

        mAMap?.uiSettings?.setAllGesturesEnabled(false)
        mAMap?.uiSettings?.isZoomControlsEnabled = false
        db = MyDb(this)
        Thread(Runnable {
            latlngs = db?.allInfoLatlng
            for (i in 0..11) {
                val polygonOptions = PolygonOptions()
                val ulist = latlngs?.get(i)
                polygonOptions.addAll(ulist)
                var strokewidth = 3
                if (i == 0) {
                    strokewidth = 5
                }
                polygonOptions.fillColor(colors[i]).strokeWidth(strokewidth.toFloat()).strokeColor(Color.RED)
                mAMap?.addPolygon(polygonOptions)
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map?.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        map?.onDestroy()
        ImmersionBar.with(this).destroy()
    }

}
