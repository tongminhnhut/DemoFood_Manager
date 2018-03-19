package com.tongminhnhut.orderfood_manager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;
import com.tongminhnhut.orderfood_manager.Common.Common;
import com.tongminhnhut.orderfood_manager.model.User;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInActivity extends AppCompatActivity {
    MaterialEditText edtPhone, edtPass;
    Button btnSignIn ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference tab_user;
    CheckBox cb ;


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


        setContentView(R.layout.activity_sign_in);
        firebaseDatabase = FirebaseDatabase.getInstance();
        tab_user = firebaseDatabase.getReference("User");
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhonenumber_SignIp);
        edtPass = (MaterialEditText) findViewById(R.id.edtPass_SignIp);
        btnSignIn = findViewById(R.id.btnSignIn_SignIp);

        //init Checkbox
        cb= findViewById(R.id.cbRemember);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                finish();
            }
        });
    }

    private void signInUser() {
        if (cb.isChecked()){
            Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
            Paper.book().write(Common.PMW_KEY, edtPass.getText().toString());
        }
        final AlertDialog dialog = new SpotsDialog(SignInActivity.this);
        dialog.show();
        tab_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(edtPhone.getText().toString()).exists()){
                    dialog.dismiss();
                    User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                    user.setPhone(edtPhone.getText().toString());
                    if (Boolean.parseBoolean(user.getIsStaff())){
//                        System.out.print(user.getPass().equals(pass));
                        if (user.getPassword().equals(edtPass.getText().toString().trim())) // Các thuộc tính trong class và trên Firebase phải giống nhau
                        {
                            Common.curentUser = user;
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                            Toast.makeText(SignInActivity.this, "Complete !", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SignInActivity.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignInActivity.this, "Pleas login with STAFF account !", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(SignInActivity.this, "User not exist !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("AAA", databaseError.toString());
            }
        }) ;
    }


}
