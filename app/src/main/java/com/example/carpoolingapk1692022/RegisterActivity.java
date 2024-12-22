package com.example.carpoolingapk1692022;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.Driver;
import com.example.carpoolingapk1692022.models.User;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameInput;
    private TextInputEditText surnameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton registerButton;
    private RadioGroup userTypeGroup;
    private LinearLayout driverDetailsLayout;
    private TextInputEditText carModelInput;
    private TextInputEditText carPlateInput;
    private TextInputEditText carYearInput;
    private TextInputEditText seatsInput;
    private TextInputEditText priceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        surnameInput = findViewById(R.id.surnameInput);
        emailInput = findViewById(R.id.emailRegInput);
        passwordInput = findViewById(R.id.passwordRegInput);
        registerButton = findViewById(R.id.registerConfirmButton);
        userTypeGroup = findViewById(R.id.userTypeGroup);
        driverDetailsLayout = findViewById(R.id.driverDetailsLayout);
        carModelInput = findViewById(R.id.carModelInput);
        carPlateInput = findViewById(R.id.carPlateInput);
        carYearInput = findViewById(R.id.carYearInput);
        seatsInput = findViewById(R.id.seatsInput);
        priceInput = findViewById(R.id.priceInput);

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.driverRadio) {
                driverDetailsLayout.setVisibility(View.VISIBLE);
            } else {
                driverDetailsLayout.setVisibility(View.GONE);
            }
        });

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String surname = surnameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            boolean isDriver = userTypeGroup.getCheckedRadioButtonId() == R.id.driverRadio;

            if (name.isEmpty() || surname.isEmpty() || 
                email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, 
                    "Внесете ги сите полиња", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(RegisterActivity.this);

            if (isDriver) {
                String carModel = carModelInput.getText().toString();
                String carPlate = carPlateInput.getText().toString();
                String carYearStr = carYearInput.getText().toString();
                String seatsStr = seatsInput.getText().toString();
                String priceStr = priceInput.getText().toString();

                if (carModel.isEmpty() || carPlate.isEmpty() || 
                    carYearStr.isEmpty() || seatsStr.isEmpty() || 
                    priceStr.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                        "Внесете ги сите полиња за возило", Toast.LENGTH_SHORT).show();
                    return;
                }

                Driver driver = new Driver(name, surname, email, password,
                    carModel, carPlate, 
                    Integer.parseInt(carYearStr),
                    Integer.parseInt(seatsStr),
                    Double.parseDouble(priceStr));

                long userId = db.addUser(driver);
                if (userId != -1 && db.addDriver(driver, userId)) {
                    Toast.makeText(RegisterActivity.this,
                        "Успешна регистрација!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                        "Грешка при регистрација", Toast.LENGTH_SHORT).show();
                }
            } else {
                User user = new User(name, surname, email, password, false);
                long userId = db.addUser(user);
                if (userId != -1) {
                    Toast.makeText(RegisterActivity.this,
                        "Успешна регистрација!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                        "Грешка при регистрација", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
} 