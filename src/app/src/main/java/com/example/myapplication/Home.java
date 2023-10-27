package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {
    private Button button4;
    private Button button5;
    private Button button6;
    private Button buttoncul;
    private Button buttonsol;
    private Button buttonwhe;
    TextView name;

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = findViewById(R.id.nav);

        SharedPreferences sp = getSharedPreferences("entry", MODE_PRIVATE);
        String userName = sp.getString("username", "");
        name.setText(userName);

        button4=findViewById(R.id.signout);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });

        button5=findViewById(R.id.calculator);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity6();
            }
        });

        button6=findViewById(R.id.disease);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openActivity7(); }
        });

        buttoncul=findViewById(R.id.cultivation);
        buttoncul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity8();
            }
        });

        buttonsol=findViewById(R.id.solution);
        buttonsol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openActivity9(); }
        });

        buttonwhe = findViewById(R.id.whether);
        buttonwhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openActivity10(); }
        });

    }

    public void openActivity5(){
        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("flag", false);
        editor.apply();
        Toast.makeText(getApplicationContext(),"Sign Out Successfully",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivity6(){
        Intent intent=new Intent(this, Calculator.class);
        startActivity(intent);
    }

    public void openActivity7(){
        Intent intent=new Intent(this, Disease.class);
        startActivity(intent);
    }

    public void openActivity8(){
        Intent intent=new Intent(this, Cultivation.class);
        startActivity(intent);
    }

    public void openActivity9(){
        Intent intent=new Intent(this, Solution.class);
        startActivity(intent);
    }

    public void openActivity10(){
        Intent intent = new Intent(this, Wheather.class);
        startActivity(intent);
    }
}