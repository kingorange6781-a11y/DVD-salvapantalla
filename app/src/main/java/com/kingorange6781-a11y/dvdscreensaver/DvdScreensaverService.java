package com.tuusuario.dvdscreensaver;

import android.service.dreams.DreamService;

public class DvdScreensaverService extends DreamService {

    private DvdView dvdView;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
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