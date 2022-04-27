package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class teditsQuestions extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final int REQUEST_CODF_IMAGE = 101;

    private ImageView sketchViewAdd;

    private EditText questionOne, questionTwo, questionThree;
    private RadioGroup radioGroupOne, radioGroupTwoOne, radioGroupTwoTwo, radioGroupThreeOne, radioGroupFour;
    //private RadioButton logoType;

    //Question one String values
    private String logoType;

    //Question two String values
    private String genderAudience;
    private String ageDemographic;

    //Question three String values
    private String brandIndustry;

    Button submitOne;
    Button submitTwo;
    Button submitThree;
    Button submitFour;

    boolean isSketchAdded=false;

    Uri imageUri;

    ImageView mImage, cImage, aImage;

    //Firebase Reference
    private CollectionReference rFireStore;
    private DatabaseReference Dataref;
    private StorageReference Storageref;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_questions);


        sketchViewAdd = findViewById(R.id.sketchupload);

        questionOne = findViewById(R.id.logonameText);
        questionTwo = findViewById(R.id.targetAudience);
        questionThree = findViewById(R.id.brandIndustry);

        radioGroupOne = (RadioGroup) findViewById(R.id.optionone);
        radioGroupTwoOne = (RadioGroup) findViewById(R.id.optiontwoone);
        radioGroupTwoTwo = (RadioGroup) findViewById(R.id.optiontwotwo);
        radioGroupThreeOne = (RadioGroup) findViewById(R.id.optionthree);
        radioGroupFour = (RadioGroup) findViewById(R.id.optionfour);

        submitOne = (Button) findViewById(R.id.submitOne);
        submitTwo = (Button) findViewById(R.id.submitTwo);

        storage = FirebaseStorage.getInstance();
        Storageref = storage.getReference().child("SketchBackedUp");

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
        radioGroupOne.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton)radioGroup.findViewById(checkId);
                    }
                }
        );


        //SUBMIT ANSWER ONE
        submitOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_tedits_questions_two);

                int selectedId = radioGroupOne.getCheckedRadioButtonId();

                //1. What is the name of your brand?
                final String qOne = questionOne.getText().toString();

                RadioButton radioButton = (RadioButton) radioGroupOne.findViewById(selectedId);
                Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String logoType = radioButton.getText().toString();

                if(isSketchAdded!=false && qOne!=null && logoType!=null) {
                    submitOne(qOne, logoType);
                }
//                //1.2 Select a logo type
//                else if(selectedId == -1) {
//                    Toast.makeText(teditsQuestions.this, "No option has been selected", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    RadioButton radioButton = (RadioButton) radioGroupOne.findViewById(selectedId);
//                    //Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
//                    final String logoType = radioButton.getText().toString();
//
//                }
            }
        });

        //SUBMIT PAGE TWO
        submitTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //2. Describe the target audience of your brand
                final String qTwo = questionTwo.getText().toString();

                //2.1 What is the gender audience?
                int selectedId = radioGroupTwoOne.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radioGroupTwoOne.findViewById(selectedId);
                Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String genderAudience = radioButton.getText().toString();

                //2.2 What is the age demographic?
                int selectedtwoId = radioGroupTwoTwo.getCheckedRadioButtonId();

                RadioButton radioButton2 = (RadioButton) radioGroupTwoTwo.findViewById(selectedId);
                Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
                final String ageDemographic = radioButton2.getText().toString();

                if (qTwo != null && genderAudience != null && ageDemographic != null) {
                    submitTwo(qTwo, genderAudience, ageDemographic);
                }

            }
        });



   }

    //ANSWER PAGE ONE
    private void submitOne(final String qOne, final String logoType) {

        //Data reference key
        final String key = Dataref.push().getKey();

        Storageref.child(key+ ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Storageref.child(key+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("1 Name of logo: ", qOne);
                        hashMap.put("1*2 Type of logo: ", logoType);
                        hashMap.put("1*3 ImageUri", uri.toString());

                        //Push key
                        Dataref.child("Q1.1").setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(teditsQuestions.this,"Question one has been submitted", Toast.LENGTH_LONG).show();
                                //LAUNCHES THE PAGE TWO
//                                Intent intent = new Intent(this, teditsUser.class);
//                                this.startActivity (intent);
                                setContentView(R.layout.activity_tedits_questions);
                                //SOLUTION ONE
//                                submitTwo = (Button) findViewById(R.id.submitTwo);
//
//                                //SUBMIT PAGE TWO
//                                submitTwo.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                        //2. Describe the target audience of your brand
//                                        final String qTwo = questionTwo.getText().toString();
//
//                                        //2.1 What is the gender audience?
//                                        int selectedId = radioGroupTwoOne.getCheckedRadioButtonId();
//
//                                        RadioButton radioButton = (RadioButton) radioGroupTwoOne.findViewById(selectedId);
//                                        Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
//                                        final String genderAudience = radioButton.getText().toString();
//
//                                        //2.2 What is the age demographic?
//                                        int selectedtwoId = radioGroupTwoTwo.getCheckedRadioButtonId();
//
//                                        RadioButton radioButton2 = (RadioButton) radioGroupTwoTwo.findViewById(selectedId);
//                                        Toast.makeText(teditsQuestions.this, radioButton.getText(), Toast.LENGTH_LONG).show();
//                                        final String ageDemographic = radioButton2.getText().toString();
//
//                                        if (qTwo != null && genderAudience != null && ageDemographic != null) {
//                                            submitTwo(qTwo, genderAudience, ageDemographic);
//                                        }
//
//                                    }
//                                });
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, Image not uploaded
                Toast.makeText(teditsQuestions.this, "Failed to submit answer " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ANSWER PAGE TWO
    private void submitTwo(String qTwo, String genderAudience, String ageDemographic) {
        final String key = Dataref.push().getKey();

            HashMap hashMap = new HashMap();
            hashMap.put("2* Description of brand: ", qTwo);
            hashMap.put("2*1 Type of Gender audience: ", genderAudience);
            hashMap.put("2*2 Age demographic: ", ageDemographic);

            Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(teditsQuestions.this,"Question two has been submitted", Toast.LENGTH_LONG).show();
                    setContentView(R.layout.activity_tedits_questions);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    Toast.makeText(teditsQuestions.this, "Failed to submit answer " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

//        //Data reference key
//        final String key = Dataref.push().getKey();
//
//        Storageref.child(key+ ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Storageref.child(key+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        HashMap hashMap = new HashMap();
//                        hashMap.put("2. Description of brand: ", qTwo);
//                        hashMap.put("2.1 Type of Gender audience: ", genderAudience);
//                        hashMap.put("2.2 Age demographic: ", ageDemographic);
//
//                        //Push key
//                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(teditsQuestions.this,"Question two has been submitted", Toast.LENGTH_LONG).show();
//                                setContentView(R.layout.activity_tedits_questions_three);
//                            }
//                        });
//
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // Error, Image not uploaded
//                Toast.makeText(teditsQuestions.this, "Failed to submit answer " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //ANSWER PAGE THREE
//    private void submitTwo(String qThree, String brandIndustry) {
//    }


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

//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch (view.getId()) {
//            case R.id.radio_adults:
//                if (checked)
//                    break;
//            case R.id.radio_teenager:
//                if (checked);
//        }
//    }

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

    public void onClickTwo(View view) {
    }
}