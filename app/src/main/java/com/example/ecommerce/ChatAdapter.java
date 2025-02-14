package com.example.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


  private ArrayList<MessageModel> msglist;
  String a = FirebaseAuth.getInstance().getUid();
  Context chatcontext;

  String myimageurl,friendimageurl;

    public String getMyimageurl() {
        return myimageurl;
    }

    public void setMyimageurl(String myimageurl) {
        this.myimageurl = myimageurl;
    }

    public String getFriendimageurl() {
        return friendimageurl;
    }

    public void setFriendimageurl(String friendimageurl) {
        this.friendimageurl = friendimageurl;
    }

    int RECIEVER_ID = 1,SENDER_ID = 2;;
    public ArrayList<MessageModel> getMsglist() {
        return msglist;
    }

    public void setMsglist(ArrayList<MessageModel> msglist) {
        this.msglist = msglist;
    }

    public Context getChatcontext() {
        return chatcontext;
    }

    public void setChatcontext(Context chatcontext) {
        this.chatcontext = chatcontext;
    }

    @Override
    public int getItemViewType(int position) {
        if(msglist.get(position).getSenderid().equals(a)){

            return SENDER_ID;
        }
       else{


            return RECIEVER_ID;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater l = LayoutInflater.from(parent.getContext());
        View v;
        if(viewType == RECIEVER_ID){
            v = l.inflate(R.layout.recievercard,parent,false);
            return new reciever(v);
        }
        else {
            v = l.inflate(R.layout.sendercard,parent,false);
            return new sender(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       if(holder.getClass() == sender.class){

           ((sender) holder).senderimag.setImageResource(R.drawable.me);
           ((sender) holder).sendedmsg.setText(msglist.get(position).getMessage());
       }
       else if(holder.getClass() == reciever.class){
           ((reciever) holder).recieverimag.setImageResource(R.drawable.other);
           ((reciever) holder).recievedmsg.setText(msglist.get(position).getMessage());
       }
    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    class sender extends RecyclerView.ViewHolder{
TextView sendedmsg;
ShapeableImageView senderimag;
        public sender(@NonNull View itemView) {
            super(itemView);

            sendedmsg = itemView.findViewById(R.id.sendedmsg);
            senderimag = itemView.findViewById(R.id.senderimag);

        }
    }

    class reciever extends RecyclerView.ViewHolder{
TextView recievedmsg;
ShapeableImageView recieverimag;
        public reciever(@NonNull View itemView) {
            super(itemView);
            recievedmsg = itemView.findViewById(R.id.recievedmsg);
            recieverimag = itemView.findViewById(R.id.recieverimag);

        }
    }
}
