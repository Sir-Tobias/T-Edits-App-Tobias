package com.example.t_edits_app_tobias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class viewContent extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewOne;
    private TextView textViewTwo;
    private TextView textViewThree;
    private TextView textViewFour;
    private CardView cardView;
    private EditText editText;
    private Button btnDelete;
    private Button btnUpdate;

    FirebaseFirestore rFireStore;
    DatabaseReference ref, DataRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        //cardView =findViewById(R.id.menupopbutton);
//        imageView =findViewById(R.id.view_memory_image);
//        textView = findViewById(R.id.view_memory_text);
//        editText = findViewById(R.id.updateImageNameText);
//        btnUpdate = findViewById(R.id.btnUpdate);

        //rFireStore.getInstance().collection("Users");
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Content");
        //ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Content");

        DataRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Content");
        //DataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Content");
        String ContentID=getIntent().getStringExtra("ContentID");

        //DataRef=FirebaseDatabase.getInstance().getReference().child("ContentBackedup").child(ContentID);
        StorageRef= FirebaseStorage.getInstance().getReference().child("ContentBackedup").child(ContentID+".jpg");

        ref.child(ContentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    //String memoryCreated = snapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Memory").child("memoryCreated").getValue().toString();
                    String contentCreatedOne = snapshot.child("Category 1").getValue().toString();
                    String contentCreatedTwo = snapshot.child("Category 2").getValue().toString();
                    String contentCreatedThree = snapshot.child("Category 3").getValue().toString();
                    String contentCreatedFour = snapshot.child("Category 4").getValue().toString();
                    String ImageUri = snapshot.child("ImageUri").getValue().toString();

                    //Picasso.get().load(ImageUri).into((Target) cardView);
                    Picasso.get().load(ImageUri).into(imageView);
                    textViewOne.setText(contentCreatedOne);
                    textViewTwo.setText(contentCreatedTwo);
                    textViewThree.setText(contentCreatedThree);
                    textViewFour.setText(contentCreatedFour);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
