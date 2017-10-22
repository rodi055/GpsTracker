package com.rawad.gpstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For first switch butto
        switchButton = (Switch) findViewById(R.id.switchButton);

        switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
        if (switchButton.isChecked()) {
            startService(new Intent(this, MyService.class));
        } else {
            stopService(new Intent(this, MyService.class));
        }
    }

}