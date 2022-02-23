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

    public void startMemoryActivity(View view) {
        Toast.makeText(teditsUser.this,"Memory button worked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(teditsUser.this,teditsContent.class);
        startActivity(intent);
    }
}