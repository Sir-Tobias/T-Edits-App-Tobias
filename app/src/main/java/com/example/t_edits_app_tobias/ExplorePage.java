package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;

public class ExplorePage extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TPost> options;
    FirebaseRecyclerAdapter<TPost, MyViewHolder> adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    EditText editText;

    ImagePostAdapter pAdapter;
    ArrayList<TPost> tContent;
    Context pContext;
    ArrayList<TPost> pContent;
    ArrayList<TPost> uContent;
    SearchView searchView;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private ImageView imagePostAdd;


    private FirebaseAuth mAuth;

    private DatabaseReference Dataref;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(ExplorePage.this, RegisterUser.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(ExplorePage.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(ExplorePage.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(ExplorePage.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(ExplorePage.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(ExplorePage.this, MainActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_page);

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
                        startActivity(new Intent(ExplorePage.this, teditsUser.class));
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(ExplorePage.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(ExplorePage.this, ControlPanel.class));
                        break;

                    case R.id.nav_tedits_package:
                        Toast.makeText(getApplicationContext(),"T-Edits Package",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(ExplorePage.this, PageOne.class));
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
                        startActivity(new Intent(ExplorePage.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });




        imagePostAdd = findViewById(R.id.postUpload);

        recyclerView = findViewById(R.id.recyclerExploreView);
        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Content");

        StorageRef = FirebaseStorage.getInstance().getReference("Content");
        //DataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Content");
        //recyclerView = findViewById(R.id.recyclerExploreView);
        floatingActionButton = findViewById(R.id.floatingBtn);


        //recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);

        //Edit text box to search the tedits explore page
        editText = (EditText) findViewById(R.id.searchExploreText);
        //Create a listener for the edit text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                {
                    search(s.toString());
                }
                else {
                    search("");
                }
            }
        });

        //Method to upload photo from phone gallery
        imagePostAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), teditsPost.class));
            }
        });

        //Creating a method to load the data into the recycler view
        LoadData();

        //LOAD INFORMATION METHOD WILL GIVE USERS ACCESS TO DIFFERENT CONTROLS IN THE NAVIGATION
        loadInformation();
    }

    private void LoadData() {
        pContent = new ArrayList<>();
        //Dataref = FirebaseDatabase.getInstance().getReference().child("TeditsPost").child(FirebaseAuth.getInstance().getUid());
        DataRef = FirebaseDatabase.getInstance().getReference().child("TeditsPost");

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("did not work " + postSnapshot.child("NameOfPost").getValue().toString());
                    //TContent content = postSnapshot.getValue(TContent.class);
                    TPost post = new TPost();

                    //Retrieving the the tag values from the realtime database
                    post.setNameOfPost(postSnapshot.child("NameOfPost").getValue().toString());
                    post.setCaption(postSnapshot.child("Caption").getValue().toString());
                    post.setNameOfDesigner(postSnapshot.child("NameofDesigner").getValue().toString());

                    //Retrieving the image from realtime database
                    post.setImageUri(postSnapshot.child("ImageUri").getValue().toString());

                    //Adding the retrieved content to the tContent Arraylist
                    pContent.add(post);
                }

                pAdapter = new ImagePostAdapter(getApplicationContext(), pContent);
                recyclerView.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExplorePage.this, error.getMessage(), Toast.LENGTH_LONG);

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

                    //IF THE USER IS A CUSTOMER THEY CANNOT UPLOAD CONTENT ONTO THE T-EDITS EXPLORE PAGE
                    //THEY WILL NOT HAVE THE OPTION TO PRESS THE UPLOAD BUTTON
                    imagePostAdd.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExplorePage.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }

    //Method for searching the arraylist catalogue for the string that matches
    private void search(String s) {
        ArrayList<TPost> myList = new ArrayList<>();
        for (TPost object : pContent) {
            if (object.getNameOfPost().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);

            } else if (object.getCaption().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
            if (myList.isEmpty()) {
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                pAdapter = new ImagePostAdapter(getApplicationContext(), pContent);
                pAdapter.filterList(myList);
                recyclerView.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
            }
        }
    }
}