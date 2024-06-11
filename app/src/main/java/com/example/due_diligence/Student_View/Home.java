package com.example.due_diligence.Student_View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.due_diligence.Firebase.Authentication;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.User;
import com.example.due_diligence.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomenametxt, notification;
    private Authentication mAuth;
    Realtime_Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = new Authentication();
        FirebaseUser user = mAuth.getCurrentUser();

        welcomenametxt = findViewById(R.id.welcomenametxt);
        notification = findViewById(R.id.notificationtxt);

        recyclerView = findViewById(R.id.projectrecycler);

        setDetails();
    }
    private void setDetails() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            database = new Realtime_Database();

            database.getUser(userId, new Realtime_Database.UserCallback() {
                @Override
                public void onUserCallback(User user) {
                    welcomenametxt.setText(user.getName().toString());
                    database.getNotifications(userId, new Realtime_Database.NotificationCallback() {
                        @Override
                        public void onNotificationCallback(int count) {
                            notification.setText(String.valueOf(count));
                        }
                    });
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        } else {
            Snackbar.make(this.getCurrentFocus(), "User not found", Snackbar.LENGTH_SHORT).show();
        }
    }

}