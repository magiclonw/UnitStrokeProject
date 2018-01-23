package com.magiclon.unitstrokeproject.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
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
import com.magiclon.unitstrokeproject.R
import com.magiclon.unitstrokeproject.adapter.NextUnitAdapter
import com.magiclon.unitstrokeproject.db.MyDb
import com.magiclon.unitstrokeproject.db.UnitInfoBean
import com.magiclon.unitstrokeproject.tools.InfoDialog
import com.magiclon.unitstrokeproject.tools.MyMarkerView
import kotlinx.android.synthetic.main.activity_next_unit.*
import kotlinx.android.synthetic.main.main_dragview.*
import me.itangqi.waveloadingview.WaveLoadingView
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * 下一级数据信息
 */
class NextUnitActivity : AppCompatActivity() {
    private var db: MyDb? = null
    var depname = ""//区划名称
    var unit_id = ""//区划id
    var total = 0//当前一级户口数量
    var total_pre = 0//上一级户数量
    var mulriple = 1//倍数
    private var year = Calendar.getInstance().get(Calendar.YEAR) - 4//开始年份
    protected var mParties = arrayOf("农村", "城镇")
    protected var mBarlabels = arrayOf("农业", "非农", "总计")
    protected var mParties_gander = arrayOf("男", "女")
    protected var mParties_type = arrayOf("新申请", "现享受")
    private var values_hk = arrayOf("0.456", "0.534")
    private var values_gander = arrayOf("0.539", "0.461")
    private var values_type = arrayOf("0.012", "0.988")
    private var values_country = arrayOf("452", "435", "467", "487", "497")
    private var values_city = arrayOf("552", "565", "577", "587", "595")
    private var values_total = arrayOf("1020", "1030", "1040", "1070", "1090")
    private var nextadapter: NextUnitAdapter? = null
    private var unitinfos = java.util.ArrayList<UnitInfoBean>()
    private var subscription: Subscription? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_unit)
        ImmersionBar.with(this).titleBar(toolbar, false).transparentBar()?.fullScreen(false)?.navigationBarColor(R.color.white)?.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)?.statusBarDarkFont(true, 0.2f)?.init()
        depname = intent.extras.getString("depname")
        unit_id = intent.extras.getString("unit_id")
        total_pre = intent.extras.getInt("total_cur")
        db = MyDb(this)
        initEvents()
        initData()
    }

    private fun initEvents() {
        iv_next_back.setOnClickListener { super.onBackPressed() }
        nextadapter = NextUnitAdapter(unitinfos, 2)
        rv_qulist.layoutManager = LinearLayoutManager(this)
        rv_qulist.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv_qulist.itemAnimator = DefaultItemAnimator()
        rv_qulist.adapter = nextadapter
        nextadapter?.setOnItemClickListener { _, position ->
            var intent = Intent(this, NextUnitActivity::class.java)
            intent.putExtra("unit_id", unitinfos[position].unit_id)
            intent.putExtra("depname", unitinfos[position].depname)
            intent.putExtra("total_cur", total)
            startActivity(intent)
        }
        iv_next_more.setOnClickListener {
            var intent = Intent(this, PeopleListActivity::class.java)
            intent.putExtra("unit_id",unit_id)
            intent.putExtra("depname", depname)
            startActivity(intent)
        }
    }

    private fun initData() {
        tv_next_title.text = depname
        if (unit_id.length < 12) {
            rv_qulist.visibility = View.VISIBLE
            ll_main_unit.visibility = View.VISIBLE
            total = (800 + Math.random() * 1000).toInt()
            mulriple = 1
            initChartData()
            initListData()
        } else {
            iv_next_more.visibility = View.VISIBLE
            total = (80 + Math.random() * 100).toInt()
            mulriple = 10
            initChartData()
        }
    }

    //加载列表信息
    private fun initListData() {
        subscription = Observable.create(Observable.OnSubscribe<ArrayList<UnitInfoBean>> { t ->
            t.onNext(db?.getUnitNext(unit_id) as java.util.ArrayList<UnitInfoBean>)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    unitinfos.clear()
                    unitinfos.addAll(t)
                    nextadapter?.notifyDataSetChanged()
                    subscription?.unsubscribe()
                }
    }

    //加载图表数据
    fun initChartData() {
        waveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE)
        waveLoadingView.setAnimDuration(4000)
        waveLoadingView.centerTitle = "${total}户"
        var percent = (total * 100.0 / total_pre).toInt()
        waveLoadingView.bottomTitle = "$percent%"
        waveLoadingView.progressValue = percent + 55
        initPieChart(mChart_hukou, "低保类型统计")//低保类型
        initPieChart(mChart_gander, "男女比例统计")//男女比例
        initPieChart(mChart_type, "享受类型统计")//享受类型比例
        initBarChart(mChart_year)
        setPieData(mChart_hukou, mParties, values_hk.toList())
        setPieData(mChart_gander, mParties_gander, values_gander.toList())
        setPieData(mChart_type, mParties_type, values_type.toList())
        setBarData(mChart_year, year, 5, mBarlabels, values_country.toList(), values_city.toList(), values_total.toList())
    }

    //饼图设置
    fun initPieChart(mChart: PieChart, centerText: String) {
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                InfoDialog(this@NextUnitActivity, "总计：${total}户\n\n所占比例：${(e?.y!! *100).toInt()}%\n\n${(total* e.y).toInt()}户")
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
//        mChart.setEntryLabelTypeface(mTfRegular)
        mChart.setEntryLabelTextSize(12f)
    }

    //饼图数据来源
    private fun setPieData(mChart: PieChart, parties: Array<String>, values: List<String>) {
        val entries = ArrayList<PieEntry>()
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        (0 until parties.size).mapTo(entries) {
            PieEntry(values[it % values.size].toFloat(),
                    parties[it % parties.size])
        }
        val dataSet = PieDataSet(entries, " ")
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
        val groupSpace = 0.04f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.29f // x4 DataSet
        // (0.27 + 0.03) * 3 + 0.1 = 1.00 -> interval per "group"

        val yVals1 = java.util.ArrayList<BarEntry>()
        val yVals2 = java.util.ArrayList<BarEntry>()
        val yVals3 = java.util.ArrayList<BarEntry>()

        for (i in 1..groupCount) {
            yVals1.add(BarEntry(i.toFloat(), values[0][i - 1].toFloat() / mulriple))
            yVals2.add(BarEntry(i.toFloat(), values[1][i - 1].toFloat() / mulriple))
            yVals3.add(BarEntry(i.toFloat(), values[2][i - 1].toFloat() / mulriple))
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

    override fun onDestroy() {
        super.onDestroy()
        waveLoadingView.endAnimation()
    }
}