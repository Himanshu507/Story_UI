package com.hoax.story_ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.karan.churi.PermissionManager.PermissionManager;
public class MainActivity extends AppCompatActivity {

    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        Handler handler = new Handler();
        Runnable delayrunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainCamera.class);
                startActivity(i);
                finish();
            }
        };
        handler.postDelayed(delayrunnable, 500);

    }
}