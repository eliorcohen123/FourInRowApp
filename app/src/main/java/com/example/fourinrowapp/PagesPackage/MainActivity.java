package com.example.fourinrowapp.PagesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourinrowapp.R;

public class MainActivity extends AppCompatActivity {

    private boolean isBackButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doActions();
    }

    private void doActions() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            finish();

            if (!isBackButtonClicked) {
                Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isBackButtonClicked = true;
    }

}