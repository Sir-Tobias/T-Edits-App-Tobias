package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t_edits_app_tobias.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class teditsChats extends AppCompatActivity {

    EditText message;
    Button send;
    ListView chatListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> messages = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference databaseReference, chatdata, convoData, Uref;


    NavigationView nav;
    View header;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;


    TextView username;


    EditText fullnameUpdate, phonenoUpdate;

    //MENU VALUE REFERENCES
    TextView viewNew, mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, profileImage, menuProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_chats);

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

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        chatdata = FirebaseDatabase.getInstance().getReference("Chat");
        convoData = FirebaseDatabase.getInstance().getReference("Chat").child("ChatProfiles");

        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        chatListView = findViewById(R.id.chatListView);

        Intent intent = getIntent();

        String otherEmail = intent.getStringExtra("email");
        String email = mAuth.getCurrentUser().getEmail();

        setTitle("Chat with " + otherEmail);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(teditsChats.this, "Write a message!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> messageData = new HashMap<>();
                    messageData.put("sender", email);
                    messageData.put("receiver", otherEmail);
                    messageData.put("message", message.getText().toString());

                    convoData.child("convo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int count;
                            if (snapshot.exists()){
                                count = (int) (snapshot.getChildrenCount() + 1);
                            } else {
                                count = 1;
                            }
                            convoData.child(userID).child("convo").child(String.valueOf(count)).setValue(messageData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        message.setText("");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(teditsChats.this, "Error in sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        convoData.child("convo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.child("sender").getValue().toString().equals(email) || dataSnapshot.child("receiver").getValue().toString().equals(email)) {

                            String message = dataSnapshot.child("message").getValue().toString();
                            //messages.remove(-1);

                            if (!dataSnapshot.child("sender").getValue().toString().equals(email)){
                                message = "> " + message;
                            }
                            messages.size();
                            messages.clear();
                            messages.add(message);
                        }
                    }
                    arrayAdapter = new ArrayAdapter(teditsChats.this, android.R.layout.simple_list_item_1, messages);
                    chatListView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}