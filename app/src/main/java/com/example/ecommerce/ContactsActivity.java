package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    Toolbar cntool;
    RecyclerView conrcv;
    ArrayList<ContactUserProfile> user;
    adapter a;
    ShapeableImageView refresher;
    LinearLayoutManager llm;
    LottieAnimationView loaderanim;
    FirebaseDatabase fd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts);

        cntool = findViewById(R.id.cntool);
        conrcv = findViewById(R.id.conrcv);
        user = new ArrayList<>();
        a = new adapter();


        refresher = findViewById(R.id.refresherr);
        loaderanim = findViewById(R.id.loaderanimm);


        refresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.clear();
                fetchContacts();
                loaderanim.setVisibility(View.VISIBLE);
                conrcv.setVisibility(View.GONE);
                Handler h = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        loaderanim.setVisibility(View.GONE);
                        conrcv.setVisibility(View.VISIBLE);
                    }
                };
                h.postDelayed(r,5000);
            }
        });



        fd  = FirebaseDatabase.getInstance();
        setSupportActionBar(cntool);


       llm = new LinearLayoutManager(ContactsActivity.this,LinearLayoutManager.VERTICAL,false);
        a.setContactcontext(ContactsActivity.this);
        a.setItemid(6);

        if(getSupportActionBar()!=null)

        {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

        }


        fetchContacts();

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchContacts() {

        DatabaseReference dr = fd.getReference("Contacts").child(FirebaseAuth.getInstance().getUid());
                   dr.addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                           ContactUserProfile u = snapshot.getValue(ContactUserProfile.class);
                         //  Toast.makeText(ContactsActivity.this, u.getMyname(), Toast.LENGTH_SHORT).show();
                               user.add(u);

                           a.setContactprofile(user);
                           conrcv.setLayoutManager(llm);
                           conrcv.setAdapter(a);
                           a.notifyDataSetChanged();
                       }

                       @Override
                       public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                       }

                       @Override
                       public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                       }

                       @Override
                       public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}