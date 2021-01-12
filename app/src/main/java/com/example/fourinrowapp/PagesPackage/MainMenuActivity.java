package com.example.fourinrowapp.PagesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourinrowapp.R;
import com.google.firebase.auth.FirebaseAuth;

// AppCompatActivity - Give you access to use the LifeCycle of Activity.
public class MainMenuActivity extends AppCompatActivity {

    private ListView menuList; // View which groups several items and displays them in a vertically scrollable list
    private FirebaseAuth mAuth; // Set FirebaseAuth - The authentication of Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu); // Give you to see the design of your Activity, Give you access to use the ids of the elements

        initUI();
        listViewMainMenu();
    }

    private void initUI() {
        // Initialize id to the element
        menuList = findViewById(R.id.listView1);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
    }

    private void listViewMainMenu() {
        // Array of Strings that contain Strings to show in the ListView
        String[] items = {
                getResources().getString(R.string.play),
                getResources().getString(R.string.scores),
                getResources().getString(R.string.sign_out),
        };

        // Adapts the design to each item in the ListView and receives the Array of Strings - the information of the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.menu_item, items);
        // Set the adapter in the ListView
        menuList.setAdapter(adapter);

        // Perform executes when items are clicked in the ListView
        menuList.setOnItemClickListener((parent, itemClicked, position, id) -> {
            TextView textview = (TextView) itemClicked;
            String strText = textview.getText().toString();
            if (strText.equalsIgnoreCase(getResources().getString(R.string.play))) {
                startActivity(new Intent(MainMenuActivity.this, GameStartActivity.class));
            } else if (strText.equalsIgnoreCase(getResources().getString(R.string.scores))) {
                startActivity(new Intent(MainMenuActivity.this, ScoresActivity.class));
            } else if (strText.equalsIgnoreCase(getResources().getString(R.string.sign_out))) {
                mAuth.signOut(); // SignOut from Firebase Auth

                startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
                // Destroy the current Activity
                finish();
            }
        });
    }

}
