package com.example.fourinrowapp.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.fourinrowapp.AdapterPackage.AdapterType;
import com.example.fourinrowapp.R;
import com.example.fourinrowapp.DataPackage.TypeDBOpenHelper;

import java.util.ArrayList;

// AppCompatActivity - Give you access to use the LifeCycle of Activity.
public class ScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // Set RecyclerView - contains list of items
    private final ArrayList<String> typesArrayList = new ArrayList<>();
    private AdapterType adapterType; // Set AdapterType
    private TypeDBOpenHelper typeDBOpenHelper; // Set TypeDBOpenHelper - SQLiteDBOpenHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores); // Give you to see the design of your Activity, Give you access to use the ids of the elements

        initUI();
        getData();
    }

    private void initUI() {
        // Initialize id to the element
        recyclerView = findViewById(R.id.recyclerView);

        typeDBOpenHelper = new TypeDBOpenHelper(this); // Initialize TypeDBOpenHelper - SQLiteDBOpenHelper

        adapterType = new AdapterType(typesArrayList); // Initialize AdapterType - adapts the design to each item in the RecyclerView and receives data
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the layout of the contents
        recyclerView.setAdapter(adapterType); // Set the adapter in the RecyclerView
    }

    private void getData() {
        // Conduct tasks
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) { // Perform tasks in background
                typesArrayList.clear(); // Clear the ArrayList that contains the data of the RecyclerView
                typesArrayList.addAll(typeDBOpenHelper.getAllTypes()); // Add all the data from TypeDBOpenHelper - SQLiteDBOpenHelper, to the ArrayList that data of the RecyclerView
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) { // After "doInBackground" finish to executes the tasks the "onPostExecute" called
                super.onPostExecute(aVoid);

                adapterType.notifyDataSetChanged(); // Update the data in the RecyclerView
            }
        }.execute(); // Run AsyncTask
    }

}
