package com.hoax.story_ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hoax.story_ui.views.CustumView;
import com.karan.churi.PermissionManager.PermissionManager;

public class MainActivity extends AppCompatActivity {

    private Button swap_Colors ;
    PermissionManager permissionManager;
    private CustumView custumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        Handler handler = new Handler();
        Runnable delayrunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainCamera.class);
                startActivity(i);
            }
        };
        handler.postDelayed(delayrunnable, 2500);

       /* custumView = findViewById(R.id.customview);

        swap_Colors = findViewById(R.id.swap_Color);
        swap_Colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custumView.swapColor();
            }
        });

*/
    }
}
