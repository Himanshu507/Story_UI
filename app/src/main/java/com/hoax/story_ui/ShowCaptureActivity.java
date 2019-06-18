package com.hoax.story_ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowCaptureActivity extends AppCompatActivity {

    protected PinchZoomPan pinchZoomPan;
    RecyclerView list;
    float smallbrush, mediumbrush, largebrush;
    List<Integer> colors = new ArrayList<Integer>();
    private ImageView brush;
    LinearLayout linearLayout;
    ImageView white,yellow,purple,black,orange,red,cyan,grey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_capture);

        pinchZoomPan = findViewById(R.id.capturedimage);
        brush = findViewById(R.id.pen);

        initview();

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] b = extras.getByteArray("capture");
        if (b != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            Bitmap rotateBitmap = rotate(bitmap);
            pinchZoomPan.loadImageOnCanvas(rotateBitmap);
        }
    }

    private void initview() {


        String[] colorsTxt = getApplicationContext().getResources().getStringArray(R.array.colors);
        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            colors.add(newColor);
        }

        linearLayout = findViewById(R.id.penColors);
        cyan = findViewById(R.id.colors_Cyan);
        cyan.setBackgroundColor(colors.get(2));
        orange = findViewById(R.id.colors_Orange);
        orange.setBackgroundColor(colors.get(5));
        purple = findViewById(R.id.colors_Purple);
        purple.setBackgroundColor(colors.get(8));
        red = findViewById(R.id.colors_Red);
        red.setBackgroundColor(colors.get(9));
        white = findViewById(R.id.colors_white);
        white.setBackgroundColor(colors.get(0));
        yellow = findViewById(R.id.colors_Yellow);
        yellow.setBackgroundColor(colors.get(4));
        black = findViewById(R.id.colors_black);
        black.setBackgroundColor(colors.get(1));
        grey = findViewById(R.id.colors_grey);
        grey.setBackgroundColor(colors.get(11));
        //list = findViewById(R.id.colors_recycle);

    }


    public void PainColorchooser(View view) {
        linearLayout.setVisibility(View.VISIBLE);
    }


    private Bitmap rotate(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

    }

    public void Downloades(View view) {
        PinchZoomPan pinchZoomPan = new PinchZoomPan(this);
        pinchZoomPan.setBrushSize(15f);
        pinchZoomPan.setBrushColor(Color.CYAN);
        pinchZoomPan.setFlag_paint();
        Toast.makeText(this,"Something hits",Toast.LENGTH_LONG).show();
    }

    public void cancel(View view) {
        onBackPressed();
    }
}
