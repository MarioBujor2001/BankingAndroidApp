package com.example.bankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText tvIban, tvPin, tvPinRepeat;
    private TextView txtWarning;
    private DBUtils dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    Client toAdd = new Client(Integer.parseInt(tvIban.getText().toString()),Integer.parseInt(tvPin.getText().toString()),0);
                    boolean succ = dbHelper.addOne(toAdd);
                    if(succ) {
                        Snackbar.make(v, "User created succesfuly!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent mainPage = new Intent(v.getContext(), MainActivity.class);
                                        setResult(RESULT_OK, mainPage);
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("Error creating account!");
                    }
                }else{
                    txtWarning.setText("Input error! Please check info");
                    txtWarning.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean checkInput(){
        if(tvIban.getText().toString().equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Empty IBan");
            return false;
        }
        if(tvPin.getText().toString().equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Empty Pin");
            return false;
        }
        if(!tvPin.getText().toString().equals(tvPinRepeat.getText().toString())){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Pins don't match");
            return false;
        }
        if(tvPin.getText().toString().length()!=4){
            return false;
        }
        return true;
    }

    private void initComponents(){
        btnRegister = findViewById(R.id.btnRegisterUser);
        tvIban = findViewById(R.id.tvIbanR);
        tvPin = findViewById(R.id.tvPinR);
        tvPinRepeat = findViewById(R.id.tvPinRRepeat);
        txtWarning = findViewById(R.id.txtWarnRegisterInput);
        dbHelper = new DBUtils(RegisterActivity.this);
    }
}