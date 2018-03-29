package com.tongminhnhut.orderfood_manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.Interface.ItemClickListener;
import com.tongminhnhut.orderfood_manager.Remote.APIService;
import com.tongminhnhut.orderfood_manager.ViewHolder.OrderViewHolder;
import com.tongminhnhut.orderfood_manager.model.MyResponse;
import com.tongminhnhut.orderfood_manager.model.Notification;
import com.tongminhnhut.orderfood_manager.model.Requests;
import com.tongminhnhut.orderfood_manager.model.Sender;
import com.tongminhnhut.orderfood_manager.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderStatusActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase mData ;
    DatabaseReference tab_request ;

    FirebaseRecyclerAdapter<Requests, OrderViewHolder> adapter ;

    MaterialSpinner spinner ;
    APIService mService ;

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


        setContentView(R.layout.activity_order_status);
        mData = FirebaseDatabase.getInstance();
        tab_request = mData.getReference("Requests");
        mService = Common.getFCMService();

        recyclerView = findViewById(R.id.recylerOrderStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrder();



    }

    private void loadOrder() {
        FirebaseRecyclerOptions<Requests> options = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(tab_request, Requests.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder viewHolder, final int position, @NonNull final Requests model) {
                viewHolder.txtTen.setText("Bàn số " +model.getAddress());
                viewHolder.txtStatus.setText(Common.convertCodeStatus(""+model.getStatus()));
                viewHolder.txtPhone.setText(model.getPhone()+"");
                viewHolder.txtTime.setText(model.getTime()+"");

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.btnEdit.setText("Complete");
                        adapter.getItem(position).setStatus("1");
                        adapter.notifyDataSetChanged(); // Them vao để cập nhật item size

                        tab_request.child(adapter.getRef(position).getKey()).setValue(adapter.getItem(position));

                        sendOrderStatusToUser(adapter.getRef(position).getKey(),adapter.getItem(position));

//                        showUpDateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));

                    }
                });

                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFood(adapter.getRef(position).getKey());

                    }
                });

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DetailOrderActivity.class);
                        Common.currentRequest = model;
                        intent.putExtra("data", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(view);
            }
        };


        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getTitle().equals(Common.UPDATE)){
//            showUpDateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
//        }
//        else if (item.getTitle().equals(Common.DELETE)){
//            deleteFood(adapter.getRef(item.getOrder()).getKey());
//
//        }
//        return super.onContextItemSelected(item);
//    }

    private void deleteFood(String key) {
        tab_request.child(key).removeValue();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    private void showUpDateDialog(final String key, final Requests item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = view.findViewById(R.id.spinnerStatus);
//        spinner.setItems("Đang làm","Hoàn thành");

        alertDialog.setView(view);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                item.setStatus("Complete");
                adapter.notifyDataSetChanged(); // Them vao để cập nhật item size

                tab_request.child(key).setValue(item);

                sendOrderStatusToUser(key,item);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void sendOrderStatusToUser(final String key, final Requests item) {
        DatabaseReference tokens = mData.getReference("Tokens");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postDataSnapShot:dataSnapshot.getChildren()){
                            Token token = postDataSnapShot.getValue(Token.class);

                            //Make raw payload
                            Notification notification = new Notification("Polaris VN", "Your order "+key+"was updated");
                            Sender content = new Sender(token.getToken(), notification);
                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.body().success==1){
                                                Toast.makeText(OrderStatusActivity.this, "Order was upadted", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(OrderStatusActivity.this, "order wa updated but failed to send notification !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
