package com.tongminhnhut.orderfood_manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tongminhnhut.orderfood_manager.model.User;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {
    MaterialEditText edtPhone, edtName, edtPass ;
    FButton btnSigup;
    DatabaseReference user ;

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


        setContentView(R.layout.activity_sign_up);

        user = FirebaseDatabase.getInstance().getReference("User");

        edtName = findViewById(R.id.edtFullName_SignUp);
        edtPhone = findViewById(R.id.edtPhoneNumber_SignUp);
        edtPass = findViewById(R.id.edtPass_SignUp);
        btnSigup = findViewById(R.id.btnSignUp_SignUp);

        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage("Loading . . .");
                dialog.show();
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone number already ragister!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            User user1 = new User(edtName.getText().toString().trim(),
                                    edtPass.getText().toString().trim()
                                    );
                            user.child(edtPhone.getText().toString()).setValue(user1);
                            Toast.makeText(SignUpActivity.this, "Sign Up successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



    }
}
