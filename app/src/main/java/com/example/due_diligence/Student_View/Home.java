package com.example.due_diligence.Student_View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.due_diligence.Adapter_Classes.Adapter_Home_Projects;
import com.example.due_diligence.Firebase.Authentication;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.Fragments.Notification_Fragment;
import com.example.due_diligence.ModelClasses.Notification;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.User;
import com.example.due_diligence.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ImageView notificationimg;
    RecyclerView recyclerView;
    TextView welcomenametxt, notification;
    ArrayList<Notification> notifications;
    private Authentication mAuth;
    Realtime_Database database;
    Notification_Fragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notificationimg = findViewById(R.id.notification_icon_view);
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

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            database.getUser(userId, new Realtime_Database.UserCallback() {
                @Override
                public void onUserCallback(User user) {
                    welcomenametxt.setText(user.getName().toString());
                    getNotifications(userId);
                }
            });

            notificationOnclick();

            onProjectLoad(userId);

        } else {
            Snackbar.make(this.getCurrentFocus(), "User not found", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void onProjectLoad(String userId) {
        database.getProjects(userId, new Realtime_Database.ProjectCallback() {
            @Override
            public void onProjectCallback(List<Project> projects) {
                Adapter_Home_Projects adapter = new Adapter_Home_Projects(Home.this, projects);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new Adapter_Home_Projects.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(Home.this, Project_Details.class);
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
    }

    private void getNotifications(String userId) {
        database.getNotifications(userId, new Realtime_Database.NotificationCallback() {
            @Override
            public void onNotificationCallback(int count, ArrayList<Notification> notificationsList) {
                notification.setText(String.valueOf(count));
                notifications = notificationsList;
                Log.d("TAG", "onNotificationCallback: " + count + " " + notificationsList.size());
            }
        });
    }

    private void notificationOnclick() {
        notificationimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifications != null && !notifications.isEmpty()) {
                    if (notificationFragment == null) {
                        notificationFragment = Notification_Fragment.newInstance(notifications);
                        addFragment(notificationFragment);
                    } else {
                        toggleFragmentVisibility(notificationFragment);
                    }
                } else {
                    Snackbar.make(v, "No notifications to show", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void toggleFragmentVisibility(Notification_Fragment fragment) {
        if (fragment.isVisible()) {
            fragment.hideNotifications();
        } else {
            fragment.showNotifications();
        }
    }

    public void Project_Request(View view) {
        Intent intent = new Intent(this, Project_Request.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            setDetails();
        }
    }
}
