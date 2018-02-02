package com.magiclon.unitstrokeproject.tools

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.magiclon.unitstrokeproject.R
import java.util.ArrayList

/**
 * 作者：MagicLon
 * 时间：2018/2/2 002
 * 邮箱：1348149485@qq.com
 * 描述：
 */
object ChartUtils{

    //饼图设置
    fun initPieChart(context: Context,mChart: PieChart, centerText: String,total:Int) {
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                InfoDialog(context, "总计：${total}户\n\n所占比例：${(e?.y!! * 100).toInt()}%\n\n${(total * e.y).toInt()}户")
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
    fun initBarChart(context: Context,mChart: BarChart) {
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
        val mv = MyMarkerView(context, R.layout.custom_marker_view)
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
    private fun setBarData(mChart: BarChart, startYear: Int, groupCount: Int, labels: Array<String>, vararg values: List<String>) {
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

    private fun initRadarChart(context: Context,mChart: RadarChart,total: Int,values: ArrayList<ArrayList<RadarEntry>>,titles:ArrayList<String>) {
        mChart.description.isEnabled = false

        mChart.webLineWidth = 1f
        mChart.webColor = Color.LTGRAY
        mChart.webLineWidthInner = 1f
        mChart.webColorInner = Color.LTGRAY
        mChart.webAlpha = 100
        mChart.animateY(1400)


        val xAxis = mChart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f

        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> titles[value.toInt() % titles.size] }
        xAxis.textColor = Color.GRAY

        val yAxis = mChart.yAxis
        yAxis.setLabelCount(6, true)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f
        yAxis.setDrawLabels(false)

        val l = mChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f
        l.textColor = Color.GRAY
        setRadarData(context ,mChart, total,values)
    }

    private fun setRadarData(context: Context,mChart: RadarChart, total: Int,values: ArrayList<ArrayList<RadarEntry>>) {

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = RadarMarkerView(context, R.layout.custom_marker_view, total)
        mv.chartView = mChart // For bounds control
        mChart.marker = mv // Set the marker to the chart

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        val set1 = RadarDataSet(values[0], "去年")
        set1.color = Color.rgb(103, 110, 129)
        set1.fillColor = Color.rgb(103, 110, 129)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)

        val set2 = RadarDataSet(values[1], "今年")
        set2.color = Color.rgb(121, 162, 175)
        set2.fillColor = Color.rgb(121, 162, 175)
        set2.setDrawFilled(true)
        set2.fillAlpha = 180
        set2.lineWidth = 2f
        set2.isDrawHighlightCircleEnabled = true
        set2.setDrawHighlightIndicators(false)

        val sets = java.util.ArrayList<IRadarDataSet>()
        sets.add(set1)
        sets.add(set2)

        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.GRAY)

        mChart.data = data
        mChart.invalidate()
    }

}