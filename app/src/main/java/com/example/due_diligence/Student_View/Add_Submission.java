package com.example.due_diligence.Student_View;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.due_diligence.Firebase.Cloud_Storage;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.R;

import java.net.URL;

public class Add_Submission extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    ProgressBar progressBar;
    Cloud_Storage storage;
    Uri data;
    Realtime_Database database;
    Project project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_submission);
        project = (Project) getIntent().getSerializableExtra("project");
    }



    public void Submission_Upload(View view) {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                this.data = data.getData();
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    public void Add_Submission(View view) {
        storage = new Cloud_Storage();
        storage.addSubmission(data, new Cloud_Storage.SubmissionCallback() {
            @Override
            public void onUploadCallback(String uri) {
                if (uri != null) {
                    database = new Realtime_Database();
                    database.addSubmission(uri, project, new Realtime_Database.SubmissionCallback() {
                        @Override
                        public void onSubmissionCallback(String submissionId) {
                            if (submissionId != null) {
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}