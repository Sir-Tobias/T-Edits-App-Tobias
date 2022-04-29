package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ThankYou extends AppCompatActivity {

    ImageView mImage, cImage, aImage;
    TextView mName, mDescription, aDescription;
    Button button;
    private DatabaseReference DataRef, Uref, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);


        uploadToAdmin();
    }

    //IN THIS METHOD I WILL PULL DOWN THE REQUIRED INFORMATION FROM THE CLIENT GENERATED PACKAGE AND VIEW IT FROM THE ADMIN PAGE
    private void uploadToAdmin() {

        Uref = FirebaseDatabase.getInstance().getReference().child("Orders");

        data = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("UserDetails");


        DataRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("UserDetails")
                .child("PackageAnswers");

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("PRINTING OUT CHILD VALUES " + snapshot.getValue().toString() );
                System.out.println("PRINTING OUT CHILD  " + snapshot.child("1NameOfLogo").getValue() );
                String mName = snapshot.child("1NameOfLogo").getValue().toString();
                String lName = snapshot.child("1NameOfLogo").getValue().toString();
                String pCode = snapshot.child("PackageAnswerCodeValue").getValue().toString();
                packageInfo(lName, pCode);
            }

            private void packageInfo(String lName, String pCode) {

                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot newsnapshot : snapshot.getChildren()) {
                            String npost = snapshot.child("fullname").getValue().toString();

                            packageInfoUpdated(lName, pCode, npost);
                        }
                    }

                    private void packageInfoUpdated(final String lName, final String pCode, final String npost) {

                        HashMap hashMap = new HashMap();
                        hashMap.put("ClientLogoName", lName);
                        hashMap.put("ClientPackageCode", pCode);
                        hashMap.put("ClientName", npost);

                        Uref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(ThankYou.this,"PACKAGE HAS BEEN SENT TO T-EDITOR", Toast.LENGTH_LONG).show();
                                //setContentView(R.layout.activity_page_four);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error, Image not uploaded
                                Toast.makeText(ThankYou.this, "PACKAGE DID NOT SEND TO USER" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThankYou.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }


    public void backtoMain(View view) {
        startActivity(new Intent(ThankYou.this, teditsUser.class));
    }
}