package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

public class DemoDesign extends AppCompatActivity {
    TabLayout tab;
    ViewPager vp;
    MainSViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_design);

        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);

        // Retrieve UID from intent extras
        Intent i = getIntent();
        String uid = i.getStringExtra("UID");

        // Initialize the adapter and set UID
        adapter = new MainSViewPagerAdapter(getSupportFragmentManager());
        if (uid != null) {
           // Toast.makeText(getApplicationContext(), "UID ye hai bro\n " + uid, Toast.LENGTH_LONG).show();
            adapter.setUid(uid);

        } else {
            Toast.makeText(getApplicationContext(), "UID null hai bro ", Toast.LENGTH_LONG).show();
        }
   //  Toast.makeText(getApplicationContext(), "UID set ho gai dekho\n " + adapter.getUid(), Toast.LENGTH_LONG).show();

        // Set adapter and attach ViewPager with TabLayout
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
