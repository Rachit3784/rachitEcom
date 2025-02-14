package com.example.ecommerce;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;


public class viewholderr extends RecyclerView.ViewHolder {
    ImageView dimg,catimg,cartimg,opimage,cpimage;
    ShapeableImageView contactimg,chatcontactimg;

    TextView name, price,ctgry,title,rate,contactname,opname,oprice,cpname,cprice,chatcontactname;
     Button Remove,Buy;
RelativeLayout ecompcard,categorylayout;



    public viewholderr(@NonNull View itemView) {
        super(itemView);
        dimg = itemView.findViewById(R.id.dimg);
        name = itemView.findViewById(R.id.name);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        rate = itemView.findViewById(R.id.rate);
        catimg = itemView.findViewById(R.id.catimg);
        cartimg = itemView.findViewById(R.id.cartimg);
        ctgry = itemView.findViewById(R.id.ctgry);
        Remove = itemView.findViewById(R.id.Remove);
        Buy = itemView.findViewById(R.id.Buy);
        categorylayout = itemView.findViewById(R.id.categorylayout);
        ecompcard = itemView.findViewById(R.id.ecompcard);
        contactname = itemView.findViewById(R.id.contactname);
        contactimg = itemView.findViewById(R.id.contactimg);
        opimage = itemView.findViewById(R.id.opimag);
        opname = itemView.findViewById(R.id.opname);
        oprice = itemView.findViewById(R.id.opprice);
        cpimage = itemView.findViewById(R.id.cpimag);
        cpname = itemView.findViewById(R.id.cpname);
        cprice = itemView.findViewById(R.id.cpprice);
        chatcontactimg = itemView.findViewById(R.id.chatcontactimg);
        chatcontactname = itemView.findViewById(R.id.chatcontactname);
    }
}
