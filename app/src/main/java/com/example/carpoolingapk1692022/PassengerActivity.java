package com.example.carpoolingapk1692022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.User;
import com.example.carpoolingapk1692022.models.Driver;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.example.carpoolingapk1692022.adapters.DriversAdapter;
import android.os.Handler;
import android.widget.RatingBar;
import androidx.appcompat.app.AlertDialog;
import com.example.carpoolingapk1692022.models.Ride;
import com.example.carpoolingapk1692022.adapters.ActiveRidesAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.os.Looper;

public class PassengerActivity extends AppCompatActivity {
    private TextView welcomeText;
    private TextInputEditText fromLocationInput;
    private TextInputEditText toLocationInput;
    private MaterialButton searchButton;
    private RecyclerView driversRecyclerView;
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private DriversAdapter driversAdapter;
    private static final int PICK_FROM_LOCATION = 1;
    private static final int PICK_TO_LOCATION = 2;
    private MaterialButton myRidesButton;
    private RecyclerView activeRidesRecyclerView;
    private ActiveRidesAdapter activeRidesAdapter;
    private ActivityResultLauncher<Intent> fromLocationLauncher;
    private ActivityResultLauncher<Intent> toLocationLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        
        if (savedInstanceState != null) {
            // Реставрирајте ја состојбата ако е потребно
        }

        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        currentUser = databaseHelper.getUserById(userId);

        initializeViews();
        setupViews();
        setupRecyclerView();

        fromLocationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String address = result.getData().getStringExtra("address");
                    fromLocationInput.setText(address);
                }
            });

        toLocationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String address = result.getData().getStringExtra("address");
                    toLocationInput.setText(address);
                }
            });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Зачувајте ја потребната состојба
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
        fromLocationInput = findViewById(R.id.fromLocationInput);
        toLocationInput = findViewById(R.id.toLocationInput);
        searchButton = findViewById(R.id.searchButton);
        driversRecyclerView = findViewById(R.id.driversRecyclerView);
        myRidesButton = findViewById(R.id.myRidesButton);
        myRidesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RideHistoryActivity.class);
            intent.putExtra("USER_ID", currentUser.getId());
            intent.putExtra("IS_DRIVER", false);
            startActivity(intent);
        });

        activeRidesRecyclerView = findViewById(R.id.activeRidesRecyclerView);
        setupActiveRidesRecyclerView();
    }

    private void setupViews() {
        if (currentUser != null) {
            welcomeText.setText("Добредојдовте, " + currentUser.getFullName());
        }

        fromLocationInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, LocationPickerActivity.class);
            fromLocationLauncher.launch(intent);
        });

        toLocationInput.setOnClickListener(v -> {
            Intent intent = new Intent(this, LocationPickerActivity.class);
            toLocationLauncher.launch(intent);
        });

        searchButton.setOnClickListener(v -> {
            String from = fromLocationInput.getText().toString();
            String to = toLocationInput.getText().toString();
            
            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(this, "Внесете ги двете локации", Toast.LENGTH_SHORT).show();
                return;
            }
            
            searchAvailableDrivers(from, to);
        });
    }

    private void setupRecyclerView() {
        driversAdapter = new DriversAdapter(new ArrayList<>(), driver -> {
            // Кога корисникот ќе кликне на возач
            String fromLocation = fromLocationInput.getText().toString();
            String toLocation = toLocationInput.getText().toString();
            
            if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                Toast.makeText(this, "Внесете ги локациите", Toast.LENGTH_SHORT).show();
                return;
            }

            // Додади го барањето во базата
            long requestId = databaseHelper.addRideRequest(
                currentUser.getId(),
                driver.getId(),
                fromLocation,
                toLocation,
                0.0, // fromLat - може да се додаде подоцна
                0.0, // fromLng
                0.0, // toLat
                0.0  // toLng
            );

            if (requestId != -1) {
                Toast.makeText(this, 
                    "Успешно испратено барање до " + driver.getFullName(), 
                    Toast.LENGTH_LONG).show();
                
                // Исчисти ги полињата
                fromLocationInput.setText("");
                toLocationInput.setText("");
                
                // Започни проверка на статус
                startStatusCheck(requestId);
                
                // Освежи ја листата на возачи
                searchAvailableDrivers(fromLocation, toLocation);
            } else {
                Toast.makeText(this, 
                    "Грешка при испраќање на барањето", 
                    Toast.LENGTH_SHORT).show();
            }
        });
        driversRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        driversRecyclerView.setAdapter(driversAdapter);
    }

    private void setupActiveRidesRecyclerView() {
        activeRidesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Ride> activeRides = databaseHelper.getPassengerActiveRides(currentUser.getId());
        
        activeRidesAdapter = new ActiveRidesAdapter(activeRides, ride -> {
            showRideCompletionDialog(ride);
        });
        activeRidesRecyclerView.setAdapter(activeRidesAdapter);
    }

    private void searchAvailableDrivers(String from, String to) {
        List<Driver> availableDrivers = databaseHelper.getActiveDrivers();
        if (availableDrivers.isEmpty()) {
            Toast.makeText(this, "Нема достапни возачи", Toast.LENGTH_SHORT).show();
        }
        driversAdapter.updateDrivers(availableDrivers);
        
        // Прикажи ја RecyclerView-то со возачи
        driversRecyclerView.setVisibility(View.VISIBLE);
    }

    private void startStatusCheck(long requestId) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) return; // Провери дали активноста е уште активна

                String status = databaseHelper.getRequestStatus((int)requestId);
                if (status != null) {
                    switch (status) {
                        case "ACCEPTED":
                            Toast.makeText(PassengerActivity.this,
                                "Вашето барање е прифатено!", 
                                Toast.LENGTH_LONG).show();
                            break;
                        case "REJECTED":
                            Toast.makeText(PassengerActivity.this,
                                "Вашето барање е одбиено", 
                                Toast.LENGTH_LONG).show();
                            break;
                        case "PENDING":
                            // Продолжи со проверка
                            new Handler(Looper.getMainLooper())
                                .postDelayed(this, 5000); // Провери повторно после 5 секунди
                            return;
                    }
                }
            }
        }, 5000); // Прва проверка после 5 секунди
    }

    private void loadActiveRides() {
        List<Ride> activeRides = databaseHelper.getPassengerActiveRides(currentUser.getId());
        activeRidesAdapter.updateRides(activeRides);
    }

    private void showRideCompletionDialog(Ride ride) {
        View view = getLayoutInflater().inflate(R.layout.dialog_complete_ride, null);
        EditText distanceInput = view.findViewById(R.id.distanceInput);
        TextView priceText = view.findViewById(R.id.priceText);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText reviewText = view.findViewById(R.id.reviewText);

        Driver driver = databaseHelper.getDriverById(ride.getDriverId());
        if (driver == null) return;

        distanceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double distance = Double.parseDouble(s.toString());
                    double price = distance * driver.getPricePerKm();
                    priceText.setText(String.format("Цена: %.2f ден.", price));
                } catch (NumberFormatException e) {
                    priceText.setText("Цена: 0.00 ден.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Заврши возење")
            .setView(view)
            .setPositiveButton("Потврди", (dialogInterface, i) -> {
                String distanceStr = distanceInput.getText().toString();
                if (!distanceStr.isEmpty()) {
                    double distance = Double.parseDouble(distanceStr);
                    double price = distance * driver.getPricePerKm();
                    float rating = ratingBar.getRating();
                    String comment = reviewText.getText().toString();

                    // Ажурира го возењето
                    ride.setPrice(price);
                    ride.setStatus("COMPLETED");
                    databaseHelper.updateRide(ride);

                    // Додади рејтинг и коментар
                    databaseHelper.addReview(currentUser.getId(), driver.getId(), rating, comment, ride.getId());

                    Toast.makeText(this, "Возењето е успешно завршено", Toast.LENGTH_SHORT).show();
                    loadActiveRides();
                }
            })
            .setNegativeButton("Откажи", null)
            .create();

        dialog.show();
    }
} 