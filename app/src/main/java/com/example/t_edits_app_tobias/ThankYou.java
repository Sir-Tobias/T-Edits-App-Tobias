package com.example.t_edits_app_tobias;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ThankYou extends AppCompatActivity {

    ImageView mImage, cImage, aImage;
    TextView mName, mDescription, aDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
    }
}