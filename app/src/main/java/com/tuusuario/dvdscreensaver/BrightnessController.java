package com.tuusuario.dvdscreensaver;

public final class BrightnessController {
    public static final int MIN = 0;
    public static final int MAX = 100;

    private BrightnessController() {
    }

    public static int clamp(int value) {
        return Math.max(MIN, Math.min(MAX, value));
    }

    public static int adjust(int current, int delta) {
        return clamp(current + delta);
    }
}
