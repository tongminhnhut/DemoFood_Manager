package com.tongminhnhut.orderfood_manager.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongminhnhut.orderfood_manager.R;
import com.tongminhnhut.orderfood_manager.model.Order;

import java.util.List;

/**
 * Created by tongminhnhut on 05/03/2018.
 */

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName, txtSoluong, txtPrice, txtDiscount;

    public MyViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName_OrderDetailItem);
        txtSoluong = itemView.findViewById(R.id.txtSoluong_OrderDetailItem);
        txtPrice = itemView.findViewById(R.id.txtPrice_OrderDetailItem);
        txtDiscount = itemView.findViewById(R.id.txtDiscount_OrderDetailItem);

    }
}

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {
    List<Order> listOrder ;

    public OrderDetailAdapter(List<Order> listOrder) {
        this.listOrder = listOrder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_order_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = listOrder.get(position);
        holder.txtName.setText(String.format("Name : %s ", order.getProductName()));
        holder.txtSoluong.setText(String.format("Quantity : %s ", order.getQuanlity()));
        holder.txtPrice.setText(String.format("Price : %s ", order.getPrice()));
        holder.txtDiscount.setText(String.format("Discount : %s ", order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}
