package com.magiclon.unitstrokeproject.activity

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.magiclon.unitstrokeproject.R
import kotlinx.android.synthetic.main.activity_peoplelist.*
import android.support.v4.app.FragmentPagerAdapter
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.WindowManager
import com.gyf.barlibrary.ImmersionBar
import com.magiclon.unitstrokeproject.fragment.PeopleListFragment


/**
 * 现享受新申请列表
 */
class PeopleListActivity : AppCompatActivity() {
    var mFragment1: PeopleListFragment? = null

    var mFragment2: PeopleListFragment? = null
    private val mFragments = ArrayList<PeopleListFragment>()
    var mPagerAdapter: PagerAdapter? = null
    var depname = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peoplelist)
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){//沉浸栏手持机4.4版本会引起不能滑动问题
            ImmersionBar.with(this).statusBarColor(R.color.white).keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.statusBarDarkFont(true)?.navigationBarColor(R.color.white)?.init()
        }else{
            ImmersionBar.with(this).titleBar(toolbar).statusBarColor(R.color.white).keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.statusBarDarkFont(true)?.navigationBarColor(R.color.white)?.init()
        }
        depname = intent.extras.getString("depname")
        initEvents()
        initData()
        initFragment()
    }

    private fun initEvents() {
        iv_next_back.setOnClickListener { super.onBackPressed() }
    }

    private fun initData() {
        tv_next_title.text = depname
    }

    private fun initFragment() {
        if (mFragment1 == null) {
            mFragment1 = PeopleListFragment(2)
        }
        if (mFragment2 == null) {
            mFragment2 = PeopleListFragment(1)
        }

        mFragments.add(mFragment1!!)
        mFragments.add(mFragment2!!)
        mPagerAdapter = PagerAdapter(supportFragmentManager)
        view_pager.adapter = mPagerAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(toolbar_tab))
        toolbar_tab.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
    }


    inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            if (position == 0) {
                return mFragment1
            } else if (position == 1) {
                return mFragment2
            }

            return null
        }

        override fun getCount(): Int {
            return 2
        }

    }
}
