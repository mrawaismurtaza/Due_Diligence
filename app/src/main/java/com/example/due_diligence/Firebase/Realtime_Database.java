package com.example.due_diligence.Firebase;

import android.util.Log;
import android.view.View;

import com.example.due_diligence.ModelClasses.Notification;
import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.ModelClasses.Task;
import com.example.due_diligence.ModelClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Realtime_Database {

    private FirebaseDatabase database;
    private DatabaseReference usersRef, projectsRef, notificationsRef;
    private Authentication authentication;

    public Realtime_Database() {
        database = FirebaseDatabase.getInstance("https://due-diligence-28ddc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        usersRef = database.getReference("users");
        projectsRef = database.getReference("projects");
        notificationsRef = database.getReference("notifications");
        authentication = new Authentication();
    }

    public void addUser(String email, String name, String role, View view) {
        String userId = authentication.getCurrentUser().getUid();
        Log.d("TAG", "addUser: " + userId);
        User user = new User(email, name, role);

        usersRef.child(userId).child("email").setValue(user.getEmail());
        usersRef.child(userId).child("name").setValue(user.getName());
        usersRef.child(userId).child("role").setValue(user.getRole());

        Notification dummyNotification = new Notification("Welcome to the app!", false, userId);
        notificationsRef.push().setValue(dummyNotification);

        Task task1 = new Task("Task 1", "completed");
        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);
        String projectId = projectsRef.push().getKey();
        Project dummyProject = new Project(userId, "Dummy Project", "This is a dummy project", "Awais", taskList, projectId, "sample proposal uri", 0);
        projectsRef.child(projectId).setValue(dummyProject);
    }


    public void getProjects(String userId, Realtime_Database.ProjectCallback projectCallback) {
        projectsRef.orderByChild("studentId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String supervisor = snapshot.child("supervisor").getValue(String.class);
                    List<Task> tasks = new ArrayList<>();
                    for (DataSnapshot taskSnapshot : snapshot.child("tasks").getChildren()) {
                        String taskName = taskSnapshot.child("name").getValue(String.class);
                        String taskStatus = taskSnapshot.child("status").getValue(String.class);
                        Task task = new Task(taskName, taskStatus);
                        tasks.add(task);
                    }
                    String id = snapshot.child("id").getValue(String.class);
                    String proposalUrl = snapshot.child("proposalUrl").getValue(String.class);
                    int submissionCount = snapshot.child("submissionCount").getValue(int.class); // Use primitive int
                    Project project = new Project(userId, name, description, supervisor, tasks, id, proposalUrl, submissionCount);
                    projects.add(project);
                }
                projectCallback.onProjectCallback(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void createProject(String userId, String description, String name, String supervisor, String memberEmail, String url, Realtime_Database.ProjectCreateCallback projectCreateCallback) {
        String projectId = projectsRef.push().getKey();
        List<Task> taskList = new ArrayList<>();
        Project project = new Project(userId, name, description, supervisor, taskList, projectId, url, 0);
        projectsRef.child(projectId).setValue(project);
        projectCreateCallback.onProjectCallback(projectId);
    }

    public void addSubmission(String uri, Project project, Realtime_Database.SubmissionCallback submissionCallback) {
        String userId = authentication.getCurrentUser().getUid();
        int submissions = project.getSubmissionCount();
        String id = project.getId();
        Log.d("TAG", "addSubmission: " + id);
        String name = project.getName();
        String description = project.getDescription();
        String supervisor = project.getSupervisor();
        List<Task> tasks = project.getTasks();
        String proposalUrl = project.getProposalUrl();
        Project updatedProject = new Project(userId, name, description, supervisor, tasks, id, proposalUrl, submissions + 1);
        projectsRef.child(id).setValue(updatedProject);
        submissionCallback.onSubmissionCallback(id);
    }

    public void getTeacherProjects(String email, Realtime_Database.TeacherProjectCallback teacherProjectCallback) {
        projectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Project> projects = new ArrayList<>();
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    String supervisor = projectSnapshot.child("supervisor").getValue(String.class);
                    if (supervisor != null && supervisor.equals(email)) {
                        Log.d("TAG", "onDataChange: " + projectSnapshot.child("supervisor").getValue(String.class));
                        String name = projectSnapshot.child("name").getValue(String.class);
                        String description = projectSnapshot.child("description").getValue(String.class);
                        List<Task> tasks = new ArrayList<>();
                        for (DataSnapshot taskSnapshot : projectSnapshot.child("tasks").getChildren()) {
                            String taskName = taskSnapshot.child("name").getValue(String.class);
                            String taskStatus = taskSnapshot.child("status").getValue(String.class);
                            Task task = new Task(taskName, taskStatus);
                            tasks.add(task);
                        }
                        String id = projectSnapshot.child("id").getValue(String.class);
                        String proposalUrl = projectSnapshot.child("proposalUrl").getValue(String.class);
                        int submissionCount = projectSnapshot.child("submissionCount").getValue(int.class);
                        // Use primitive int
                        String studentId = projectSnapshot.child("studentId").getValue(String.class);
                        Project project = new Project(studentId, name, description, supervisor, tasks, id, proposalUrl, submissionCount);
                        projects.add(project);
                    }
                }
                teacherProjectCallback.onProjectCallback(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void addTask(String taskName, Project project) {
        String id = project.getId();
        List<Task> tasks = project.getTasks();
        Task task = new Task(taskName, "pending");
        tasks.add(task);
        Project updatedProject = new Project(project.getStudentId(), project.getName(), project.getDescription(), project.getSupervisor(), tasks, id, project.getProposalUrl(), project.getSubmissionCount());
        projectsRef.child(id).setValue(updatedProject);
    }

    // For getting User Details from REALTIME

    public interface UserCallback {
        void onUserCallback(User user);
    }

    public void getUser(String userId, Realtime_Database.UserCallback userCallback) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    User user = new User(email, name, role);
                    if (user != null) {
                        userCallback.onUserCallback(user);
                    } else {
                        Log.d("TAG", "User object is null");
                    }
                } else {
                    Log.d("TAG", "Data snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Database error: " + databaseError.getMessage());
            }
        });
    }

    // For getting Notifications from REALTIME

    public interface NotificationCallback {
        void onNotificationCallback(int count);
    }

    public void getNotifications(String userId, NotificationCallback notificationCallback) {
        notificationsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                notificationCallback.onNotificationCallback((int) count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    // get projects by getting teacher email and check it if it is in project supervisor

    public interface ProjectCallback {
        void onProjectCallback(List<Project> projects);
    }

    public interface ProjectCreateCallback {
        void onProjectCallback(String projectId);
    }

    public interface SubmissionCallback {
        void onSubmissionCallback(String submissionId);
    }

    public interface TeacherProjectCallback {
        void onProjectCallback(List<Project> projects);
    }
}
