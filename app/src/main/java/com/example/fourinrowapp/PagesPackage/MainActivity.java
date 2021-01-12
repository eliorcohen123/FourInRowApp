package com.example.fourinrowapp.PagesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourinrowapp.R;

// AppCompatActivity - Give you access to use the LifeCycle of Activity.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Give you to see the design of your Activity, Give you access to use the ids of the elements

        doActions();
    }

    private void doActions() {
        // Intent from MainActivity to LoginActivity after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            // Destroy the current Activity
            finish();
        }, 2000);
    }

}
