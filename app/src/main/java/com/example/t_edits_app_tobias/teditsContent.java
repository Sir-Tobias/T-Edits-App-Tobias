package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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

        private FirebaseAuth mAuth;

        NavigationView nav;
        ActionBarDrawerToggle toggle;
        DrawerLayout drawerLayout;


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

            Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            nav=(NavigationView)findViewById(R.id.navimenu);
            drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

            toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home :
                            Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;

                        case R.id.nav_profile :
                            Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            finish();
                            startActivity(new Intent(teditsContent.this, teditsContent.class));
                            break;

                        case R.id.nav_user_catalogue :
                            Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            finish();
                            startActivity(new Intent(teditsContent.this, teditsCatalogue.class));
                            break;

                        case R.id.nav_control_panel:
                            Toast.makeText(getApplicationContext(),"Control Panel",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            finish();
                            startActivity(new Intent(teditsContent.this, ControlPanel.class));
                            break;

                        case R.id.tedits_chats :
                            Toast.makeText(getApplicationContext(),"T-Edits Chats",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;

                        case R.id.nav_logout :
                            Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            finish();
                            startActivity(new Intent(teditsContent.this, MainActivity.class));
                            break;
                    }

                    return true;
                }
            });

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
                            hashMap.put("Category1", catOne);
                            hashMap.put("Category2", catTwo);
                            hashMap.put("Category3", catThree);
                            hashMap.put("Category4", catFour);
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
