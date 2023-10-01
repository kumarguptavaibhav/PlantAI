package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityCalculatorBinding;

public class Calculator extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private Button btnurea;
    private Button btndap;
    private Button btnmop;
    private TextView txtresult;



    private AppBarConfiguration appBarConfiguration;
    private ActivityCalculatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        et1=(EditText) findViewById(R.id.nitrogen);
        et2=(EditText) findViewById(R.id.phosphorus);
        et3=(EditText) findViewById(R.id.pottas);
        btnurea=(Button) findViewById(R.id.urea);
        btndap=(Button) findViewById(R.id.dap);
        btnmop=(Button) findViewById(R.id.mop);
        txtresult=(TextView) findViewById(R.id.result);

        btnurea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((et1.getText().length()>0)){
                    double opr1=Double.parseDouble(et1.getText().toString());
                    double result= (opr1*100)/46;
                    txtresult.setText(Double.toString(result));
                }
            }
        });

        btndap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((et2.getText().length()>0)){
                    double opr2=Double.parseDouble(et2.getText().toString());
                    double result= (opr2*100)/16;
                    txtresult.setText(Double.toString(result));
                }
            }
        });

        btnmop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((et3.getText().length()>0)){
                    double opr3=Double.parseDouble(et3.getText().toString());
                    double result= (opr3*100)/60;
                    txtresult.setText(Double.toString(result));
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}