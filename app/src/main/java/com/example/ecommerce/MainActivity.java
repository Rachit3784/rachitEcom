package com.example.ecommerce;

import android.annotation.SuppressLint;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar srchtool;
    SearchView sv;
    RecyclerView rcv;
    ArrayList<UserProfile> crtlist;
    adapter a;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        srchtool = findViewById(R.id.srchtool);
        sv = findViewById(R.id.usearch);
        rcv = findViewById(R.id.userrcv);
        crtlist = new ArrayList<>();
        a = new adapter();

        EditText et = sv.findViewById(androidx.appcompat.R.id.search_src_text);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.GRAY);

        setupSearchView();

    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void setupSearchView() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty() || query == null || query.isBlank()) {
                    searchUser(query);
                    crtlist.clear();
                    updateRecyclerView(query);
                    a.notifyDataSetChanged();
                } else {

                    searchUser(query);
                    a.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty() || newText == null || newText.isBlank()) {
                    searchUser(newText);
                    crtlist.clear();
                    updateRecyclerView(newText);
                    a.notifyDataSetChanged();
                } else {

                    searchUser(newText);
                    a.notifyDataSetChanged();

                }
                return true;
            }
        });


    }

    private void searchUser(String searchTerm) {

        if(searchTerm.isEmpty() || searchTerm == null || searchTerm.isBlank()){
            updateRecyclerView(searchTerm);
            crtlist.clear();
            a.notifyDataSetChanged();
        }
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference dr = fd.getReference("Customer").child("UserDetails");

        if(searchTerm.isEmpty() || searchTerm == null || searchTerm.isBlank()){
            crtlist.clear();
            a.notifyDataSetChanged();
        }else{

            dr.orderByChild("name").startAt(searchTerm).endAt(searchTerm + "\uf8ff")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            crtlist.clear();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                UserProfile p = new UserProfile();
                                p.setName(snap.child("name").getValue(String.class));
                                p.setMurl(snap.child("murl").getValue(String.class));
                                p.setUId(snap.child("uid").getValue(String.class));
                                //Toast.makeText(MainActivity.this, p.getName(), Toast.LENGTH_SHORT).show();
                                crtlist.add(p);
                            }
                            updateRecyclerView(searchTerm);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle potential errors here
                        }
                    });
        }

    }


//    private void loadcontact() {
//
//
//        FirebaseDatabase fd = FirebaseDatabase.getInstance();
//        DatabaseReference dr = fd.getReference("Customer").child("UserDetails");
//
//        dr.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                crtlist.clear();
//                List<DataSnapshot> dst = new ArrayList<>();
//                if (snapshot != null) {
//                    for (DataSnapshot snap : snapshot.getChildren()) {
//                        dst.add(snap);
//
//
//                    }
//                }
//
//                if (dst != null) {
//                    for (int i = 0; i < dst.size(); i++) {
//                        DataSnapshot snap = dst.get(i);
//                        UserProfile p = new UserProfile();
//                        p.setName(snap.child("name").getValue(String.class));
//                        p.setMurl(snap.child("murl").getValue(String.class));
//                        crtlist.add(p);
//
//
//                    }
//                    if (crtlist != null && !crtlist.isEmpty()) {
//                        // Toast.makeText(getContext(), "kuch bhi ho i am not null", Toast.LENGTH_SHORT).show();
//                        a.setUsercontext(MainActivity.this);
//                        a.setUser(crtlist);
//                        a.setItemid(3);
//                        LinearLayoutManager l = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
//                        rcv.setLayoutManager(l);
//                        rcv.setAdapter(a);
//
//                    }
//
//                }
//
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }

    private void updateRecyclerView(String terms) {
        if(terms==null || terms.isBlank()){
            crtlist.clear();
            rcv.setVisibility(View.GONE);
        }else {
            rcv.setVisibility(View.VISIBLE);
        }
        a.setUser(crtlist);
        a.setItemid(3);
        a.setUsercontext(MainActivity.this);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(a);
        a.notifyDataSetChanged();
    }


}