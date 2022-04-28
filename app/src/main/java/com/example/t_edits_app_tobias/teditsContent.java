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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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

        View header;

        TextView mName, mDescription, aDescription;
        ImageView mImage, cImage, aImage, profileImage, menuProfileImage;


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

            mName=(TextView)findViewById(R.id.userNameMenu);
            mDescription=(TextView)findViewById(R.id.userDescription);


            nav=(NavigationView)findViewById(R.id.navimenu);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            //GETTING THE HEADER VIEW FROM MY NAVIGATION MENU
            header = nav.getHeaderView(0);


            profileImage=(ImageView)findViewById(R.id.profile_image);
            menuProfileImage=(ImageView)header.findViewById(R.id.profile_image);


            toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            mImage=(ImageView)header.findViewById(R.id.designerImage);
            cImage=(ImageView)header.findViewById(R.id.customerImage);
            aImage=(ImageView)header.findViewById(R.id.adminImage);

            //GETTING THE TEXT VALUES OF THE NAV MENU
            mName=(TextView)header.findViewById(R.id.userNameMenu);
            mDescription=(TextView)header.findViewById(R.id.userDescription);
            aDescription=(TextView)header.findViewById(R.id.adminDescription);


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

            //LOAD INFORMATION METHOD WILL GIVE USERS ACCESS TO DIFFERENT CONTROLS IN THE NAVIGATION
            loadInformation();

            //LOAD INFORMATION METHOD WILL GIVE USERS ACCESS TO DIFFERENT CONTROLS IN THE NAVIGATION
            loadInformation();
        }

    private void loadInformation() {
        Dataref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");
        //Dataref = FirebaseDatabase.getInstance().getReference("Users").child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");

        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User post = snapshot.getValue(User.class);
                //DataSnapshot post = snapshot.child("userType");
                String post = snapshot.child("fullname").getValue().toString();
                System.out.println("This is working hello "+ post);


                //SETTING THE NAME OF THE USER IN THE MENU
                mName.setText(post);

                //IF PROFILE PIC EXIST ALREADY IN DATABASE RUN THIS CODE
                if(snapshot.hasChild("profilePic")) {
                    //SETTING THE PROFILE PICTURE FOR THE MENU AND USER PAGE
                    //String link = snapshot.getValue(String.class);
                    System.out.println("THERE IS NO PROFILE");
                    String link = snapshot.child("profilePic").getValue().toString();
                    Picasso.get().load(link).into(menuProfileImage);
                }



                //CHECKING THE USER TYPE THAT IS LOGGED IN
                String uType = snapshot.child("userType").getValue().toString();
                if (uType.equalsIgnoreCase("Designer")) {

                    //IF THE USER IS A DESIGNER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND TEDITS PACKAGE GENERATOR
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    System.out.println("Updating the menu works");

                    //SETTING ICON AS DESIGNER IF USER TYPE IS DESIGNER
                    mImage.setVisibility(View.VISIBLE);
                    cImage.setVisibility(View.GONE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO DESIGNER
                    mDescription.setText(uType);


                } else if(uType.equalsIgnoreCase("Customer")) {
                    //IF THE USER IS A CUSTOMER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                } else if(uType.equalsIgnoreCase("Admin")) {

                    //SETTING ICON AS ADMIN IF USER TYPE IS ADMIN
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.GONE);
                    aImage.setVisibility(View.VISIBLE);

                    //SETTING THE DESCRIPTION TO ADMIN
                    aDescription.setText(uType);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teditsContent.this, error.getMessage(), Toast.LENGTH_LONG);

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
