package com.example.carpoolingapk1692022.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpoolingapk1692022.R;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.Driver;
import com.example.carpoolingapk1692022.models.Ride;
import com.example.carpoolingapk1692022.models.Review;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.RideHistoryViewHolder> {
    private List<Ride> rides;
    private SimpleDateFormat dateFormat;
    private OnRateDriverListener rateListener;
    private boolean isDriver;

    public interface OnRateDriverListener {
        void onRateDriver(Ride ride);
    }

    public RideHistoryAdapter(List<Ride> rides, boolean isDriver, OnRateDriverListener rateListener) {
        this.rides = rides;
        this.isDriver = isDriver;
        this.rateListener = rateListener;
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public RideHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_history, parent, false);
        return new RideHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideHistoryViewHolder holder, int position) {
        Ride ride = rides.get(position);
        holder.bind(ride);
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public void updateRides(List<Ride> newRides) {
        this.rides = newRides;
        notifyDataSetChanged();
    }

    class RideHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView dateText;
        private TextView driverNameText;
        private TextView driverRatingText;
        private TextView carInfoText;
        private TextView fromLocationText;
        private TextView toLocationText;
        private TextView priceText;
        private TextView statusText;
        private View reviewContainer;
        private TextView reviewRatingText;
        private TextView reviewCommentText;
        private DatabaseHelper databaseHelper;

        public RideHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            driverNameText = itemView.findViewById(R.id.driverNameText);
            driverRatingText = itemView.findViewById(R.id.driverRatingText);
            carInfoText = itemView.findViewById(R.id.carInfoText);
            fromLocationText = itemView.findViewById(R.id.fromLocationText);
            toLocationText = itemView.findViewById(R.id.toLocationText);
            priceText = itemView.findViewById(R.id.priceText);
            statusText = itemView.findViewById(R.id.statusText);
            reviewContainer = itemView.findViewById(R.id.reviewContainer);
            reviewRatingText = itemView.findViewById(R.id.reviewRatingText);
            reviewCommentText = itemView.findViewById(R.id.reviewCommentText);
            databaseHelper = new DatabaseHelper(itemView.getContext());
        }

        public void bind(Ride ride) {
            dateText.setText(dateFormat.format(new Date(ride.getTimestamp())));
            
            // Земи информации за возачот
            Driver driver = databaseHelper.getDriverById(ride.getDriverId());
            if (driver != null) {
                driverNameText.setText(driver.getFullName());
                driverRatingText.setText(String.format("%.1f ★", driver.getRating()));
                carInfoText.setText(driver.getCarInfo());
            }

            fromLocationText.setText("Од: " + ride.getFromLocation());
            toLocationText.setText("До: " + ride.getToLocation());
            priceText.setText(String.format("%.2f ден.", ride.getPrice()));
            
            String status;
            switch (ride.getStatus()) {
                case "PENDING": status = "Во очекување"; break;
                case "ACCEPTED": status = "Прифатено"; break;
                case "REJECTED": status = "Одбиено"; break;
                case "COMPLETED": status = "Завршено"; break;
                default: status = ride.getStatus();
            }
            statusText.setText(status);

            // Провери дали има review и прикажи го ако постои
            Review review = databaseHelper.getRideReview(ride.getId());
            if (review != null) {
                reviewContainer.setVisibility(View.VISIBLE);
                reviewRatingText.setText(String.format("%.1f ★", review.getRating()));
                reviewCommentText.setText(review.getReviewText());
            } else {
                reviewContainer.setVisibility(View.GONE);
            }
        }
    }
} 