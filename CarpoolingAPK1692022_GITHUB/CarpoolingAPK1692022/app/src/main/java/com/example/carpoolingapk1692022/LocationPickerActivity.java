package com.example.carpoolingapk1692022;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Geocoder geocoder;
    private TextView addressText;
    private MaterialButton confirmButton;
    private static final String TAG = "LocationPickerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        // Иницијализација
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        addressText = findViewById(R.id.addressText);
        confirmButton = findViewById(R.id.confirmButton);

        // Иницијализација на мапата
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        confirmButton.setOnClickListener(v -> {
            String address = addressText.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("address", address);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady called");
        mMap = googleMap;

        // Конфигурирај ја мапата
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Постави default локација (Скопје)
        LatLng defaultLocation = new LatLng(41.9981, 21.4254);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        if (checkLocationPermission()) {
            setupMap();
        } else {
            requestLocationPermission();
        }

        // Додадете маркер кога корисникот ќе кликне на мапата
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Избрана локација"));
            getAddressFromLocation(latLng);
        });
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void setupMap() {
        if (checkLocationPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
                
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(
                                        location.getLatitude(),
                                        location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        currentLocation, 15));
                                getAddressFromLocation(currentLocation);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Ако не може да добие локацијата, користи default
                            LatLng skopje = new LatLng(41.9981, 21.4254);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(skopje, 12));
                        });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAddressFromLocation(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressString = address.getAddressLine(0);
                addressText.setText(addressString);
                confirmButton.setEnabled(true);
            } else {
                addressText.setText("Не може да се најде адреса");
                confirmButton.setEnabled(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressText.setText("Грешка при добивање адреса");
            confirmButton.setEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            }
        }
    }
} 