package com.example.fourinrowapp.AdapterPackage;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fourinrowapp.R;

// RecyclerView.ViewHolder - describes an item view about its place within the RecyclerView
public class ViewHolderAdapterType extends RecyclerView.ViewHolder {

    public TextView win, lose;

    ViewHolderAdapterType(View itemView) {
        super(itemView);

        win = itemView.findViewById(R.id.win);
        lose = itemView.findViewById(R.id.lose);
    }

}
