package com.sp.navdrawertest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyHolder> {
    private ArrayList<String> data;

    public RvAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.rvTitle.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<String> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView rvTitle;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rvTitle = itemView.findViewById(R.id.rvTitle);
        }
    }
}
