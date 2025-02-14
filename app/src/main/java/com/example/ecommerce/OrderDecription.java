package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDecription extends AppCompatActivity {


    Toolbar tool;
    ImageView prdcimg;
    TextView name,price,pcount,norate,address,pin;
    Button cancel;
    RatingBar ratingBar;
    FirebaseDatabase fd;
    int Id,People;
    double Rate;
  float Price;
    String Name,Address,Pincode,Imageurl,Key,myuid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_decription);

         tool = findViewById(R.id.desctool);
         name  = findViewById(R.id.descname);
         price  = findViewById(R.id.descprice);
         pcount   = findViewById(R.id.descpcount);
         norate = findViewById(R.id.descnorate);
         address  = findViewById(R.id.descaddress);
         pin  = findViewById(R.id.descpin);
         cancel  = findViewById(R.id.cancel);
         ratingBar = findViewById(R.id.descratingbar);
         prdcimg = findViewById(R.id.descimg);

         Intent i = getIntent();
         Name = i.getStringExtra("NAME");
        Address  = i.getStringExtra("ADDRESS");
        Pincode = i.getStringExtra("PINCODE");
        Price  = i.getFloatExtra("PRICE",0);
        Imageurl  = i.getStringExtra("IMAGEURL");
       Key = i.getStringExtra("KEYY");
       Id = i.getIntExtra("ID",0);
       Rate = i.getDoubleExtra("RATING",0);
       People = i.getIntExtra("PEOPLE",0);
       ratingBar.setRating((float) Rate);
       pcount.setText(String.valueOf(People));
       norate.setText(String.valueOf(Rate));
        fd = FirebaseDatabase.getInstance();
           myuid = FirebaseAuth.getInstance().getUid();
        Glide.with(this).load(Imageurl).into(prdcimg);
        price.setText(String.valueOf(Price));
        address.setText(Address);
        name.setText(Name);
        pin.setText(Pincode);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFromOrder();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void DeleteFromOrder() {
        DatabaseReference DR = fd.getReference("Cancelled").child(myuid).push();
        DatabaseReference dr = fd.getReference("Orders").child(myuid).child(Key);
        OrderProductModel o = new OrderProductModel(Imageurl,Name,Id,Price,Address,Pincode,Key,People,Rate);
        DR.setValue(o).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dr.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(OrderDecription.this, OrderListActivity.class);
                       // Toast.makeText(OrderDecription.this, Key, Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                });
            }
        });



    }
}
