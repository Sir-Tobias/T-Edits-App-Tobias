package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PageFour extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private static final int REQUEST_CODF_IMAGE = 101;

    private ImageView sketchViewAdd;

    private EditText questionFour;
    private RadioGroup radioGroupFour, radioGroupFourOne, radioGroupFourTwo;
    //private RadioButton logoType;

    //Question one String values
    private String primaryColour;

    //Question two String values
    private String genderAudience;
    private String ageDemographic;

    //Question three String values
    private String brandIndustry;

    Button submitFour;

    boolean isSketchAdded=false;

    Uri imageUri;

    //Firebase Reference
    private CollectionReference rFireStore;
    private DatabaseReference Dataref;
    private StorageReference Storageref;
    private FirebaseStorage storage;

    //SHARED PREFERENCES
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_questions_one);

        radioGroupFour = (RadioGroup) findViewById(R.id.optionfour);
        radioGroupFourOne = (RadioGroup) findViewById(R.id.optionfourone);
        radioGroupFourTwo = (RadioGroup) findViewById(R.id.optionfourtwo);

        submitFour = (Button) findViewById(R.id.submitFour);

        storage = FirebaseStorage.getInstance();
        Storageref = storage.getReference().child("SketchBackedUp");

        //LOADING THE SHARED PREFERENCES FROM PAGE ONE
        SharedPreferences sa = getApplicationContext().getSharedPreferences("AnswerOne", Context.MODE_PRIVATE);
        String nameLogo = sa.getString("nameLogo", "");
        String typeLogo = sa.getString("typeLogo", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE TWO
        SharedPreferences sb = getApplicationContext().getSharedPreferences("AnswerTwo", Context.MODE_PRIVATE);
        String targetAudience = sb.getString("targetAudience", "");
        String genderAudience = sb.getString("genderAudience", "");
        String ageDemographic = sb.getString("ageDemographic", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE THREE
        SharedPreferences sc = getApplicationContext().getSharedPreferences("AnswerThree", Context.MODE_PRIVATE);
        String industryDescription = sc.getString("industryDescription", "");
        String industryType = sc.getString("industryType", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE FOUR
        SharedPreferences sd = getApplicationContext().getSharedPreferences("AnswerFour", Context.MODE_PRIVATE);
        String primaryColour = sd.getString("primaryColour", "");
        String secondaryColour = sd.getString("secondaryColour", "");
        String neutralColour = sd.getString("neutralColour", "");


        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LogoPackage");

        //rFireStore = FirebaseFirestore.getInstance().collection("Users").document().collection("LogoPackage");
        sketchViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODF_IMAGE);

            }
        });
        radioGroupFour.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButtonfour = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );

        radioGroupFourOne.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButtonfourone = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );

        radioGroupFourTwo.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButtonfourtwo = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );

        //SUBMIT PAGE FOUR
        submitFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroupFour.getCheckedRadioButtonId();
                int selectedOneId = radioGroupFourOne.getCheckedRadioButtonId();
                int selectedTwoId = radioGroupFourTwo.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radioGroupFour.findViewById(selectedId);
                //Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String primaryColour = radioButton.getText().toString();

                RadioButton radioButtonOne = (RadioButton) radioGroupFourOne.findViewById(selectedOneId);
                //Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String secondaryColour = radioButtonOne.getText().toString();

                RadioButton radioButtonTwo = (RadioButton) radioGroupFourTwo.findViewById(selectedTwoId);
                //Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String neutralColour = radioButtonTwo.getText().toString();

                //Creating my shared preference editor
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("primaryColour", primaryColour);
                editor.putString("secondaryColour", secondaryColour);
                editor.putString("neutralColour", neutralColour);
                editor.commit();
                Toast.makeText(PageFour.this,"Successfully saved question four to preferences", Toast.LENGTH_LONG).show();

                if(primaryColour!=null) {
                    submitFour(primaryColour, secondaryColour, neutralColour);
                }

                retrieveAnswers();
            }
        });


    }

    private void retrieveAnswers() {
        //STRING PACKAGE ANSWER
        String packageAnswer;

        //LOADING THE SHARED PREFERENCES FROM PAGE ONE
        SharedPreferences sa = getApplicationContext().getSharedPreferences("AnswerOne", Context.MODE_PRIVATE);
        String nameLogo = sa.getString("nameLogo", "");
        String typeLogo = sa.getString("typeLogo", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE TWO
        SharedPreferences sb = getApplicationContext().getSharedPreferences("AnswerTwo", Context.MODE_PRIVATE);
        String targetAudience = sb.getString("targetAudience", "");
        String genderAudience = sb.getString("genderAudience", "");
        String ageDemographic = sb.getString("ageDemographic", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE THREE
        SharedPreferences sc = getApplicationContext().getSharedPreferences("AnswerThree", Context.MODE_PRIVATE);
        String industryDescription = sc.getString("industryDescription", "");
        String industryType = sc.getString("industryType", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE FOUR
        SharedPreferences sd = getApplicationContext().getSharedPreferences("AnswerFour", Context.MODE_PRIVATE);
        String primaryColour = sd.getString("primaryColour", "");
        String secondaryColour = sd.getString("secondaryColour", "");
        String neutralColour = sd.getString("neutralColour", "");

        if (nameLogo.isEmpty()) {
            Toast.makeText(PageFour.this, "Logo name is required on page one", Toast.LENGTH_LONG).show();
        }
        //ANSWER 1.1 WHAT IS YOUR LOGO TYPE?
        if (typeLogo.equalsIgnoreCase("Icon type")) {
        }
        else if (typeLogo.equalsIgnoreCase("Name type")) {
        }

        //ANSWER 2.1 WHAT IS THE GENDER AUDIENCE OF YOUR BRAND?
        if (genderAudience.equalsIgnoreCase("Male")) {
        }
        else if (genderAudience.equalsIgnoreCase("Female")) {
        }
        else if (genderAudience.equalsIgnoreCase("Gender Neutral")) {
        }

        //ANSWER 2.2 WHAT IS THE AGE DEMOGRAPHIC FOR YOUR BRAND?
        if (ageDemographic.equalsIgnoreCase("15 >")) {
        }
        else if (ageDemographic.equalsIgnoreCase("15 - 45")) {
        }
        else if (ageDemographic.equalsIgnoreCase("45 +")) {
        }

        //ANSWER 3.1 WHAT IS THE INDUSTRY OF YOUR BRAND?
        if (industryType.equalsIgnoreCase("Apparel")) {
        }
        else if (industryType.equalsIgnoreCase("Media")) {
        }
        else if (industryType.equalsIgnoreCase("Cosmetics")) {
        }
        else if (industryType.equalsIgnoreCase("Tech")) {
        }

        //ANSWER 4.0 ENTER YOUR PRIMARY COLOUR?
        if (primaryColour.equalsIgnoreCase("White")) {
        }
        else if (primaryColour.equalsIgnoreCase("Yellow")) {
        }
        else if (primaryColour.equalsIgnoreCase("Red")) {
        }
        else if (primaryColour.equalsIgnoreCase("Blue")) {
        }
        else if (primaryColour.equalsIgnoreCase("Black")) {
        }

        //ANSWER 4.1 ENTER YOUR SECONDARY COLOUR?
        if (primaryColour.equalsIgnoreCase("Orange")) {
        }
        else if (secondaryColour.equalsIgnoreCase("Green")) {
        }
        else if (secondaryColour.equalsIgnoreCase("Purple")) {
        }

        //ANSWER 4.2 ENTER YOUR NEUTRAL COLOUR?
        if (primaryColour.equalsIgnoreCase("Brown")) {
        }
        else if (secondaryColour.equalsIgnoreCase("Beige")) {
        }
        else if (secondaryColour.equalsIgnoreCase("Grey")) {
        }

    }

    //ANSWER PAGE THREE
    private void submitFour(String primaryColour, String secondaryColour, String neutralColour) {
        final String key = Dataref.push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put("4* Select a primary colour: ", primaryColour);
        hashMap.put("4*1 Select a secondary colour: ", secondaryColour);
        hashMap.put("4*2 Select a neutral colour: ", neutralColour);

        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PageFour.this,"Question four has been submitted", Toast.LENGTH_LONG).show();
                //setContentView(R.layout.activity_page_four);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, Image not uploaded
                Toast.makeText(PageFour.this, "Failed to submit answer four" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(PageFour.this, MainActivity.class));
                break;

            case R.id.nav_upload_content:
                finish();
                startActivity(new Intent(PageFour.this, teditsContent.class));
                break;

            case R.id.nav_content_catalogue:
                finish();
                startActivity(new Intent(PageFour.this, teditsCatalogue.class));
                break;

            case R.id.nav_user_catalogue:
                finish();
                startActivity(new Intent(PageFour.this, teditsUserCatalogue.class));
                break;
            case R.id.nav_elements_catalogue:
                finish();
                startActivity(new Intent(PageFour.this, teditsElementCatalogue.class));
                break;

            case R.id.nav_home:
                finish();
                startActivity(new Intent(PageFour.this, teditsUser.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODF_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isSketchAdded=true;
            sketchViewAdd.setImageURI(imageUri);
        }
    }
}