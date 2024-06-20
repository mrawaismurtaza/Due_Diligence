package com.example.due_diligence.Teacher_View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.due_diligence.Firebase.Realtime_Database;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.R;

public class New_Task extends AppCompatActivity {

    TextView project_name;
    EditText task;
    Project project;
    Realtime_Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        project = (Project) getIntent().getSerializableExtra("project");

        database = new Realtime_Database();

        project_name = findViewById(R.id.projtitletxt);
        task = findViewById(R.id.assignedtxt);

        setDetails();
    }

    private void setDetails() {
        project_name.setText(project.getName());
    }

    public void Assign_New_Task(View view) {
        String task_name = task.getText().toString();
        database.addTask(task_name, project);
        finish();
    }
}