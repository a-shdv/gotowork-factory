package com.example.coursework.visuals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class EraseView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint mPaint;
    private FaceQuestActivity parent;

    boolean isFirstDraw = true;
    boolean isClear = false;

    public void setParent(FaceQuestActivity parent){
        this.parent = parent;
    }

    public EraseView(Context context) {
        super(context);
        init(context);
    }

    void init(Context c) {
        context = c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(164);
    }

    public EraseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EraseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isFirstDraw) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            int step = 10;
            Random random = new Random();
            int eye_radius = 80;
            RectF oval = new RectF();

            int radius = 300;
            int center_x = 725;
            int center_y = 1000;

            //Left pouches
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            for (int i = 1; i < 3; i++) {
                oval.set(center_x - 50 - eye_radius * 2,
                        center_y - eye_radius,
                        center_x - 50,
                        center_y + eye_radius + 15 * i);
                mCanvas.drawArc(oval, 25, 130, false, paint);
            }

            //Right pouches
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            for (int i = 1; i < 3; i++) {
                oval.set(center_x + 60,
                        center_y - eye_radius,
                        center_x + 60 + eye_radius * 2,
                        center_y + eye_radius + 15 * i);
                mCanvas.drawArc(oval, 25, 130, false, paint);
            }

            isFirstDraw = false;
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);

        if (isClear) {
            parent.updateThePicture();
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                checkClearness();
                invalidate();
                break;
        }
        return true;
    }

    private void checkClearness() {
        this.setDrawingCacheEnabled(true);
        this.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        Bitmap b = Bitmap.createBitmap(this.getDrawingCache());
        Bitmap emptyBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
        if (b.sameAs(emptyBitmap)) {
            isClear = true;
        }
    }

    private boolean IsInTheBeard(int center_x, int center_y, int radius, int y, int x, int oval_radius) {

        if (Math.pow((x - center_x), 2) / Math.pow(radius, 2)
                + Math.pow((y - center_y), 2) / Math.pow(oval_radius, 2) >= 1) {
            if (Math.pow((x - center_x), 2) + Math.pow((y - center_y), 2) <= Math.pow(radius, 2)) {
                return true;
            }
        }
        return false;
    }
}

