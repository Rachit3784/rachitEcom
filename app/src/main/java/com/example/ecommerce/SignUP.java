package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUP extends AppCompatActivity {

    EditText email, pass;
    FirebaseAuth mAuth;
    ProgressBar prgrss;
    TextView fpts,Alogin;



    Button signup, verify;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.getUid()!=null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent i = new Intent(SignUP.this,DemoDesign.class);

            i.putExtra("UID",user.getUid());
            startActivity(i);
                   finish();

        } else {

            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_sign_up);

            SharedPreferences s = getSharedPreferences("useriddetail",MODE_PRIVATE);
            SharedPreferences.Editor e = s.edit();
            e.putString("Address","");
            e.putString("Pin","");
            e.apply();

            Alogin = findViewById(R.id.Alogin);
          fpts = findViewById(R.id.fpts);
            email = findViewById(R.id.email);
            pass = findViewById(R.id.pass);
            signup = findViewById(R.id.signup);
            verify = findViewById(R.id.verify);
            prgrss = findViewById(R.id.prgrss);
            mAuth = FirebaseAuth.getInstance();

            //-----------------signupbutton--------------------------------------------
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the current input from the EditText fields
                    String Email = email.getText().toString().trim();
                    String Pass = pass.getText().toString().trim();

                    // Check if the fields are not empty
                    if (Email.isEmpty() || Pass.isEmpty()) {
                        Toast.makeText(SignUP.this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        // Call the signuppp method with the current email and password
                        signuppp(Email, Pass);

                        // Handle verification after 30 seconds
                        Handler h = new Handler();
                        Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (user.isEmailVerified()) {
                                                    verify.setVisibility(View.VISIBLE);
                                                } else {
                                                    deleteuser();
                                                    Toast.makeText(SignUP.this, "Enter details again\nYour request has been restricted", Toast.LENGTH_LONG).show();
                                                    signup.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        };
                        h.postDelayed(run, 30000);
                    }
                }
            });

            Alogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(SignUP.this, LogIn.class);
                    startActivity(in);
                    finish();
                }
            });


         

            //------------------------------------------

//fpts.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Intent i = new Intent(SignUP.this,Pf.class);
//        startActivity(i);
//    }
//});



            //----------------verifybutton---------------------
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifying();
                }
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {  // Added null check
            user.reload().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful() && !user.isEmailVerified()) {
                        deleteuser();
                    }
                }
            });
        }
    }

    private void signuppp(String Email, String Pass) {
        mAuth.createUserWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                sendlink(user);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection time Out", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //------------------------sendlink(user)-----------------
    public void sendlink(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Email Verification link sent\nVerify under 30 seconds", Toast.LENGTH_LONG).show();
                            signup.setVisibility(View.GONE);
                            prgrss.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "First fill the details", Toast.LENGTH_LONG).show();
        }
    }

    //------------------------------------------------verification--------------------------------------------
    public void verifying() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (user.isEmailVerified()) {
                        String uidofuser = user.getUid();
                        String uemail =user.getEmail();
                        SharedPreferences userpref = getSharedPreferences("useriddetail",MODE_PRIVATE);
                            SharedPreferences.Editor editor = userpref.edit();
                            editor.putString("signuserid",uidofuser);
                            editor.putString("useremail",uemail);
                            editor.apply();
                        Intent i = new Intent(SignUP.this, Pf.class);
                        i.putExtra("UID",user.getUid());
                        startActivity(i);
                        finish();
                    } else {
                        deleteuser();
                    }
                }
            });
        }
    }

//------------------------------deleteuser();----------------------------------------------------------------
    public void deleteuser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.isEmailVerified()) {  // Added null check
            user.delete().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }
}
