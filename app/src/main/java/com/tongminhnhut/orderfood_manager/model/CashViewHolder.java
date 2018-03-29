package com.tongminhnhut.orderfood_manager.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tongminhnhut.orderfood_manager.R;


/**
 * Created by tongminhnhut on 09/03/2018.
 */

public class CashViewHolder extends RecyclerView.ViewHolder  {
    public TextView txtBan, txtPhone, txtTotal, txtStatus, txtDate;
    public Button btnDetail_Cash, btnXoa;


    public CashViewHolder(View itemView) {
        super(itemView);
        txtBan = (TextView) itemView.findViewById(R.id.txtBan_CachItem);
        txtStatus = (TextView) itemView.findViewById(R.id.txtStatus_CashItem);
        txtPhone = (TextView) itemView.findViewById(R.id.txtphone_CashItem);
        txtTotal = (TextView) itemView.findViewById(R.id.txtPrice_CashItem);
        txtDate = itemView.findViewById(R.id.txtDate_CashItem);
        btnDetail_Cash = itemView.findViewById(R.id.btnDetail_CashItem);
        btnXoa = itemView.findViewById(R.id.btnXoa_CashItem);
    }
}
