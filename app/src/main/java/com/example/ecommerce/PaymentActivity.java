package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {
Toolbar tool;
TextView Pname,Prize;
FirebaseDatabase fd;
FirebaseAuth mAuth;
    String ImageUrl,Namee,Address,Pin,cn,ci,pn,pi;
    int id,people,CrtID,CAARTID;
    float price,cp,pp;
    double rate;
String Myuid;
RelativeLayout cash,GooglPay,PhonePay,Paytm,netbank;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        tool = findViewById(R.id.tool);
        cash = findViewById(R.id.cash);
        GooglPay = findViewById(R.id.GooglePay);
        PhonePay = findViewById(R.id.PhonePay);
        Paytm = findViewById(R.id.Paytm);
        netbank = findViewById(R.id.netbank);
        Pname = findViewById(R.id.Pname);
        Prize = findViewById(R.id.Prize);
        mAuth = FirebaseAuth.getInstance();
        fd = FirebaseDatabase.getInstance();
        Myuid = mAuth.getUid();
      Intent i = getIntent();
      ImageUrl =   i.getStringExtra("ImageURl");
      Namee =  i.getStringExtra("Name");
      id = i.getIntExtra("ID",0);
      price =  i.getFloatExtra("Pricee",0);
      Address = i.getStringExtra("addrsss");
      Pin = i.getStringExtra("pinnn");
       people = i.getIntExtra("peoples",0);
       rate = i.getDoubleExtra("rating",0);




       cn = i.getStringExtra("CN");
       ci = i.getStringExtra("CI");
       pn = i.getStringExtra("PN");
       pi = i.getStringExtra("PI");
       cp = i.getFloatExtra("CP",0);
       pp = i.getFloatExtra("PP",0);
       CrtID = i.getIntExtra("IDDD",0);
       CAARTID = i.getIntExtra("CAARTIDDD",0);
      if(Namee!=null){
          Pname.setText(Namee);
          Prize.setText(String.valueOf(price));
      } else if (cn!=null) {
          Pname.setText(cn);
          Prize.setText(String.valueOf(cp));
      } else if (pn!=null) {
          Pname.setText(pn);
          Prize.setText(String.valueOf(pp));
      }


        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           StoreToDataBase();

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void StoreToDataBase() {

        DatabaseReference dr = fd.getReference("Orders").child(Myuid).push();
        DatabaseReference dd;
        if(String.valueOf(CrtID)!=null){
          dd = fd.getReference("CartData").child("Carts/"+Myuid).child(String.valueOf(CrtID));
        }
        else{
            dd = fd.getReference("CartData").child("Carts/"+Myuid).child(String.valueOf(CAARTID));
        }

        String Key = dr.getKey();
        Toast.makeText(this, Key, Toast.LENGTH_SHORT).show();
        OrderProductModel model;
        if(ImageUrl!=null){
          model = new OrderProductModel(ImageUrl,Namee,id,price,Address,Pin,Key,people,rate);
        } else if (pn!=null) {
            model = new OrderProductModel(pi,pn,id,pp,Address,Pin,Key,people,rate);
        }
            else {
            model = new OrderProductModel(ci,cn,id,cp,Address,Pin,Key,people,rate);
        }
        dr.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(PaymentActivity.this,OrderAddressActivity.class);
           //     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ProductImage",ImageUrl);
                i.putExtra("ProductName",Namee);
                i.putExtra("ProductPrice",price);
                i.putExtra("PII",pi);
                i.putExtra("PNN",pn);
                i.putExtra("PPP",pp);
                i.putExtra("CII",ci);
                i.putExtra("CNN",cn);
                i.putExtra("CPP",cp);
                i.putExtra("CRTIDDD",CrtID);
                Toast.makeText(PaymentActivity.this, "Order Successful....", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }
        });
         dd.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {

             }
         });
    }

}