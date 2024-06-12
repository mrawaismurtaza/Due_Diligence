package com.example.due_diligence.Firebase;

import android.icu.text.UnicodeSet;
import android.util.Log;
import android.view.View;

import com.example.due_diligence.ModelClasses.Notification;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.Task;
import com.example.due_diligence.ModelClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Realtime_Database {

    private FirebaseDatabase database;
    private DatabaseReference usersRef, projectsRef, notificationsRef;
    private Authentication authentication;

    public Realtime_Database() {
        database = FirebaseDatabase.getInstance("https://due-diligence-28ddc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        usersRef = database.getReference("users");
        projectsRef = database.getReference("projects");
        notificationsRef = database.getReference("notifications");
        authentication = new Authentication();
    }


    public void addUser(String email, String name, String role, View view) {
        String userId = authentication.getCurrentUser().getUid();
        Log.d("TAG", "addUser: " + userId);
        User user = new User(email, name, role);

        usersRef.child(userId).child("email").setValue(user.getEmail());
        usersRef.child(userId).child("name").setValue(user.getName());
        usersRef.child(userId).child("role").setValue(user.getRole());

        Notification dummyNotification = new Notification("Welcome to the app!", false);
        notificationsRef.child(userId).push().setValue(dummyNotification);

        Task task1 = new Task("Task 1", "completed");
        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);
        Project dummyProject = new Project("Dummy Project", "This is a dummy project", "Awais", taskList);
        projectsRef.child(userId).push().setValue(dummyProject);


    }

    public void getProjects(String userId, Realtime_Database.ProjectCallback projectCallback) {
        projectsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String supervisor = snapshot.child("supervisor").getValue(String.class);
                    List<Task> tasks = new ArrayList<>();
                    for (DataSnapshot taskSnapshot : snapshot.child("tasks").getChildren()) {
                        String taskName = taskSnapshot.child("name").getValue(String.class);
                        String taskStatus = taskSnapshot.child("status").getValue(String.class);
                        Task task = new Task(taskName, taskStatus);
                        tasks.add(task);
                    }
                    Project project = new Project(name, description, supervisor, tasks);
                    projects.add(project);
                }
                projectCallback.onProjectCallback(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }


    //For getting User Deatails from REALTIME

    public interface UserCallback {
        void onUserCallback(User user);
    }

    public void getUser(String userId, Realtime_Database.UserCallback userCallback) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    User user = new User(email, name, role);
                    if (user != null) {
                        userCallback.onUserCallback(user);
                    } else {
                        Log.d("TAG", "User object is null");
                    }
                } else {
                    Log.d("TAG", "Data snapshot does not exist");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Database error: " + databaseError.getMessage());
            }
        });
    }


    //For getting Notifications from REALTIME

    public interface NotificationCallback {
        void onNotificationCallback(int count);
    }

    public void getNotifications(String userId, NotificationCallback notificationCallback) {
        usersRef.child("notifications").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                notificationCallback.onNotificationCallback((int) count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }


    public interface ProjectCallback {
        void onProjectCallback(List<Project> projects);
    }
}
