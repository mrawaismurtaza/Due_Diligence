package com.example.due_diligence.Student_View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.Task;
import com.example.due_diligence.R;

import java.util.List;

public class Project_Details extends AppCompatActivity {

    TextView project_title;
    TextView no_of_submissions;
    TextView assigned;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Log.d("TAG", "onCreate: ");
        Intent intent = getIntent();
        project = (Project) intent.getSerializableExtra("project");
        Log.d("TAG", "onCreate: ");


        project_title = findViewById(R.id.projtitletxt);
        no_of_submissions = findViewById(R.id.noofsubtxt);
        assigned = findViewById(R.id.assignedtxt);
        
        setStats();
    }

    private void setStats() {
        Log.d("TAG", "setStats: ");
        project_title.setText(project.getName());
        no_of_submissions.setText(project.getTasks().size() + "");

        // Get the last task from the task list
        List<Task> tasks = project.getTasks();
        if (!tasks.isEmpty()) {
            Task lastTask = tasks.get(tasks.size() - 1);
            StringBuilder tasksStringBuilder = new StringBuilder();
            tasksStringBuilder.append(lastTask.getName());

            assigned.setText(tasksStringBuilder.toString());
        } else {
            assigned.setText("No tasks assigned");
        }
    }

    public void Add_Submission(View view) {
        Intent intent = new Intent(this, Add_Submission.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}