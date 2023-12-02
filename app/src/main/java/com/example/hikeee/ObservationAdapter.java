package com.example.hikeee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private List<Observation> observationList;
    private Context context;


    public ObservationAdapter(Context context) {
        this.context = context;
    }
    public void updateObservationList(List<Observation> newObservationList) {
        this.observationList = newObservationList;
        notifyDataSetChanged();
    }

    public void setObservationList(List<Observation> observationList) {
        this.observationList = observationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observationList.get(position);
        holder.bind(observation);

        // Xử lý sự kiện khi nhấn nút "Sửa"
        holder.editObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editObservation(holder.itemView.getContext(), observation);
            }
        });

        // Sự kiện khi nhấn nút "Xóa"
        holder.deleteObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteObservation(holder.itemView.getContext(), observation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList != null ? observationList.size() : 0;
    }

    static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView timeTextView;
        TextView weatherTextView;
        TextView trailConditionTextView;
        Button editObservationButton;
        Button deleteObservationButton;

        ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            weatherTextView = itemView.findViewById(R.id.weatherTextView);
            trailConditionTextView = itemView.findViewById(R.id.trailConditionTextView);
            editObservationButton = itemView.findViewById(R.id.editObservationButton);
            deleteObservationButton = itemView.findViewById(R.id.deleteObservationButton);
        }

        void bind(Observation observation) {
            contentTextView.setText("Content: " + observation.getContent());
            timeTextView.setText("Time: " + observation.getTime());
            weatherTextView.setText("Weather: " + observation.getWeather());
            trailConditionTextView.setText("Trail condition: " + observation.getTrailCondition());
        }
    }

    private void editObservation(Context context, Observation observation) {
        // Chuyển sang EditObservationActivity và truyền thông tin quan sát
        Intent intent = new Intent(context, EditObservationActivity.class);
        intent.putExtra("Observation", observation);
        context.startActivity(intent);
    }

    private void deleteObservation(Context context, Observation observation) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm delete");
        builder.setMessage("Do you want delete this observation?");


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ObservationDbHelper dbHelper = new ObservationDbHelper(context);
                dbHelper.deleteObservation(observation.getId());

                // Xóa quan sát khỏi danh sách
                int position = observationList.indexOf(observation);
                if (position != -1) {
                    observationList.remove(position);

                    // Cập nhật RecyclerView
                    notifyItemRemoved(position);
                }
            }
        });

        // Nút từ chối (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog khi nhấn No
                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
