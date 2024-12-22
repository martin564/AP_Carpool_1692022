package com.example.carpoolingapk1692022;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.Driver;
import com.example.carpoolingapk1692022.models.Ride;
import com.example.carpoolingapk1692022.adapters.RideRequestsAdapter;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.os.Handler;
import com.example.carpoolingapk1692022.utils.SpacingItemDecoration;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.widget.RatingBar;
import android.app.AlertDialog;
import android.view.View;
import java.util.Calendar;
import java.util.Locale;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputEditText;

public class DriverActivity extends AppCompatActivity {
    private TextView welcomeText;
    private TextView ratingText;
    private TextView carInfoText;
    private SwitchMaterial activeSwitch;
    private DatabaseHelper databaseHelper;
    private Driver currentDriver;
    private RideRequestsAdapter requestsAdapter;
    private final Handler handler = new Handler();
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {
                loadRequests();
                handler.postDelayed(this, 10000);
            }
        }
    };
    private TextInputEditText startTimeInput;
    private TextInputEditText endTimeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        currentDriver = (Driver) databaseHelper.getDriverById(userId);

        initializeViews();
        setupViews();
        setupRequestsRecyclerView();
    }

    private void setupRequestsRecyclerView() {
        RecyclerView requestsRecyclerView = findViewById(R.id.ridesRecyclerView);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        requestsRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        List<Ride> requests = databaseHelper.getDriverRequests(currentDriver.getId());
        requestsAdapter = new RideRequestsAdapter(requests, 
            new RideRequestsAdapter.OnRequestActionListener() {
                @Override
                public void onAccept(Ride request) {
                    if (databaseHelper.updateRequestStatus(request.getId(), "ACCEPTED")) {
                        Ride ride = new Ride();
                        ride.setPassengerId(request.getPassengerId());
                        ride.setDriverId(currentDriver.getId());
                        ride.setFromLocation(request.getFromLocation());
                        ride.setToLocation(request.getToLocation());
                        ride.setFromLat(request.getFromLat());
                        ride.setFromLng(request.getFromLng());
                        ride.setToLat(request.getToLat());
                        ride.setToLng(request.getToLng());
                        ride.setStatus("ACCEPTED");
                        ride.setTimestamp(System.currentTimeMillis());
                        
                        long rideId = databaseHelper.addRide(ride);
                        if (rideId != -1) {
                            Toast.makeText(DriverActivity.this, 
                                "Барањето е прифатено", Toast.LENGTH_SHORT).show();
                        }
                    }
                    loadRequests();
                }

                @Override
                public void onReject(Ride request) {
                    if (databaseHelper.updateRequestStatus(request.getId(), "REJECTED")) {
                        Toast.makeText(DriverActivity.this, 
                            "Барањето е одбиено", Toast.LENGTH_SHORT).show();
                    }
                    loadRequests();
                }

                @Override
                public void onComplete(Ride request) {
                    if (databaseHelper.updateRequestStatus(request.getId(), "COMPLETED")) {
                        Toast.makeText(DriverActivity.this, 
                            "Возењето е завршено", Toast.LENGTH_SHORT).show();
                        showRatingDialog(request.getPassengerId(), request.getId());
                    }
                    loadRequests();
                }
            });
        requestsRecyclerView.setAdapter(requestsAdapter);

        if (requests.isEmpty()) {
            // TODO: Прикажи празен state
        }
    }

    private void loadRequests() {
        List<Ride> requests = databaseHelper.getDriverRequests(currentDriver.getId());
        requestsAdapter.updateRequests(requests);
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
        ratingText = findViewById(R.id.ratingText);
        carInfoText = findViewById(R.id.carInfoText);
        activeSwitch = findViewById(R.id.activeSwitch);

        MaterialButton historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RideHistoryActivity.class);
            intent.putExtra("USER_ID", currentDriver.getId());
            intent.putExtra("IS_DRIVER", true);
            startActivity(intent);
        });

        startTimeInput = findViewById(R.id.startTimeInput);
        endTimeInput = findViewById(R.id.endTimeInput);

        startTimeInput.setOnClickListener(v -> showTimePickerDialog(true));
        endTimeInput.setOnClickListener(v -> showTimePickerDialog(false));
    }

    private void setupViews() {
        if (currentDriver != null) {
            welcomeText.setText("Добредојдовте, " + currentDriver.getFullName());
            ratingText.setText(String.format("Рејтинг: %.1f (%d оценки)", 
                currentDriver.getRating(), currentDriver.getNumberOfRatings()));
            carInfoText.setText("Возило: " + currentDriver.getCarInfo());
            activeSwitch.setChecked(currentDriver.isActive());
        }

        activeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (databaseHelper.updateDriverActiveStatus(currentDriver.getId(), isChecked)) {
                currentDriver.setActive(isChecked);
                Toast.makeText(DriverActivity.this,
                    isChecked ? "Сега сте активни за возење" : "Неактивни сте за возење",
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRequests();
        handler.postDelayed(refreshRunnable, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshRunnable);
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            (view, hourOfDay, minute1) -> {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                if (isStartTime) {
                    startTimeInput.setText(time);
                } else {
                    endTimeInput.setText(time);
                }
                updateActiveTime();
            }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateActiveTime() {
        String startTime = startTimeInput.getText().toString();
        String endTime = endTimeInput.getText().toString();
        
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            if (databaseHelper.updateDriverActiveTime(currentDriver.getId(), startTime, endTime)) {
                Toast.makeText(this, "Активното време е ажурирано", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showRatingDialog(int passengerId, int rideId) {
        View view = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Оцени", (dialogInterface, i) -> {
                float rating = ratingBar.getRating();
                if (databaseHelper.ratePassenger(passengerId, rideId, rating)) {
                    Toast.makeText(this, "Успешно оценет патник", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Откажи", null)
            .create();
        dialog.show();
    }
} 