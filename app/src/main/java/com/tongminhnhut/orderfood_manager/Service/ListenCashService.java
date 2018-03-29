package com.tongminhnhut.orderfood_manager.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.orderfood_manager.CashDetailActivity;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.R;
import com.tongminhnhut.orderfood_manager.TableActivity;
import com.tongminhnhut.orderfood_manager.model.Requests;
import com.tongminhnhut.orderfood_manager.model.Table_Status;

public class ListenCashService extends Service implements ChildEventListener {
    FirebaseDatabase mData ;
    DatabaseReference cash ;

    public ListenCashService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mData = FirebaseDatabase.getInstance();
        cash= mData.getReference("Table");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cash.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Table_Status request = dataSnapshot.getValue(Table_Status.class);
        showNoti(dataSnapshot.getKey(),request);

    }

    private void showNoti(String key, Table_Status request) {
        Intent intent = new Intent(getBaseContext(), TableActivity.class);
//        intent.putExtra("Detail",request.getPhone());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getBaseContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("PolarisVN")
                .setContentInfo("Update your order")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(Common.convertCodeCash(request.getStatus())+" bàn số " + key)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
