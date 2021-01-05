package com.example.fourinrowapp.PagesPackage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourinrowapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    private ListView menuList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        initUI();
        listViewMainMenu();
    }

    private void initUI() {
        menuList = findViewById(R.id.listView1);

        mAuth = FirebaseAuth.getInstance();
    }

    private void listViewMainMenu() {
        String[] items = {
                getResources().getString(R.string.play),
                getResources().getString(R.string.scores),
                getResources().getString(R.string.sign_out),
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.menu_item, items);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener((parent, itemClicked, position, id) -> {
            TextView textview = (TextView) itemClicked;
            String strText = textview.getText().toString();
            if (strText.equalsIgnoreCase(getResources().getString(R.string.play))) {
                startActivity(new Intent(MainMenu.this, GameStart.class));
            } else if (strText.equalsIgnoreCase(getResources().getString(R.string.scores))) {
                startActivity(new Intent(MainMenu.this, ScoresActivity.class));
            } else if (strText.equalsIgnoreCase(getResources().getString(R.string.sign_out))) {
                mAuth.signOut();
                startActivity(new Intent(MainMenu.this, LoginActivity.class));
                finish();
            }
        });
    }

}