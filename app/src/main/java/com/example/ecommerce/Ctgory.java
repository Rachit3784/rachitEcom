package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ctgory extends AppCompatActivity {
Toolbar tool;
RecyclerView rcvo;
adapter a;
ArrayList<ecomproducts> d;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ctgory);
        tool = findViewById(R.id.tool);
     setSupportActionBar(tool);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        d = new ArrayList<>();
            Intent i = getIntent();
          String elect =  i.getStringExtra("addrss");
          String rad = i.getStringExtra("realadd");
          if(rad!=null){
              //Toast.makeText(getApplicationContext(),"bhai no tension rad null nahi hai",Toast.LENGTH_LONG).show();
              loaddata(rad);
          }
           if(elect!=null){
              // Toast.makeText(getApplicationContext(),"bhai no tension elect null nahi hai",Toast.LENGTH_LONG).show();
              loaddata(elect);
           }else {
               //Toast.makeText(getApplicationContext(),"bhaiya elect null hai ",Toast.LENGTH_LONG).show();
           }
           a = new adapter(d,Ctgory.this,1);
            rcvo = findViewById(R.id.rcvo);
           rcvo.setAdapter(a);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                    rcvo.setLayoutManager(llm);








        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loaddata(String address) {
      //  Toast.makeText(getApplicationContext(),"load ke andar aaa gae bhai",Toast.LENGTH_LONG).show();
        Call<List<ecomproducts>> call =  RetrofitClient.getInstance().getMyApi().getcat(address,"title,image,price,id");
        call.enqueue(new Callback<List<ecomproducts>>() {

            @Override
            public void onResponse(Call<List<ecomproducts>> call, Response<List<ecomproducts>> response) {
               // Toast.makeText(getApplicationContext(),"onResponseke andar aaa gae bhai",Toast.LENGTH_LONG).show();
                if (response.isSuccessful() && response.body() != null) {
                    List<ecomproducts> l = response.body();
                    if (l != null && !l.isEmpty()) {
                        d.addAll(l);
                        a.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No saman found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve saman", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ecomproducts>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}