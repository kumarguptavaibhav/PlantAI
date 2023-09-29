package com.example.myapplication;

import static android.icu.lang.UProperty.NAME;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.WindowDecorActionBar;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityRegisterBinding;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private Button button3;
    private EditText name, email, contact_no, pass;

    

    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterBinding binding;
    MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = findViewById(R.id.NAME);
        email = findViewById(R.id.EMAIL);
        contact_no = findViewById(R.id.CONTACT);
        pass = findViewById(R.id.PASSWORD);

        myDBHelper = new MyDBHelper(this);

        button3=findViewById(R.id.button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });

    }
    public void openActivity4(){
        String Name=name.getText().toString();
        String Email=email.getText().toString();
        String Contact_no=contact_no.getText().toString();
        String Password=pass.getText().toString();

        if(Name.isEmpty() || Email.isEmpty() || Contact_no.isEmpty() || Password.isEmpty()){
            Toast.makeText(Register.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
        } else {
            myDBHelper.addInfo(Name, Email, Contact_no, Password);
            Toast.makeText(Register.this, "Details has been submitted.", Toast.LENGTH_SHORT).show();
            name.setText("");
            email.setText("");
            contact_no.setText("");
            pass.setText("");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}