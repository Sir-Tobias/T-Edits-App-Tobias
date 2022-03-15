package com.example.t_edits_app_tobias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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

public class teditsUserCatalogue extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TContent> options;
    FirebaseRecyclerAdapter<TContent, MyViewHolder> adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    //ImageAdapter mAdapter;
    ImageUserAdapter uAdapter;
    List<TContent> tContent;
    Context uContext;

    EditText editText;

    ArrayList<TContent> arrayList;



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
                startActivity(new Intent(teditsUserCatalogue.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(teditsUserCatalogue.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(teditsUserCatalogue.this, teditsCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(teditsUserCatalogue.this, teditsUser.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_user_catalogue);
        recyclerView = findViewById(R.id.recyclerUserView);

        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Content");

        StorageRef = FirebaseStorage.getInstance().getReference("Content");

        arrayList = new ArrayList<>();

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
                if (!s.toString().isEmpty())
                {
                    search(s.toString());
                }
                else {
                    search("");
                }
            }
        });

        //recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);

        //Creating a method to load the data into the recycler view
        LoadData();

    }

    //Method for searching the arraylist catalogue for the string that matches
    private void search(String s) {
        Query query = DataRef.orderByChild("Content").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    arrayList.clear();
                    for(DataSnapshot ss: snapshot.getChildren()) {
                        final TContent tContent = ss.getValue(TContent.class);
                        arrayList.add(tContent);
                    }
                    uAdapter = new ImageUserAdapter(getApplicationContext(), tContent);
                    recyclerView.setAdapter(uAdapter);
                    uAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadData() {

        tContent = new ArrayList<>();
        DataRef = FirebaseDatabase.getInstance().getReference().child("Content");

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("does work "+ postSnapshot.child("Category2").getValue().toString());
                    //TContent content = postSnapshot.getValue(TContent.class);
                    TContent content = new TContent();

                    //Retrieving the image from realtime database
                    content.setImageUri(postSnapshot.child("ImageUri").getValue().toString());

                    //Adding the retrieved content to the tContent Arraylist
                    tContent.add(content);
                }

                uAdapter = new ImageUserAdapter(getApplicationContext(), tContent);
                recyclerView.setAdapter(uAdapter);
                uAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teditsUserCatalogue.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }
}