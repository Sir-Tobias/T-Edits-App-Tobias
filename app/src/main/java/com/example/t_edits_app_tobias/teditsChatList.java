package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class teditsChatList extends AppCompatActivity {

    ListView userListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> users = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference DataRef, chatdata, Uref;

    NavigationView nav;
    View header;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    TextView username;

    EditText fullnameUpdate, phonenoUpdate;

    //MENU VALUE REFERENCES
    TextView viewNew, mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, profileImage, menuProfileImage;

    private StorageReference Storageref;
    private FirebaseStorage storage;


    private ImageView imagePostAdd, viewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_chat_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        //InfoLayout=(Layout) findViewById(R.layout.navheader);
        nav=(NavigationView)findViewById(R.id.navimenu);

        //GETTING THE HEADER VIEW FROM MY NAVIGATION MENU
        header = nav.getHeaderView(0);

        //EDITTEXTVIEW OF UPDATING USER
        fullnameUpdate=(EditText)findViewById(R.id.uFullname);
        phonenoUpdate=(EditText)findViewById(R.id.uPhoneNo);

        //GETTING THE TEXT VALUES OF THE NAV MENU
        mName=(TextView)header.findViewById(R.id.userNameMenu);
        mDescription=(TextView)header.findViewById(R.id.userDescription);
        aDescription=(TextView)header.findViewById(R.id.adminDescription);

        mImage=(ImageView)header.findViewById(R.id.designerImage);
        cImage=(ImageView)header.findViewById(R.id.customerImage);
        aImage=(ImageView)header.findViewById(R.id.adminImage);


        username = (TextView) findViewById(R.id.text2);

        Uref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println("THIS IS THE USER ID VALUE BELOW");
        System.out.println(Uref.getKey());
        String userID = Uref.getKey();


        profileImage=(ImageView)findViewById(R.id.profile_image);
        menuProfileImage=(ImageView)header.findViewById(R.id.profile_image);


        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        viewNew=(TextView)findViewById(R.id.banner2);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //SHARED PREFERENCES
        SharedPreferences sp;

        //INSTANTIATING MY SHARED PREFERENCE
        sp = getSharedPreferences("newAnswer", Context.MODE_PRIVATE);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                //loadInformation();

                switch (menuItem.getItemId())
                {
                    case R.id.nav_home :
                        Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(teditsChatList.this, ExplorePage.class));
                        break;

                    case R.id.nav_profile :
                        Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(teditsChatList.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(teditsChatList.this, ControlPanel.class));
                        break;

                    case R.id.nav_tedits_package:
                        Toast.makeText(getApplicationContext(),"T-Edits Package",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(teditsChatList.this, PageOne.class));
                        break;

                    case R.id.tedits_chats :
                        Toast.makeText(getApplicationContext(),"T-Edits Chats",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;

                    case R.id.nav_tedits_orders :
                        Toast.makeText(getApplicationContext(),"T-Edits Orders",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(teditsChatList.this, ViewOrders.class));
                        break;

                    case R.id.nav_logout :
                        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(teditsChatList.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });

        System.out.println("THE CHAT LIST CLASS IS CALLED");

        chatdata = FirebaseDatabase.getInstance().getReference("Chat").child("ChatProfiles");

        mAuth = FirebaseAuth.getInstance();
        DataRef = FirebaseDatabase.getInstance().getReference();
        userListView = findViewById(R.id.userListView);
        //FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");
        //FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("UserDetails");
        DataRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("UserDetails");
        chatdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if (snapshot.exists()){
//                System.out.println("THIS IS THE USER DETAILS "+ snapshot.child("ChatEmail").getValue().toString() );
//                String email = snapshot.child("ChatEmail").getValue().toString();
                for (DataSnapshot postsnapshot : snapshot.getChildren()) {

                    String temail = postsnapshot.child("ChatEmail").getValue().toString();
                    System.out.println("THIS IS THE USER DETAILS "+ temail );

                    String tname= postsnapshot.child("ChatName").getValue().toString();
                    System.out.println("THIS IS THE USER NAMES "+ tname );
                    //username.setText(tname);
                    String newKey = postsnapshot.child("ChatID").getValue().toString();
                    System.out.println("THIS IS THE USER KEY "+ newKey );


                    if (!userID.equalsIgnoreCase(newKey)) {
                        users.add(temail);
                    }
                }
//                if (!email.equalsIgnoreCase(mAuth.getCurrentUser().getEmail())) {
//                    users.add(email);
//                }
//                users.add(email);
                    arrayAdapter = new ArrayAdapter(teditsChatList.this, android.R.layout.simple_list_item_1, users);
                    userListView.setAdapter(arrayAdapter);
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teditsChatList.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });


        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(teditsChatList.this, teditsChats.class);
                intent.putExtra("email", users.get(position));
                startActivity(intent);
            }
        });

        //LOAD INFORMATION METHOD WILL GIVE USERS ACCESS TO DIFFERENT CONTROLS IN THE NAVIGATION
        loadInformation();
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

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                }else if(uType.equalsIgnoreCase("Designer2")) {
                    //IF THE USER IS A DESIGNER 2 THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                }else if(uType.equalsIgnoreCase("Designer3")) {
                    //IF THE USER IS A DESIGNER 3 THEY DO NOT HAVE ACCESS TO THE CONTROL PANEL AND UPLOADING CONTENT TO THE EXPLORE PAGE
                    nav.getMenu().getItem(3).setVisible(false);

                    //SETTING ICON AS CUSTOMER IF USER TYPE IS CUSTOMER
                    mImage.setVisibility(View.GONE);
                    cImage.setVisibility(View.VISIBLE);
                    aImage.setVisibility(View.GONE);

                    //SETTING THE DESCRIPTION TO CUSTOMER
                    mDescription.setText(uType);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teditsChatList.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }

}