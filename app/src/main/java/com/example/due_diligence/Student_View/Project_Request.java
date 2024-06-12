package com.example.due_diligence.Student_View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.due_diligence.Firebase.Cloud_Storage;
import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.R;
import com.google.firebase.auth.FirebaseAuth;

public class Project_Request extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    EditText TeacherEmail, memberEmail, projectName;
    Cloud_Storage storage;
    Realtime_Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_request);

        TeacherEmail = findViewById(R.id.teacheredt);
        memberEmail = findViewById(R.id.memberedt);
        projectName = findViewById(R.id.projectnameedt);
    }

    public void Upload_Proposal(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();
            }
        }
    }

    public void Sent_Propsal(View view) {
        if (fileUri != null) {
            storage = new Cloud_Storage();
            database = new Realtime_Database();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            storage.uploadFile(fileUri, userId, new Cloud_Storage.UploadCallback() {
                @Override
                public void onUploadCallback(String url) {
//                    database.sendRequest(userId, TeacherEmail.getText().toString(), memberEmail.getText().toString(), projectName.getText().toString(), url);
                }
            });
        }
    }
}