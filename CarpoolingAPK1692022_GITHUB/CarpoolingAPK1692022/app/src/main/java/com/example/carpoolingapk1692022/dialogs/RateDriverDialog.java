package com.example.carpoolingapk1692022.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.carpoolingapk1692022.R;
import com.example.carpoolingapk1692022.models.Driver;
import com.google.android.material.button.MaterialButton;

public class RateDriverDialog extends Dialog {
    private Driver driver;
    private OnRatingSubmittedListener listener;

    public interface OnRatingSubmittedListener {
        void onRatingSubmitted(double rating);
    }

    public RateDriverDialog(@NonNull Context context, Driver driver, OnRatingSubmittedListener listener) {
        super(context);
        this.driver = driver;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_driver);

        TextView driverNameText = findViewById(R.id.driverNameText);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        MaterialButton cancelButton = findViewById(R.id.cancelButton);
        MaterialButton submitButton = findViewById(R.id.submitButton);

        driverNameText.setText(driver.getFullName());

        cancelButton.setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            listener.onRatingSubmitted(ratingBar.getRating());
            dismiss();
        });
    }
} 