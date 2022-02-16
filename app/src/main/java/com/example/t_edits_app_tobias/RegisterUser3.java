package com.example.t_edits_app_tobias;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser3 extends AppCompatActivity implements View.OnClickListener {

    private TextView AppName, registerUser;
    private EditText eFullname, ePhoneNo, eEmail, ePassword;
    private EditText fullnameUpdate, phonenoUpdate;

    String _FULLNAME, _EMAIL, _PASSWORD, _PHONENO;
    DatabaseReference reference;


    private FirebaseAuth mAuth;
    private boolean isNameChanged, isPhoneNoChanged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        AppName = (TextView) findViewById(R.id.appName);
        AppName.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        //Initializing the user registration details
        eFullname = (EditText) findViewById(R.id.fullname);
        ePhoneNo = (EditText) findViewById(R.id.phoneNo);
        eEmail = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);


        //Initializing the update details
//        fullnameUpdate = (EditText) findViewById(R.id.uFullname);
//        phonenoUpdate = (EditText) findViewById(R.id.uPhoneNo);

    }

    //Creating the register user method
    private void registerUser() {
        String fullname = eFullname.getText().toString().trim();
        String phoneNo = ePhoneNo.getText().toString().trim();
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            Toast.makeText(RegisterUser3.this, "Full name is required", Toast.LENGTH_LONG).show();
            eFullname.requestFocus();
            return;
        }
        if (phoneNo.isEmpty()) {
            Toast.makeText(RegisterUser3.this, "Phone Number is empty", Toast.LENGTH_LONG).show();
            ePhoneNo.setError("Phone number field is empty");
            ePhoneNo.requestFocus();
            return;
        }
        if (phoneNo.length() < 8) {
            Toast.makeText(RegisterUser3.this, "Phone number length must be atleast 8 numbers", Toast.LENGTH_LONG).show();
            ePhoneNo.setError("Phone number length must be atleast 8 numbers");
            ePhoneNo.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(RegisterUser3.this, "Email id empty", Toast.LENGTH_LONG).show();
            eEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(RegisterUser3.this, "Password is required", Toast.LENGTH_LONG).show();
            ePassword.setError("Password is required in this field");
            ePassword.requestFocus();
            return;
        }
        //Checks the length of the password
        if (password.length() < 6) {
            Toast.makeText(RegisterUser3.this, "The minimum password length should be 6 characters", Toast.LENGTH_LONG).show();
            ePassword.setError("The minimum password length is 6 characters");
            ePassword.requestFocus();
            return;
        }
        //Checks if email has been repeated in the database
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterUser3.this, "Please provide a valid email", Toast.LENGTH_LONG).show();
            eEmail.requestFocus();
            return;
        }

        //Passing the registration details into the database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(fullname, phoneNo, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser3.this, "User has been registered successfully!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUser3.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterUser3.this, "Failed to register this user please try again 1"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser3.this, "Failed to register this user please try again 2"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

//            case R.id.updateUser:
//                updateUser();
//                break;

        }

    }

    private void updateUser() {
        String fullname = fullnameUpdate.getText().toString().trim();
        String phoneNo = phonenoUpdate.getText().toString().trim();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        User userObj = new User(fullname, phoneNo); //updade the whole node
        fireDB.setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid) {      // Write was successful!
                Toast.makeText(RegisterUser3.this, "Update succesful", Toast.LENGTH_LONG).show();
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {// Write failed
                Toast.makeText(RegisterUser3.this, "Update failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void update(View view) {

        if(isNameChanged || isPhoneNoChanged)  {
            Toast.makeText(this, "Data has been changed", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Data is the same and cannot be updated", Toast.LENGTH_LONG).show();
    }


    private boolean isPhoneNoChanged() {
        if(!_PHONENO.equals(ePhoneNo.getText().toString())){
            reference.child(_PHONENO).child("phoneNo").setValue(ePhoneNo.getText().toString());
            _PHONENO = ePhoneNo.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isNameChanged() {

        if(!_FULLNAME.equals(eFullname.getText().toString())){
            reference.child(_FULLNAME).child("fullname").setValue(eFullname.getText().toString());
            _FULLNAME = eFullname.getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
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
}
   /*
    public void showAllUserData() {
        Intent intent = getIntent();
        _FULLNAME = intent.getStringExtra("fullname");
        _PHONENO = intent.getStringExtra("phoneNo");
        _EMAIL = intent.getStringExtra("email");
        _PASSWORD = intent.getStringExtra("password");

        eFullname.setText(_FULLNAME);
        ePhoneNo.setText(_PHONENO);
        eEmail.setText(_EMAIL);
        ePassword.setText(_PASSWORD);

    }
    */