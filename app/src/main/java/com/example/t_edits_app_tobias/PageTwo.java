package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.widget.Toast;

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

import java.util.HashMap;

public class PageTwo extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODF_IMAGE = 101;

    private ImageView sketchViewAdd;

    private EditText questionOne, questionTwo, questionThree;
    private RadioGroup radioGroupOne, radioGroupTwoOne, radioGroupTwoTwo, radioGroupThreeOne, radioGroupFour;
    //private RadioButton logoType;

    //Question two String values
    private String genderAudience;
    private String ageDemographic;

    //Question three String values
    private String brandIndustry;

    Button submitOne;
    Button submitTwo;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

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
        setContentView(R.layout.activity_page_two);

        sketchViewAdd = findViewById(R.id.sketchupload);

        questionOne = findViewById(R.id.logonameText);
        questionTwo = findViewById(R.id.targetAudience);
        questionThree = findViewById(R.id.brandIndustry);

        radioGroupOne = (RadioGroup) findViewById(R.id.optionone);
        radioGroupTwoOne = (RadioGroup) findViewById(R.id.optiontwoone);
        radioGroupTwoTwo = (RadioGroup) findViewById(R.id.optiontwotwo);
        radioGroupThreeOne = (RadioGroup) findViewById(R.id.optionthree);
        radioGroupFour = (RadioGroup) findViewById(R.id.optionfour);

        submitOne = (Button) findViewById(R.id.submitOne);
        submitTwo = (Button) findViewById(R.id.submitTwo);

        storage = FirebaseStorage.getInstance();

        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LogoPackage");

        //INSTANTIATING MY SHARED PREFERENCE
        sp = getSharedPreferences("AnswerTwo", Context.MODE_PRIVATE);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navimenu);

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
                        startActivity(new Intent(PageTwo.this, ExplorePage.class));
                        break;

                    case R.id.nav_profile :
                        Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageTwo.this, teditsUser.class));
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageTwo.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_tedits_package:
                        Toast.makeText(getApplicationContext(),"T-Edits Package",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(PageTwo.this, PageOne.class));
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
                        startActivity(new Intent(PageTwo.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });

        //rFireStore = FirebaseFirestore.getInstance().collection("Users").document().collection("LogoPackage");

        radioGroupTwoOne.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );

        radioGroupTwoTwo.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );

        //SUBMIT PAGE TWO
        submitTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //2. Describe the target audience of your brand
                final String qTwo = questionTwo.getText().toString();

                //2.1 What is the gender audience?
                int selectedId = radioGroupTwoOne.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radioGroupTwoOne.findViewById(selectedId);
                Toast.makeText(PageTwo.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String genderAudience = radioButton.getText().toString();

                //2.2 What is the age demographic?
                int selectedtwoId = radioGroupTwoTwo.getCheckedRadioButtonId();

                RadioButton radioButton2 = (RadioButton) radioGroupTwoTwo.findViewById(selectedtwoId);
                Toast.makeText(PageTwo.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String ageDemographic = radioButton2.getText().toString();

                //Creating my shared preference editor
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("targetAudience", qTwo);
                editor.putString("genderAudience", genderAudience);
                editor.putString("ageDemographic", ageDemographic);
                editor.commit();
                Toast.makeText(PageTwo.this,"Successfully saved question two to preferences", Toast.LENGTH_LONG).show();

                if (qTwo != null && genderAudience != null && ageDemographic != null) {
                    submitTwo(qTwo, genderAudience, ageDemographic);
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

                //CHECKING THE USER TYPE THAT IS LOGGED IN
                String uType = snapshot.child("userType").getValue().toString();
                if (uType.equalsIgnoreCase("Designer")) {

                    //IF THE USER IS A DESIGNER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND TEDITS PACKAGE GENERATOR
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    System.out.println("Updating the menu works");

                } else if(uType.equalsIgnoreCase("Customer")) {
                    //IF THE USER IS A CUSTOMER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PageTwo.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }

    //ANSWER PAGE TWO
    private void submitTwo(String qTwo, String genderAudience, String ageDemographic) {
        final String key = Dataref.push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put("2* Description of brand ", qTwo);
        hashMap.put("2*1 Type of Gender audience ", genderAudience);
        hashMap.put("2*2 Age demographic ", ageDemographic);

        Dataref.child("Q2").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PageTwo.this,"Question two has been submitted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PageTwo.this, PageThree.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, Image not uploaded
                Toast.makeText(PageTwo.this, "Failed to submit answer two" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(PageTwo.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(PageTwo.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(PageTwo.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(PageTwo.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(PageTwo.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(PageTwo.this, teditsUser.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClickTwo(View view) {
    }
}