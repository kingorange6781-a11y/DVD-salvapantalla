package com.tuusuario.dvdscreensaver;

import android.service.dreams.DreamService;

public class DvdScreensaverService extends DreamService {

    private DvdView dvdView;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        // Configurar para que sea interactivo a la pantalla completa y sin interrupciones
        setInteractive(false);
        setFullscreen(true);

        // Instanciar y colocar nuestra vista personalizada del rebote
        dvdView = new DvdView(this);
        setContentView(dvdView);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (dvdView != null) {
            dvdView.stopAnimation();
        }
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        if (dvdView != null) {
            dvdView.startAnimation();
        }
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        if (dvdView != null) {
            dvdView.stopAnimation();
        }
    }
}