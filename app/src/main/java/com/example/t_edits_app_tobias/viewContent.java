package com.example.t_edits_app_tobias;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
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

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

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
                        startActivity(new Intent(viewContent.this, ExplorePage.class));
                        break;

                    case R.id.nav_profile :
                        Toast.makeText(getApplicationContext(),"Profile is open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(viewContent.this, teditsUser.class));
                        break;

                    case R.id.nav_user_catalogue :
                        Toast.makeText(getApplicationContext(),"Content catalogue",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(viewContent.this, teditsUserCatalogue.class));
                        break;

                    case R.id.nav_control_panel:
                        Toast.makeText(getApplicationContext(),"Control Panel",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(viewContent.this, ControlPanel.class));
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
                        startActivity(new Intent(viewContent.this, MainActivity.class));
                        break;
                }

                return true;
            }
        });

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
