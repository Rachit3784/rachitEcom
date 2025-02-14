package com.example.ecommerce;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<viewholderr> {
    public interface Updates{
        void loadarraylist();
    }


    public Updates update;


//***********************************  ARRAYLISTS VARIABLE + [ Getter & Setter ] ***********************************//
    private ArrayList<ecomproducts> ecomp;


    public ArrayList<ecomproducts> getEcomp() {
        return ecomp;
    }


    public void setEcomp(ArrayList<ecomproducts> ecomp) {
        this.ecomp = ecomp;

    }


/*****************************************************************/
    private ArrayList<UserProfile> user;

    public ArrayList<UserProfile> getUser() {
        return user;
    }

    public void setUser(ArrayList<UserProfile> user) {
        this.user = user;
    }

/*****************************************************************/

 private ArrayList<ContactUserProfile> contactprofile;

    public ArrayList<ContactUserProfile> getContactprofile() {
        return contactprofile;
    }

    public void setContactprofile(ArrayList<ContactUserProfile> contactprofile) {
        this.contactprofile = contactprofile;
    }


/****************************************************************/
    private ArrayList<CrtDtaFirbsModel> crt;

    public ArrayList<CrtDtaFirbsModel> getCrt() {
        return crt;
    }

    public void setCrt(ArrayList<CrtDtaFirbsModel> crt) {
        this.crt = crt;
    }


/***************************************************************/

    private ArrayList<Catgorymodel> cat;


    public ArrayList<Catgorymodel> getCat() {
        return cat;
    }

    public void setCat(ArrayList<Catgorymodel> cat) {
        this.cat = cat;
    }

/***********************************************************************************************/

private ArrayList<OrderProductModel> orderlist,cancellist;

    public ArrayList<OrderProductModel> getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(ArrayList<OrderProductModel> orderlist) {
        this.orderlist = orderlist;
    }

    //***************----------------**********************************************

    public ArrayList<OrderProductModel> getCancellist() {
        return cancellist;
    }

    public void setCancellist(ArrayList<OrderProductModel> cancellist) {
        this.cancellist = cancellist;
    }


//********************** CONTEXT VARIABLE + [GETTER & SETTER] ********************************//

    private Context c,cont,crtcntxt,searchcontext,usercontext,ordercontext,cancelcontext,contactcontext;

    public Context getOrdercontext() {
        return ordercontext;
    }

    public void setOrdercontext(Context ordercontext) {
        this.ordercontext = ordercontext;
    }

    public Context getUsercontext() {
        return usercontext;
    }

    public void setUsercontext(Context usercontext) {
        this.usercontext = usercontext;
    }

    public Context getSearchcontext() {
        return searchcontext;
    }

    public void setSearchcontext(Context searchcontext) {
        this.searchcontext = searchcontext;
    }

    public Context getC() {
        return cont;
    }

    public void setC(Context cont) {
        this.cont = cont;
    }

    public Context getCrtcntxt() {
        return crtcntxt;
    }

    public void setCrtcntxt(Context crtcntxt) {
        this.crtcntxt = crtcntxt;
    }

    public Updates getUpdate() {
        return update;
    }

    public void setUpdate(Updates update) {
        this.update = update;
    }

    public Context getContactcontext() {
        return contactcontext;
    }

    public void setContactcontext(Context contactcontext) {
        this.contactcontext = contactcontext;
    }

    //********************* item id ***************************////
    private int itemid = 0;
    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }


    public Context getCancelcontext() {
        return cancelcontext;
    }

    public void setCancelcontext(Context cancelcontext) {
        this.cancelcontext = cancelcontext;
    }

    //********************************* COSTRUCTOR *****************************************************//
    public adapter() {

    }

    public adapter(ArrayList<ecomproducts> ecomp, Context c, int itemid) {
        this.ecomp = ecomp;
        this.c = c;
        this.itemid = itemid;

    }

//********************** Methods **************************************************//


//*************************************** ON CREATE METHOD ***************************************//
    @NonNull
    @Override
    public viewholderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater l = LayoutInflater.from(parent.getContext());
        View  s;
if(itemid==1) {
s = l.inflate(R.layout.dishescard, parent, false);

    return new viewholderr(s);
}else if(itemid == 2){
   s = l.inflate(R.layout.cartdatadesign, parent, false);
    return new viewholderr(s);
}
else if(itemid == 3){
    s = l.inflate(R.layout.searchcarddesign, parent, false);
    return new viewholderr(s);   }
else if (itemid == 4) {
    s = l.inflate(R.layout.ordercards,parent,false);
    return new viewholderr(s);
} else if(itemid == 5){
    s =l.inflate(R.layout.cancelcard,parent,false);
    return new viewholderr(s);

} else if (itemid == 6) {
    s = l.inflate(R.layout.chatcontactcards,parent,false);
    return  new viewholderr(s);
} else {
  s= l.inflate(R.layout.categorycard,parent,false);
    return new viewholderr(s);
}

    }


//*************************************** onBINDING ***************************************//


    @Override
    public void onBindViewHolder(@NonNull viewholderr holder, @SuppressLint("RecyclerView") int position) {
if(itemid==1) {

    final ecomproducts temp = ecomp.get(position);
    Glide.with(c)
            .load(ecomp.get(position).getImage()) // URL of the image
            .into(holder.dimg);
    holder.name.setText(ecomp.get(position).getTitle());
    String pricee = String.valueOf(ecomp.get(position).getPrice());
    holder.price.setText(pricee);
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             Intent i = new Intent(c, Productdesc.class);
             i.putExtra("imageurl",ecomp.get(position).getImage());
             i.putExtra("title",temp.getTitle());
             i.putExtra("price",temp.getPrice());
             i.putExtra("category",temp.getCategory());
             i.putExtra("productid",temp.getId());
             i.putExtra("rate",temp.getRating().getRate());
             i.putExtra("count",temp.getRating().getCount());
             c.startActivity(i);

         }
     });

}


