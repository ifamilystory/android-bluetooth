package com.ccydsz.cloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ccydsz.cloud.activity.HomeActivity;
import com.ccydsz.cloud.manager.BlueToothManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlueToothManager.getInstance().createBluetoothClient(this);
        startActivity(new Intent(this, HomeActivity.class));
    }
}
