package com.example.carpoolingapk1692022.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpoolingapk1692022.R;
import com.example.carpoolingapk1692022.models.Ride;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class ActiveRidesAdapter extends RecyclerView.Adapter<ActiveRidesAdapter.ViewHolder> {
    private List<Ride> rides;
    private OnRideActionListener listener;

    public interface OnRideActionListener {
        void onCompleteRide(Ride ride);
    }

    public ActiveRidesAdapter(List<Ride> rides, OnRideActionListener listener) {
        this.rides = rides;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_ride, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(rides.get(position));
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fromText;
        private TextView toText;
        private TextView statusText;
        private MaterialButton completeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            statusText = itemView.findViewById(R.id.statusText);
            completeButton = itemView.findViewById(R.id.completeButton);
        }

        public void bind(Ride ride) {
            fromText.setText("Од: " + ride.getFromLocation());
            toText.setText("До: " + ride.getToLocation());
            statusText.setText(getStatusText(ride.getStatus()));

            if ("ACCEPTED".equals(ride.getStatus())) {
                completeButton.setVisibility(View.VISIBLE);
                completeButton.setOnClickListener(v -> listener.onCompleteRide(ride));
            } else {
                completeButton.setVisibility(View.GONE);
            }
        }

        private String getStatusText(String status) {
            switch (status) {
                case "ACCEPTED": return "Во тек";
                case "COMPLETED": return "Завршено";
                case "CANCELLED": return "Откажано";
                default: return status;
            }
        }
    }

    public void updateRides(List<Ride> newRides) {
        this.rides = newRides;
        notifyDataSetChanged();
    }
} 