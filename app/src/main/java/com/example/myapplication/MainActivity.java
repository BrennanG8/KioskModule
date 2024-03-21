package com.example.myapplication;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;

public class MainActivity extends AppCompatActivity {
    Button b_enable, b_lock;
    static final int RESULT_ENABLED = 1;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;
    private void exitFullScreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    public void enterFullScreenMode() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_enable = findViewById(R.id.b_enable);
        b_lock = findViewById(R.id.b_lock);
        devicePolicyManager =(DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(MainActivity.this, Controller.class);
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if(active) {
            b_enable.setText("Disable");
            b_lock.setVisibility(View.VISIBLE);
        }else {
            b_enable.setText("Enable");
            b_lock.setVisibility(View.GONE);
        }

        b_enable.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            boolean active = devicePolicyManager.isAdminActive(componentName);
                if(active) {
                    devicePolicyManager.removeActiveAdmin(componentName);
                    b_enable.setText("Enable");
                    b_lock.setVisibility(View.GONE);
                }else{
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"Enable the app");
                    startActivityForResult(intent, RESULT_ENABLED);
                }
        }
        });
        b_lock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (b_lock.getText().equals("Lock")) {
                    // If it says "Lock", change it to "Unlock"
                    b_lock.setText("Unlock");
                    enterFullScreenMode();

                } else {
                    // If it says "Unlock", change it to "Lock"
                    b_lock.setText("Lock");
                    exitFullScreenMode();
                }
            }
        });
}
        @SuppressLint("SetTextI18n")
        @Override
        protected void onActivityResult(int requestCode,int resultCode,Intent data) {
            if (requestCode == RESULT_ENABLED) {
                if (resultCode == Activity.RESULT_OK) {
                    b_enable.setText("LOCK");
                } else {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }