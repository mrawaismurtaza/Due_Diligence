package com.example.due_diligence.Student_View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.due_diligence.Firebase.Cloud_Storage;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.R;

public class Add_Submission extends AppCompatActivity {

    EditText detailEditText;
    private static final int PICK_FILE_REQUEST = 1;
    ProgressBar progressBar;
    Cloud_Storage storage;
    Uri fileUri;
    Realtime_Database database;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_submission);

        project = (Project) getIntent().getSerializableExtra("project");

        detailEditText = findViewById(R.id.subdetailedt);
        progressBar = findViewById(R.id.progressBar);
    }

    public void Submission_Upload(View view) {
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
                fileUri = data.getData();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "File selected: " + fileUri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Add_Submission(View view) {
        String detail = detailEditText.getText().toString();

        if (fileUri == null) {
            Toast.makeText(this, "Please select a file first.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        storage = new Cloud_Storage();
        storage.addSubmission(fileUri, new Cloud_Storage.SubmissionCallback() {
            @Override
            public void onUploadCallback(String uri) {
                if (uri != null) {
                    database = new Realtime_Database();
                    database.addSubmission(detail, uri, project, new Realtime_Database.SubmissionCallback() {
                        @Override
                        public void onSubmissionCallback(String submissionId) {
                            progressBar.setVisibility(View.GONE);
                            if (submissionId != null) {
                                Toast.makeText(Add_Submission.this, "Submission added successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Add_Submission.this, "Failed to add submission.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Add_Submission.this, "Failed to upload file.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
