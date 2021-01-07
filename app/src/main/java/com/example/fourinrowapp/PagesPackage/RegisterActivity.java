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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;
    private EditText etEmail, etPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private static final String TAG = "check1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        initListeners();
    }

    private void initUI() {
        tvLogin = findViewById(R.id.tvLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initListeners() {
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void register() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (!EmailPasswordValidator.getInstance().isValidEmail(email) && !EmailPasswordValidator.getInstance().isValidPassword(password)) {
            etEmail.setError("The email is invalid");
            etPassword.setError("The password is invalid");
            etEmail.requestFocus();
            etPassword.requestFocus();
        } else if (!EmailPasswordValidator.getInstance().isValidEmail(email)) {
            etEmail.setError("The email is invalid");
            etEmail.requestFocus();
        } else if (!EmailPasswordValidator.getInstance().isValidPassword(password)) {
            etPassword.setError("The password is invalid");
            etPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "createUserWithEmail:success");
                            startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "The register failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                onBackPressed();
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

}
