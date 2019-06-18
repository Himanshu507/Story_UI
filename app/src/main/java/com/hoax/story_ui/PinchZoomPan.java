package com.hoax.story_ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class PinchZoomPan extends View {


    private static final int INVALID_POINTER_ID = -1;
    private final static float mMinZoom = 0.3f;
    private final static float mMaxZoom = 5.0f;
    //Paint objects
    public ViewGroup.LayoutParams params;
    private boolean flag_paint = false;
    private Bitmap bitmap;
    private int mImageWidth, mImageHeight;
    private float mPositionX;
    private float mPositionY;
    private float mLastTouchX;
    private Path path = new Path();
    private Paint brush = new Paint();
    private int color = Color.WHITE;
    private float brushwidth = 10f;

    //Pinch Zoom in and out objects
    private float mLastTouchY;
    private int mActivePointerID = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    public PinchZoomPan(Context context) {
        super(context);
    }

    public PinchZoomPan(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

       /* brush.setAntiAlias(true);
        brush.setColor(color);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(6f);*/
        setBrushproperties();
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushwidth = pixelAmount;
        setBrushproperties();
    }

    public void setBrushColor(int color) {
        this.color = color;
        setBrushproperties();
    }

    public void setFlag_paint() {
        flag_paint = true;
    }

    public void setFlag_paintoff() {
        flag_paint = false;
    }

    public void setBrushproperties() {
        brush.setAntiAlias(true);
        brush.setColor(color);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(brushwidth);
        brush.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null && !flag_paint) {
            canvas.save();
            canvas.translate(mPositionX, mPositionY);
            canvas.scale(mScaleFactor, mScaleFactor);
            canvas.drawBitmap(bitmap, 0, 0, null);

        }
        if (flag_paint) {
            canvas.drawPath(path, brush);
            canvas.translate(0,0);
            canvas.scale(0,0);
            canvas.save();
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

        canvas.restore();
    }


    public void loadImageOnCanvas(Bitmap rotateBitmap) {

        float aspectRatio = (float) rotateBitmap.getHeight() / (float) rotateBitmap.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mImageWidth = displayMetrics.widthPixels;
        mImageHeight = Math.round(mImageWidth * aspectRatio);
        bitmap = rotateBitmap;
        invalidate();
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();
        //the scale gesture detector should inspect all the touch events
        mScaleDetector.onTouchEvent(event);

        final int action = event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                //Paint objects
                if (flag_paint) {
                    path.moveTo(pointX, pointY);
                }
                //get x and y coordinates of where we touch on the screen

                else {
                    final float x = event.getX();
                    final float y = event.getY();

                    //remember where touch event started

                    mLastTouchX = x;
                    mLastTouchY = y;

                    //save the ID of this pointer
                    mActivePointerID = event.getPointerId(0);
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (flag_paint) {
                    path.lineTo(pointX, pointY);
                } else {

                    //Find the index of the active pointer and find the position
                    final int pointerIndex = event.findPointerIndex(mActivePointerID);

                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);

                    if (!mScaleDetector.isInProgress()) {


                        final float distanceX = x - mLastTouchX;
                        final float distanceY = y - mLastTouchY;

                        mPositionX += distanceX;
                        mPositionY += distanceY;

                        //redraw canva call
                        invalidate();

                    }
                    //remember this touch event for next movement

                    mLastTouchX = x;
                    mLastTouchY = y;

                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerID = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerID = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                //Extract the index of the pointer the left the screen
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerID = event.getPointerId(pointerIndex);
                if (pointerID == mActivePointerID) {
                    //Our active pointer is going up terko nhi pta terko ab nind aarhi ha soo ja bas ab
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerID = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor *= detector.getScaleFactor();
            //don't to let the image get too small and large

            mScaleFactor = Math.max(mMinZoom, Math.min(mScaleFactor, mMaxZoom));

            invalidate();

            return true;
        }
    }
}
