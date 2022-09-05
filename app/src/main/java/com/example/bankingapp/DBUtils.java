package com.example.bankingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBUtils extends SQLiteOpenHelper {

    public static final String clients_table = "clients_table";
    public static final String col_Iban = "iban";
    public static final String col_Pin = "pin";
    public static final String col_balance = "balance";


    public DBUtils(@Nullable Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "create table " + clients_table +
                "(" + col_Iban + " integer primary key, " +
                col_Pin + " integer," +
                col_balance + " double)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_Iban, client.getIban());
        cv.put(col_Pin, client.getPin());
        cv.put(col_balance, client.getBalance());

        long success = db.insert(clients_table, null, cv);
        db.close();
        return success != -1;
    }

    public Client searchClient(int iban) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + clients_table + " where iban = " + iban, null);
        Client toRet = null;
        if (cursor.moveToFirst()) {
            toRet = new Client(iban, cursor.getInt(1), cursor.getInt(2));
        }

        cursor.close();
        db.close();

        return toRet;
    }

    public boolean addFounds(Client client, double sum) {
        SQLiteDatabase db = this.getWritableDatabase();
        client.setBalance(client.getBalance() + sum);
        ContentValues cv = new ContentValues();
        cv.put(col_Iban, client.getIban());
        cv.put(col_Pin, client.getPin());
        cv.put(col_balance, client.getBalance());

        long success = db.update(clients_table, cv, "iban=?", new String[]{String.valueOf(client.getIban())});

        return success != -1;
    }

    public boolean withdrawFounds(Client client, double sum){
        if(client.getBalance() - sum < 0){
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        client.setBalance(client.getBalance() - sum);
        ContentValues cv = new ContentValues();
        cv.put(col_Iban, client.getIban());
        cv.put(col_Pin, client.getPin());
        cv.put(col_balance, client.getBalance());

        long success = db.update(clients_table, cv, "iban=?", new String[]{String.valueOf(client.getIban())});

        return success != -1;
    }
}
