package com.example.due_diligence.Firebase;

import android.util.Log;
import android.view.View;

import com.example.due_diligence.ModelClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.rpc.context.AttributeContext;

import java.io.Serializable;

public class Authentication implements Serializable {
    FirebaseAuth mAuth;

    public Authentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }


    public void registerUser(String email, String password, Authentication.RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onRegisterResult(true);
                    } else {
                        callback.onRegisterResult(false);
                    }
                });
    }


    public void loginUser(String email, String password, Authentication.LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onLoginResult(true);
                        Log.d("TAG", "loginUser: " + mAuth.getCurrentUser().getUid());
                    } else {
                        callback.onLoginResult(false);
                        Log.d("TAG", "loginUser: " + task.getException().getMessage());
                    }
                });
    }


    public interface LoginCallback {
        void onLoginResult(boolean success);
    }

    public interface RegisterCallback {
        void onRegisterResult(boolean success);
    }
}
