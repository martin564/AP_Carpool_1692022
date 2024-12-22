package com.example.carpoolingapk1692022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.carpoolingapk1692022.database.DatabaseHelper;
import com.example.carpoolingapk1692022.models.User;
import com.example.carpoolingapk1692022.models.Driver;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginConfirmButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, 
                        "Внесете ги сите полиња", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = databaseHelper.checkLogin(email, password);
                
                if (user != null) {
                    // Постави го тековниот корисник
                    DatabaseHelper.setCurrentUserId(user.getId());

                    // Успешна најава
                    if (user.isDriver()) {
                        // Навигација кон Driver активност
                        Intent intent = new Intent(LoginActivity.this, DriverActivity.class);
                        intent.putExtra("USER_ID", user.getId());
                        startActivity(intent);
                    } else {
                        // Навигација кон Passenger активност
                        Intent intent = new Intent(LoginActivity.this, PassengerActivity.class);
                        intent.putExtra("USER_ID", user.getId());
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                        "Погрешна е-пошта или лозинка", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
} 