package com.example.due_diligence.Teacher_View;

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

public class Project_Stats extends AppCompatActivity {

    TextView project_title;
    TextView no_of_submissions;
    TextView assigned;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_stats);

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
        String sub = String.valueOf(project.getSubmissionCount());
        no_of_submissions.setText(sub);

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

    public void Check_Submissions(View view) {
        Intent intent = new Intent(this, Submissions.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    public void Assign_Task(View view) {
        Intent intent = new Intent(this, New_Task.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}