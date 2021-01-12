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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    // Set FirebaseAuth - The authentication of Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "check1"; // String for the check in "Logcat"

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Give you to see the design of your Activity, Give you access to use the ids of the elements

        initUI();
        initListeners();
    }

    @Override
    protected void onStart() { // onStart - Called when Activity is becoming visible to the user
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener); // Listener of FirebaseAuth
    }

    private void initUI() {
        // Initialize id to the element
        tvRegister = findViewById(R.id.tvRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        // Check if the user login before to the Firebase(and not sign out), if login before - intent Activity from LoginActivity to MainMenuActivity
        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                // Destroy the current Activity
                finish();
            }
        };
    }

    private void initListeners() {
        // Give access of clickable to the elements are need to be clicked
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void login() {
        // Get the texts are written in the EditText
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        // Check if the email and the password are invalid
        if (!EmailPasswordValidator.getInstance().isValidEmail(email) && !EmailPasswordValidator.getInstance().isValidPassword(password)) {
            // Pop error on the EditText
            etEmail.setError("The email is invalid");
            etPassword.setError("The password is invalid");
            // Emphasize the EditText with the error
            etEmail.requestFocus();
            etPassword.requestFocus();
            // Check if the email is invalid
        } else if (!EmailPasswordValidator.getInstance().isValidEmail(email)) {
            // Pop error on the EditText
            etEmail.setError("The email is invalid");
            // Emphasize the EditText with the error
            etEmail.requestFocus();
            // Check if the password is invalid
        } else if (!EmailPasswordValidator.getInstance().isValidPassword(password)) {
            // Pop error on the EditText
            etPassword.setError("The password is invalid");
            // Emphasize the EditText with the error
            etPassword.requestFocus();
            // If the email and password are valid we are get in to the executes in the else
        } else {
            // We are put in the email and password into the method signInWithEmailAndPassword()
            mAuth.signInWithEmailAndPassword(email, password)
                    // After trying to connect to Firebase authentication, we check the following
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        // If we get ok to connect to Firebase authentication, we'll move from LoginActivity to MainMenuActivity
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithEmail:success");

                            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                            // Destroy the current Activity
                            finish();
                            // If we not get ok to connect to Firebase authentication, we'll pop Toast
                        } else {
                            Log.e(TAG, "singInWithEmail:Fail");

                            Toast.makeText(LoginActivity.this, "The login failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    // Perform executes when elements are clicked
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                login();
                break;
        }
    }

}
