package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Productdesc extends AppCompatActivity {

    Toolbar tool;
    Button atc,bp;
    SearchView search;
    RecyclerView rcvtw,rcv;
    adapter a;
    adapter b;
    ArrayList<ecomproducts> frameone;
    ArrayList<Catgorymodel> cat;
    TextView name,pric,norate,pcount;
    ImageView prdcimg;
    String address;
    RatingBar ratingBar;

 // Assuming you have an ArrayList for ecomproducts

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productdesc);

        tool = findViewById(R.id.tool);
        search = findViewById(R.id.searchbar);
        rcvtw = findViewById(R.id.rcvt);
        rcv = findViewById(R.id.rcv);
        atc = findViewById(R.id.atc);
        bp = findViewById(R.id.bp);
        ratingBar = findViewById(R.id.ratingbar);
        name = findViewById(R.id.name);
        pric = findViewById(R.id.price);
        prdcimg  = findViewById(R.id.prdcimg);
        norate = findViewById(R.id.norate);
        pcount = findViewById(R.id.pcount);
        cat = new ArrayList<>();
        cat = getdata();
         a = new adapter();
         a.setCat(cat);
         a.setC(Productdesc.this);
        setSupportActionBar(tool);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        Intent i = getIntent();
        int id = i.getIntExtra("productid",0);
       String url =  i.getStringExtra("imageurl");
       String title =  i.getStringExtra("title");
       float price = i.getFloatExtra("price",0);
       double rate = i.getDoubleExtra("rate",0.0);
       int c = i.getIntExtra("count",0);
       String Count = String.valueOf(c);
       String Rate = String.valueOf(rate);
       String pricee = String.valueOf(price);
       String category = i.getStringExtra("category");
       if(url!=null){
          Glide.with(this).load(url).into(prdcimg);
         //  Toast.makeText(this, title, Toast.LENGTH_LONG).show();
           name.setText(title);
           pric.setText(pricee);
          address = "products/category/"+category;
          // Toast.makeText(this,address,Toast.LENGTH_LONG).show();
       ratingBar.setRating((float) rate);
       pcount.setText(Count);
       norate.setText(Rate);
       }
     else{
           Toast.makeText(this, "sab null hai", Toast.LENGTH_LONG).show();
       }




     // ********* Buy Product ***********  //


        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Productdesc.this, OrderAddressActivity.class);
              i.putExtra("ID",id);
                i.putExtra("IURL",url);
                i.putExtra("PNAME",title);
                i.putExtra("PRICE",price);
                  i.putExtra("RETING",rate);
                  i.putExtra("NOOFPEOPLE",c);

                startActivity(i);
            }
        });



// ********* Add to Cart ***********  //
     atc.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
//             String rateeee = String.valueOf(rate);
//             Toast.makeText(Productdesc.this, rateeee, Toast.LENGTH_SHORT).show();
             CrtDtaFirbsModel crtmdl = new CrtDtaFirbsModel();
             crtmdl.setProductImageUrl(url);
             crtmdl.setNameOfProduct(title);
             crtmdl.setPriceOfProduct(pricee);
             crtmdl.setId(id);
             addtocart(crtmdl);

         }
     });









        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rcvtw.setLayoutManager(llm);
        rcvtw.setAdapter(a);
        search.clearFocus();


        frameone = new ArrayList<>();
        getframeone(address);
        b=new adapter(frameone,Productdesc.this,1);

        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rcv.setLayoutManager(l);
        rcv.setAdapter(b);

        EditText searchEditText = search.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.BLACK);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addtocart(CrtDtaFirbsModel crtmdl) {
        SharedPreferences userpref = getSharedPreferences("useriddetail",MODE_PRIVATE);
        String user = userpref.getString("signuserid","hello it is null");
        String uniqueid = String.valueOf(crtmdl.getId());
         String AddressofCart = "Carts/"+user+"/"+uniqueid;
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference dr = fd.getReference("CartData").child(AddressofCart);
        dr.setValue(crtmdl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(Productdesc.this, "Data Saved to Cart", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Productdesc.this, "Failed ho gaya yarr", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getframeone(String address) {

        Log.d("getframeone", "Fetching data from address: " + address);

        Call<List<ecomproducts>> c = RetrofitClient.getInstance().getMyApi()
                .getcat(address, "title,image,price,category");
        c.enqueue(new Callback<List<ecomproducts>>() {
            @Override
            public void onResponse(Call<List<ecomproducts>> call, Response<List<ecomproducts>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<ecomproducts> l = response.body();
                    Log.d("getframeone", "Response received: " + l);

                    if (l != null && !l.isEmpty()) {

                        frameone.addAll(l);
                        b.notifyDataSetChanged();
                        if(frameone!=null&&!frameone.isEmpty()){
                            //Toast.makeText(Productdesc.this, frameone.get(1).getTitle()+ "\n  "+frameone.get(1).getCategory(), Toast.LENGTH_SHORT).show();

                        }

                    }
                } else {
                    Toast.makeText(Productdesc.this, "Response null hai", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<ecomproducts>> call, Throwable throwable) {
                Toast.makeText(Productdesc.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("getframeone", "Request failed with error: " + throwable.getMessage(), throwable);
            }
        });
    }


    private ArrayList<Catgorymodel> getdata() {
        ArrayList<Catgorymodel> c = new ArrayList<>();
        String ad = "products/category/";
        Catgorymodel cat1 = new Catgorymodel();
        cat1.setCatimage(R.drawable.jwellary);
        cat1.setCatnamebro("Jwellary");
        cat1.setAddress(ad+"jewelery");
        c.add(cat1);
        Catgorymodel cat2 = new Catgorymodel();
        cat2.setCatimage(R.drawable.homeappliances);
        cat2.setCatnamebro("Electronics");
        cat2.setAddress(ad+"electronics");
        c.add(cat2);

        Catgorymodel cat3 = new Catgorymodel();
        cat3.setCatimage(R.drawable.menswear);
        cat3.setCatnamebro("Mens Wear");
        cat3.setAddress(ad+"men's clothing");
        c.add(cat3);

        Catgorymodel cat4 = new Catgorymodel();
        cat4.setCatimage(R.drawable.suit);
        cat4.setCatnamebro("Womens Wear");
        cat4.setAddress(ad+"women's clothing");
        c.add(cat4);

        return c;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
