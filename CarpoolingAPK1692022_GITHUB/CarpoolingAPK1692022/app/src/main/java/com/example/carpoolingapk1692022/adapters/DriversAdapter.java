package com.example.carpoolingapk1692022.adapters;

import com.example.carpoolingapk1692022.database.DatabaseHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpoolingapk1692022.R;
import com.example.carpoolingapk1692022.models.Driver;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class DriversAdapter extends RecyclerView.Adapter<DriversAdapter.DriverViewHolder> {
    private List<Driver> drivers;
    private OnDriverClickListener listener;
    private DatabaseHelper databaseHelper;

    public interface OnDriverClickListener {
        void onDriverClick(Driver driver);
    }

    public DriversAdapter(List<Driver> drivers, OnDriverClickListener listener) {
        this.drivers = drivers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driver, parent, false);
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(parent.getContext());
        }
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = drivers.get(position);
        holder.bind(driver);
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public void updateDrivers(List<Driver> newDrivers) {
        this.drivers = newDrivers;
        notifyDataSetChanged();
    }

    class DriverViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView ratingText;
        private TextView carInfoText;
        private TextView priceText;
        private MaterialButton requestButton;
        private DatabaseHelper databaseHelper;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            ratingText = itemView.findViewById(R.id.ratingText);
            carInfoText = itemView.findViewById(R.id.carInfoText);
            priceText = itemView.findViewById(R.id.priceText);
            requestButton = itemView.findViewById(R.id.requestButton);
            databaseHelper = new DatabaseHelper(itemView.getContext());
        }

        public void bind(Driver driver) {
            Driver updatedDriver = databaseHelper.getDriverById(driver.getId());
            if (updatedDriver != null) {
                nameText.setText(updatedDriver.getFullName());
                ratingText.setText(String.format("%.1f ★", updatedDriver.getRating()));
                carInfoText.setText(updatedDriver.getCarInfo());
                priceText.setText(String.format("%.2f ден/км", updatedDriver.getPricePerKm()));
            } else {
                nameText.setText(driver.getFullName());
                ratingText.setText(String.format("%.1f ★", driver.getRating()));
                carInfoText.setText(driver.getCarInfo());
                priceText.setText(String.format("%.2f ден/км", driver.getPricePerKm()));
            }

            requestButton.setOnClickListener(v -> listener.onDriverClick(driver));
        }
    }
} 