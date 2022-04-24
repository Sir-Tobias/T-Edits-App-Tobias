package com.example.t_edits_app_tobias;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class UserCheckout extends AppCompatActivity {
    Button button;
    String SECRET_KEY="sk_test_51Ks3pqDxAnj6vyQ9CHngvYHc1Hw6mxTByZ83PkcJ8xlVf8OjI9cWskyyJjJvjIHkyUgd3xVEX9yRRi6enJZCWK7h00VQYA6rr7";
    String PUBLISH_KEY="pk_test_51Ks3pqDxAnj6vyQ9OCXA35bCZRRrMhjJk2sSYopmLV77h3lZVBx6f2BiyspTkd9bGJIq5q3Q4i2sTGS6nN3ZnxFZ00yxDvtVUB";

    PaymentSheet paymentSheet;
    String customerID;
    String EphericalKey;
    String ClientSecret;

    ImageView gpayButton, spayButton;

    TextView viewOne, viewTwo;

    private DatabaseReference Dataref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checkout);

        gpayButton = (ImageView) findViewById(R.id.googlepay);
        spayButton = (ImageView) findViewById(R.id.stripepay);

        viewOne = (TextView) findViewById(R.id.paragraphOne);
        viewTwo = (TextView) findViewById(R.id.paragraphTwo);

        //INITIALIZING STRIPE SDK
        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet=new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });

        //STRIPE PAY BUTTON
        spayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });

        loadInformation();


        //THE PAYMENT BUTTON WILL START THE PAYMENT FLOW
//        paymentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PaymentFlow();
//            }
//        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            Toast.makeText(UserCheckout.this, customerID, Toast.LENGTH_LONG).show();
                            System.out.println("CustomerID: "+customerID);


                            getEphericalKey(customerID);
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //RETRIVING THE AUTHORIZATION SECRET KEY FROM VOLLEY
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserCheckout.this);
        requestQueue.add(stringRequest);
    }

    //Query q = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

    private void loadInformation() {
        Dataref = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("UserDetails");

        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User post = snapshot.getValue(User.class);
                System.out.println("This is working Tobias"+ post);
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //System.out.println("does work "+ postSnapshot.child("fullname").getValue().toString());
                    System.out.println("Hello "+postSnapshot.child("fullname").getValue().toString());
                    //RETRIEVING THE FULL NAME OF THE USER FROM THE FIREBASE DATABASE
                    viewOne.setText("Hello "+postSnapshot.child("fullname").getValue().toString());


                    //LOADING THE SHARED PREFERENCES FROM PAGE ONE TO GET THE LOGO NAME
                    SharedPreferences sa = getApplicationContext().getSharedPreferences("AnswerOne", Context.MODE_PRIVATE);
                    String nameLogo = sa.getString("nameLogo", "");
                    viewTwo.setText("We are delighted that you have made it this far. We look forward to bringing your " + nameLogo + " logo to life.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserCheckout.this, error.getMessage(), Toast.LENGTH_LONG);

            }
        });
    }


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "your payment was successful thank you for using T-Edits ", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "your payment was unsuccessful please try again ", Toast.LENGTH_SHORT).show();
        }
    }

    //RETRIEVING THE EPHERICAL KEY
    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            Toast.makeText(UserCheckout.this, EphericalKey, Toast.LENGTH_LONG).show();
                            System.out.println("EphericalKey: "+EphericalKey);


                            //getEphericalKey(EphericalKey);
                            getClientSecret(customerID, EphericalKey);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //RETRIVING THE AUTHORIZATION SECRET KEY FROM VOLLEY
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2020-08-27");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserCheckout.this);
        requestQueue.add(stringRequest);
    }



    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");

                            //Toast.makeText(UserCheckout.this, ClientSecret, Toast.LENGTH_LONG).show();
                            System.out.println("ClientSecret: "+ClientSecret);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //RETRIVING THE AUTHORIZATION SECRET KEY FROM VOLLEY
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "38" + "00");
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserCheckout.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("T-EDITS AI", new PaymentSheet.CustomerConfiguration(
                        customerID, EphericalKey
                ))
        );
    }

}