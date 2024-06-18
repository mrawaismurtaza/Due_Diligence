package com.example.due_diligence.Registration_Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.due_diligence.Firebase.Authentication;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.User;
import com.example.due_diligence.R;
import com.example.due_diligence.Student_View.Home;
import com.example.due_diligence.Teacher_View.Home_Teacher;
import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    Authentication authentication;
    Realtime_Database database;
    EditText emailtxt, passwordtxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authentication = new Authentication();

        database = new Realtime_Database();

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
                    String userId = authentication.getCurrentUser().getUid();
                    database.getUser(userId, new Realtime_Database.UserCallback() {
                        @Override
                        public void onUserCallback(User user) {
                            if (user.getRole().equals("student")) {
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Login.this, Home_Teacher.class);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Snackbar.make(view, "Login failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}