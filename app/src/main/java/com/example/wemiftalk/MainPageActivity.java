package com.example.wemiftalk;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button mLogout = findViewById(R.id.logout);
        Button mFindUser = findViewById(R.id.findUser);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);// wyjscie do ekranu startowego
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//wyczyszczenie informacji z inych activity
                startActivity(intent);
                finish();
                return;
            }
        });

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
            }
        });

        getPermisions();
    }

    private void getPermisions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//tylko w niektórych wersjach androida
            requestPermissions(new String[]{
                Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);//pytanie o dostęp o kontaktów
        }
    }
}
