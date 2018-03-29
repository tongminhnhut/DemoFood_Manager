package com.tongminhnhut.orderfood_manager.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.R;

/**
 * Created by tongminhnhut on 01/03/2018.
 */

public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener
{
    public TextView txtBan, txtStatus, txtNgay, txtGio, txtCash_Status ;

    private ItemClickListener itemClickListener ;

    public TableViewHolder(View itemView) {
        super(itemView);

        txtBan = itemView.findViewById(R.id.txtBan_TableItem);
        txtStatus = itemView.findViewById(R.id.txtStatus_TableItem);
        txtNgay = itemView.findViewById(R.id.txtDate_TableItem);
        txtGio = itemView.findViewById(R.id.txtTime_TableItem);
        txtCash_Status = itemView.findViewById(R.id.txtThanhToan_TableItem);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Vui lòng chọn ");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
//        menu.add(0,1,getAdapterPosition(), Common.CASH);


    }
}
