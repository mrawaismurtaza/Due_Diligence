package com.example.due_diligence.Teacher_View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.due_diligence.Adapter_Classes.Adapter_Submissions;
import com.example.due_diligence.Firebase.Cloud_Storage;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.Submission;
import com.example.due_diligence.R;

import java.util.List;

public class Submissions extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView projectname;
    Realtime_Database database;
    Project project;
    Cloud_Storage cloud_storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);

        project = (Project) getIntent().getSerializableExtra("project");

        projectname = findViewById(R.id.projtitletxt);

        recyclerView = findViewById(R.id.subrecycler);

        database = new Realtime_Database();

        setDetails();
    }

    private void setDetails() {

        projectname.setText(project.getName());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        database.getSubmissions(project.getId(), new Realtime_Database.GetSubmissionCallback() {
            @Override
            public void onSubmissionCallback(List<Submission> submissions) {
                Adapter_Submissions adapter = new Adapter_Submissions(submissions, Submissions.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                adapter.setOnItemClickListener(new Adapter_Submissions.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        cloud_storage = new Cloud_Storage();
                        Submission selectedSubmission = submissions.get(position);
                        if (selectedSubmission != null) {
                            Log.d("TAG", "onItemClick: " + selectedSubmission.getSubmissionURL());
                            cloud_storage.downloadFile(Submissions.this, selectedSubmission, selectedSubmission.getSubmissionURL());
                        } else {
                            Log.d("TAG", "onItemClick: " + "No Submission Found");
                        }
                    }
                });
            }
        });
    }

    public void Back(View view) {
        finish();
    }
}