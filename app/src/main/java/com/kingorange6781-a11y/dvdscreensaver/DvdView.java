package com.tuusuario.dvdscreensaver;

import android.content.Context;
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

    private float posX = 100f, posY = 100f;
    private float velX = 4f, velY = 4f; // Velocidad del logo
    private final int rectWidth = 350;  // Ancho del logo en pantalla (puedes ajustarlo)
    private final int rectHeight = 175; // Alto del logo en pantalla (puedes ajustarlo)
    
    private final Paint paint;
    private Bitmap dvdBitmap;
    private int currentColor;
    private final Handler handler;
    private final Runnable runnable;
    private final Random random;

    private final int[] colors = {
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, 
        Color.CYAN, Color.MAGENTA, Color.WHITE, Color.rgb(255, 165, 0) // Naranja
    };

    public DvdView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        random = new Random();
        currentColor = colors[random.nextInt(colors.length)];

        // Cargar la imagen desde res/drawable/dvd.png y escalarla al tamaño deseado
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dvd);
        if (rawBitmap != null) {
            dvdBitmap = Bitmap.createScaledBitmap(rawBitmap, rectWidth, rectHeight, true);
        }

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                updatePosition();
                invalidate(); // Redibujar en cada frame
                handler.postDelayed(this, 16); // ~60 FPS
            }
        };
    }

    public void startAnimation() {
        handler.post(runnable);
    }

    public void stopAnimation() {
        handler.removeCallbacks(runnable);
    }

    private void updatePosition() {
        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return;

        posX += velX;
        posY += velY;

        boolean hitWall = false;

        // Rebote en Eje X (Izquierda / Derecha)
        if (posX <= 0) {
            posX = 0;
            velX = -velX;
            hitWall = true;
        } else if (posX + rectWidth >= width) {
            posX = width - rectWidth;
            velX = -velX;
            hitWall = true;
        }

        // Rebote en Eje Y (Arriba / Abajo)
        if (posY <= 0) {
            posY = 0;
            velY = -velY;
            hitWall = true;
        } else if (posY + rectHeight >= height) {
            posY = height - rectHeight;
            velY = -velY;
            hitWall = true;
        }

        // Si choca, cambiar de color aleatoriamente (evitando repetir el mismo color consecutivo)
        if (hitWall) {
            int newColor;
            do {
                newColor = colors[random.nextInt(colors.length)];
            } while (newColor == currentColor);
            currentColor = newColor;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fondo negro puro (ideal para televisiones OLED/LED)
        canvas.drawColor(Color.BLACK);

        if (dvdBitmap != null) {
            // Aplicar el filtro de color para que el logo cambie de color al chocar
            paint.setColorFilter(new PorterDuffColorFilter(currentColor, PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(dvdBitmap, posX, posY, paint);
        }
    }
}