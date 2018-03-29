package com.tongminhnhut.orderfood_manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.ViewHolder.TableViewHolder;
import com.tongminhnhut.orderfood_manager.model.Table_Status;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TableActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;
    FirebaseRecyclerAdapter<Table_Status, TableViewHolder> adapter ;
    FirebaseDatabase mData ;
    DatabaseReference table ;
    MaterialSpinner spinner ;
    ImageButton btnDate, btnTime ;
    TextView txtDate, txtTime ;
    View view ;


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

        setContentView(R.layout.activity_table);

        mData = FirebaseDatabase.getInstance() ;
        table = mData.getReference("Table");

        recyclerView = findViewById(R.id.recylerTab);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadTable();

    }

    private void loadTable() {
        FirebaseRecyclerOptions<Table_Status> options = new FirebaseRecyclerOptions.Builder<Table_Status>()
                .setQuery(table, Table_Status.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Table_Status, TableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TableViewHolder viewHolder, int position, @NonNull Table_Status model) {
                viewHolder.txtBan.setText("Bàn số "+adapter.getRef(position).getKey());
                viewHolder.txtStatus.setText(Common.convertCodeStatus(model.getStatus()+""));
                viewHolder.txtNgay.setText(model.getNgay()+"");
                viewHolder.txtGio.setText(model.getGio()+"");
                viewHolder.txtCash_Status.setText(Common.convertCodeCash(model.getCash_Status()));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getApplicationContext(), CashActivity.class);
//                        Common.currentRe = model ;
                        intent.putExtra("Table", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);

                return new TableViewHolder(item);
            }
        };


        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
