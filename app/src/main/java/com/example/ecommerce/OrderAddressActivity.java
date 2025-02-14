package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class OrderAddressActivity extends AppCompatActivity {


    ImageView prdcimag;
    TextView name,prize;
    EditText addrss,pin;
    Button edit,next;
    Toolbar adtool;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_address);

        prdcimag = findViewById(R.id.prdcimag);
        name = findViewById(R.id.name);
       prize = findViewById(R.id.prize);
       addrss = findViewById(R.id.addrss);
       pin = findViewById(R.id.pin);
        edit = findViewById(R.id.edit);
        next = findViewById(R.id.next);
        adtool =findViewById(R.id.Adrsstool);

        setSupportActionBar(adtool);

        Intent i =getIntent();

        String ImageUrl = i.getStringExtra("IURL");
        String Name = i.getStringExtra("PNAME");
        float price = i.getFloatExtra("PRICE",0);
        int idd = i.getIntExtra("ID",0);
       double ratee = i.getDoubleExtra("RETING",0);
       int peoplee = i.getIntExtra("NOOFPEOPLE",0);



       String ProductImage = i.getStringExtra("ProductImage");
       String ProductName = i.getStringExtra("ProductName");
       float ProductPrice = i.getFloatExtra("ProductPrice",100);





        String CartImage = i.getStringExtra("CartProductImage");
        String CartName = i.getStringExtra("CartProductName");
        String CartPrice = i.getStringExtra("CartPrice");
        int CartID = i.getIntExtra("IDD",0);



        String CI = i.getStringExtra("CII");
        String CN = i.getStringExtra("CNN");
        float CP = i.getFloatExtra("CPP",0);
        int crtiddd = i.getIntExtra("CRTIDDD",0);



        String PI = i.getStringExtra("PII");
        String PN = i.getStringExtra("PNN");
        float PP = i.getFloatExtra("PPP",0);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if(ImageUrl!=null){
            Glide.with(this).load(ImageUrl).into(prdcimag);
            name.setText(Name);
            prize.setText(String.valueOf(price));
        }else if(ProductImage!=null){
            Glide.with(this).load(ProductImage).into(prdcimag);
            name.setText(ProductName);
            prize.setText(String.valueOf(ProductPrice));
        } else if (CartImage!=null) {
            Glide.with(this).load(CartImage).into(prdcimag);
            name.setText(CartName);
            prize.setText(String.valueOf(CartPrice));
        } else if (CI!=null) {
            Glide.with(this).load(CI).into(prdcimag);
            name.setText(CN);
            prize.setText(String.valueOf(CP));
        } else if (PI!=null) {
            Glide.with(this).load(PI).into(prdcimag);
            name.setText(PN);
            prize.setText(String.valueOf(PP));
        }


        SharedPreferences s = getSharedPreferences("useriddetail",MODE_PRIVATE);
        SharedPreferences.Editor e = s.edit();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrss.setText("");
                pin.setText("");
            }
        });

       String PreAddress =  s.getString("Address","No");
       String piin = s.getString("Pin","00");

        if(PreAddress == "No"){
            Toast.makeText(this, "Adress null", Toast.LENGTH_SHORT).show();
            addrss.setText("");
            pin.setText("");

        } else if (PreAddress!="No") {
            addrss.setText(PreAddress);
            pin.setText(piin);

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Addresses = addrss.getText().toString();
                String PIN  = pin.getText().toString();
                if(Addresses.isEmpty()|| PIN.isEmpty()){

                    Toast.makeText(OrderAddressActivity.this, "Enter details first...", Toast.LENGTH_SHORT).show();
                } else if (Addresses.isBlank()|| PIN.isBlank()) {
                    Toast.makeText(OrderAddressActivity.this, "Enter details first...", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(OrderAddressActivity.this,PaymentActivity.class);
                    i.putExtra("ImageURl",ImageUrl);
                    i.putExtra("Name",Name);
                    i.putExtra("ID",idd);
                    i.putExtra("Pricee",price);
                    i.putExtra("addrsss",Addresses);
                    i.putExtra("pinnn",PIN);
                    i.putExtra("rating",ratee);
                    i.putExtra("peoples",peoplee);


                    i.putExtra("PN",ProductName);
                    i.putExtra("PI",ProductImage);
                    i.putExtra("PP",ProductPrice);


                    i.putExtra("CN",CartName);
                    i.putExtra("CI",CartImage);
                    float crtprice = -42;
                    if(CartPrice!= null){
                     crtprice   = Float.parseFloat(CartPrice);
                    }
                    i.putExtra("CP",crtprice);
                    i.putExtra("IDDD",CartID);
                    i.putExtra("CAARTIDDD",crtiddd);





                    e.putString("Address",Addresses);
                    e.putString("Pin",PIN);
                    e.apply();
                    startActivity(i);
                    finish();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
