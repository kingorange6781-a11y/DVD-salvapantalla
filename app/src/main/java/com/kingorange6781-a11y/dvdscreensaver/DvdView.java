package com.tuusuario.dvdscreensaver;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Random;

public class DvdView extends View {

    private static final float MIN_SPEED = 1.5f;
    private static final float MAX_SPEED = 12f;
    private static final int MIN_DARKNESS = 0;
    private static final int MAX_DARKNESS = 80;
    private static final float BASE_RECT_WIDTH = 350f;
    private static final float BASE_RECT_HEIGHT = 175f;

    private float posX = 100f, posY = 100f;
    private float velX = 4f, velY = 4f;
    private int rectWidth = 350;
    private int rectHeight = 175;
    private float sizeScale = 1f;
    private float speedScale = 1f;

    private final Paint paint;
    private final Paint overlayPaint;
    private Bitmap dvdBitmap;
    private int currentColor;
    private int darkness = MAX_DARKNESS;
    private final Handler handler;
    private final Runnable runnable;
    private final Random random;

    private final int[] colors = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.WHITE, Color.rgb(255, 165, 0)
    };

    public DvdView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        overlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        overlayPaint.setColor(Color.BLACK);
        random = new Random();
        currentColor = colors[random.nextInt(colors.length)];

        SharedPreferences prefs = context.getSharedPreferences("dvd_settings", Context.MODE_PRIVATE);
        sizeScale = clampScale(prefs.getFloat("size_scale", 1f));
        speedScale = clampSpeed(prefs.getFloat("speed_scale", 1f));
        darkness = clampDarkness(prefs.getInt("darkness", MAX_DARKNESS));

        applySize(sizeScale);
        applySpeed(speedScale);

        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dvd);
        if (rawBitmap != null) {
            dvdBitmap = Bitmap.createScaledBitmap(rawBitmap, rectWidth, rectHeight, true);
        }

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                updatePosition();
                invalidate();
                handler.postDelayed(this, 16);
            }
        };
    }

    public void startAnimation() {
        handler.post(runnable);
    }

    public void stopAnimation() {
        handler.removeCallbacks(runnable);
    }

    public void setDarkness(int value) {
        darkness = clampDarkness(value);
        invalidate();
    }

    public int getDarkness() {
        return darkness;
    }

    public void adjustDarkness(int delta) {
        setDarkness(darkness + delta);
    }

    public void setSizeScale(float scale) {
        sizeScale = clampScale(scale);
        applySize(sizeScale);
    }

    public void setSpeedScale(float scale) {
        speedScale = clampSpeed(scale);
        applySpeed(speedScale);
    }

    public void adjustSpeed(float delta) {
        setSpeedScale(speedScale + delta);
    }

    public void toggleColor() {
        int newColor;
        do {
            newColor = colors[random.nextInt(colors.length)];
        } while (newColor == currentColor);
        currentColor = newColor;
    }

    private void applySize(float scale) {
        rectWidth = Math.round(BASE_RECT_WIDTH * scale);
        rectHeight = Math.round(BASE_RECT_HEIGHT * scale);
        if (dvdBitmap != null) {
            dvdBitmap = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.dvd),
                    rectWidth,
                    rectHeight,
                    true
            );
        }
        invalidate();
    }

    private void applySpeed(float scale) {
        float magnitude = 4f * scale;
        if (magnitude < MIN_SPEED) {
            magnitude = MIN_SPEED;
        }
        if (magnitude > MAX_SPEED) {
            magnitude = MAX_SPEED;
        }

        velX = Math.copySign(magnitude, velX == 0 ? 1f : velX);
        velY = Math.copySign(magnitude, velY == 0 ? 1f : velY);
    }

    private float clampScale(float value) {
        return Math.max(0.4f, Math.min(2.2f, value));
    }

    private float clampSpeed(float value) {
        return Math.max(0.5f, Math.min(2.5f, value));
    }

    private int clampDarkness(int value) {
        return Math.max(MIN_DARKNESS, Math.min(MAX_DARKNESS, value));
    }

    private void updatePosition() {
        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return;

        posX += velX;
        posY += velY;

        boolean hitWall = false;

        if (posX <= 0) {
            posX = 0;
            velX = -velX;
            hitWall = true;
        } else if (posX + rectWidth >= width) {
            posX = width - rectWidth;
            velX = -velX;
            hitWall = true;
        }

        if (posY <= 0) {
            posY = 0;
            velY = -velY;
            hitWall = true;
        } else if (posY + rectHeight >= height) {
            posY = height - rectHeight;
            velY = -velY;
            hitWall = true;
        }

        if (hitWall) {
            toggleColor();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        overlayPaint.setAlpha((int) ((darkness / 100f) * 255f));
        canvas.drawRect(0, 0, getWidth(), getHeight(), overlayPaint);

        if (dvdBitmap != null) {
            paint.setAlpha(255);
            paint.setColorFilter(new PorterDuffColorFilter(currentColor, PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(dvdBitmap, posX, posY, paint);
        }
    }
}