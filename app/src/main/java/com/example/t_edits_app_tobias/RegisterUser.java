package com.example.t_edits_app_tobias;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    private Button btnRegister;

//    private EditText edtxtEmail;
//    private EditText edtxtTpnumber;
//    private EditText edtxtDepartment;
//    private EditText edtxtPassword;
//    private EditText edtxtConfirmpassword;

    private TextView AppName, registerUser;
    private EditText eFullname, ePhoneNo, eEmail, ePassword;
    private EditText fullnameUpdate, phonenoUpdate;

    private FirebaseFirestore rFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        rFireStore = FirebaseFirestore.getInstance();

//        btnRegister = (Button) findViewById(R.id.btnregister);
//        edtxtEmail = (EditText) findViewById(R.id.edtxtemail);
//        edtxtTpnumber = (EditText) findViewById(R.id.edtxttpnumber);
//        edtxtDepartment = (EditText) findViewById(R.id.edtxtdepartment);
//        edtxtPassword = (EditText) findViewById(R.id.edtxtpassword);
//        edtxtConfirmpassword = (EditText) findViewById(R.id.edtxtconfirmPassword);

        AppName = (TextView) findViewById(R.id.appName);
        AppName.setOnClickListener((View.OnClickListener) this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener((View.OnClickListener) this);

        //Initializing the user registration details
        eFullname = (EditText) findViewById(R.id.fullname);
        ePhoneNo = (EditText) findViewById(R.id.phoneNo);
        eEmail = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                Map<String, String> userMap = new HashMap<>();
                userMap.put("Full Name", fullname);
                userMap.put("Phone number", phoneNo);
                userMap.put("Email", email);
                userMap.put("Password", password);

                rFireStore.collection("Users").document("TeditsUser").set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterUser.this, "Failed to register this user please try again 1", Toast.LENGTH_SHORT).show();
                    }
                });

                /*rFireStore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterActivity.this, "Data Saved!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Data Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
    }
}
