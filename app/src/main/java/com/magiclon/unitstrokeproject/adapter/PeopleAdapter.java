package com.magiclon.unitstrokeproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.magiclon.unitstrokeproject.R;

/**
 * Created by MagicLon on 2018/01/22 人员列表 adapter
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private String[] names = {"*党", "*学光", "*国韦", "*兴", "*回", "*想", "*光", "*思纯", "*惜", "*宏伟"};
    private String[] type_hk = {"农业", "非农业"};
    private int[] heads = {R.mipmap.head, R.mipmap.head1, R.mipmap.head2,R.mipmap.head3};

    private int type = 0;

    public PeopleAdapter(int type) {
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peoplelist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (type == 1) {
            holder.iv_peoplelist_neworxs.setImageResource(R.mipmap.newin);
        } else {
            holder.iv_peoplelist_neworxs.setImageResource(R.mipmap.xiangshou);
        }
        holder.iv_peoplelist_head.setImageResource(heads[(int) (Math.random() * 4)]);
        holder.tv_peoplelist_info.setText("姓名：" + names[(int) (Math.random() * 10)] + " 　　身份证号：150102***" + (int) (Math.random() * 4000 + 1000));
        holder.tv_peoplelist_money.setText("低保金额：" + (int) (Math.random() * 1000) + "元");
        holder.tv_peoplelist_type.setText(type_hk[(int) (Math.random() * 2)]);
        holder.tv_peoplelist_xstime.setText("享受时间：201" + (int) (Math.random() * 8) + "-" + (int) (Math.random() * 12) + "-" + (int) (Math.random() * 29));
        holder.tv_peoplelist_rh.setText("最近入户：2018-1-" + (int) (Math.random() * 29));
    }

    @Override
    public int getItemCount() {
        return 30;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView iv_peoplelist_neworxs;
        public ImageView iv_peoplelist_head;
        public TextView tv_peoplelist_info;
        public TextView tv_peoplelist_money;
        public TextView tv_peoplelist_type;
        public TextView tv_peoplelist_xstime;
        public TextView tv_peoplelist_rh;


        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.iv_peoplelist_neworxs = rootView.findViewById(R.id.iv_peoplelist_neworxs);
            this.iv_peoplelist_head = rootView.findViewById(R.id.iv_peoplelist_head);
            this.tv_peoplelist_info = rootView.findViewById(R.id.tv_peoplelist_info);
            this.tv_peoplelist_money = rootView.findViewById(R.id.tv_peoplelist_money);
            this.tv_peoplelist_type = rootView.findViewById(R.id.tv_peoplelist_type);
            this.tv_peoplelist_xstime = rootView.findViewById(R.id.tv_peoplelist_xstime);
            this.tv_peoplelist_rh = rootView.findViewById(R.id.tv_peoplelist_rh);
        }
    }
}
