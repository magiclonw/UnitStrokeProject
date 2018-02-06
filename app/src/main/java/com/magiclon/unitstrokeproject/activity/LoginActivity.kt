package com.magiclon.unitstrokeproject.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.gyf.barlibrary.ImmersionBar
import com.magiclon.unitstrokeproject.R
import junit.framework.Test
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ImmersionBar.with(this).transparentBar()?.fullScreen(false)?.navigationBarColor(R.color.white)?.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.init()
        Glide.with(this).load("http://static01.lvye.com/forum/201308/13/105444sz3ukp93uxx1bbsh.jpg").into(kview)
        initEvents()
    }

    private fun initEvents() {
        tv_login_login.setOnClickListener {
            var intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        kview.destroyDrawingCache()
        super.onDestroy()
    }
}
