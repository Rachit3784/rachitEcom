package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {

    ShapeableImageView fimag, send;
    TextView fname;
    EditText msgg;
    FirebaseDatabase fd;
    LinearLayoutManager llm;
    String SendBase,FetchBase,time = null;
    RecyclerView chatrcv;
    String CFriendUID,CFriendImageURL,CFname;
    ArrayList<MessageModel> msgmodellist;
    String fuserid, fimg, fnam;
    String myuid;
    ChatAdapter chatAdapter ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_page);

        // Initialize UI elements
        fimag = findViewById(R.id.fimag);
        fname = findViewById(R.id.fname);
        msgg = findViewById(R.id.msg);
        send = findViewById(R.id.send);
        chatrcv = findViewById(R.id.chatrcv);

        msgmodellist = new ArrayList<>();

       chatAdapter = new ChatAdapter();
       chatAdapter.setChatcontext(this);
    llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
  llm.setStackFromEnd(true);

        fd = FirebaseDatabase.getInstance();

        LocalTime lt = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          lt = LocalTime.now();

        }
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           dtf = DateTimeFormatter.ofPattern("hh:mma");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = lt.format(dtf);
        }

        // Get sender info from Firebase and intent data
        myuid = FirebaseAuth.getInstance().getUid();
        Intent i = getIntent();
        fuserid = i.getStringExtra("puid");
        fnam = i.getStringExtra("fname");
        fimg = i.getStringExtra("fprofile");







         CFriendUID = i.getStringExtra("FriendUserID");
        CFname = i .getStringExtra("FriendUserName");
        CFriendImageURL = i.getStringExtra("FriendProfileURL");

        if(fnam!=null){
            fname.setText(fnam);
        }

        if (fimg != null) {
            Glide.with(this).load(fimg).into(fimag);
        } else {
            Glide.with(this).load(R.drawable.progile).into(fimag);
        }


        if(CFname!=null ){
            fname.setText(CFname);
            fname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ChatPage.this, OtherProfilePage.class);
                    startActivity(i);
                }
            });
        }
        if( CFriendImageURL!=null){
            Glide.with(this).load(CFriendImageURL).into(fimag);
        }



//        DatabaseReference dbr = fd.getReference("Customer").child("UserDetails/"+myuid);
//        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String myimageurl = snapshot.child("murl").getValue(String.class);
//                chatAdapter.setMyimageurl(myimageurl);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

      if(fuserid!=null){
          SendBase = fuserid + myuid;
          FetchBase = myuid + fuserid;
      } else if (CFriendUID!=null) {
          SendBase = CFriendUID + myuid;
          FetchBase = myuid + CFriendUID;
      }



        // Send message on button click
        send.setOnClickListener(v -> {
            String messageText = msgg.getText().toString();
            if (messageText.isEmpty()) {
                Toast.makeText(ChatPage.this, "Enter something...", Toast.LENGTH_SHORT).show();
            } else {
               SendMessage(messageText);
            }
        });

        fetchChats();





    }





    private void SendMessage(String messageText) {

        MessageModel msgmodel = new MessageModel(messageText,myuid,time);
        DatabaseReference sendbase = fd.getReference("Chats").child(SendBase)
                .child("MESSAGE").push();
        DatabaseReference fetchbase = fd.getReference("Chats").child(FetchBase)
                .child("MESSAGE").push();
        sendbase.setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                fetchbase.setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                          msgg.setText("");
                    }
                });

            }
        });
    }


    private void fetchChats(){


        DatabaseReference fetch = fd.getReference("Chats").child(FetchBase).child("MESSAGE");

      fetch.limitToLast(30).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MessageModel msgmodel = snapshot.getValue(MessageModel.class);
                msgmodellist.add(msgmodel);
               // Toast.makeText(ChatPage.this, msgmodel.getMessage(), Toast.LENGTH_SHORT).show();
                chatAdapter.setMsglist(msgmodellist);
                chatrcv.setLayoutManager(llm);
                chatrcv.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();
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

}
