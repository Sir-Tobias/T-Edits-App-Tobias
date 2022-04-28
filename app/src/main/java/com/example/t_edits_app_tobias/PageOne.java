package com.example.t_edits_app_tobias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PageOne extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODF_IMAGE = 101;

    private ImageView sketchViewAdd;

    private EditText questionOne;
    private RadioGroup radioGroupOne;
    //private RadioButton logoType;

    //String values to save the shared preferences
    String nameLogo, typeLogo;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    View header;

    TextView mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, profileImage, menuProfileImage;

    //Question one String values
    private String logoType;

    //Question three String values
    private String brandIndustry;

    Button submitOne;

    boolean isSketchAdded=false;

    Uri imageUri;

    //Firebase Reference
    private CollectionReference rFireStore;
    private DatabaseReference Dataref;
    private StorageReference Storageref;
    private FirebaseStorage storage;

    //SHARED PREFERENCES
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_one);

        sketchViewAdd = findViewById(R.id.sketchupload);

        questionOne = findViewById(R.id.logonameText);

        radioGroupOne = (RadioGroup) findViewById(R.id.optionone);

        submitOne = (Button) findViewById(R.id.submitOne);

        int selectedId = radioGroupOne.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroupOne.findViewById(selectedId);

        storage = FirebaseStorage.getInstance();
        Storageref = storage.getReference().child("SketchBackedUp");

        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LogoPackage");

        //INSTANTIATING MY SHARED PREFERENCE
        sp = getSharedPreferences("AnswerOne", Context.MODE_PRIVATE);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navimenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        //GETTING THE HEADER VIEW FROM MY NAVIGATION MENU
        header = nav.getHeaderView(0);

        profileImage=(ImageView)findViewById(R.id.profile_image);
        menuProfileImage=(ImageView)header.findViewById(R.id.profile_image);

        //GETTING THE TEXT VALUES OF THE NAV MENU
        mName=(TextView)header.findViewById(R.id.userNameMenu);
        mDescription=(TextView)header.findViewById(R.id.userDescription);
        aDescription=(TextView)header.findViewById(R.id.adminDescription);

        mImage=(ImageView)header.findViewById(R.id.designerImage);
        cImage=(ImageView)header.findViewById(R.id.customerImage);
        aImage=(ImageView)header.findViewById(R.id.adminImage);


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
                        finish();
                        startActivity(new Intent(PageOne.this, ExplorePage.class));
                        break;

                    case R.id.nav_profile :
                        Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageOne.this, teditsUser.class));
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageOne.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageOne.this, ControlPanel.class));
                        break;

                    case R.id.nav_tedits_package:
                        Toast.makeText(getApplicationContext(),"T-Edits Package",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageOne.this, PageOne.class));
                        break;

                    case R.id.tedits_chats :
                        Toast.makeText(getApplicationContext(),"T-Edits Chats",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout :
                        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(PageOne.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });

        //rFireStore = FirebaseFirestore.getInstance().collection("Users").document().collection("LogoPackage");
        sketchViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODF_IMAGE);

            }
        });
        radioGroupOne.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );



        //SUBMIT ANSWER ONE
        submitOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_tedits_questions_two);

                int selectedId = radioGroupOne.getCheckedRadioButtonId();

                //Values to be stored in the database
                //1. What is the name of your brand?
                final String qOne = questionOne.getText().toString();
                RadioButton radioButton = (RadioButton) radioGroupOne.findViewById(selectedId);
                final String logoType = radioButton.getText().toString();

                //Values to be stored in the shared preference
                nameLogo = questionOne.getText().toString();
                typeLogo = radioButton.getText().toString();

                //Creating my shared preference editor
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("nameLogo", nameLogo);
                editor.putString("typeLogo", typeLogo);
                editor.putString("imageURI", imageUri.toString());

                editor.commit();
                //Toast.makeText(PageOne.this,"Successfully saved question one to preferences", Toast.LENGTH_LONG).show();

                if(isSketchAdded!=false && qOne!=null && logoType!=null) {
                    submitOne(qOne, logoType);
                }
            }
        });

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


                //IF PROFILE PIC EXIST ALREADY IN DATABASE RUN THIS CODE
                if(snapshot.hasChild("profilePic")) {
                    //SETTING THE PROFILE PICTURE FOR THE MENU AND USER PAGE
                    //String link = snapshot.getValue(String.class);
                    System.out.println("THERE IS NO PROFILE");
                    String link = snapshot.child("profilePic").getValue().toString();
                    //Picasso.get().load(link).into(profileImage);
                    Picasso.get().load(link).into(menuProfileImage);
                }


                //SETTING THE NAME OF THE USER IN THE MENU
                mName.setText(post);

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
                Toast.makeText(PageOne.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }
    //ANSWER PAGE ONE
    private void submitOne(final String qOne, final String logoType) {

        //Data reference key
        final String key = Dataref.push().getKey();

        Storageref.child(key+ ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Storageref.child(key+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("1 Name of logo ", qOne);
                        hashMap.put("1*2 Type of logo ", logoType);
                        hashMap.put("1*3 ImageUri", uri.toString());


                        //Creating my shared preference editor
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("imageURI", uri.toString());
                        editor.commit();

                        //Push key
                        Dataref.child("Q1").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PageOne.this,"Question one has been submitted", Toast.LENGTH_LONG).show();
                                //LAUNCHES THE PAGE TWO
//                                Intent intent = new Intent(this, teditsUser.class);
//                                this.startActivity (intent);
                                startActivity(new Intent(PageOne.this, PageTwo.class));
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, Image not uploaded
                Toast.makeText(PageOne.this, "Failed to submit answer " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(PageOne.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(PageOne.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(PageOne.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(PageOne.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(PageOne.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(PageOne.this, teditsUser.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODF_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isSketchAdded=true;
            sketchViewAdd.setImageURI(imageUri);
        }
    }

    public void onClickTwo(View view) {
    }
}