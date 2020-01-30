package com.aefyr.flexfilter.builtin.filter.sort.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aefyr.flexfilter.builtin.R;
import com.aefyr.flexfilter.builtin.filter.sort.SortFilterConfig;
import com.aefyr.flexfilter.builtin.filter.sort.SortFilterConfigOption;
import com.google.android.material.chip.Chip;

public class SortFilterConfigOptionAdapter extends RecyclerView.Adapter<SortFilterConfigOptionAdapter.OptionViewHolder> {

    private LayoutInflater mInflater;

    private SortFilterConfig mFilter;

    public SortFilterConfigOptionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setFilter(SortFilterConfig filter) {
        mFilter = filter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SortFilterConfigOptionAdapter.OptionViewHolder(mInflater.inflate(R.layout.item_sort_filter_option, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.bind(mFilter.options().get(position));
    }

    @Override
    public int getItemCount() {
        return mFilter != null ? mFilter.options().size() : 0;
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {

        private Chip mChip;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);

            mChip = (Chip) itemView;

            mChip.setOnClickListener((v) -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION)
                    return;

                SortFilterConfigOption option = mFilter.options().get(adapterPosition);

                if (option.isSelected())
                    option.setAscending(!option.ascending());
                else
                    option.setSelected();

                notifyDataSetChanged();
            });
        }

        void bind(SortFilterConfigOption option) {
            String text = option.name().toString();
            if (option.isSelected()) {
                text += option.ascending() ? " ↑" : " ↓";
            }
            mChip.setText(text);
            mChip.setChecked(option.isSelected());
        }
    }
}
