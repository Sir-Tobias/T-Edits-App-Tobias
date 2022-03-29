package com.example.t_edits_app_tobias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class teditsElementCatalogue extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TElement> options;
    FirebaseRecyclerAdapter<TElement, MyViewHolder> adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    //ImageAdapter mAdapter;
    ImageElementAdapter eAdapter;
    ArrayList<TElement> tElement;
    Context uContext;

    EditText editText;

    ArrayList<TElement> arrayList;



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
                startActivity(new Intent(teditsElementCatalogue.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(teditsElementCatalogue.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(teditsElementCatalogue.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(teditsElementCatalogue.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(teditsElementCatalogue.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(teditsElementCatalogue.this, teditsUser.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_elements_catalogue);
        recyclerView = findViewById(R.id.recyclerElementsView);
        floatingActionButton = findViewById(R.id.floatingBtnNew);

        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Elements");

        StorageRef = FirebaseStorage.getInstance().getReference("Elements");

        arrayList = new ArrayList<>();

        //Edit text box to search the catalogue
        //Edit text box to search the catalogue
        editText = (EditText) findViewById(R.id.searchCatalogueText);
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
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {
                    search("");
                }
            }
        });

        //recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), teditsElementsContent.class));
            }
        });

        //Creating a method to load the data into the recycler view
        LoadData();

    }

    private void search(String s) {
        ArrayList<TElement> myList = new ArrayList<>();
        for (TElement object : tElement) {
            if (object.getElementCreatedOne().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        if (myList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            eAdapter = new ImageElementAdapter(getApplicationContext(), tElement);
            eAdapter.filterList(myList);
            recyclerView.setAdapter(eAdapter);
            eAdapter.notifyDataSetChanged();
        }
    }

    private void LoadData() {

        tElement = new ArrayList<>();
        DataRef = FirebaseDatabase.getInstance().getReference().child("Elements");

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("does work "+ postSnapshot.child("Element Name").getValue().toString());
                    //TContent content = postSnapshot.getValue(TContent.class);
                    TElement element = new TElement();
                    //Retrieving Element value name from the realtime database
                    element.setElementCreated(postSnapshot.child("Element Name").getValue().toString());
                    //Retrieving the image from realtime database
                    element.setImageUri(postSnapshot.child("ImageUri").getValue().toString());

                    //Adding the retrieved content to the tContent Arraylist
                    tElement.add(element);
                }

                eAdapter = new ImageElementAdapter(getApplicationContext(), tElement);
                recyclerView.setAdapter(eAdapter);
                eAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teditsElementCatalogue.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }
}