else if(itemid == 2){
    final CrtDtaFirbsModel model = crt.get(position);

    Glide.with(crtcntxt).load(crt.get(position).getProductImageUrl()).into(holder.cartimg);
    holder.title.setText(crt.get(position).getNameOfProduct());
    holder.rate.setText(crt.get(position).getPriceOfProduct());
    holder.Remove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences pref = crtcntxt.getSharedPreferences("useriddetail",Context.MODE_PRIVATE);
            String uid = pref.getString("signuserid","nahi hai");
            String id = String.valueOf(model.getId());
            String Address =  "Carts/"+uid+"/"+id;



            FirebaseDatabase fd = FirebaseDatabase.getInstance();
            DatabaseReference dr = fd.getReference("CartData").child(Address);

            dr.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
crt.remove(position);
notifyItemRemoved(position);
notifyItemRangeChanged(position,crt.size());

if(crt.isEmpty() || crt == null){
    update.loadarraylist();
}
  //  Toast.makeText(crtcntxt, "Item removed from cart", Toast.LENGTH_SHORT).show();
                    } else {
   Toast.makeText(crtcntxt, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    });

    holder.Buy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(crtcntxt, OrderAddressActivity.class);
            i.putExtra("CartProductImage",model.getProductImageUrl());
            i.putExtra("CartPrice",model.getPriceOfProduct());
            i.putExtra("CartProductName",model.getNameOfProduct());
            i.putExtra("IDD",model.getId());
            crtcntxt.startActivity(i);
        }
    });

}



else if (itemid == 3) {


UserProfile p = user.get(position);
    holder.contactname.setText(user.get(position).getName());
    Glide.with(usercontext).load(user.get(position).getMurl()).into(holder.contactimg);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(usercontext, OtherProfilePage.class);
            i.putExtra("uid",p.getUId());
            i.putExtra("friendname",p.getName());
            i.putExtra("ProfileUrl",p.getMurl());
            usercontext.startActivity(i);
        }
    });

} else if (itemid == 4) {
      String price = String.valueOf(orderlist.get(position).getPrice());
      OrderProductModel o = orderlist.get(position);
    holder.oprice.setText(price);
    holder.opname.setText(orderlist.get(position).getName());
    Glide.with(ordercontext).load(orderlist.get(position).getImageurl()).into(holder.opimage);
   holder.itemView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(getOrdercontext()!=null){
               Intent i = new Intent(getOrdercontext(),OrderDecription.class);
               i.putExtra("NAME",o.getName());
               i.putExtra("IMAGEURL",o.getImageurl());
               i.putExtra("ADDRESS",o.getAddress());
               i.putExtra("PINCODE",o.getPincode());
               i.putExtra("PRICE",o.getPrice());
               i.putExtra("KEYY",o.getKEY());
               i.putExtra("ID",o.getId());
               i.putExtra("RATING",o.getRate());
               i.putExtra("PEOPLE",o.getPeople());
               getOrdercontext().startActivity(i);
               if(getOrdercontext() instanceof Activity){
                   ((Activity)getOrdercontext()).finish();
               }
           }
          else{
               Toast.makeText(getOrdercontext(), "Context null", Toast.LENGTH_SHORT).show();
           }


       }
   });


} else if (itemid==5) {

    holder.cpname.setText(cancellist.get(position).getName());
    holder.cprice.setText(String.valueOf(cancellist.get(position).getPrice()));
    Glide.with(cancelcontext).load(cancellist.get(position).getImageurl()).into(holder.cpimage);
} else if (itemid == 6) {
    ContactUserProfile c = contactprofile.get(position);

        holder.chatcontactname.setText(contactprofile.get(position).getMyname());
        Glide.with(contactcontext).load(contactprofile.get(position).getMyprofileurl()).into(holder.chatcontactimg);



    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(contactcontext, ChatPage.class);
            i.putExtra("FriendUserID",c.getMyUID());
            i.putExtra("FriendUserName",c.getMyname());
            i.putExtra("FriendProfileURL",c.getMyprofileurl());
            contactcontext.startActivity(i);
        }
    });
} else{

    final Catgorymodel ct = cat.get(position);
    holder.catimg.setImageResource(cat.get(position).getCatimage());
    holder.ctgry.setText(cat.get(position).getCatnamebro());
    holder.categorylayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(cont, Ctgory.class);
            i.putExtra("realadd",ct.getAddress());
            cont.startActivity(i);
        }
    });
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(cont, Ctgory.class);
            i.putExtra("realadd",ct.getAddress());
            cont.startActivity(i);
        }
    });

}

}

//*************************************** ITEM COUNT METHOD ***************************************//
    @Override
    public int getItemCount() {
if(itemid == 1){

    return ecomp.size();
}

else if (itemid == 2) {

    return crt.size();

}
else if (itemid == 3) {

    return user.size();

} else if (itemid == 4) {

    return  orderlist.size();

} else if (itemid==5) {
    return  cancellist.size();
} else if (itemid == 6) {
   return  contactprofile.size();
} else {

    return cat.size();
}

    }


}


