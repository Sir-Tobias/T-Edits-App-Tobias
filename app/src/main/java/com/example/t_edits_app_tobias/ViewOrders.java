package com.example.t_edits_app_tobias;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<TOrder> options;
    FirebaseRecyclerAdapter<TOrder, MyViewHolder> adapter;
    DatabaseReference DataRef;
    StorageReference StorageRef;

    //ImageAdapter mAdapter;
    ImageElementAdapter eAdapter;
    ArrayList<TOrder> tOrder;
    Context uContext;

    EditText editText;

    ArrayList<TOrder> arrayList;

    View header;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    private DatabaseReference Dataref;

    TextView mName, mDescription, aDescription;
    ImageView mImage, cImage, aImage, profileImage, menuProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
    }
}