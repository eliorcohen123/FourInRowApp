package com.example.fourinrowapp.AdapterPackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourinrowapp.R;

import java.util.List;

public class AdapterType extends RecyclerView.Adapter<AdapterType.CustomViewHolder> {

    private List<String> dataList;

    public AdapterType(List<String> dataList) {
        this.dataList = dataList;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView win, lose;

        CustomViewHolder(View itemView) {
            super(itemView);

            win = itemView.findViewById(R.id.win);
            lose = itemView.findViewById(R.id.lose);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_type, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        String current = dataList.get(position);
        holder.win.setText(current);
        if (current.equals("X")) {
            holder.lose.setText("O");
        } else if (current.equals("O")) {
            holder.lose.setText("X");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
