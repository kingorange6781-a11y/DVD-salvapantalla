package com.tuusuario.dvdscreensaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int brightness = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.titleText);
        title.setText("VibeSaver - TV Controls");

        SeekBar brightnessBar = findViewById(R.id.brightnessBar);
        TextView brightnessValue = findViewById(R.id.brightnessValue);
        brightnessBar.setMax(100);
        brightnessBar.setProgress(brightness);
        brightnessValue.setText("Brillo: " + brightness + "%");

        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                brightnessValue.setText("Brillo: " + brightness + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                applyBrightness(brightness);
            }
        });

        Button startDreamButton = findViewById(R.id.startDreamButton);
        startDreamButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DvdScreensaverService.class);
            startService(intent);
            Toast.makeText(this, "DVD activo", Toast.LENGTH_SHORT).show();
        });

        Button openSettingsButton = findViewById(R.id.openSettingsButton);
        openSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
            startActivity(intent);
        });
    }

    private void applyBrightness(int value) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
            Toast.makeText(this, "Brillo ajustado a " + value + "%", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo cambiar el brillo", Toast.LENGTH_SHORT).show();
        }
    }
}
