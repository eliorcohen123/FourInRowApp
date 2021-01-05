package com.example.fourinrowapp.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.fourinrowapp.AdapterPackage.AdapterType;
import com.example.fourinrowapp.R;
import com.example.fourinrowapp.DataPackage.TypeDBHelper;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final ArrayList<String> resultsArrayList = new ArrayList<>();
    private AdapterType adapterType;
    private TypeDBHelper typeDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        initUI();
        getData();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);

        typeDBHelper = new TypeDBHelper(this);

        adapterType = new AdapterType(resultsArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterType);
    }

    private void getData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                resultsArrayList.clear();
                resultsArrayList.addAll(typeDBHelper.getAllTypes());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                adapterType.notifyDataSetChanged();
            }
        }.execute();
    }

}
