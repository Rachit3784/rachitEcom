package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OtherProfilePage extends AppCompatActivity {


    Button msg;
    ShapeableImageView aimg;
    TextView admin;
    String username,profileurl,uidd;
    String Myusername,Myprofileurl,Myuid;
    String fusernam,fprofileur,fui;
    FirebaseDatabase fd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_other_profile_page);
           fd = FirebaseDatabase.getInstance();
        msg = findViewById(R.id.message);
        aimg = findViewById(R.id.aimg);
        admin = findViewById(R.id.admin);



        loadProfileanduser(getIntent());

        Myuid = FirebaseAuth.getInstance().getUid();
        Intent i = getIntent();
        fui = i.getStringExtra("uid");
        fusernam = i.getStringExtra("friendname");
       fprofileur =  i.getStringExtra("ProfileUrl");
msg.setVisibility(View.GONE);

           msg.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(getApplicationContext(),ChatPage.class);
                   i.putExtra("puid",uidd);
                   i.putExtra("fname",username);
                   UpdateContact();
                   startActivity(i);
                   finish();
               }
           });




        loadMydetails();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadMydetails() {

        {
            Toast.makeText(this, "loadmydetailkeandar", Toast.LENGTH_SHORT).show();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dr = db.getReference("Customer/").child("UserDetails/"+Myuid);
            if(Myuid!=null){

                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("name").exists()){
                         Myusername = snapshot.child("name").getValue(String.class);
                          //  Toast.makeText(OtherProfilePage.this, Myusername, Toast.LENGTH_SHORT).show();
                            msg.setVisibility(View.VISIBLE);
                        }
                        if(snapshot.child("murl").exists()){
                            // Toast.makeText(getContext(), "photo bhi dali hai bhai", Toast.LENGTH_SHORT).show();
                            Myprofileurl = snapshot.child("murl").getValue(String.class);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }


    private void UpdateContact() {
        ContactUserProfile c  = new ContactUserProfile(fui,fusernam,fprofileur, Myuid,Myusername,Myprofileurl);
        DatabaseReference dr = fd.getReference("Contacts").child(fui).child(Myuid);

        DatabaseReference d = fd.getReference("Contacts").child(Myuid).child(Myuid);

        dr.setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ContactUserProfile a  = new ContactUserProfile(fui,Myusername,Myprofileurl, Myuid,fusernam,fprofileur);
             //  Toast.makeText(OtherProfilePage.this, "added", Toast.LENGTH_SHORT).show();
d.setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(OtherProfilePage.this, "added", Toast.LENGTH_SHORT).show();
    }
});

            }
        });

    }

    private void loadProfileanduser(Intent i) {
      uidd = i.getStringExtra("uid");
       if(uidd!=null){

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference dr = fd.getReference("Customer").child("UserDetails/"+uidd);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("name").getValue(String.class);
                admin.setText(username);
               if(snapshot.child("murl").exists()){
                   profileurl = snapshot.child("murl").getValue(String.class);
                   Glide.with(getApplicationContext()).load(profileurl).into(aimg);

               }else {
                   Glide.with(getApplicationContext()).load(R.drawable.progile).into(aimg);
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }
}