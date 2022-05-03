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
import com.google.android.gms.tasks.OnSuccessListener;
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


public class ExplorePage extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TPost> options;
    FirebaseRecyclerAdapter<TPost, MyViewHolder> adapter;
    DatabaseReference DataRef, data, Uref;
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

    View header;

    TextView mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, menuProfileImage;

    private ImageView imagePostAdd, viewPost;


    private FirebaseAuth mAuth;


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

        //GETTING THE HEADER VIEW FROM MY NAVIGATION MENU
        header = nav.getHeaderView(0);

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
                        finish();
                        startActivity(new Intent(ExplorePage.this, teditsChatList.class));
                        break;

                    case R.id.nav_tedits_orders :
                        Toast.makeText(getApplicationContext(),"T-Edits Orders",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(ExplorePage.this, TeditorOrder.class));
                        break;

                    case R.id.nav_orders :
                        Toast.makeText(getApplicationContext(),"T-Editor Orders",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(ExplorePage.this, ViewOrders.class));
                        break;

                    case R.id.nav_logout :
                        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(ExplorePage.this, MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;
                }

                return true;
            }
        });


        imagePostAdd = findViewById(R.id.postUpload);
        viewPost = findViewById(R.id.myPost);

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

        viewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewMyPost.class));

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

        //RETRIEVING THE SHARED PREFERENCE FOR USER ID FROM THE TEDITS POST ACTIVITY
        SharedPreferences sa = getApplicationContext().getSharedPreferences("DesignerDetails", Context.MODE_PRIVATE);
        //String desID = sa.getString("userid", "");

        //RATHER THAN RETRIEVING SHAREDPREFERENCE USER ID I RETRIEVE THE USER ID FROM THE DATABASE DIRECTLY
        Uref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println("THIS IS THE USER ID VALUE BELOW");
        System.out.println(Uref.getKey());
        String userKey = Uref.getKey();

        data = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String npost = snapshot.child("fullname").getValue().toString();
                System.out.println("This is working hello "+ npost);
                retrievedName(npost);
            }

            private void retrievedName(String npost) {

                pContent = new ArrayList<>();
                //uContent = new ArrayList<>();
                DataRef = FirebaseDatabase.getInstance().getReference().child("TeditsPost");
                DataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            System.out.println("THIS IS SUPPOSE TO BE NAME " + postSnapshot.child("NameOfDesigner").getValue().toString());
                            System.out.println("THIS IS SUPPOSE TO BE KEY " + postSnapshot.getKey());
                            String idPostKey = postSnapshot.getKey();

                            TPost post = new TPost();
                            //CHECKING TO SEE THE IF THE CURRENT USER ID MATCHES THE KEY VALUE WHICH THE POST IS STORED IN
                            if (userKey.equalsIgnoreCase(idPostKey)) {
                                System.out.println("THIS WORKED FIRST");


                                //EVERYTIME THE EXPLORE PAGE IS CALLED IT WILL SET THE DESIGNER NAME TO THE NAME OF THE CURRENT USER ID IF THE POST ID MATCHES
                                //UPDATING THE DESIGNER DETAIL VALUE
                                DataRef.child(userKey).child("NameOfDesigner").setValue(npost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ExplorePage.this,"Details successfully updated", Toast.LENGTH_LONG).show();
                                    }
                                });

                                //post.setNameOfDesigner(npost);
                                post.setCaption(postSnapshot.child("Caption").getValue().toString());
                                post.setNameOfDesigner(postSnapshot.child("NameOfDesigner").getValue().toString());
                                post.setNameOfPost(postSnapshot.child("NameOfPost").getValue().toString());


                                //Retrieving the image from realtime database
                                post.setImageUri(postSnapshot.child("ImageUri").getValue().toString());

                                //Adding the retrieved content to the tContent Arraylist
                                pContent.add(post);


                                //THIS IS RETRIEVING ALL THE OTHER CONTENT THAT ISNT EQUAL TO THE DESIGNER NAME
                            } else if (!userKey.equalsIgnoreCase(idPostKey)){
                            post.setCaption(postSnapshot.child("Caption").getValue().toString());
                            post.setNameOfDesigner(postSnapshot.child("NameOfDesigner").getValue().toString());
                            post.setNameOfPost(postSnapshot.child("NameOfPost").getValue().toString());
                            //Retrieving the image from realtime database
                            post.setImageUri(postSnapshot.child("ImageUri").getValue().toString());
                            pContent.add(post);

                        } else {
                                System.out.println("NOTHING TO UPDATE");
                            }

                        }
                        pAdapter = new ImagePostAdapter(getApplicationContext(), pContent);
                        recyclerView.setAdapter(pAdapter);
                        pAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void loadInformation() {
        DataRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");
        //Dataref = FirebaseDatabase.getInstance().getReference("Users").child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");

        DataRef.addValueEventListener(new ValueEventListener() {
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
                    //Picasso.get().load(link).into(profileImage);
                    Picasso.get().load(link).into(menuProfileImage);
                }



                //CHECKING THE USER TYPE THAT IS LOGGED IN
                String uType = snapshot.child("userType").getValue().toString();
                if (uType.equalsIgnoreCase("Designer")) {

                    //IF THE USER IS A DESIGNER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND TEDITS PACKAGE GENERATOR
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    nav.getMenu().getItem(7).setVisible(false);
                    System.out.println("Updating the menu works");

                    //SETTING ICON AS DESIGNER IF USER TYPE IS DESIGNER
                    mImage.setVisibility(View.VISIBLE);
                    cImage.setVisibility(View.GONE);
                    aImage.setVisibility(View.GONE);
                    viewPost.setVisibility(View.VISIBLE);

                    //SETTING THE DESCRIPTION TO DESIGNER
                    mDescription.setText(uType);

                } else if(uType.equalsIgnoreCase("Customer")) {
                    //IF THE USER IS A CUSTOMER THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(6).setVisible(false);
                    nav.getMenu().getItem(7).setVisible(false);


                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                    //IF THE USER IS A CUSTOMER THEY CANNOT UPLOAD CONTENT ONTO THE T-EDITS EXPLORE PAGE
                    //THEY WILL NOT HAVE THE OPTION TO PRESS THE UPLOAD BUTTON
                    imagePostAdd.setVisibility(View.GONE);
                    viewPost.setVisibility(View.GONE);

                }  else if(uType.equalsIgnoreCase("Admin")) {

                    //SETTING ICON AS ADMIN IF USER TYPE IS ADMIN
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.GONE);
                    aImage.setVisibility(View.VISIBLE);

                    //SETTING THE DESCRIPTION TO ADMIN
                    aDescription.setText(uType);
                }else if(uType.equalsIgnoreCase("Designer1")) {
                    //IF THE USER IS A DESIGNER 2 THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    nav.getMenu().getItem(6).setVisible(false);
                    nav.getMenu().getItem(5).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //IF THE USER IS A CUSTOMER THEY CANNOT UPLOAD CONTENT ONTO THE T-EDITS EXPLORE PAGE
                    //THEY WILL NOT HAVE THE OPTION TO PRESS THE UPLOAD BUTTON
                    imagePostAdd.setVisibility(View.GONE);
                    viewPost.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                }else if(uType.equalsIgnoreCase("Designer2")) {
                    //IF THE USER IS A DESIGNER 2 THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    nav.getMenu().getItem(6).setVisible(false);
                    nav.getMenu().getItem(5).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //IF THE USER IS A CUSTOMER THEY CANNOT UPLOAD CONTENT ONTO THE T-EDITS EXPLORE PAGE
                    //THEY WILL NOT HAVE THE OPTION TO PRESS THE UPLOAD BUTTON
                    imagePostAdd.setVisibility(View.GONE);
                    viewPost.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                }else if(uType.equalsIgnoreCase("Designer3")) {
                    //IF THE USER IS A DESIGNER 3 THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);
                    nav.getMenu().getItem(4).setVisible(false);
                    nav.getMenu().getItem(6).setVisible(false);
                    nav.getMenu().getItem(5).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //IF THE USER IS A CUSTOMER THEY CANNOT UPLOAD CONTENT ONTO THE T-EDITS EXPLORE PAGE
                    //THEY WILL NOT HAVE THE OPTION TO PRESS THE UPLOAD BUTTON
                    imagePostAdd.setVisibility(View.GONE);
                    viewPost.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

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