package com.tongminhnhut.orderfood_manager.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.R;

/**
 * Created by nhut on 2/22/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder
{
    public TextView txtTen, txtStatus, txtPhone, txtTime ;
    public Button btnEdit, btnRemove, btnDetail ;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtTen = (TextView) itemView.findViewById(R.id.txtName_OrderItem);
        txtStatus = (TextView) itemView.findViewById(R.id.txtStatus_OrderItem);
        txtPhone= (TextView) itemView.findViewById(R.id.txtPhone_OrderItem);
        txtTime = (TextView) itemView.findViewById(R.id.txtTime_OrderItem);

        btnDetail = itemView.findViewById(R.id.btnDetail_OrderItem);
        btnEdit = itemView.findViewById(R.id.btnEdit_OrderItem);
        btnRemove = itemView.findViewById(R.id.btnRemove_OrderItem);

    }


}
