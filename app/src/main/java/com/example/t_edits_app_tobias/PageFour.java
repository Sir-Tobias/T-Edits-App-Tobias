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
    SharedPreferences op;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_four);

        radioGroupFour = (RadioGroup) findViewById(R.id.optionfour);
        radioGroupFourOne = (RadioGroup) findViewById(R.id.optionfourone);
        radioGroupFourTwo = (RadioGroup) findViewById(R.id.optionfourtwo);

        submitFour = (Button) findViewById(R.id.submitFour);

        storage = FirebaseStorage.getInstance();
        Storageref = storage.getReference().child("SketchBackedUp");

        //INSTANTIATING MY SHARED PREFERENCE
        sp = getSharedPreferences("AnswerFour", Context.MODE_PRIVATE);

        //INSTANTIATING MY SHARED PREFERENCE for my package answer
        op = getSharedPreferences("PackageAnswer", Context.MODE_PRIVATE);

        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LogoPackage");

        //rFireStore = FirebaseFirestore.getInstance().collection("Users").document().collection("LogoPackage");

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

//                if(primaryColour!=null) {
//                    submitFour(primaryColour, secondaryColour, neutralColour);
//                }

                retrieveAnswers();
            }
        });


    }

    private void retrieveAnswers() {
        //STRING PACKAGE ANSWER
        String packageAnswer = null;

        String answerOne = null;
        String answerTwo = null;
        String answerThree = null;
        String answerFour = null;
        String answerFive = null;
        String answerSix = null;
        String answerSeven = null;


        //LOADING THE SHARED PREFERENCES FROM PAGE ONE
        SharedPreferences sa = getApplicationContext().getSharedPreferences("AnswerOne", Context.MODE_PRIVATE);
        String nameLogo = sa.getString("nameLogo", "");
        //Answer for the generator
        String typeLogo = sa.getString("typeLogo", "");

        String imageUri = sa.getString("imageURI", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE TWO
        SharedPreferences sb = getApplicationContext().getSharedPreferences("AnswerTwo", Context.MODE_PRIVATE);
        String targetAudience = sb.getString("targetAudience", "");
        //Answer for the generator
        String genderAudience = sb.getString("genderAudience", "");
        String ageDemographic = sb.getString("ageDemographic", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE THREE
        SharedPreferences sc = getApplicationContext().getSharedPreferences("AnswerThree", Context.MODE_PRIVATE);
        String industryDescription = sc.getString("industryDescription", "");
        //Answer for the generator
        String industryType = sc.getString("industryType", "");

        //LOADING THE SHARED PREFERENCES FROM PAGE FOUR
        SharedPreferences sd = getApplicationContext().getSharedPreferences("AnswerFour", Context.MODE_PRIVATE);
        //Answer for the generator
        String primaryColour = sd.getString("primaryColour", "");
        //Answer for the generator
        String secondaryColour = sd.getString("secondaryColour", "");
        //Answer for the generator
        String neutralColour = sd.getString("neutralColour", "");

        //ANSWER 1.1 WHAT IS YOUR LOGO TYPE?
        HashMap<String, String>lType = new HashMap<>();
        lType.put("Icon type", "AA,");
        lType.put("Name type", "AB,");
        //SETTING ANSWERS TO THE SHAREDPREFERENCE VALUE WHICH WILL CONVERT IT INTO A CODE VALUE
        answerOne = lType.get(typeLogo);


        //ANSWER 2.1 WHAT IS THE GENDER AUDIENCE OF YOUR BRAND?
        HashMap<String, String>gAudience = new HashMap<>();
        gAudience.put("Male", "BA,");
        gAudience.put("Female", "BB,");
        gAudience.put("Gender Neutral", "BC,");

        answerTwo = answerOne + gAudience.get(genderAudience);


        //ANSWER 2.2 WHAT IS THE AGE DEMOGRAPHIC FOR YOUR BRAND?
        HashMap<String, String>aDemographic = new HashMap<>();
        aDemographic.put("15 >", "BD,");
        aDemographic.put("15 - 45", "BE,");
        aDemographic.put("45 +", "BF,");

        answerThree = answerTwo + aDemographic.get(ageDemographic);


        //ANSWER 3.1 WHAT IS THE INDUSTRY OF YOUR BRAND?
        HashMap<String, String>bIndustry = new HashMap<>();
        bIndustry.put("Apparel", "CA,");
        bIndustry.put("Media", "CB,");
        bIndustry.put("Cosmetics", "CC,");
        bIndustry.put("Tech", "CD,");

        answerFour = answerThree + bIndustry.get(industryType);

        //ANSWER 4.0 ENTER YOUR PRIMARY COLOUR?
        HashMap<String, String>pColours = new HashMap<>();
        pColours.put("White", "DA,");
        pColours.put("Yellow", "DB,");
        pColours.put("Red", "DC,");
        pColours.put("Blue", "DD,");
        pColours.put("Black", "DE,");

        answerFive = answerFour + pColours.get(primaryColour);

        //ANSWER 4.2 ENTER YOUR SECONDARY COLOUR?
        HashMap<String, String>sColours = new HashMap<>();
        sColours.put("Orange", "DF,");
        sColours.put("Green", "DG,");
        sColours.put("Purple", "DH,");

        answerSix = answerFive + sColours.get(secondaryColour);

        //ANSWER 4.3 ENTER YOUR NEUTRAL COLOUR?
        HashMap<String, String>nColours = new HashMap<>();
        nColours.put("Brown", "DJ");
        nColours.put("Beige", "DK");
        nColours.put("Grey", "DL");

        answerSeven = answerSix + nColours.get(neutralColour);

        packageAnswer = answerSeven;

        //PASSING THE PACKAGE ANSWER STRING INTO A SHAREDPREFERENCE
        SharedPreferences.Editor oEditor = op.edit();

        oEditor.putString("packageAnswer", packageAnswer);
        oEditor.commit();
        Toast.makeText(PageFour.this,"Successfully saved packageAnswer", Toast.LENGTH_LONG).show();

        submitFour(nameLogo, typeLogo, imageUri, targetAudience, genderAudience , ageDemographic, industryDescription, industryType, primaryColour, secondaryColour, neutralColour, packageAnswer);


//        if (nameLogo.isEmpty()) {
//            Toast.makeText(PageFour.this, "Logo name is required on page one", Toast.LENGTH_LONG).show();
//        }
//        //ANSWER 1.1 WHAT IS YOUR LOGO TYPE?
//        if (typeLogo.equalsIgnoreCase("Icon type")) {
//        }
//        else if (typeLogo.equalsIgnoreCase("Name type")) {
//        }




        //ANSWER 2.1 WHAT IS THE GENDER AUDIENCE OF YOUR BRAND?
//        if (genderAudience.equalsIgnoreCase("Male")) {
//        }
//        else if (genderAudience.equalsIgnoreCase("Female")) {
//        }
//        else if (genderAudience.equalsIgnoreCase("Gender Neutral")) {
//        }



        //ANSWER 2.2 WHAT IS THE AGE DEMOGRAPHIC FOR YOUR BRAND?
//        if (ageDemographic.equalsIgnoreCase("15 >")) {
//        }
//        else if (ageDemographic.equalsIgnoreCase("15 - 45")) {
//        }
//        else if (ageDemographic.equalsIgnoreCase("45 +")) {
//        }



        //ANSWER 3.1 WHAT IS THE INDUSTRY OF YOUR BRAND?
//        if (industryType.equalsIgnoreCase("Apparel")) {
//        }
//        else if (industryType.equalsIgnoreCase("Media")) {
//        }
//        else if (industryType.equalsIgnoreCase("Cosmetics")) {
//        }
//        else if (industryType.equalsIgnoreCase("Tech")) {
//        }




//        //ANSWER 4.0 ENTER YOUR PRIMARY COLOUR?
//        if (primaryColour.equalsIgnoreCase("White")) {
//        }
//        else if (primaryColour.equalsIgnoreCase("Yellow")) {
//        }
//        else if (primaryColour.equalsIgnoreCase("Red")) {
//        }
//        else if (primaryColour.equalsIgnoreCase("Blue")) {
//        }
//        else if (primaryColour.equalsIgnoreCase("Black")) {
//        }


//        if (primaryColour.equalsIgnoreCase("Orange")) {
//        }
//        else if (secondaryColour.equalsIgnoreCase("Green")) {
//        }
//        else if (secondaryColour.equalsIgnoreCase("Purple")) {
//        }


        //ANSWER 4.3 ENTER YOUR NEUTRAL COLOUR?
//        if (primaryColour.equalsIgnoreCase("Brown")) {
//        }
//        else if (secondaryColour.equalsIgnoreCase("Beige")) {
//        }
//        else if (secondaryColour.equalsIgnoreCase("Grey")) {
//        }
//
    }

    //ANSWER PAGE THREE
    private void submitFour(String nameLogo, String typeLogo, String imageUri, String targetAudience, String genderAudience, String ageDemographic, String industryDescription, String industryType, String primaryColour, String secondaryColour, String neutralColour, String packageAnswer) {
        final String key = Dataref.push().getKey();

        HashMap hashMap = new HashMap();
        hashMap.put("1 Name of logo ", nameLogo);
        hashMap.put("1*2 Type of logo ", typeLogo);
        hashMap.put("1*3 Sketch of logo ", imageUri);

        hashMap.put("2* Description of brand ", targetAudience);
        hashMap.put("2*1 Type of Gender audience ", genderAudience);
        hashMap.put("2*2 Age demographic ", ageDemographic);

        hashMap.put("3* Description of brand industry ", industryDescription);
        hashMap.put("3*1 Type of Gender audience ", industryType);

        hashMap.put("4* Select a primary colour ", primaryColour);
        hashMap.put("4*1 Select a secondary colour ", secondaryColour);
        hashMap.put("4*2 Select a neutral colour ", neutralColour);

        hashMap.put("Package answer code Value ", packageAnswer);

        Dataref.child("Package Answers").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(PageFour.this,"All answers have been submitted has been submitted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PageFour.this, UserPallette.class));
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