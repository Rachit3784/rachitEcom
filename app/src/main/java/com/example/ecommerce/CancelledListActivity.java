package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CancelledListActivity extends AppCompatActivity {


    Toolbar ctool;
    RecyclerView crcv;
    FirebaseDatabase fd;
    ArrayList<OrderProductModel> cancellist;
    String Myuid;
    adapter a;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancelled_list);

        Myuid = FirebaseAuth.getInstance().getUid();
        ctool = findViewById(R.id.ctool);
        crcv = findViewById(R.id.crcv);
        setSupportActionBar(ctool);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        a = new adapter();
        cancellist = new ArrayList<>();
        a.setCancelcontext(CancelledListActivity.this);
        a.setItemid(5);
        fd = FirebaseDatabase.getInstance();

        loadCancelList();

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadCancelList() {

        DatabaseReference dr = fd.getReference("Cancelled").child(Myuid);
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                OrderProductModel o = snapshot.getValue(OrderProductModel.class);
                    cancellist.add(o);
                    a.setCancellist(cancellist);
                    crcv.setLayoutManager(new LinearLayoutManager(CancelledListActivity.this));
                    crcv.setAdapter(a);
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