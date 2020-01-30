package com.aefyr.flexfilter.builtin.filter.singlechoice.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aefyr.flexfilter.builtin.R;
import com.aefyr.flexfilter.builtin.filter.singlechoice.SingleChoiceFilterConfig;
import com.aefyr.flexfilter.builtin.filter.singlechoice.SingleChoiceFilterConfigOption;
import com.google.android.material.chip.Chip;

public class SingleChoiceFilterConfigOptionAdapter extends RecyclerView.Adapter<SingleChoiceFilterConfigOptionAdapter.OptionViewHolder> {

    private LayoutInflater mInflater;

    private SingleChoiceFilterConfig mFilter;

    public SingleChoiceFilterConfigOptionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setFilter(SingleChoiceFilterConfig filter) {
        mFilter = filter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionViewHolder(mInflater.inflate(R.layout.item_single_choice_filter_option, parent, false));
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

                mFilter.options().get(adapterPosition).setSelected();
                notifyDataSetChanged();
            });
        }

        void bind(SingleChoiceFilterConfigOption option) {
            mChip.setText(option.name());
            mChip.setChecked(option.isSelected());
        }
    }
}
