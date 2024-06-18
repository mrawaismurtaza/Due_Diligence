package com.example.due_diligence.Teacher_View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.due_diligence.Adapter_Classes.Adapter_Home_Projects;
import com.example.due_diligence.Firebase.Authentication;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.User;
import com.example.due_diligence.R;
import com.example.due_diligence.Student_View.Home;
import com.example.due_diligence.Student_View.Project_Details;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Home_Teacher extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomenametxt, notification;
    private Authentication mAuth;
    Realtime_Database database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

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
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            database.getProjects(userId, new Realtime_Database.ProjectCallback() {
                @Override
                public void onProjectCallback(List<Project> projects) {
                    Adapter_Home_Projects adapter = new Adapter_Home_Projects( Home_Teacher.this, projects);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    adapter.setOnItemClickListener(new Adapter_Home_Projects.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(Home_Teacher.this, Project_Stats.class);
                            Project selectedProject = projects.get(position);
                            if (selectedProject != null) {
                                Log.d("TAG", "onItemClick: " + selectedProject.getName());
                                intent.putExtra("project", selectedProject);
                                startActivity(intent);
                            } else {
                                Snackbar.make(getCurrentFocus(), "Project not found", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        } else {
            Snackbar.make(this.getCurrentFocus(), "User not found", Snackbar.LENGTH_SHORT).show();
        }
    }
}