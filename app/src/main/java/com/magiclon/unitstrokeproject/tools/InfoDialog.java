package com.magiclon.unitstrokeproject.tools;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.magiclon.unitstrokeproject.R;


/**
 * @author MagicLon
 * @data: 2018/1/23 10:29
 * @email： 1348149485@qq.com
 * @version: v1.0
 */
public class InfoDialog {
    private Context context;
    private String content;
    private Dialog dialog;
    private TextView tv_content, tv_center;
    private CardView card;

    public InfoDialog(Context context, String content) {
        this.context = context;
        this.content = content;
        if (context != null) {
            showDialog();
        }
    }

    private void showDialog() {
        View dialogv = LayoutInflater.from(context).inflate(R.layout.dialog_customdialog, null);
        dialog = new Dialog(context, R.style.customdialog);
        dialog.setContentView(dialogv);
        tv_center = dialogv.findViewById(R.id.customdialog_center);
        tv_content = dialogv.findViewById(R.id.customdialog_content);
        card = dialogv.findViewById(R.id.card);
        tv_content.setText(content);

        tv_center.setOnClickListener(view -> dialog.dismiss());
        dialog.setOnShowListener(dialogInterface -> {
            AnimatorSet animatorset=new AnimatorSet();
            animatorset.playTogether(
                    ObjectAnimator.ofFloat(card, "scaleX", 0.1f, 0.5f, 1).setDuration(500),
                    ObjectAnimator.ofFloat(card, "scaleY", 0.1f, 0.5f, 1).setDuration(500),
                    ObjectAnimator.ofFloat(card, "alpha", 0, 1).setDuration(500)
            );
            animatorset.start();
        });
        dialog.show();
    }

}
