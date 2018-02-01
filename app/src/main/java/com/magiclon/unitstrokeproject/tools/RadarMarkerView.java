
package com.magiclon.unitstrokeproject.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.magiclon.unitstrokeproject.R;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class RadarMarkerView extends MarkerView {

    private TextView tvContent;
    //    private DecimalFormat format = new DecimalFormat("##0");
    int total;

    public RadarMarkerView(Context context, int layoutResource, int total) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        this.total = total;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText((int) (e.getY() / 100 * total) + " 户");

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}
