package com.example.due_diligence.Firebase;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Cloud_Storage {

    StorageReference storageReference;

    public Cloud_Storage() {
        storageReference = FirebaseStorage.getInstance("gs://due-diligence-28ddc.appspot.com").getReference();
    }

    public void uploadFile(Uri fileUri, String userId, UploadCallback uploadCallback) {
        StorageReference fileReference = storageReference.child(userId + "/" + System.currentTimeMillis() + "." + fileUri);
        fileReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                uploadCallback.onUploadCallback(uri.toString());
            });
        });
    }


    public interface UploadCallback {
        void onUploadCallback(String url);
    }
}
