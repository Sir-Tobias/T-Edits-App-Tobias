package com.example.t_edits_app_tobias;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegister;

    private TextView AppName, registerUser;
    private EditText eFullname, ePhoneNo, eEmail, ePassword;
    private EditText fullnameUpdate, phonenoUpdate;

    private RadioGroup userOption;

    private FirebaseFirestore rFireStore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        rFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        AppName = (TextView) findViewById(R.id.appName);
        AppName.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        userOption = (RadioGroup) findViewById(R.id.userOption);

        //Initializing the user registration details
        eFullname = (EditText) findViewById(R.id.fullname);
        ePhoneNo = (EditText) findViewById(R.id.phoneNo);
        eEmail = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_designer:
                if (checked)
                    // Designer rule
                    break;
            case R.id.radio_customer:
                if (checked)
                    // Customer rule
                    break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appName:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String fullname = eFullname.getText().toString().trim();
        String phoneNo = ePhoneNo.getText().toString().trim();
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            Toast.makeText(RegisterUser.this, "Full name is required", Toast.LENGTH_LONG).show();
            eFullname.requestFocus();
            return;
        }
        if (phoneNo.isEmpty()) {
            Toast.makeText(RegisterUser.this, "Phone Number is empty", Toast.LENGTH_LONG).show();
            ePhoneNo.setError("Phone number field is empty");
            ePhoneNo.requestFocus();
            return;
        }
        if (phoneNo.length() < 8) {
            Toast.makeText(RegisterUser.this, "Phone number length must be atleast 8 numbers", Toast.LENGTH_LONG).show();
            ePhoneNo.setError("Phone number length must be atleast 8 numbers");
            ePhoneNo.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(RegisterUser.this, "Email id empty", Toast.LENGTH_LONG).show();
            eEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(RegisterUser.this, "Password is required", Toast.LENGTH_LONG).show();
            ePassword.setError("Password is required in this field");
            ePassword.requestFocus();
            return;
        }
        //Checks the length of the password
        if (password.length() < 6) {
            Toast.makeText(RegisterUser.this, "The minimum password length should be 6 characters", Toast.LENGTH_LONG).show();
            ePassword.setError("The minimum password length is 6 characters");
            ePassword.requestFocus();
            return;
        }
        //Checks if email has been repeated in the database
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterUser.this, "Please provide a valid email", Toast.LENGTH_LONG).show();
            eEmail.requestFocus();
            return;
        }


        //2.1 What is the gender audience?
        int selectedId = userOption.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) userOption.findViewById(selectedId);
        Toast.makeText(RegisterUser.this, radioButton.getText(), Toast.LENGTH_LONG).show();
        final String userType = radioButton.getText().toString();
        //Passing the registration details into the database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(fullname, phoneNo, email, userType);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserDetails")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register this user please try again 1" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register this user please try again 2" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

//FIRESTORE REGISTRATION
/*
        Map<String, String> userMap = new HashMap<>();
        userMap.put("Full Name", fullname);
        userMap.put("Phone number", phoneNo);
        userMap.put("Email", email);
        userMap.put("Password", password);


        rFireStore.collection("Users").document("TeditsUser").set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterUser.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterUser.this, "Failed to register this user please try again 1", Toast.LENGTH_SHORT).show();
            }
        });

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
*/


