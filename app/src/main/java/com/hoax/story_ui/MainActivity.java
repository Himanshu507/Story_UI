package com.hoax.story_ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hoax.story_ui.views.CustumView;
import com.karan.churi.PermissionManager.PermissionManager;
import com.rm.freedrawview.FreeDrawView;
import com.rm.freedrawview.PathDrawnListener;
import com.rm.freedrawview.PathRedoUndoCountChangeListener;
import com.rm.freedrawview.ResizeBehaviour;

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