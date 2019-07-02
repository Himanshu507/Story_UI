package com.hoax.story_ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowCaptureActivity extends AppCompatActivity {

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private static final int REQUEST_CODE = 43;
    protected PinchZoomPan pinchZoomPan;
    List<Integer> colors = new ArrayList<Integer>();
    File file;
    Uri fileuri;
    ImageView upload;
    Bitmap bitmap;
    String selectedImagePath;
    private ImageView brush;
    private Intent pictureActionIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_capture);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            }
        });

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

    }


    public void PainColorchooser(View view) {
    }

    private void startSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
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

    public void penChooser(View view) {
        pinchZoomPan.setFlag_paint();
    }

   /* protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pinchZoomPan.loadImageOnCanvas(bitmap);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pinchZoomPan.loadImageOnCanvas(bitmap);
                }
                break;
        }
    }
*/
}

