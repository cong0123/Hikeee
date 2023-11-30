package com.example.hikeee;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> implements Filterable {
    private List<Hike> hikes;
    private List<Hike> hikesOld;

    private Context context;
    private OnItemClickListener listener;

    public HikeAdapter(Context context, List<Hike> hikes) {
        this.context = context;
        this.hikes = hikes;
        this.hikesOld = hikes;
    }
    public void updateData(List<Hike> updatedData) {
        this.hikes = updatedData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.bind(hike);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return hikes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    hikes = hikesOld;
                } else {
                    List<Hike> list = new ArrayList<>();
                    for (Hike hike : hikesOld) {
                        if (hike.getName().toLowerCase().contains(strSearch) ||
                                hike.getLocation().toLowerCase().contains(strSearch) ||
                                hike.getDate().toLowerCase().contains(strSearch)) {
                            list.add(hike);
                        }
                    }
                    hikes = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hikes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hikes = (List<Hike>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView dateTextView;


        HikeViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

        }

        void bind(Hike hike) {
            nameTextView.setText(hike.getName());
            locationTextView.setText(hike.getLocation());
            dateTextView.setText(hike.getDate());
        }
    }
}

