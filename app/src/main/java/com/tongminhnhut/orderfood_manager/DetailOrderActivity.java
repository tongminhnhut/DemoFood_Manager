package com.tongminhnhut.orderfood_manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.ViewHolder.OrderDetailAdapter;
import com.tongminhnhut.orderfood_manager.model.Foods;
import com.tongminhnhut.orderfood_manager.model.Requests;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailOrderActivity extends AppCompatActivity {
   FirebaseDatabase mData;
   DatabaseReference tabRequest ;
   TextView txtID, txtPhone, txtTotal , txtComment;
   String id_order_food = "";
   RecyclerView recyclerView ;
   RecyclerView.LayoutManager layoutManager ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fs.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_detail_order);
        mData = FirebaseDatabase.getInstance();
        tabRequest = mData.getReference("Foods");

        recyclerView = findViewById(R.id.recyler_OrderDetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtID = findViewById(R.id.txtID_OrderDetail);
        txtPhone =findViewById(R.id.txtPhone_OrderDetail);
        txtTotal = findViewById(R.id.txtTotal_OrderDetail);
        txtComment = findViewById(R.id.txtComment_OrderDetail);

        if (getIntent()!= null)
            id_order_food = getIntent().getStringExtra("data");

        // set Value
        txtID.setText("Bàn số "+Common.currentRequest.getAddress());
        txtPhone.setText(Common.currentRequest.getPhone());
        txtTotal.setText(Common.currentRequest.getTotal());
        txtComment.setText(Common.currentRequest.getNote());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);



    }


}
