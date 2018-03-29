package com.tongminhnhut.orderfood_manager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.ViewHolder.CashAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CashDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    FirebaseDatabase mData ;
    DatabaseReference tab_Request ;
    TextView txtBan, txtStatus, txtTotal ;

    String id = "";

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
        setContentView(R.layout.activity_cash_detail);
        mData = FirebaseDatabase.getInstance();
        tab_Request = mData.getReference("foods");

        recyclerView = findViewById(R.id.recyler_CashDetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtBan = findViewById(R.id.txtBan_CashDetail);
        txtStatus = findViewById(R.id.txtStatus_CashDetail);
        txtTotal = findViewById(R.id.txtTotal_CashDetail);

        if (getIntent()!= null)
            id = getIntent().getStringExtra("Detail");

        //setValue
        txtBan.setText("Bàn số "+ Common.currentRequest.getAddress());
        txtStatus.setText(Common.convertCodeStatus(Common.currentRequest.getStatus()));
        txtTotal.setText(Common.currentRequest.getTotal());

        CashAdapter adapter = new CashAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}
