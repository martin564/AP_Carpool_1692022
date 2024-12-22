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

public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.ViewHolder> {
    private List<Ride> requests;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onAccept(Ride request);
        void onReject(Ride request);
        void onComplete(Ride request);
    }

    public RideRequestsAdapter(List<Ride> requests, OnRequestActionListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ride request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void updateRequests(List<Ride> newRequests) {
        this.requests = newRequests;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fromText;
        private TextView toText;
        private MaterialButton acceptButton;
        private MaterialButton rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.fromText);
            toText = itemView.findViewById(R.id.toText);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(Ride request) {
            fromText.setText("Од: " + request.getFromLocation());
            toText.setText("До: " + request.getToLocation());

            if ("ACCEPTED".equals(request.getStatus())) {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setText("Заврши");
                rejectButton.setOnClickListener(v -> listener.onComplete(request));
            } else {
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setText("Одбиј");
                acceptButton.setOnClickListener(v -> listener.onAccept(request));
                rejectButton.setOnClickListener(v -> listener.onReject(request));
            }
        }
    }
} 