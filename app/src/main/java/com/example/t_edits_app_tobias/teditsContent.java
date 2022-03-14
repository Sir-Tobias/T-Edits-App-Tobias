package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.UUID;

public class teditsContent extends AppCompatActivity {

        private static final int REQUEST_CODF_IMAGE = 101;

        private ImageView imageViewAdd;

        private EditText categoryOne;
        private EditText categoryTwo;
        private EditText categoryThree;
        private EditText categoryFour;

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
            setContentView(R.layout.activity_tedits_content);

            imageViewAdd = findViewById(R.id.contentupload);

            categoryOne = findViewById(R.id.categoryOneText);
            categoryTwo = findViewById(R.id.categoryTwoText);
            categoryThree = findViewById(R.id.categoryThreeText);
            categoryFour = findViewById(R.id.categoryFourText);

            progressBarm = findViewById(R.id.progressBarn);
            btnUpload = findViewById(R.id.btnUpload);

            storage = FirebaseStorage.getInstance();
            Storageref = storage.getReference().child("ContentBackedUp");

            progressBarm.setVisibility(View.GONE);

            //Initializing data reference
            Dataref = FirebaseDatabase.getInstance().getReference().child("Content");
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
                    final String catOne = categoryOne.getText().toString();
                    final String catTwo = categoryTwo.getText().toString();
                    final String catThree = categoryThree.getText().toString();
                    final String catFour = categoryFour.getText().toString();
                    if (isImageAdded!=false && catOne!=null && catTwo!=null && catThree!=null && catFour!=null){
                        uploadImage(catOne,catTwo, catThree, catFour);

                    }
                }
            });
        }

        private void uploadImage(final String catOne, final String catTwo, final String catThree, final String catFour) {

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
                            hashMap.put("Category 1", catOne);
                            hashMap.put("Category 2", catTwo);
                            hashMap.put("Category 3", catThree);
                            hashMap.put("Category 4", catFour);
                            hashMap.put("ImageUri", uri.toString());

                            //Push key
                            Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(teditsContent.this,"Your T-Edits Content has successfully been uploaded", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(teditsContent.this,teditsCatalogue.class);
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
                    Toast.makeText(teditsContent.this, "Failed to upload content " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
