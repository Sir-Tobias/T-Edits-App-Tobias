package com.example.t_edits_app_tobias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;

public class teditsQuestions extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedits_questions);
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
                if (checked)
                    break;
            case R.id.radio_secondaryStudents:
                if (checked)
                    break;
            case R.id.radio_collegeTeenager:
                if (checked)
                    break;
            case R.id.radio_luxury:
                if (checked)
                    break;
            case R.id.radio_sporty:
                if (checked)
                    break;
            case R.id.radio_techwear:
                if (checked)
                    break;
            case R.id.radio_Apparel:
                if (checked)
                    break;
            case R.id.radio_cosmetics:
                if (checked)
                    break;
            case R.id.radio_tech:
                if (checked)
                    break;
            case R.id.radio_green:
                if (checked)
                    break;
            case R.id.radio_blue:
                if (checked)
                    break;
            case R.id.radio_orange:
                if (checked)
                    break;
        }
    }
}