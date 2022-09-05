package com.example.bankingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    private TextView tvBalance;
    private Button btnDeposit, btnWithdraw, btnTransfer, btnLogout;
    private FloatingActionButton btnAddContact;
    private RecyclerView contactRecv;
    private int currentIban;
    private Client currentClient;
    private DBUtils dbHelper;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ContactsAdapter adapter;
    public static String autoFillContactIban = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        initComponents();
        
        tvBalance.setText(String.valueOf(currentClient.getBalance()));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDepositDialog();
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWithdrawDialog();
            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTransferDialog();
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContactDialog();
            }
        });
    }

    public static void autoFillTransfer(String contactToSend){

    }

    public void createContactDialog(){
        dialogBuilder = new AlertDialog.Builder(MainPageActivity.this);
        final View addPop = getLayoutInflater().inflate(R.layout.popup_add_contact,null);
        Button btnSubmitContact = (Button) addPop.findViewById(R.id.btnSubmitContact);
        EditText ed_contactIban = (EditText) addPop.findViewById(R.id.ed_contactIban);
        EditText ed_contactName = (EditText) addPop.findViewById(R.id.ed_contactName);
        dialogBuilder.setView(addPop);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnSubmitContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("e","starting creating contact");
                if(!ed_contactIban.getText().toString().equals("")
                    && !ed_contactName.getText().toString().equals("")){
                    Client contact = null;
                    contact = dbHelper.searchClient(Integer.parseInt(ed_contactIban.getText().toString()));
                    if(contact!=null){
                        if(!checkContactAlreadyExist(contact, ed_contactName.getText().toString())){
                            addContactToRecv(contact, ed_contactName.getText().toString());
                            dialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Successfully added contact", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainPageActivity.this, "User already in contact list", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainPageActivity.this, "Iban doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainPageActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkContactAlreadyExist(Client contact, String name){
        for(Contact c : contacts){
            if(c.getClientInfo().getIban() == contact.getIban() || c.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void addContactToRecv(Client contact, String name){
        contacts.add(new Contact(contact, name));
        adapter = new ContactsAdapter(this);
        adapter.setContacts(contacts);
        contactRecv.setAdapter(adapter);
        contactRecv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    }

    public void createTransferDialog(){
        dialogBuilder = new AlertDialog.Builder(MainPageActivity.this);
        final View transferPopUp = getLayoutInflater().inflate(R.layout.popup_transfer,null);
        Button btnTransfer = (Button) transferPopUp.findViewById(R.id.btnSubmitTransfer);
        EditText ed_sumTransfer = (EditText) transferPopUp.findViewById(R.id.ed_TransferSum);
        EditText ed_ibanTransfer = (EditText) transferPopUp.findViewById(R.id.ed_ibanTransfer);
        if(autoFillContactIban!=null){
            ed_ibanTransfer.setText(autoFillContactIban);
            autoFillContactIban = null;
        }
        dialogBuilder.setView(transferPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("e","starting transfer");
                if(!ed_sumTransfer.getText().toString().equals("") && Double.parseDouble(ed_sumTransfer.getText().toString())!=0
                    && !ed_ibanTransfer.getText().toString().equals("")
                    && !ed_ibanTransfer.getText().toString().equals(String.valueOf(currentClient.getIban()))){
                    Client destination = null;
                    destination = dbHelper.searchClient(Integer.parseInt(ed_ibanTransfer.getText().toString()));
                    if(destination!=null){//daca exista destinatia
                        boolean res1 = dbHelper.withdrawFounds(currentClient, Double.parseDouble(ed_sumTransfer.getText().toString()));
                        boolean res2 = dbHelper.addFounds(destination, Double.parseDouble(ed_sumTransfer.getText().toString()));
                        if(res1 && res2){
                            dialog.dismiss();
                            Toast.makeText(MainPageActivity.this, "Successful transfer", Toast.LENGTH_SHORT).show();
                            tvBalance.setText(String.valueOf(currentClient.getBalance()));
                        }else{
                            Toast.makeText(MainPageActivity.this, "Insufficient funds", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainPageActivity.this, "Iban doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainPageActivity.this, "Sum/iban field error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createWithdrawDialog(){
        dialogBuilder = new AlertDialog.Builder(MainPageActivity.this);
        final View depositPopUp = getLayoutInflater().inflate(R.layout.popup_withdraw,null);
        Button btnWithdraw = (Button) depositPopUp.findViewById(R.id.btnSubmitWithdraw);
        EditText ed_sumWithdraw = (EditText) depositPopUp.findViewById(R.id.ed_withdrawSum);
        dialogBuilder.setView(depositPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("e","starting deposit");
                if(!ed_sumWithdraw.getText().toString().equals("") && Double.parseDouble(ed_sumWithdraw.getText().toString())!=0){
                    boolean res = dbHelper.withdrawFounds(currentClient, Double.parseDouble(ed_sumWithdraw.getText().toString()));
                    if(res){
                        dialog.dismiss();
                        Toast.makeText(MainPageActivity.this, "Successful withdraw", Toast.LENGTH_SHORT).show();
                        tvBalance.setText(String.valueOf(currentClient.getBalance()));
                    }else{
                        Toast.makeText(MainPageActivity.this, "Error withdrawing founds", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainPageActivity.this, "Sum field empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createDepositDialog(){
        dialogBuilder = new AlertDialog.Builder(MainPageActivity.this);
        final View depositPopUp = getLayoutInflater().inflate(R.layout.popup_deposit,null);
        Button btnDeposit = (Button) depositPopUp.findViewById(R.id.btnSubmitDeposit);
        EditText ed_sumDeposit = (EditText) depositPopUp.findViewById(R.id.ed_inputSum);
        dialogBuilder.setView(depositPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("e","starting deposit");
                if(!ed_sumDeposit.getText().toString().equals("") && Double.parseDouble(ed_sumDeposit.getText().toString())!=0){
                    boolean res = dbHelper.addFounds(currentClient, Double.parseDouble(ed_sumDeposit.getText().toString()));
                    if(res){
                        dialog.dismiss();
                        Toast.makeText(MainPageActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        tvBalance.setText(String.valueOf(currentClient.getBalance()));
                    }else{
                        Toast.makeText(MainPageActivity.this, "Error adding founds", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainPageActivity.this, "Sum field empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initComponents(){
        tvBalance = findViewById(R.id.actualBalance);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddContact = findViewById(R.id.btnAddContact);
        contactRecv = findViewById(R.id.contactsRecv);
        dbHelper = new DBUtils(MainPageActivity.this);
        currentIban = getIntent().getIntExtra("iban", 0);
        if(currentIban!=0){
            currentClient = dbHelper.searchClient(currentIban);
        }else{
            finish();
        }
    }
}