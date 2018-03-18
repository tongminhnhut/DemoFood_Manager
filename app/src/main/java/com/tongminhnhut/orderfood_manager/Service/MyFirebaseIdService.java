package com.tongminhnhut.orderfood_manager.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.model.Token;

/**
 * Created by tongminhnhut on 06/03/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateToServer(refreshedToken);
    }

    private void updateToServer(String refreshedToken) {
        FirebaseDatabase mData = FirebaseDatabase.getInstance();
        DatabaseReference tokens = mData.getReference("Tokens");
        Token data = new Token(refreshedToken, true); // false because token send from Client App
        tokens.child(Common.curentUser.getPhone()).setValue(data);
    }
}
