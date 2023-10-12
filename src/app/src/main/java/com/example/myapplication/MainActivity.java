package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private EditText user;
    private EditText password;
    private long pressedTime;
    MyDBHelper myDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        user = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        myDBHelper = new MyDBHelper(this);

        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
        Boolean check = pref.getBoolean("flag", false);
        if(check){ //user logged in
            Intent iNext;
            iNext = new Intent(MainActivity.this, Home.class);
            startActivity(iNext);
        }

        button1 = findViewById(R.id.footer);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        button2 = findViewById(R.id.login);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { performLogin(); }
        });
    }



    public void performLogin() {
        String userStr = user.getText().toString();
        String passStr = password.getText().toString();
        Log.d("uday", "performLogin()");
        if (userStr.isEmpty() || passStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
        } else {
            if (myDBHelper.checkInfo(userStr,passStr)) {
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                openActivity3();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Credentials !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openActivity2(){
        Intent intent=new Intent(this, Register.class);
        startActivity(intent);
    }
    public void openActivity3(){
        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("flag", true);
        editor.apply();



        Intent intent=new Intent(MainActivity.this, Home.class);
        startActivity(intent);
    }

    //onBackPressed function used to back the activity
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}