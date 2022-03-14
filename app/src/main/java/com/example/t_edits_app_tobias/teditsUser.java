package com.example.t_edits_app_tobias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class teditsUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_user);
    }

    public void startContentActivity(View view) {
        Toast.makeText(teditsUser.this,"Upload content", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(teditsUser.this,teditsContent.class);
        startActivity(intent);
    }

    public void viewContentActivity(View view) {
        Toast.makeText(teditsUser.this,"View content", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(teditsUser.this,teditsCatalogue.class);
        startActivity(intent);
    }
}