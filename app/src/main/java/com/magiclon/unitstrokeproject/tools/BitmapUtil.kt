package com.magiclon.unitstrokeproject.tools

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.view.View
import android.view.Window
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.RectangleReadOnly
import com.itextpdf.text.pdf.PdfWriter
import es.dmoral.toasty.Toasty
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
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
        bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.RGB_565)
        val c = Canvas(bitmap)
        c.drawColor(Color.WHITE)
        view.draw(c)
        subscription = Observable.create(Observable.OnSubscribe<Boolean> { t ->
            var fos: FileOutputStream? = null
            try {
                val sdRoot = File(Environment.getExternalStorageDirectory().absolutePath + "/社会救助决策支持平台")
                if (!sdRoot.exists()) {
                    sdRoot.mkdirs()
                }
                val file = File(sdRoot, filename)
                fos = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
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

    fun saveToPdf(filename: String, view: View, context: Context) {
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
        bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.RGB_565)
        val c = Canvas(bitmap)
        c.drawColor(Color.WHITE)
        view.draw(c)
        subscription = Observable.create(Observable.OnSubscribe<Boolean> { t ->
            var document: Document? = null
            var writer: PdfWriter? = null
            var baos:ByteArrayOutputStream?=null
            var fileoutputstream:FileOutputStream?=null
            try {
                val sdRoot = File(Environment.getExternalStorageDirectory().absolutePath + "/社会救助决策支持平台")
                if (!sdRoot.exists()) {
                    sdRoot.mkdirs()
                }
                val file = File(sdRoot, filename)
                fileoutputstream = FileOutputStream(file)
                document = Document(RectangleReadOnly(bitmap.width.toFloat(), bitmap.height.toFloat()), 0f, 0f, 0f, 0f)
                writer = PdfWriter.getInstance(document, fileoutputstream)
                document.open()
                baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val image = Image.getInstance(baos.toByteArray())
                document.add(image)
                document.newPage()
                t.onNext(true)
            } catch (e: Exception) {
                t.onNext(false)
                e.printStackTrace()
            } finally {
                fileoutputstream?.flush()
                baos?.flush()
                document?.close()
                writer?.close()
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