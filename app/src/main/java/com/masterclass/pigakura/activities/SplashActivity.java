package com.masterclass.pigakura.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.Users;

public class SplashActivity extends AppCompatActivity {
    private DatabaseReference dr;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        checkUser();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);*/


    }

    private void checkUser(){
        dr = FirebaseDatabase.getInstance().getReference("users").child(id);

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Users users = new Users(id, false);
                    dr.setValue(users);
                    //Toast.makeText(SplashActivity.this, "New user added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Users users = dataSnapshot.getValue(Users.class);

                    if (users.isHasVoted()){
                        Intent intent = new Intent(SplashActivity.this, VotingResult.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Log in error: " , databaseError.getMessage());
            }
        });

    }
}
