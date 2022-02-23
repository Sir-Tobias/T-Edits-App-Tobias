package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class teditsCatalogue extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TContent> options;
    FirebaseRecyclerAdapter<TContent, MyViewHolder>adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

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
                startActivity(new Intent(teditsCatalogue.this, MainActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_catalogue);

        //Initializing
        DataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memory");
//        inputSearch=findViewById(R.id.inputSearch);
        recyclerView=findViewById(R.id.recyclerView);
        floatingActionButton=findViewById(R.id.floatingBtn);


        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setHasFixedSize(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), teditsContent.class));

            }
        });

        LoadData();

    }

    private void LoadData() {
        //Query query=DataRef.orderByChild("createdMemory").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<TContent>().setQuery(DataRef,TContent.class).build();
        adapter=new FirebaseRecyclerAdapter<TContent, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull TContent model) {

                ImageView popupbutton = holder.itemView.findViewById(R.id.menupopbutton);

                holder.viewOne.setText(model.getContentCreatedOne());
                holder.viewTwo.setText(model.getContentCreatedTwo());
                holder.viewThree.setText(model.getContentCreatedThree());
                holder.viewFour.setText(model.getContentCreatedFour());
                Picasso.get().load(model.getImageUri()).into(holder.imageView);
                //Picasso.get().load(model.getImageUri()).into((Target) holder.imageView);

                //String docID = adapter.getSnapshots().getSnapshot(position).getKey();
                String docID = adapter.getRef(position).getKey();
                Toast.makeText(teditsCatalogue.this,"Click test", Toast.LENGTH_LONG).show();

                holder.view.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
                        //popupMenu.(Gravity.END);
                        popupMenu.setGravity(Gravity.END);
                        //popupMenu.inflate(R.menu.class);
                        //getMenuInflater().inflate(R.menu.menu, (Menu) popupMenu);
                        Toast.makeText(teditsCatalogue.this,"Click works3", Toast.LENGTH_LONG).show();
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent=new Intent(view.getContext(), viewContent.class);
                                //Picasso.get().load(model.getImageUri()).into(holder.imageView);
                                intent.putExtra("MemoryID", docID);
                                startActivity(intent);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                DataRef=FirebaseDatabase.getInstance().getReference().child("Memory").child(docID);
                                StorageRef= FirebaseStorage.getInstance().getReference().child("MemoriesBackedup").child(docID+".jpg");
                                //DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(getApplicationContext(), teditsUser.class));
                                            }
                                        });
                                        Toast.makeText(view.getContext(), "This memory is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                    }
                });
            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent,false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}