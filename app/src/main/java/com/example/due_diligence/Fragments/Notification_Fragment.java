package com.example.due_diligence.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.due_diligence.ModelClasses.Notification;
import com.example.due_diligence.R;

import java.util.ArrayList;

public class Notification_Fragment extends Fragment {

    private static final String ARG_NOTIFICATIONS = "notifications";
    View view;

    public static Notification_Fragment newInstance(ArrayList<Notification> notifications) {
        Notification_Fragment fragment = new Notification_Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATIONS, notifications);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        ListView listView = view.findViewById(R.id.notifications_list);

        Bundle args = getArguments();
        if (args != null) {
            ArrayList<Notification> notifications = (ArrayList<Notification>) args.getSerializable(ARG_NOTIFICATIONS);
            ArrayList<String> notificationsString = new ArrayList<>();
            for (Notification notification : notifications) {
                notificationsString.add(notification.getMessage());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    notificationsString
            );
            listView.setAdapter(adapter);
        }

        return view;
    }

    public void hideNotifications() {
        view.setVisibility(View.GONE);
    }

    public void showNotifications() {
        view.setVisibility(View.VISIBLE);
    }
}
