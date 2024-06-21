package com.example.due_diligence.Firebase;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.due_diligence.ModelClasses.Submission;
import com.example.due_diligence.Teacher_View.Submissions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Cloud_Storage {

    StorageReference storageReference;
    private long downloadId;

    public Cloud_Storage() {
        storageReference = FirebaseStorage.getInstance("gs://due-diligence-28ddc.appspot.com").getReference();
    }

    public void uploadFile(Uri fileUri, String userId, String projectId, UploadCallback uploadCallback) {
        StorageReference fileReference = storageReference.child("proposals/"  + fileUri.getLastPathSegment());
        fileReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                uploadCallback.onUploadCallback(uri.toString());
            });
        }).addOnFailureListener(e -> {
            uploadCallback.onUploadCallback(null);
        });
    }


    public void getProposal(String userId, String projectId, FileUrlCallback callback) {
        StorageReference proposalRef = storageReference.child("proposals/" + userId + "/" + projectId);

        proposalRef.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().isEmpty()) {
                callback.onFileUrlCallback(null); // No proposal found
            } else {
                // Assuming there's only one proposal file per project
                StorageReference proposalFileRef = listResult.getItems().get(0);
                proposalFileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    callback.onFileUrlCallback(uri.toString());
                }).addOnFailureListener(e -> {
                    callback.onFileUrlCallback(null);
                });
            }
        }).addOnFailureListener(e -> {
            callback.onFileUrlCallback(null);
        });
    }

    public void addSubmission(Uri data, SubmissionCallback SubmissionCallback) {
        StorageReference fileReference = storageReference.child("submissions/" + data.getLastPathSegment());
        fileReference.putFile(data).addOnSuccessListener(taskSnapshot -> {
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                SubmissionCallback.onUploadCallback(uri.toString());
            });
        }).addOnFailureListener(e -> {
            SubmissionCallback.onUploadCallback(null);
        });
    }

    public void downloadFile(Context context, Submission submission, String submissionURL) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(submissionURL);
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Downloading " + submission.getSubmissionId());
            request.setDescription("Downloading file...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, submission.getSubmissionId() + "_" + submissionURL);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

            if (downloadManager != null) {
                downloadId = downloadManager.enqueue(request);
                Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show();
                registerDownloadReceiver(context); // Register BroadcastReceiver for download complete
            } else {
                Toast.makeText(context, "Download manager is not available", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {
            Toast.makeText(context, "Failed to get download URL: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void registerDownloadReceiver(Context context) {
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                    context.unregisterReceiver(this); // Unregister receiver after handling
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    public interface SubmissionCallback {
        void onUploadCallback(String uri);
    }

    public interface FileUrlCallback {
        void onFileUrlCallback(String url);
    }



    public interface UploadCallback {
        void onUploadCallback(String url);
    }
}
