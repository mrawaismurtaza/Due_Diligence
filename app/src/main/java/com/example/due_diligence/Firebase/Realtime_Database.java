package com.example.due_diligence.Firebase;

import android.icu.text.UnicodeSet;
import android.util.Log;
import android.view.View;

import com.example.due_diligence.ModelClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Realtime_Database {

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private Authentication authentication;

    public Realtime_Database() {
        database = FirebaseDatabase.getInstance("https://due-diligence-28ddc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        usersRef = database.getReference("users");
        authentication = new Authentication();
    }


    public void addUser(String email, String name, String role, View view) {
        String userId = authentication.getCurrentUser().getUid();
        Log.d("TAG", "addUser: " + userId);
        User user = new User(email, name, role);
        usersRef.child(userId).setValue(user);

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





}
