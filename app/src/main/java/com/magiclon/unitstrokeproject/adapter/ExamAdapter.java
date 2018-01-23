package com.magiclon.unitstrokeproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magiclon.unitstrokeproject.R;

/**
 * Created by MagicLon on 2018/01/16 首页统计示例 adapter
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
    private int[] colors = new int[]{
            Color.argb(250, 250, 5, 22),
            Color.argb(250, 155, 5, 251),
            Color.argb(250, 51, 5, 251),
            Color.argb(250, 5, 167, 251),
            Color.argb(250, 5, 237, 251),
            Color.argb(250, 171, 249, 239),
            Color.argb(250, 5, 251, 28),
            Color.argb(250, 251, 248, 5),
            Color.argb(250, 251, 150, 5)};

    public ExamAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_item_main_color.setBackgroundColor(colors[position]);
        holder.tv_item_main_count.setText((position / 10.0 + 1) + "万户");
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tv_item_main_color;
        public TextView tv_item_main_count;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.tv_item_main_color = rootView.findViewById(R.id.tv_item_main_color);
            this.tv_item_main_count = rootView.findViewById(R.id.tv_item_main_count);
        }
    }
}
