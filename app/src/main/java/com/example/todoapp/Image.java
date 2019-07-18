package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class Image extends AppCompatActivity {
private static final int PICK_IMAGE_REQUEST =1;
Button choose,upload;
EditText filename;
TextView show;
ProgressBar progressBar;
ImageView image;
private Uri mImageUri;
private StorageReference mStorageRef;
private DatabaseReference mDatabaseRef;
private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        filename=findViewById(R.id.edittext);
        show=findViewById(R.id.showupload);
        choose=findViewById(R.id.choose);
        upload=findViewById(R.id.upload);
        image=findViewById(R.id.image);
        progressBar=findViewById(R.id.progress);
     mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
     mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       openFileChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUploadTask !=null && mUploadTask.isInProgress())
                {

                } else {
                    uploadFile ();
                }            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void openFileChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data !=null && data.getData() !=null) {
          mImageUri= data.getData();
          image.setImageURI(mImageUri);

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(mImageUri !=null){
            StorageReference fileReference =mStorageRef.child(System.currentTimeMillis()
            +"." + getFileExtension(mImageUri));
            mUploadTask= fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(Image.this, "Upload succcesful", Toast.LENGTH_SHORT).show();
                            Upload upload= new Upload(filename.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId= mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Image.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                         double progress= (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                         progressBar.setProgress((int) progress);
                        }
                    });
        } else{
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }

    }
}
