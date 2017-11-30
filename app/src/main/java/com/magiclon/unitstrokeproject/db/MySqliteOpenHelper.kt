package com.magiclon.unitstrokeproject.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * 作者：MagicLon
 * 时间：2017/11/28 028
 * 邮箱：1348149485@qq.com
 * 描述：
 */

class MySqliteOpenHelper(private val mContext: Context) : SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION, null) {
    private val tablename = "unitstroke"

    init {
        DB_PATH = File.separator + "data" + Environment.getDataDirectory().absolutePath + File.separator + mContext.packageName + File.separator + "databases" + File.separator
        copyDBFile()
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {}

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }

    fun copyDBFile() {
        val dir = File(DB_PATH)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val dbFile = File(DB_PATH + DATABASE_NAME)
        if (!dbFile.exists()) {
            val inputstream: InputStream
            val os: OutputStream
            try {
                inputstream = mContext.resources.assets.open(DATABASE_NAME)
                os = FileOutputStream(dbFile)
                val buffer = ByteArray(BUFFER_SIZE)
                var length= inputstream.read(buffer,0,buffer.size)
                while (length > 0) {
                    os.write(buffer, 0, length)
                    length= inputstream.read(buffer,0,buffer.size)
                }
                os.flush()
                os.close()
                inputstream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    companion object {
        private val DATABASE_NAME = "unitstroke.db"
        private val DATABASE_VERSION = 1
        private val BUFFER_SIZE = 1024
        private var DB_PATH: String = ""
    }
}
