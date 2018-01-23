package com.magiclon.unitstrokeproject.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magiclon.unitstrokeproject.R
import com.magiclon.unitstrokeproject.adapter.PeopleAdapter
import kotlinx.android.synthetic.main.list_layout.view.*
@SuppressLint("ValidFragment")
/**
 * 作者：MagicLon
 * 时间：2018/1/23 023
 * 邮箱：1348149485@qq.com
 * 描述：
 */
class PeopleListFragment : Fragment {
    var type = 0
    internal var view: View? = null
    internal var mContext: Context? = null

    constructor(type: Int) {
        this.type = type
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view=inflater?.inflate(R.layout.list_layout,container,false)
        initView()
        return view
    }

    private fun initView() {
        view?.list_view?.layoutManager=LinearLayoutManager(mContext)
        view?.list_view?.adapter=PeopleAdapter(type)
    }
}