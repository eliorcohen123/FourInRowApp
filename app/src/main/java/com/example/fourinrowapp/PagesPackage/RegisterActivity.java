package com.example.fourinrowapp.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fourinrowapp.R;
import com.example.fourinrowapp.UtilsPackage.EmailPasswordValidator;
import com.google.firebase.auth.FirebaseAuth;

// AppCompatActivity - Give you access to use the LifeCycle of Activity.
// View.OnClickListener - Give you access to use the function - onClick() - Order the clicks on elements
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;
    private EditText etEmail, etPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth; // Set FirebaseAuth - The authentication of Firebase
    private static final String TAG = "check1"; // String for the check in "Logcat"

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Give you to see the design of your Activity, give you access to use the ids of the elements

        initUI();
        initListeners();
    }

    private void initUI() {
        // Initialize id to the element
        tvLogin = findViewById(R.id.tvLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
    }

    private void initListeners() {
        // Give access of clickable to the elements are need to be clicked
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void register() {
        // Get the texts are written in the EditText
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        // Check if the email and the password are invalid
        if (!EmailPasswordValidator.getInstance().isValidEmail(email) && !EmailPasswordValidator.getInstance().isValidPassword(password)) {
            // Pop error on the EditText
            etEmail.setError("The email is invalid");
            etPassword.setError("The password is invalid");
            // Navigate to the EditText with the error
            etEmail.requestFocus();
            etPassword.requestFocus();
            // Check if the email is invalid
        } else if (!EmailPasswordValidator.getInstance().isValidEmail(email)) {
            etEmail.setError("The email is invalid");
            etEmail.requestFocus();
        } else if (!EmailPasswordValidator.getInstance().isValidPassword(password)) {
            // Pop error on the EditText
            etPassword.setError("The password is invalid");
            // Check if the password is invalid
            etPassword.requestFocus();
            // If the email and password are valid we are get in to the executes in the else
        } else {
            // We are put in the email and password into the method signInWithEmailAndPassword()
            mAuth.createUserWithEmailAndPassword(email, password)
                    // After trying to connect to Firebase authentication, we check the following
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        // If we get ok to connect to Firebase authentication, we'll move from LoginActivity to MainMenuActivity
                        if (task.isSuccessful()) {
                            Log.i(TAG, "createUserWithEmail:success");

                            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                            finish();
                            // If we not get ok to connect to Firebase authentication, we'll pop Toast
                        } else {
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(RegisterActivity.this, "The register failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Perform executes when elements are clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                onBackPressed(); // Back to the previous Activity
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

}
