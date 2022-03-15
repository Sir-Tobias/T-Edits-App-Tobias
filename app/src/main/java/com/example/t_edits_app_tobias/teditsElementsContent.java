package com.example.t_edits_app_tobias;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class teditsElementsContent extends AppCompatActivity {

        private static final int REQUEST_CODF_IMAGE = 101;

        private ImageView imageViewAdd;

        private EditText elementOne;

        private ProgressBar progressBarm;

        private Button btnUpload;

        Uri imageUri;
        boolean isImageAdded=false;


        //Firebase Reference
        private CollectionReference rFireStore;
        private DatabaseReference Dataref;
        private StorageReference Storageref;
        private FirebaseStorage storage;

        //Linking the posts being uploaded to user
        FirebaseUser firebaseUser;

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tedits_element_content);

            imageViewAdd = findViewById(R.id.contentupload);

            elementOne = findViewById(R.id.elementOneText);

            progressBarm = findViewById(R.id.progressBarn);
            btnUpload = findViewById(R.id.btnUpload);

            storage = FirebaseStorage.getInstance();
            Storageref = storage.getReference().child("ElementsBackedUp");

            progressBarm.setVisibility(View.GONE);

            //Initializing data reference
            Dataref = FirebaseDatabase.getInstance().getReference().child("Elements");
            //Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memory");
            rFireStore = FirebaseFirestore.getInstance().collection("Users");
            //Added the date and time stamp
            //storage = FirebaseStorage.getInstance().getReference());

            //Method to upload photo from phone gallery
            imageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,REQUEST_CODF_IMAGE);

                }
            });
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String eOne = elementOne.getText().toString();
                    if (isImageAdded!=false && eOne!=null ){
                        uploadImage(eOne);

                    }
                }
            });
        }

        private void uploadImage(final String eOne) {

            progressBarm.setVisibility(View.VISIBLE);

            //final String randomKey = UUID.randomUUID().toString();

            //Data reference key
            final String key = Dataref.push().getKey();
            //final String key1 = rFireStore.push().getKey();
            //final String key1 = Storageref.push().getKey();

            Storageref.child(key+ ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Storageref.child(key+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("Element Name", eOne);
                            hashMap.put("ImageUri", uri.toString());

                            //Push key
                            Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(teditsElementsContent.this,"Your T-Edits Element has been uploaded", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(teditsElementsContent.this,teditsElementCatalogue.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                    progressBarm.setProgress((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    Toast.makeText(teditsElementsContent.this, "Failed to upload element " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==REQUEST_CODF_IMAGE && data!=null)
            {
                imageUri=data.getData();
                isImageAdded=true;
                imageViewAdd.setImageURI(imageUri);
            }
        }
    }
