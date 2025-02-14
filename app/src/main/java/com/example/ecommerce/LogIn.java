package com.example.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    TextView Fpass, Asignup;
    Button login;
    EditText email, pass;
    ProgressBar prgrss;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        Asignup = findViewById(R.id.Asignup);
        Fpass = findViewById(R.id.Fpass);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        prgrss = findViewById(R.id.prgrss);
        mAuth = FirebaseAuth.getInstance();

        // Navigate to the SignUp activity
        Asignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogIn.this, SignUP.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Properly clears the activity stack
                startActivity(i);
                finish();
            }
        });

        // Navigate to the Forgot Password activity
        Fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogIn.this, ForgetPass.class);
                startActivity(i);
            }
        });

        // Handle the login button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Pass = pass.getText().toString();

                // Disable buttons and show progress bar
                Fpass.setAlpha(0.5f);
                Fpass.setClickable(false);
                Asignup.setAlpha(0.5f);
                Asignup.setClickable(false);
                login.setVisibility(View.GONE);
                prgrss.setVisibility(View.VISIBLE);

                // Check if fields are not empty
                if (Email.isEmpty() || Pass.isEmpty()) {
                    login.setVisibility(View.VISIBLE);
                    prgrss.setVisibility(View.GONE);
                    Fpass.setAlpha(1f);
                    Fpass.setClickable(true);
                    Asignup.setAlpha(1f);
                    Asignup.setClickable(true);
                    Toast.makeText(LogIn.this, "Email or Password must be filled", Toast.LENGTH_LONG).show();
                } else {
                    loginn(Email, Pass);
                }
            }
        });

        // Set window insets (for modern layout adjustments)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginn(String Email, String Pass) {
        mAuth.signInWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Login successful, go to ProfileImage activity and clear back stack
                                Intent i = new Intent(LogIn.this,Pf.class);
                                String uidofuser = user.getUid();
                                SharedPreferences userpref = getSharedPreferences("useriddetail",MODE_PRIVATE);
                                SharedPreferences.Editor editor = userpref.edit();
                                editor.putString("signuserid",uidofuser);
                                editor.apply();
                                login.setVisibility(View.VISIBLE);
                                prgrss.setVisibility(View.GONE);
                                i.putExtra("UIDDD",user.getUid());
                                Fpass.setAlpha(1f);
                                Fpass.setClickable(true);
                                Asignup.setAlpha(1f);
                                Asignup.setClickable(true);
                                startActivity(i);
                                finish();

                            }
                        } else {
                            // Login failed, show error message
                            Toast.makeText(LogIn.this, "Login failed. Please check your credentials and try again.", Toast.LENGTH_LONG).show();

                            // Re-enable buttons and hide progress bar
                            Fpass.setAlpha(1f);
                            Fpass.setClickable(true);
                            Asignup.setAlpha(1f);
                            Asignup.setClickable(true);
                            login.setVisibility(View.VISIBLE);
                            prgrss.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
