package com.example.bankingapp;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private Context context;

    public ContactsAdapter(Context context){
        this.context = context;
    }

    public ContactsAdapter(){

    }

    public void setContacts(ArrayList<Contact> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtContactName.setText(contacts.get(position).getName());
        holder.txtContactDetails.setText("Iban:"+contacts.get(position).getClientInfo().getIban());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button toPress = ((Activity)context).findViewById(R.id.btnTransfer);
                MainPageActivity.autoFillContactIban = String.valueOf(contacts.get(holder.getAdapterPosition()).getClientInfo().getIban());
                toPress.performClick();
            }
        });
        holder.contactImage.setImageResource(R.drawable.ic_person);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtContactName, txtContactDetails;
        private CardView parent;
        private ImageView contactImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContactName = itemView.findViewById(R.id.txtContactName);
            txtContactDetails = itemView.findViewById(R.id.txtContactDetails);
            parent = itemView.findViewById(R.id.parentCardContact);
            contactImage = itemView.findViewById(R.id.contactImage);
        }
    }
}
