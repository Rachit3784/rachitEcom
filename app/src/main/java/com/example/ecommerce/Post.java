package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post extends AppCompatActivity {


    EditText id,title;
    Button post;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        id = findViewById(R.id.id);
        title = findViewById(R.id.name);
        post = findViewById(R.id.post);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no = Integer.valueOf(id.getText().toString());
                String name = title.getText().toString();

                if(name!=null){
                    postdata(no,name);
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void postdata(int id,String title) {
        Call<ecomproducts> c = RetrofitClient.getInstance().getMyApi().post(id,title);
        c.enqueue(new Callback<ecomproducts>() {
            @Override
            public void onResponse(Call<ecomproducts> call, Response<ecomproducts> response) {

            }

            @Override
            public void onFailure(Call<ecomproducts> call, Throwable throwable) {

            }
        });
    }
}