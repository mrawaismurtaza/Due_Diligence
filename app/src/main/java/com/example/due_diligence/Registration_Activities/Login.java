package com.example.due_diligence.Registration_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.due_diligence.Firebase.Authentication;
import com.example.due_diligence.R;
import com.example.due_diligence.Student_View.Home;
import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    Authentication authentication;
    EditText emailtxt, passwordtxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authentication = new Authentication();

        emailtxt = findViewById(R.id.loginemailedt);
        passwordtxt = findViewById(R.id.loginpassedt);
    }

    public void Login(View view) {
        String email = emailtxt.getText().toString();
        String password = passwordtxt.getText().toString();

        authentication.loginUser(email, password, new Authentication.LoginCallback() {
            @Override
            public void onLoginResult(boolean success) {
                if (success) {
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Login failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}