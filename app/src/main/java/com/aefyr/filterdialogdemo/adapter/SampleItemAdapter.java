package com.aefyr.filterdialogdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aefyr.filterdialogdemo.model.SampleItem;

import java.util.List;

public class SampleItemAdapter extends RecyclerView.Adapter<SampleItemAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    private List<SampleItem> mItems;

    public SampleItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<SampleItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(SampleItem item) {
            ((TextView) itemView).setText(item.text);
        }
    }
}
