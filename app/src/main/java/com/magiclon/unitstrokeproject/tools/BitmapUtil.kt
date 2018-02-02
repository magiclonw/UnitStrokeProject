package com.magiclon.unitstrokeproject.tools

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import es.dmoral.toasty.Toasty
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

/**
 * 作者：MagicLon
 * 时间：2018/2/2 002
 * 邮箱：1348149485@qq.com
 * 描述：
 */
object BitmapUtil {

    @Suppress("DEPRECATION")
    fun saveViewBitmap2File(filename: String, view: View, context: Context) {
        var dialog: ProgressDialog? = null
        dialog = ProgressDialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setMessage("请稍候...")
        dialog.show()
        var subscription: Subscription? = null
        var bitmap: Bitmap? = null
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        c.drawColor(Color.WHITE)
        view.draw(c)
        subscription = Observable.create(Observable.OnSubscribe<Boolean> { t ->
            val fos: FileOutputStream? = null
            try {
                val sdRoot = File(Environment.getExternalStorageDirectory().absolutePath + "/社会救助决策支持平台")
                if (!sdRoot.exists()) {
                    sdRoot.mkdirs()
                }
                val file = File(sdRoot, filename)
                val fileoutputstream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fileoutputstream)
                t.onNext(true)
            } catch (e: Exception) {
                t.onNext(false)
                e.printStackTrace()
            } finally {
                fos?.flush()
                bitmap?.recycle()
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: Boolean? ->
                    dialog.dismiss()
                    if (t!!) {
                        Toasty.success(context, "导出成功").show()
                    } else {
                        Toasty.error(context, "导出失败").show()
                    }
                    subscription?.unsubscribe()
                }
    }
}