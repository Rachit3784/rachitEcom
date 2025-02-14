package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class OrderHistory extends Fragment {



    public OrderHistory() {

    }

    RelativeLayout order,deliver,cancel,chats;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


View v = inflater.inflate(R.layout.fragment_order_history, container, false);


       order = v.findViewById(R.id.order);
       cancel = v.findViewById(R.id.cancel);
       deliver = v.findViewById(R.id.deliver);
      chats = v.findViewById(R.id.chats);



      order.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(getContext(), OrderListActivity.class);
              startActivity(i);
          }
      });

    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), CancelledListActivity.class);
            startActivity(i);
        }
    });

chats.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getContext(), ContactsActivity.class);
        startActivity(i);
    }
});

        return v;
    }
}