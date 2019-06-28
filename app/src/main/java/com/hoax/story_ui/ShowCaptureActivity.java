package com.hoax.story_ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowCaptureActivity extends AppCompatActivity {

    protected PinchZoomPan pinchZoomPan;
    private static final int REQUEST_CODE = 43;
    RecyclerView list;
    float smallbrush, mediumbrush, largebrush;
    List<Integer> colors = new ArrayList<Integer>();
    LinearLayout linearLayout;
    ImageView white, yellow, purple, black, orange, red, cyan, grey;
    private ImageView brush;
    File file;
    Uri fileuri;

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

       /* linearLayout = findViewById(R.id.penColors);
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
        //list = findViewById(R.id.colors_recycle);*/

    }


    public void PainColorchooser(View view) {
    }

    private void startSearch(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data!=null){
                Uri uri = data.getData();
                fileuri = uri;
                file = new File(uri.getPath());
            }
        }

    }


    private Bitmap rotate(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

    }

    public void UploadImage(View view) {
        /*PinchZoomPan pinchZoomPan = new PinchZoomPan(this);
        pinchZoomPan.setBrushSize(15f);
        pinchZoomPan.setBrushColor(Color.CYAN);
        pinchZoomPan.setFlag_paint();*/
        startSearch();
       // Toast.makeText(this, "Something hits", Toast.LENGTH_LONG).show();
    }

    public void cancel(View view) {
        onBackPressed();
    }

    public void penChooser(View view){
        pinchZoomPan.setFlag_paint();
    }

}
