package com.tuusuario.dvdscreensaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int DEFAULT_DARKNESS = 80;
    private SharedPreferences prefs;
    private int darkness = DEFAULT_DARKNESS;
    private float sizeScale = 1f;
    private float speedScale = 1f;
    private boolean landscape = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("dvd_settings", Context.MODE_PRIVATE);
        darkness = prefs.getInt("darkness", DEFAULT_DARKNESS);
        sizeScale = prefs.getFloat("size_scale", 1f);
        speedScale = prefs.getFloat("speed_scale", 1f);

        TextView title = findViewById(R.id.titleText);
        title.setText("VibeSaver - TV Controls");

        SeekBar darknessBar = findViewById(R.id.darknessBar);
        TextView darknessValue = findViewById(R.id.darknessValue);
        SeekBar sizeBar = findViewById(R.id.sizeBar);
        TextView sizeValue = findViewById(R.id.sizeValue);
        SeekBar speedBar = findViewById(R.id.speedBar);
        TextView speedValue = findViewById(R.id.speedValue);

        Button startDreamButton = findViewById(R.id.startDreamButton);
        Button settingsButton = findViewById(R.id.openSettingsButton);
        Button toggleOrientationButton = findViewById(R.id.orientationButton);
        Button remapButton = findViewById(R.id.remapButton);

        darknessBar.setMax(80);
        darknessBar.setProgress(darkness);
        darknessValue.setText("Oscurecer: " + darkness + "%");

        sizeBar.setMax(100);
        sizeBar.setProgress((int) ((sizeScale - 0.4f) * 100f));
        sizeValue.setText("Tamaño: " + String.format("%.2f", sizeScale) + "x");

        speedBar.setMax(100);
        speedBar.setProgress((int) ((speedScale - 0.5f) * 100f));
        speedValue.setText("Velocidad: " + String.format("%.2f", speedScale) + "x");

        darknessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                darkness = progress;
                darknessValue.setText("Oscurecer: " + darkness + "%");
                prefs.edit().putInt("darkness", darkness).apply();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeScale = 0.4f + (progress / 100f);
                sizeValue.setText("Tamaño: " + String.format("%.2f", sizeScale) + "x");
                prefs.edit().putFloat("size_scale", sizeScale).apply();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedScale = 0.5f + (progress / 100f);
                speedValue.setText("Velocidad: " + String.format("%.2f", speedScale) + "x");
                prefs.edit().putFloat("speed_scale", speedScale).apply();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        startDreamButton.setOnClickListener(v -> startSaver());

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
        });

        toggleOrientationButton.setOnClickListener(v -> {
            landscape = !landscape;
            if (landscape) {
                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                toggleOrientationButton.setText("Orientación: Landscape");
            } else {
                setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                toggleOrientationButton.setText("Orientación: Portrait");
            }
        });

        remapButton.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this, "Activa el servicio de accesibilidad para remapear botones", Toast.LENGTH_LONG).show();
        });

        startSaver();
    }

    private void startSaver() {
        Intent intent = new Intent(this, DvdScreensaverService.class);
        startService(intent);
        Toast.makeText(this, "Salvapantallas iniciado", Toast.LENGTH_SHORT).show();
    }
}
