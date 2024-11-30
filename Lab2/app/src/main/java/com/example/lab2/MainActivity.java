package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    // Declare UI components and DB
    TextInputEditText email, password;
    TextInputLayout emailInputLayout, passwordInputLayout;
    Button signIn;
    TextView signUp, forgotPassword;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Initialize UI components and DB
        forgotPassword = findViewById(R.id.forgotPassword);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        DB = new DBHelper(this);

        // Navigate to the RegisterPage
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Navigating to RegisterPage.");
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        // Navigate to ForgotPassword
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Navigating to ResetPassword.");
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        // Sign In Button Click Handler
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                // Reset errors at the beginning
                emailInputLayout.setError(null);
                passwordInputLayout.setError(null);

                Log.d("MainActivity", "Attempting to log in for user: " + user);

                // Check for empty or invalid inputs and show specific errors
                if (TextUtils.isEmpty(user)) {
                    emailInputLayout.setError("Email cannot be empty");
                    Log.d("MainActivity", "Login failed: Email empty.");
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    emailInputLayout.setError("Invalid email address");
                    Log.d("MainActivity", "Login failed: Invalid email.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    passwordInputLayout.setError("Password cannot be empty");
                    Log.d("MainActivity", "Login failed: Password empty.");
                    return;
                }

                // If inputs are valid, proceed to check credentials
                Boolean checkUserPass = DB.checkUserPassword(user, pass);
                if (checkUserPass) {
                    Log.d("MainActivity", "Login successful for user: " + user);
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class); // Navigate to Dashboard
                    startActivity(intent);
                } else {
                    Log.d("MainActivity", "Login failed: User does not exist or password incorrect.");
                    Toast.makeText(MainActivity.this, "Login Failed, User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
