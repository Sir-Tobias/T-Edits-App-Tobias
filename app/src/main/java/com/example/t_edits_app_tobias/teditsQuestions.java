package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class teditsQuestions extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final int REQUEST_CODF_IMAGE = 101;

    private ImageView sketchViewAdd;

    private EditText questionOne, questionTwo, questionThree;
    private RadioGroup radioGroupOne, radioGroupTwoTwo, radioGroupTwoThree, radioGroupThreeOne, radioGroupFour;

    Button submitOne;
    Button submitTwo;
    Button submitThree;
    Button submitFour;

    Uri imageUri;

    //Firebase Reference
    private CollectionReference rFireStore;
    private DatabaseReference Dataref;
    private StorageReference Storageref;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_questions_one);

        sketchViewAdd = findViewById(R.id.sketchupload);

        questionOne = findViewById(R.id.logonameText);
        questionTwo = findViewById(R.id.targetAudience);
        questionThree = findViewById(R.id.brandIndustry);

        radioGroupOne = (RadioGroup) findViewById(R.id.optionone);
        radioGroupTwoTwo = (RadioGroup) findViewById(R.id.optiontwotwo);
        radioGroupTwoThree = (RadioGroup) findViewById(R.id.optiontwothree);
        radioGroupThreeOne = (RadioGroup) findViewById(R.id.optionthree);
        radioGroupFour = (RadioGroup) findViewById(R.id.optionfour);

        submitOne = (Button) findViewById(R.id.submitOne);

        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LogoPackage");

        sketchViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODF_IMAGE);

            }
        });
        radioGroupOne.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );
        submitOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_tedits_questions_two);

                int selectedId = radioGroupOne.getCheckedRadioButtonId();
                if(selectedId == -1) {
                    Toast.makeText(teditsQuestions.this, "No option has been selected", Toast.LENGTH_LONG).show();
                }
                else {
                    RadioButton radioButton = (RadioButton) radioGroupOne.findViewById(selectedId);
                    Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(teditsQuestions.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(teditsQuestions.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(teditsQuestions.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(teditsQuestions.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(teditsQuestions.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(teditsQuestions.this, teditsUser.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_adults:
                if (checked)
                    break;
            case R.id.radio_teenager:
                if (checked);
        }
    }
}