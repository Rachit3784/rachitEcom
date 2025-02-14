package com.example.ecommerce;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    RecyclerView orcv;
    adapter a;
    FirebaseDatabase fd;
    String Myuid;
    LinearLayoutManager llm;
    ArrayList<OrderProductModel> orderlist;
    Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);
       tool =findViewById(R.id.tool);
       orcv = findViewById(R.id.orcv);
       a = new adapter();
       orderlist = new ArrayList<>();

       fd = FirebaseDatabase.getInstance();
       Myuid = FirebaseAuth.getInstance().getUid();
     llm = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        llm.setStackFromEnd(true);
        a.setItemid(4);
        a.setOrdercontext(OrderListActivity.this);
    setSupportActionBar(tool);
    if(getSupportActionBar()!=null){
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
    getSupportActionBar().setDisplayShowHomeEnabled(true);

}

     fetchorderlist();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchorderlist() {
        DatabaseReference dr = fd.getReference("Orders").child(Myuid);

        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OrderProductModel model = snapshot.getValue(OrderProductModel.class);
                orderlist.add(model);
                a.setOrderlist(orderlist);
                orcv.setLayoutManager(llm);
                orcv.setAdapter(a);
                a.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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