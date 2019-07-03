package com.hoax.story_ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoax.story_ui.base.BaseActivity;
import com.hoax.story_ui.filters.FilterListener;
import com.hoax.story_ui.filters.FilterViewAdapter;
import com.hoax.story_ui.tools.EditingToolsAdapter;
import com.hoax.story_ui.tools.ToolType;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class Post_edit extends BaseActivity implements OnPhotoEditorListener,
        PropertiesBSFragment.Properties,
        View.OnClickListener,
        EditingToolsAdapter.OnItemSelected, FilterListener {

    private static final String TAG = EditImageActivity.class.getSimpleName();
    ImageView imgClose;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private Typeface mWonderFont;
    private RecyclerView mRvFilters;
    private TextView next_Btn;
    private ImageView paint_btn, text_btn;
    //Views
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.post_edit);
        initViews();

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);


        mPropertiesBSFragment = new PropertiesBSFragment();
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] b = extras.getByteArray("capture");
        if (b != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            Bitmap rotateBitmap = rotate(bitmap);
            mPhotoEditorView.getSource().setImageBitmap(rotateBitmap);
            mPhotoEditorView.setFitsSystemWindows(true);
        }
        //Set Image Dynamically
    }

    private void initViews() {
        mPhotoEditorView = findViewById(R.id.photoEditorView_post);
        mRvFilters = findViewById(R.id.rvFilterView);
        next_Btn = findViewById(R.id.nextBtn);
        text_btn = findViewById(R.id.text_btn);
        paint_btn = findViewById(R.id.paint_post);
        mRootView = findViewById(R.id.rootView);
        imgClose = findViewById(R.id.close_btn);
        imgClose.setOnClickListener(this);
        mRvFilters.setVisibility(View.GONE);
        mPhotoEditorView.setOnTouchListener(new OnSwipeTouchListener(Post_edit.this) {
            public void onSwipeTop() {
                mRvFilters.setVisibility(View.GONE);
                next_Btn.setVisibility(View.VISIBLE);
                text_btn.setVisibility(View.VISIBLE);
                paint_btn.setVisibility(View.VISIBLE);
            }

            public void onSwipeRight() {
                next_Btn.setVisibility(View.GONE);
                text_btn.setVisibility(View.GONE);
                paint_btn.setVisibility(View.GONE);
                mRvFilters.setVisibility(View.VISIBLE);
               /* Handler handler = new Handler();
                Runnable delayrunnable = new Runnable() {
                    @Override
                    public void run() {
                        mRvFilters.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(delayrunnable, 1500);*/
                //showFilter(true);
            }

            public void onSwipeLeft() {
                Toast.makeText(Post_edit.this, "left", Toast.LENGTH_SHORT).show();
                next_Btn.setVisibility(View.GONE);
                text_btn.setVisibility(View.GONE);
                paint_btn.setVisibility(View.GONE);
                mRvFilters.setVisibility(View.VISIBLE);
               /* Handler handler = new Handler();
                Runnable delayrunnable = new Runnable() {
                    @Override
                    public void run() {
                        mRvFilters.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(delayrunnable, 1500);*/
               // showFilter(true);
            }

            public void onSwipeBottom() {
                mRvFilters.setVisibility(View.GONE);
                next_Btn.setVisibility(View.VISIBLE);
                text_btn.setVisibility(View.VISIBLE);
                paint_btn.setVisibility(View.VISIBLE);
                Toast.makeText(Post_edit.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);

                mPhotoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
        mRvFilters.setVisibility(View.GONE);
    }

    public void paintstar(View view) {
        mPhotoEditor.setBrushDrawingMode(true);
        mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());

    }

    public void textset(View view) {
        TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);

                mPhotoEditor.addText(inputText, styleBuilder);
            }
        });
    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);

                        mPhotoEditor.addText(inputText, styleBuilder);
                    }
                });
                break;

            case FILTER:
                showFilter(true);
                break;

        }
    }

    private Bitmap rotate(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        //Bitmap scaledbitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public void back_btn(View view) {
        onBackPressed();
    }

}
