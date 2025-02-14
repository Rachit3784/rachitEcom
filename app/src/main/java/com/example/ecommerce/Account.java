package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {
Toolbar tool;
FirebaseAuth mAuth;
FirebaseDatabase db;
DatabaseReference dr;
ShapeableImageView aimg;
TextView admin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        aimg = findViewById(R.id.aimg);
        admin = findViewById(R.id.admin);
        tool = findViewById(R.id.tool);

        setSupportActionBar(tool);

        mAuth = FirebaseAuth.getInstance();


        loadProfileAndUser();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadProfileAndUser() {
        String userid = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        dr = db.getReference("Customer/").child("UserDetails/"+userid);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot!=null){
           String Proifleurl   = snapshot.child("murl").getValue(String.class);
           if(Proifleurl!=null){
               Glide.with(getApplicationContext()).load(Proifleurl).into(aimg);
           }
                    else {
               Glide.with(getApplicationContext()).load(R.drawable.progile).into(aimg);
           }
                    String username = snapshot.child("name").getValue(String.class);
                    admin.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.edit){
       Intent i = new Intent(Account.this,EditProfile.class);
       startActivity(i);

            return true;
        }
        else if(itemid == R.id.post){

            return true;
        } else if (itemid == R.id.logout) {
            logoutuser();
            return true;
        } else if(itemid == R.id.account){
            return true;
        }
       else if(itemid == R.id.exit){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutuser() {

        mAuth.signOut();
        if(mAuth.getCurrentUser()==null){
            Intent i = new Intent(Account.this, LogIn.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
              finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_option_menu,menu);
        return true;
    }
}
