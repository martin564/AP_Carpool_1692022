package com.example.carpoolingapk1692022;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carpoolingapk1692022.adapters.RideHistoryAdapter;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.Ride;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.example.carpoolingapk1692022.models.Driver;
import com.example.carpoolingapk1692022.dialogs.RateDriverDialog;

public class RideHistoryActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private RideHistoryAdapter historyAdapter;
    private TabLayout tabLayout;
    private DatabaseHelper databaseHelper;
    private int userId;
    private boolean isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        userId = getIntent().getIntExtra("USER_ID", -1);
        isDriver = getIntent().getBooleanExtra("IS_DRIVER", false);

        if (userId == -1) {
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        initializeViews();
        setupRecyclerView();
        loadRides("ACTIVE");
    }

    private void initializeViews() {
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadRides("ACTIVE");
                } else {
                    loadRides("COMPLETED");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        historyAdapter = new RideHistoryAdapter(new ArrayList<>(), isDriver, ride -> {
            Driver driver = databaseHelper.getDriverById(ride.getDriverId());
            if (driver != null) {
                RateDriverDialog dialog = new RateDriverDialog(this, driver, rating -> {
                    if (databaseHelper.addRating(driver.getId(), rating)) {
                        Toast.makeText(this, "Оценката е успешно додадена", Toast.LENGTH_SHORT).show();
                        loadRides("COMPLETED"); // Освежи ја листата
                    } else {
                        Toast.makeText(this, "Грешка при додавање на оценка", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void loadRides(String type) {
        List<Ride> rides;
        if (isDriver) {
            rides = databaseHelper.getDriverRides(userId);
        } else {
            rides = databaseHelper.getPassengerRides(userId);
        }

        // Филтрирање според тип
        List<Ride> filteredRides = new ArrayList<>();
        for (Ride ride : rides) {
            if (type.equals("ACTIVE") && 
                (ride.getStatus().equals("PENDING") || ride.getStatus().equals("ACCEPTED"))) {
                filteredRides.add(ride);
            } else if (type.equals("COMPLETED") && 
                (ride.getStatus().equals("COMPLETED") || ride.getStatus().equals("REJECTED"))) {
                filteredRides.add(ride);
            }
        }

        historyAdapter.updateRides(filteredRides);
    }

    private void loadRideHistory() {
        // Земи само завршени возења
        List<Ride> completedRides = databaseHelper.getPassengerCompletedRides(userId);
        historyAdapter.updateRides(completedRides);
    }
} 