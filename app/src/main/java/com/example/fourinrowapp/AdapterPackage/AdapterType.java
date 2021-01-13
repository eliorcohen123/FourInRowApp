package com.example.fourinrowapp.AdapterPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourinrowapp.R;

import java.util.ArrayList;

// A class that extends ViewHolder that will be used by the adapter
public class AdapterType extends RecyclerView.Adapter<ViewHolderAdapterType> {

    private final ArrayList<String> dataList; // List of Strings

    public AdapterType(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    // Give you to see the design of your Adapter, Give you access to use the ids of the elements
    @NonNull
    @Override
    public ViewHolderAdapterType onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_type, parent, false);
        return new ViewHolderAdapterType(view);
    }

    // Display the data at the specified position
    @Override
    public void onBindViewHolder(ViewHolderAdapterType holder, int position) {
        String current = dataList.get(position); // Put the data by the position
        holder.win.setText(current);
        if (current.equals("X")) {
            holder.lose.setText("O");
        } else if (current.equals("O")) {
            holder.lose.setText("X");
        }
    }

    // Get the size of the items in the RecyclerView
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
