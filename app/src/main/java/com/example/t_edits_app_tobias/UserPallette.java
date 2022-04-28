package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPallette extends AppCompatActivity {


    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TPallette> options;
    FirebaseRecyclerAdapter<TPallette, MyViewHolder> adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    private ImageView checkoutButton;

    //ImageAdapter mAdapter;
    ImagePalletteAdapter pAdapter;
    ArrayList<TPallette> pElement;
    Context uContext;

    EditText editText;

    ArrayList<TPallette> arrayList;

    NavigationView nav;
    View header;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private DatabaseReference Dataref;

    TextView mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, profileImage, menuProfileImage;



    private FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pallette);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName=(TextView)findViewById(R.id.userNameMenu);
        mDescription=(TextView)findViewById(R.id.userDescription);

        nav=(NavigationView)findViewById(R.id.navimenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

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
                        startActivity(new Intent(UserPallette.this, ExplorePage.class));
                        break;

                    case R.id.nav_profile :
                        Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(UserPallette.this, teditsUser.class));
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(UserPallette.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(UserPallette.this, ControlPanel.class));
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
                        startActivity(new Intent(UserPallette.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });
        //Creating a method to load the data into the recycler view
        LoadData();

        //LOAD INFORMATION METHOD WILL GIVE USERS ACCESS TO DIFFERENT CONTROLS IN THE NAVIGATION
        loadInformation();


        recyclerView = findViewById(R.id.recyclerPalletteView);
        checkoutButton = findViewById(R.id.checkoutB);

        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Elements");

        StorageRef = FirebaseStorage.getInstance().getReference("Elements");

        arrayList = new ArrayList<>();

        //recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserCheckout.class));
            }
        });


    }

    private void loadInformation() {
        Dataref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");
        //Dataref = FirebaseDatabase.getInstance().getReference("Users").child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");

        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User post = snapshot.getValue(User.class);
                //DataSnapshot post = snapshot.child("userType");
                String designerName = snapshot.child("fullname").getValue().toString();
                System.out.println("This is working hello "+ designerName);

                String post = snapshot.child("fullname").getValue().toString();
                mName.setText(post);

                String upost = snapshot.child("userType").getValue().toString();
                mDescription.setText(upost);

                //GETTING ADMIN USER DESCRIPTION
                String apost = snapshot.child("userType").getValue().toString();
                aDescription.setText(apost);

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
                Toast.makeText(UserPallette.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }

    private void generatePallette() {

        //LOADING THE SHARED PREFERENCES PACKAGE ANSWER FROM PAGE FOUR
        SharedPreferences sa = getApplicationContext().getSharedPreferences("PackageAnswer", Context.MODE_PRIVATE);
        //I ASSUME MY PACKAGE ANSWER WILL BE PRINTED WITH THE COMMAS EG "AA,BB,CC,DD"....
        String packageAnswer = sa.getString("packageAnswer", "");

        //FIRST I CREATE A NEW ARRAY OF STRING
        String[] value = packageAnswer.split(",");
        //NEXT I CONVERT THE ARRAY OF STRINGS TO A LIST OF STRINGS
        List<String> fixedValue = Arrays.asList(value);
        //THIRDLY I WILL PUT THE FIXED VALUE LIST INTO AN ARRAYLIST
        ArrayList<String> listOfValues = new ArrayList<String>(fixedValue);

        System.out.println(pElement);

        //I ASSUME THAT THERE WILL BE SEVEN SEPERATED VALUES BECAUSE OF THE COMMA PLACEMENTS
        ArrayList<TPallette> myList = new ArrayList<>();
        for (TPallette object : pElement) {

            //ITERATE THROUGH THE INDEX STRING VALUES IN MY ARRAYLIST
            for (int i = 0; i < listOfValues.size(); i++) {
                //IF MY HASHMAP ELEMENT DATABASE IN FIREBASE CONTAINS THE STRING VALUE ADD IT TO THE NEW LIST
                if (object.getPalletteValue().contains(listOfValues.get(i))) {
                    myList.add(object);
                }
            }
            if (myList.isEmpty()) {
                //Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                pAdapter = new ImagePalletteAdapter(getApplicationContext(), pElement);
                pAdapter.filterList(myList);
                recyclerView.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
            }
        }
    }

//    private void search(String s) {
//        ArrayList<TPallette> myList = new ArrayList<>();
//        for (TPallette object : pElement) {
//            if (object.getPalletteValue().toLowerCase().contains(s.toLowerCase())) {
//                myList.add(object);
//            }
//        }
//        if (myList.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            pAdapter = new ImagePalletteAdapter(getApplicationContext(), pElement);
//            pAdapter.filterList(myList);
//            recyclerView.setAdapter(pAdapter);
//            pAdapter.notifyDataSetChanged();
//        }
//    }

    private void LoadData() {

        pElement = new ArrayList<>();
        DataRef = FirebaseDatabase.getInstance().getReference().child("Elements");

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("does work "+ postSnapshot.child("Element Name").getValue().toString());
                    //TContent content = postSnapshot.getValue(TContent.class);
                    TPallette element = new TPallette();
                    //Retrieving Element value name from the realtime database
                    element.setPalletteValue(postSnapshot.child("Element Name").getValue().toString());
                    //Retrieving the image from realtime database
                    element.setImageUri(postSnapshot.child("ImageUri").getValue().toString());

                    //Adding the retrieved content to the tContent Arraylist
                    pElement.add(element);
                }

                pAdapter = new ImagePalletteAdapter(getApplicationContext(), pElement);
                recyclerView.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
                generatePallette();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserPallette.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }
}