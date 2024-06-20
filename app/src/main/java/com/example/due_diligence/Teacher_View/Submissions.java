package com.example.due_diligence.Teacher_View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.due_diligence.Adapter_Classes.Adapter_Submissions;
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

                adapter.setOnItemClickListener(new Adapter_Submissions.OnItemClickListener(){
                    @Override
                    public void onItemClick(int position) {

                    }
                });
        }
    });
    }

    public void Back(View view) {
        finish();
    }
}