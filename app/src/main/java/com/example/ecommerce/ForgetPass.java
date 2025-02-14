package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
EditText email;
Button reset;
TextView btl;
ProgressBar prgrss;
FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);


          email = findViewById(R.id.email);
          reset = findViewById(R.id.reset);
          prgrss = findViewById(R.id.prgrss);
          btl = findViewById(R.id.btl);
          mAuth = FirebaseAuth.getInstance();
          btl.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent i = new Intent(ForgetPass.this,LogIn.class);
                  startActivity(i);
                  finish();
              }
          });
           reset.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   reset.setVisibility(View.GONE);
                   prgrss.setVisibility(View.VISIBLE);
                   String Email = email.getText().toString();
                   if (Email.isEmpty()){
                       reset.setVisibility(View.VISIBLE);
                       prgrss.setVisibility(View.GONE);
                       Toast.makeText(ForgetPass.this,"Enter email first",Toast.LENGTH_LONG).show();
                   }
                   else{
                       sendlink(Email);
                   }
               }
           });







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendlink(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"reset link sent to you email",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ForgetPass.this,LogIn.class);
                            reset.setVisibility(View.VISIBLE);
                            prgrss.setVisibility(View.GONE);
                            startActivity(i);
                            finish();
                        }else{
                            reset.setVisibility(View.VISIBLE);
                            prgrss.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Process error",Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }
}