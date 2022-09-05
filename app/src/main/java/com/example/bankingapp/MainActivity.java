package com.example.bankingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnLogIn, btnRegister;
    private EditText tvIban, tvPin;
    private TextView txtWarn;
    private DBUtils dbHelper;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            hideWarnings();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client searched = null;
                try{
                    searched = dbHelper.searchClient(Integer.parseInt(tvIban.getText().toString()));
                    if(dbHelper.searchClient(Integer.parseInt(tvIban.getText().toString()))!=null){
                        if(searched.getPin() == Integer.parseInt(tvPin.getText().toString())){
                            hideWarnings();
                            Intent mainWindow = new Intent(MainActivity.this, MainPageActivity.class);
                            mainWindow.putExtra("iban", searched.getIban());
                            startActivity(mainWindow);
                        }else {
                            txtWarn.setText("Incorect pin!");
                            txtWarn.setTextColor(Color.RED);
                        }
                    }else{
                        txtWarn.setText("User does not exist!");
                        txtWarn.setTextColor(Color.RED);
                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Input Error try again", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideWarnings();
                Intent registerWindow = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerWindow);
            }
        });
    }

    private void hideWarnings() {
        txtWarn.setText("To create an account");
        txtWarn.setTextColor(Color.parseColor("#03A9F4"));
    }

    private void showWarnings() {
        txtWarn.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
    }

    private void initComponents(){
        btnLogIn = findViewById(R.id.btnRegisterUser);
        btnRegister = findViewById(R.id.btnRegister);
        tvIban = findViewById(R.id.tvIbanR);
        tvPin = findViewById(R.id.tvPinR);
        txtWarn = findViewById(R.id.txtWarnRegister);
        dbHelper = new DBUtils(MainActivity.this);
    }
}