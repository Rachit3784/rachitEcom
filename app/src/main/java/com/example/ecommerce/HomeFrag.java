package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFrag extends Fragment {

    private static final String TAG = "HomeFrag";
    RecyclerView rcv, rcvt;
    String uid;
    adapter a;
    ArrayList<ecomproducts> ecomp;
   ShapeableImageView chatb,refresh,spi;
    SearchView sv;
    LottieAnimationView refreshanim;
    ImageView elctn,wmn,jwell,mnw;
    TextView usertxt;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View i = inflater.inflate(R.layout.fragment_home, container, false);

        usertxt = i.findViewById(R.id.usertxt);
        spi = i.findViewById(R.id.spi);
        elctn = i.findViewById(R.id.elct);
        jwell = i.findViewById(R.id.jwell);
        wmn = i.findViewById(R.id.wmn);
        mnw = i.findViewById(R.id.mnw);
        chatb = i.findViewById(R.id.chatb);
        refresh = i.findViewById(R.id.refresher);
       refreshanim = i.findViewById(R.id.refreshanim);
       usertxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getContext(), Account.class);
               startActivity(i);
           }
       });
       spi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getContext(), Account.class);
               startActivity(i);
           }

       });
        chatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),MainActivity.class);
                startActivity(i);
            }
        });
elctn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent i = new Intent(getContext(),Ctgory.class);
i.putExtra("addrss","products/category/electronics");
startActivity(i);
    }
});

        jwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Ctgory.class);
                i.putExtra("addrss","products/category/jewelery");
                startActivity(i);
            }
        });

        wmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Ctgory.class);
                i.putExtra("addrss","products/category/women's clothing");
                startActivity(i);
            }
        });

        mnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Ctgory.class);
                i.putExtra("addrss","products/category/men's clothing");
                startActivity(i);
            }
        });

















      if(getArguments()!=null){
           uid = getArguments().getString("UID");
          if(uid!=null){
            //  Toast.makeText(i.getContext(),"Uid null nahi hai\n"+uid,Toast.LENGTH_LONG).show();
              loaddata(uid);
          }
      }
//--------------------------------------searchview---------------------------------------------------




        sv = i.findViewById(R.id.sv);
        sv.clearFocus();

        EditText searchEditText = sv.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.GRAY);
        searchEditText.setFocusableInTouchMode(false);
          searchEditText.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(getContext(),MainActivity.class);
                  startActivity(i);
              }
          });

  sv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent i = new Intent(getContext(),MainActivity.class);
      startActivity(i);
      }
  });








//---------------------------------------------------------------------------------------------------
        ecomp = new ArrayList<>();

        // Set up adapter and RecyclerView
        a = new adapter(ecomp, i.getContext(),1);
        rcvt = i.findViewById(R.id.rcvt);
        rcvt.setAdapter(a);
        LinearLayoutManager llm = new LinearLayoutManager(i.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvt.setLayoutManager(llm);
        rcv = i.findViewById(R.id.rcv);
        rcv.setAdapter(a);
        GridLayoutManager gm = new GridLayoutManager(i.getContext(), 2);
        rcv.setLayoutManager(gm);

        // Fetch data from API
        getData();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setVisibility(View.GONE);
                rcv.setVisibility(View.GONE);
                rcvt.setVisibility(View.GONE);
                refreshanim.setVisibility(View.VISIBLE);
                getData();
                if(uid!=null){
                    loaddata(uid);
                }
                Handler h = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        refresh.setVisibility(View.VISIBLE);
                        refreshanim.setVisibility(View.GONE);
                        rcv.setVisibility(View.VISIBLE);
                        rcvt.setVisibility(View.VISIBLE);
                    }
                };
                h.postDelayed(r,1000);
            }
        });
        return i;
    }

    private void loaddata(String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dr = db.getReference("Customer/").child("UserDetails/"+uid);
        if(uid!=null){

            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("name").exists()){
                        String username = snapshot.child("name").getValue(String.class);
                        usertxt.setText(username);
                    }
                    if(snapshot.child("murl").exists()){
                       // Toast.makeText(getContext(), "photo bhi dali hai bhai", Toast.LENGTH_SHORT).show();
                        String purl = snapshot.child("murl").getValue(String.class);
                        Glide.with(getContext())
                                .load(purl)
                                .into(spi);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void getData() {
        Call<List<ecomproducts>> c = RetrofitClient.getInstance().getMyApi().get("title,image,price,category,id,rating");
        c.enqueue(new Callback<List<ecomproducts>>() {
            @Override
            public void onResponse(Call<List<ecomproducts>> call, Response<List<ecomproducts>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ecomproducts> l = response.body();
                    if (l != null && !l.isEmpty()) {
                        ecomp.addAll(l);
                        a.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No ecomproducts found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve ecomproducts", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ecomproducts>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
        });
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.magentaaa));
        }
    }
}
