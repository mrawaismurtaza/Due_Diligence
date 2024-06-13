package com.example.due_diligence.Firebase;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Cloud_Storage {

    StorageReference storageReference;

    public Cloud_Storage() {
        storageReference = FirebaseStorage.getInstance("gs://due-diligence-28ddc.appspot.com").getReference();
    }

    public void uploadFile(Uri fileUri, String userId, String projectId, UploadCallback uploadCallback) {
        StorageReference fileReference = storageReference.child("proposals/" + userId + "/" + projectId + "/" + fileUri.getLastPathSegment());
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

    public interface FileUrlCallback {
        void onFileUrlCallback(String url);
    }



    public interface UploadCallback {
        void onUploadCallback(String url);
    }
}
