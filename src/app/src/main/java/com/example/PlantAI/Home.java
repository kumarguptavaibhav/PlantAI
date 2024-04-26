package com.example.PlantAI;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.PlantAI.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Home extends AppCompatActivity {
    
    private ImageView profileImageView;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button buttonwhe;
    private ImageButton devbutton;
    private long pressedTime;
    FirebaseAuth mAuth;

    private TextView UserInfo;
    private EditText name;
    private EditText email;
    private EditText contact;
    private EditText message;
    private Button submit;
    // creating a variable for our
    // Firebase Database.
    private FirebaseFirestore db;
    private FirebaseUser user;

    private TextView loadweb;
    private WebView webView;




    //private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        UserInfo = findViewById(R.id.userinfo);
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            UserInfo.setText(user.getEmail());
        }

        button4 = findViewById(R.id.signout);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button5 = findViewById(R.id.calculator);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity6();
            }
        });

        button6 = findViewById(R.id.disease);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity7();
            }
        });


        buttonwhe = findViewById(R.id.whether);
        buttonwhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity10();
            }
        });

        ScrollView contform;
        contform = findViewById(R.id.contact_form);

        devbutton = findViewById(R.id.imgbutton);
        devbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contform.setVisibility(View.VISIBLE);
                toggleVisibilityWithTransition(contform);
            }

        });

        // instance of our FIrebase database.
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputEmail);
        contact = findViewById(R.id.inputPassword);
        message = findViewById(R.id.inputConfirmPassword);
        // below line is used to get the
        submit = findViewById(R.id.buttonSignUp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_contact = contact.getText().toString();
                String txt_message = message.getText().toString();
                if (txt_name.isEmpty() || txt_email.isEmpty() || txt_contact.isEmpty() || txt_message.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please Enter all the details.", Toast.LENGTH_SHORT).show();
                } else {

                    String documentName = String.valueOf(System.currentTimeMillis());

                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("Name", txt_name);
                    hashmap.put("Email", txt_email);
                    hashmap.put("Contact", txt_contact);
                    hashmap.put("Message", txt_message);
                    db.collection("Employee Information")
                            .document(documentName)
                            .set(hashmap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (contform.getVisibility() == View.VISIBLE) {
                                        contform.setVisibility(View.GONE);
                                    }

                                    Toast.makeText(getBaseContext(), "Details has been sent.", Toast.LENGTH_SHORT).show();

                                    name.setText("");
                                    email.setText("");
                                    contact.setText("");
                                    message.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (contform.getVisibility() == View.VISIBLE) {
                                        contform.setVisibility(View.GONE);
                                    }

                                    Toast.makeText(getBaseContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    name.setText("");
                                    email.setText("");
                                    contact.setText("");
                                    message.setText("");
                                }
                            });
                }
            }
        });

        loadweb = findViewById(R.id.moreInfo);
        loadweb.setClickable(true);
        webView = findViewById(R.id.loadpage);

        loadweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable JavaScript (optional, depending on your requirements)
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                // Make the WebView visible when the button is clicked
                webView.setVisibility(View.VISIBLE);

                // Load a URL (e.g., a website) when the button is clicked
                webView.loadUrl("https://www.nfuonline.com");

                ScrollView contform = findViewById(R.id.contact_form);
                contform.setVisibility(View.GONE);

            }
        });


    }
    @Override
    public void onBackPressed() {

        ScrollView contform = findViewById(R.id.contact_form);
        if (contform.getVisibility() == View.VISIBLE) {
            toggleVisibilityWithTransition((contform));
        } else if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.GONE);
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }

    private void toggleVisibilityWithTransition(ScrollView contform) {
        //ViewGroup parent = findViewById(R.id.rootView);
        //View layout = findViewById(R.id.contact_form);
        Transition transition = new Slide(Gravity.START);
        transition.setDuration(2000);
        transition.addTarget(R.id.contact_form);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        //TransitionManager.beginDelayedTransition(parent, transition);

        if (contform.getVisibility() == View.VISIBLE) {
            contform.setVisibility(View.GONE);
        } else {
            contform.setVisibility(View.VISIBLE);
        }
        RelativeLayout background = findViewById(R.id.background);
        background.setVisibility(View.VISIBLE);
        //background.setGravity(RelativeLayout.START_OF);
    }


    public void openActivity6(){
        Intent intent=new Intent(this, Calculator.class);
        startActivity(intent);
    }

    public void openActivity7(){
        Intent intent=new Intent(this, Disease.class);
        startActivity(intent);
    }

    public void openActivity10(){
        Intent intent = new Intent(this, Wheather.class);
        startActivity(intent);
    }

}