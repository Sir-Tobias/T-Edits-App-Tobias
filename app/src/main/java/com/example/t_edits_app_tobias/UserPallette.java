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
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;



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


        recyclerView = findViewById(R.id.recyclerPalletteView);
        checkoutButton = findViewById(R.id.checkoutB);

        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Elements");

        StorageRef = FirebaseStorage.getInstance().getReference("Elements");

        arrayList = new ArrayList<>();

        //Edit text box to search the catalogue
        //Edit text box to search the catalogue
//        editText = (EditText) findViewById(R.id.searchCatalogueText);
//        //Create a listener for the edit text
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().isEmpty()) {
//                    search(s.toString());
//                } else {
//                    search("");
//                }
//            }
//        });

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

    private void generatePallette() {
        //ArrayList<TPallette> myList = new ArrayList<>();

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
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
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