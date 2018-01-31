package com.magiclon.unitstrokeproject

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.model.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.gyf.barlibrary.ImmersionBar
import com.magiclon.unitstrokeproject.activity.NextUnitActivity
import com.magiclon.unitstrokeproject.adapter.ExamAdapter
import com.magiclon.unitstrokeproject.adapter.NextUnitAdapter
import com.magiclon.unitstrokeproject.db.MyDb
import com.magiclon.unitstrokeproject.db.PolygenInfoBean
import com.magiclon.unitstrokeproject.db.UnitInfoBean
import com.magiclon.unitstrokeproject.tools.InfoDialog
import com.magiclon.unitstrokeproject.tools.MyMarkerView
import com.sothree.slidinguppanel.ScrollableViewHelper
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_dragview.*
import me.itangqi.waveloadingview.WaveLoadingView
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var db: MyDb? = null
    private var mAMap: AMap? = null
    private var type_poly = true
    private var isfirst = true
    private var polygons = ArrayList<Polygon>()
    private var mlatlngs = ArrayList<List<LatLng>>()
    private var adapter: ExamAdapter? = null
    private var nextadapter: NextUnitAdapter? = null
    private var year = Calendar.getInstance().get(Calendar.YEAR) - 4
    protected var mParties = arrayOf("农村", "城镇")
    protected var mBarlabels = arrayOf("农村", "城镇", "总计")
    protected var mParties_gander = arrayOf("男", "女")
    protected var mParties_type = arrayOf("新申请", "现享受")
    private var subscription: Subscription? = null
    private var polygeninfo = PolygenInfoBean()
    private var unitinfos = ArrayList<UnitInfoBean>()
    private var colors = intArrayOf(Color.argb(0, 0, 0, 0), Color.argb(120, 250, 5, 22), Color.argb(120, 155, 5, 251), Color.argb(120, 155, 5, 251), Color.argb(120, 51, 5, 251), Color.argb(120, 5, 167, 251), Color.argb(120, 5, 237, 251), Color.argb(120, 5, 237, 251), Color.argb(120, 171, 249, 239), Color.argb(120, 5, 251, 28), Color.argb(120, 251, 248, 5), Color.argb(120, 251, 150, 5))
    private var total_hushi = 0 //呼市的户数量
    private var total_cur = 0 //当前选中的区的户数量
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImmersionBar.with(this).titleBar(toolbar, false).transparentBar()?.fullScreen(false)?.navigationBarColor(R.color.white)?.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.statusBarDarkFont(true, 0.2f)?.init()
        map.onCreate(savedInstanceState)
        db = MyDb(this)
        initAmap()
        initData()
        addOverLay(true)
    }

    fun initAmap() {//初始化地图
        mAMap = map.map
        // 西南坐标
        val southwestLatLng = LatLng(39.25, 110.27)
        // 东北坐标
        val northeastLatLng = LatLng(41.8, 112.5)
        var limitbounds = LatLngBounds(southwestLatLng, northeastLatLng)
        mAMap?.setMapStatusLimits(limitbounds)
        mAMap?.uiSettings?.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
        mAMap?.setOnMapClickListener { latlng ->
            polygons.forEachIndexed { _, polygon ->
                if (polygon.contains(latlng)) {
                    changeData(polygon.id)
                    return@setOnMapClickListener
                }
            }
        }

    }

    fun initData() {
        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE)
        waveLoadingView.setAnimDuration(4000)
        adapter = ExamAdapter()
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = adapter
        sliding_layout.setScrollableViewHelper(object : ScrollableViewHelper() {
            override fun getScrollableViewScrollPosition(scrollableView: View?, isSlidingUp: Boolean): Int {
                return if (scrollableView is NestedScrollView) {
                    if (isSlidingUp) {
                        scrollableView.getScrollY()
                    } else {
                        var nsv: NestedScrollView = scrollableView
                        var child: View = nsv.getChildAt(0)
                        (child.bottom - (nsv.height + nsv.scrollY))
                    }
                } else {
                    0
                }
            }
        })
        fab.setOnClickListener {
            changeData("Polygon1")
        }
        iv_change.setOnClickListener {
            type_poly = !type_poly
            addOverLay(type_poly)
        }
        nextadapter = NextUnitAdapter(unitinfos, 1)
        rv_qulist.layoutManager = LinearLayoutManager(this)
        rv_qulist.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv_qulist.itemAnimator = DefaultItemAnimator()
        rv_qulist.adapter = nextadapter
        nextadapter?.setOnItemClickListener { _, position ->
            var intent = Intent(this, NextUnitActivity::class.java)
            intent.putExtra("unit_id", unitinfos[position].unit_id)
            intent.putExtra("depname", unitinfos[position].depname)
            intent.putExtra("total_cur", total_cur)
            startActivity(intent)
        }
    }

    private fun changeData(polygonid: String) {
        subscription = Observable.create(Observable.OnSubscribe<PolygenInfoBean> { t ->
            polygeninfo = db?.getSomePolygenInfo(polygonid)!!
            unitinfos.clear()
            unitinfos.addAll(db?.getUnitNext(polygeninfo.unit_id) as ArrayList<UnitInfoBean>)
            t.onNext(polygeninfo)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    setPieData(mChart_hukou, mParties, t.hukou.split(","))
                    setPieData(mChart_gander, mParties_gander, t.gender.split(","))
                    setPieData(mChart_type, mParties_type, t.xstype.split(","))
                    var totals = polygeninfo.total.split(",")
                    var cur_total = totals[0].toInt()
                    tv_name.text = polygeninfo.dpname
                    tv_count.text = "共 ${cur_total} 户"
                    var objectAnimator = ObjectAnimator.ofFloat(rl_main_top, View.SCALE_X, *floatArrayOf(1.02f, 1f))
                    objectAnimator.duration = 200
                    objectAnimator.repeatCount = 2
                    objectAnimator.start()
                    setBarData(mChart_year, year, 5, mBarlabels, t.country.split(","), t.city.split(","), totals)
                    if ("Polygon1" == polygonid) {
                        rv_qulist.visibility = View.GONE
                        ll_main_unit.visibility = View.GONE
                        waveLoadingView.centerTitle = "${total_hushi}户"
                        waveLoadingView.bottomTitle = "100%"
                        waveLoadingView.progressValue = 80
                        total_cur = total_hushi
                    } else {
                        nextadapter?.notifyDataSetChanged()
                        total_cur = cur_total
                        rv_qulist.visibility = View.VISIBLE
                        ll_main_unit.visibility = View.VISIBLE
                        waveLoadingView.centerTitle = "${cur_total}户"
                        var percent = (cur_total * 100.0 / total_hushi).toInt()
                        waveLoadingView.bottomTitle = "$percent%"
                        waveLoadingView.progressValue = percent + 55
                    }
                    subscription?.unsubscribe()
                }
    }


    fun addOverLay(type: Boolean) {//添加图层
        subscription = Observable.create(Observable.OnSubscribe<PolygenInfoBean> { t ->
            mAMap?.clear()
            for (i in 0..11) {
                if (mlatlngs.size != 12) {
                    var latlngs = db?.someLatlng(i + 1)
                    mlatlngs.add(latlngs!!)
                }
                if (type) {
                    val polygonOptions = PolygonOptions()
                    polygonOptions.addAll(mlatlngs[i])
                    var strokewidth = 3
                    if (i == 0) {
                        strokewidth = 5
                    }
                    polygonOptions.fillColor(colors[i]).strokeWidth(strokewidth.toFloat()).strokeColor(Color.RED)
                    var polygen = mAMap?.addPolygon(polygonOptions)
                    if (polygons.size != 11 && i != 0) {
                        polygons.add(polygen!!)
                    }
                } else {
                    val builder = HeatmapTileProvider.Builder()
                    builder.data(mlatlngs[i]) // 设置热力图绘制的数据
                    // 设置热力图渐变，有默认值 DEFAULT_GRADIENT，可不设置该接口
                    // Gradient 的设置可见参考手册
                    // 构造热力图对象
                    val heatmapTileProvider = builder.build()
                    // 初始化 TileOverlayOptions
                    val tileOverlayOptions = TileOverlayOptions()
                    tileOverlayOptions.tileProvider(heatmapTileProvider) // 设置瓦片图层的提供者
                    // 向地图上添加 TileOverlayOptions 类对象
                    mAMap?.addTileOverlay(tileOverlayOptions)
                }
                Thread.sleep(50)
            }
            if (isfirst) {
                polygeninfo = db?.getSomePolygenInfo("Polygon1")!!
                t?.onNext(polygeninfo)
                isfirst = !isfirst
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            initPieChart(mChart_hukou, "低保类型统计")//低保类型
            initPieChart(mChart_gander, "男女比例统计")//男女比例
            initPieChart(mChart_type, "享受类型统计")//享受比例
            initBarChart(mChart_year)
            setPieData(mChart_hukou, mParties, t?.hukou?.split(",")!!)
            setPieData(mChart_gander, mParties_gander, t.gender.split(","))
            setPieData(mChart_type, mParties_type, t.xstype.split(","))
            setBarData(mChart_year, year, 5, mBarlabels, t.country.split(","), t.city.split(","), t.total.split(","))
            var totals = t.total.split(",")
            total_hushi = totals[0].toInt()
            total_cur = total_hushi
            tv_name.text = t.dpname
            tv_count.text = "共 ${totals[0]} 户"
            waveLoadingView.centerTitle = "${total_hushi}户"
            waveLoadingView.bottomTitle = "100%"
            subscription?.unsubscribe()
        }
    }

    //饼图设置
    fun initPieChart(mChart: PieChart, centerText: String) {
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                InfoDialog(this@MainActivity, "总计：${total_cur}户\n\n所占比例：${(e?.y!! *100).toInt()}%\n\n${(total_cur* e.y).toInt()}户")
            }
        })
        mChart.setUsePercentValues(true)
        mChart.description.isEnabled = false
        mChart.setExtraOffsets(5f, 10f, 5f, 5f)
        mChart.dragDecelerationFrictionCoef = 0.95f
        mChart.centerText = centerText
        mChart.setCenterTextSize(15f)
        mChart.isDrawHoleEnabled = true
        mChart.setHoleColor(Color.WHITE)
        mChart.setTransparentCircleColor(Color.WHITE)
        mChart.setTransparentCircleAlpha(110)
        mChart.holeRadius = 58f
        mChart.transparentCircleRadius = 61f
        mChart.setDrawCenterText(true)
        mChart.rotationAngle = 0F
        // enable rotation of the chart by touch
        mChart.isRotationEnabled = true
        mChart.isHighlightPerTapEnabled = true
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        // mChart.spin(2000, 0, 360);
        val l = mChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE)
        mChart.setEntryLabelTextSize(10f)
    }

    //饼图数据来源
    private fun setPieData(mChart: PieChart, parties: Array<String>, values: List<String>) {
        //type值 1户口 2性别 3享受类别
        val entries = ArrayList<PieEntry>()
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        (0 until parties.size).mapTo(entries) {
            PieEntry(values[it % values.size].toFloat(),
                    parties[it % parties.size])
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 2f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        // add a lot of colors
        val colors = ArrayList<Int>()
        ColorTemplate.JOYFUL_COLORS.forEach { c -> colors.add(c) }
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        mChart.data = data

        // undo all highlights
        mChart.highlightValues(null)
        mChart.invalidate()
    }

    //barchat设置
    fun initBarChart(mChart: BarChart) {
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {

            }
        })
        mChart.description.isEnabled = false

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false)

        mChart.setDrawBarShadow(false)

        mChart.setDrawGridBackground(false)

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.chartView = mChart // For bounds control
        mChart.marker = mv // Set the marker to the chart
        mChart.animateY(1400)

        val l = mChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f

        val xAxis = mChart.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> value.toInt().toString() }

        val leftAxis = mChart.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        mChart.axisRight.isEnabled = false
    }

    //barchat数据来源
    fun setBarData(mChart: BarChart, startYear: Int, groupCount: Int, labels: Array<String>, vararg values: List<String>) {
//        Log.e("-----","$startYear----------------")
        val groupSpace = 0.04f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.29f // x4 DataSet
        // (0.27 + 0.03) * 3 + 0.1 = 1.00 -> interval per "group"

        val yVals1 = java.util.ArrayList<BarEntry>()
        val yVals2 = java.util.ArrayList<BarEntry>()
        val yVals3 = java.util.ArrayList<BarEntry>()

        for (i in 1..groupCount) {
            yVals1.add(BarEntry(i.toFloat(), values[0][i - 1].toFloat()))
            yVals2.add(BarEntry(i.toFloat(), values[1][i - 1].toFloat()))
            yVals3.add(BarEntry(i.toFloat(), values[2][i - 1].toFloat()))
        }
        var set1: BarDataSet
        var set2: BarDataSet
        var set3: BarDataSet

        if (mChart.data != null && mChart.data.dataSetCount > 0) {

            set1 = mChart.data.getDataSetByIndex(0) as BarDataSet
            set2 = mChart.data.getDataSetByIndex(1) as BarDataSet
            set3 = mChart.data.getDataSetByIndex(2) as BarDataSet
            set1.values = yVals1
            set2.values = yVals2
            set3.values = yVals3
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()

        } else {
            // create 4 DataSets
            set1 = BarDataSet(yVals1, labels[0])
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(yVals2, labels[1])
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(yVals3, labels[2])
            set3.color = Color.rgb(220, 220, 158)

            val data = BarData(set1, set2, set3)
            data.setValueFormatter(LargeValueFormatter())
            mChart.data = data
        }

        // specify the width each bar should have
        mChart.barData.barWidth = barWidth

        // restrict the x-axis range
        mChart.xAxis.axisMinimum = startYear.toFloat()

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.xAxis.axisMaximum = startYear + mChart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
        mChart.groupBars(startYear.toFloat(), groupSpace, barSpace)
        mChart.invalidate()
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ map?.onResume() }, 1000)
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
        polygons.clear()
        waveLoadingView.endAnimation()
    }

    override fun onBackPressed() {
        if (sliding_layout != null && (sliding_layout.panelState === PanelState.EXPANDED || sliding_layout.panelState === PanelState.ANCHORED)) {
            sliding_layout.panelState = PanelState.COLLAPSED
        } else {
            super.onBackPressed()
        }
    }
}
