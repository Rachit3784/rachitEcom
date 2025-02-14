package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Cart extends Fragment implements adapter.Updates {

RecyclerView crcv;
ShapeableImageView refresh,delete;
adapter a;
Toolbar tool;


boolean emptycart = false;
    public Cart() {

    }
LottieAnimationView cartanim,loaderanim,alldeleteanim;
ArrayList<CrtDtaFirbsModel> crtlist;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadcart();

View v = inflater.inflate(R.layout.fragment_cart, container, false);

        loaderanim = v.findViewById(R.id.loaderanim);
        cartanim = v.findViewById(R.id.cartanim);
        delete = v.findViewById(R.id.delete);
        alldeleteanim = v.findViewById(R.id.alldelete);
       // cartanim.setVisibility(View.VISIBLE);
crcv = v.findViewById(R.id.crcv);
refresh = v.findViewById(R.id.refresh);
        cartanim.setVisibility(View.VISIBLE);

crcv.setItemAnimator(new DefaultItemAnimator());
refresh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        refresh.setVisibility(View.GONE);
     crcv.setVisibility(View.GONE);
        cartanim.setVisibility(View.GONE);
       loadcart();
        loaderanim.setVisibility(View.VISIBLE);

        Handler h = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                loaderanim.setVisibility(View.GONE);
                refresh.setVisibility(View.VISIBLE);
               crcv.setVisibility(View.VISIBLE);

                if(crtlist.isEmpty()){
                   cartanim.setVisibility(View.VISIBLE);
                }
            }
        };
        h.postDelayed(run,1500);
    }
});
tool = v.findViewById(R.id.tool);
crtlist = new ArrayList<>();
a = new adapter();
a.setUpdate(this);
        if(crtlist.isEmpty()){
            cartanim.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!crtlist.isEmpty()){
                    delete.setVisibility(View.GONE);
                    refresh.setVisibility(View.GONE);
                    emptycart();
                }

            }
        });
        return v;
    }

    private void emptycart() {
        SharedPreferences userref = getActivity().getSharedPreferences("useriddetail",Context.MODE_PRIVATE);
        String userid = userref.getString("signuserid","No user found");
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        String address = "Carts/"+userid;
        DatabaseReference dr = fd.getReference("CartData").child(address);
            dr.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   crtlist.clear();
                   a.notifyDataSetChanged();
                    if(crtlist.isEmpty()){
                        alldeleteanim.setVisibility(View.VISIBLE);
                        Handler h = new Handler();
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                alldeleteanim.setVisibility(View.GONE);
                                delete.setVisibility(View.VISIBLE);
                                refresh.setVisibility(View.VISIBLE);
                                 cartanim.setVisibility(View.VISIBLE);
                            }
                        };
                        h.postDelayed(r,2000);
                    }
                }
            });
    }


    public void loadcart() {


        SharedPreferences userref = getActivity().getSharedPreferences("useriddetail",Context.MODE_PRIVATE);
        String userid = userref.getString("signuserid","No user found");
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
            String address = "Carts/"+userid;
        DatabaseReference dr = fd.getReference("CartData").child(address);

        dr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(crtlist.isEmpty()){
//                    cartanim.setVisibility(View.VISIBLE);
//                   // Toast.makeText(getContext(), "DataSnapshot ke andar", Toast.LENGTH_SHORT).show();
//                }

                     crtlist.clear();
                List<DataSnapshot> dst = new ArrayList<>();
                if(snapshot!=null){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        dst.add(snap);


                    }}

                if(dst!=null) {
              for (int i = 0; i < dst.size(); i++) {
                  DataSnapshot snap = dst.get(i);
                  CrtDtaFirbsModel model = new CrtDtaFirbsModel();
                  model.setPriceOfProduct(snap.child("priceOfProduct").getValue(String.class));
                  model.setNameOfProduct(snap.child("nameOfProduct").getValue(String.class));
                  model.setProductImageUrl(snap.child("productImageUrl").getValue(String.class));
                  model.setId(snap.child("id").getValue(int.class));
                  crtlist.add(model);


                  //    Toast.makeText(getContext(), model.getPriceOfProduct()+"\n"+model.getNameOfProduct()+"\n\n"+model.getProductImageUrl(), Toast.LENGTH_SHORT).show();

              }
              if (crtlist != null && !crtlist.isEmpty()) {
                 // Toast.makeText(getContext(), "kuch bhi ho i am not null", Toast.LENGTH_SHORT).show();
                  a.setCrtcntxt(getContext());
                  a.setCrt(crtlist);

                  a.setItemid(2);
                  LinearLayoutManager l = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                  crcv.setLayoutManager(l);
                  crcv.setAdapter(a);
                  cartanim.setVisibility(View.GONE);

              }

          }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadcart();
        a.notifyDataSetChanged();
           if(crtlist.isEmpty()){
               cartanim.setVisibility(View.VISIBLE);
           }


    }

    @Override
    public void loadarraylist() {
        loadcart();
if(crtlist.isEmpty()){
    cartanim.setVisibility(View.VISIBLE);
}
    }
}
