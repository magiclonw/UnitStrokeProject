package com.magiclon.unitstrokeproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magiclon.unitstrokeproject.R;
import com.magiclon.unitstrokeproject.db.UnitInfoBean;

import java.util.List;

/**
 * Created by MagicLon on 2018/01/16 首页统计示例 adapter
 */
public class NextUnitAdapter extends RecyclerView.Adapter<NextUnitAdapter.ViewHolder> {
    List<UnitInfoBean> list;
    int type;

    public NextUnitAdapter(List<UnitInfoBean> list, int type) {
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nextunit, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(view1 -> {
            int pos = vh.getLayoutPosition();
            mOnItemClickListener.onItemClick(vh.itemView, pos);
        });
        return vh;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_item_nextunit_name.setText(list.get(position).getDepname());
        if (type == 1) {
            int total = (int) (800 + Math.random() * 1000);
            int country = (int) (300 + Math.random() * 1000);
            holder.tv_item_nextunit_count.setText("共 " + total + " 户");
            holder.tv_item_nextunit_contrycount.setText("农业 " + country + " 户");
            holder.tv_item_nextunit_citycount.setText("非农 " + (total - country) + " 户");
        } else {
            int total = (int) (80 + Math.random() * 100);
            int country = (int) (30 + Math.random() * 100);
            holder.tv_item_nextunit_count.setText("共 " + total + " 户");
            holder.tv_item_nextunit_contrycount.setText("农业 " + country + " 户");
            holder.tv_item_nextunit_citycount.setText("非农 " + (total - country) + " 户");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tv_item_nextunit_name;
        public TextView tv_item_nextunit_count;
        public TextView tv_item_nextunit_contrycount;
        public TextView tv_item_nextunit_citycount;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.tv_item_nextunit_name = rootView.findViewById(R.id.tv_item_nextunit_name);
            this.tv_item_nextunit_count = rootView.findViewById(R.id.tv_item_nextunit_count);
            this.tv_item_nextunit_contrycount = rootView.findViewById(R.id.tv_item_nextunit_contrycount);
            this.tv_item_nextunit_citycount = rootView.findViewById(R.id.tv_item_nextunit_citycount);
        }
    }
}
