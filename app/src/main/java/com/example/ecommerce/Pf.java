package com.example.ecommerce;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;


import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Pf extends AppCompatActivity {
    Button let;
    FloatingActionButton addd;
    ShapeableImageView simg;
    FirebaseStorage Storage ;
    Bitmap uploadbit;
    Uri resultURI ;
    EditText username;
    String photourlbrother;
TextView skip;
    ProgressDialog d;

    public static int pause = 5;
    private static final int CAMERA_REQUEST_CODE = 101; // Request code for camera
    private static final int GALLERY_REQUEST_CODE = 102; // Request code for gallery
    private static final int PERMISSION_REQUEST_CODE = 100; // Request code for permissions

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pf);
username = findViewById(R.id.usern);
        let = findViewById(R.id.let);
        addd = findViewById(R.id.addd);
        simg = findViewById(R.id.simg);
        Intent intent = getIntent();
        skip = findViewById(R.id.skip);
         skip.setVisibility(View.GONE);
        String userid = intent.getStringExtra("UID");
String loguser = intent.getStringExtra("UIDDD");

  //  Toast.makeText(Pf.this,"load ke pahle tak aa gae",Toast.LENGTH_LONG).show();
       if(loguser!=null){
          // Toast.makeText(Pf.this,"loaduser called",Toast.LENGTH_LONG).show();
           loaduser(loguser);
          skip.setVisibility(View.VISIBLE);
           skip.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                Intent i = new Intent(Pf.this,DemoDesign.class);
                i.putExtra("UID",loguser);
                startActivity(i);
                finish();
               }
           });
       }




         d = new ProgressDialog(this);

Storage = FirebaseStorage.getInstance();

        let.setOnClickListener(v -> {



            String Username = username.getText().toString();
            if(Username.isEmpty()){
                Toast.makeText(Pf.this,"Please enter Username first",Toast.LENGTH_LONG).show();

            }else{
                if(userid==null&&loguser!=null){
                    if(uploadbit!=null && username!=null){

                        uploadimgfire(uploadbit,loguser,Username);
                    }
                    else if(photourlbrother!=null){


                        if(loguser == null){
                            Toast.makeText(Pf.this,"User null",Toast.LENGTH_LONG).show();
                        }else{
                           // Toast.makeText(Pf.this,"Bhai ab hoga maut ka nanga naach",Toast.LENGTH_LONG).show();
                            UserProfile userprofiledetail = new UserProfile();
                            userprofiledetail.setName(Username);
                            userprofiledetail.setUId(loguser);
                            userprofiledetail.setMurl(photourlbrother);

                            saveuserinfotoDatabase(userprofiledetail);
                        }

                    }else if(photourlbrother==null){
                        UserProfile userprofiledetail = new UserProfile();
                        userprofiledetail.setName(Username);
                        userprofiledetail.setUId(loguser);
                        userprofiledetail.setMurl(photourlbrother);

                        saveuserinfotoDatabase(userprofiledetail);
                    }
                }else if(userid!=null&&loguser==null){
                    if(uploadbit!=null){
                        uploadimgfire(uploadbit,userid,Username);
                    }
                    else{


                        if(userid == null){
                            Toast.makeText(Pf.this,"User null",Toast.LENGTH_LONG).show();
                        }else{
                            UserProfile userprofiledetail = new UserProfile();
                            userprofiledetail.setName(Username);
                            userprofiledetail.setUId(userid);
                            userprofiledetail.setMurl(null);

                            saveuserinfotoDatabase(userprofiledetail);
                        }

                    }
                }

            }

        });

        addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //**************************--------CheckPermission------------------
    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        }else{

            showImageSourceDialog();
        }
    }
//-------------------*********************showimageDialog---------------
    private void showImageSourceDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Select anyone")
                .setIcon(R.drawable.profile)
                .setItems(new CharSequence[]{"Gallery","Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openGallery();
                        }
                        else{
                            openCamera();
                        }
                    }
                });
        alert.show();
    }
    private void openCamera() {
        Intent ci = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(ci,CAMERA_REQUEST_CODE);

    }
    private void openGallery() {
        Intent gi = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gi,GALLERY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == RESULT_OK && data!= null&&data.getData()!=null){
              Uri  srcuri = data.getData();
                Uri dstnuri = Uri.fromFile(new File(getCacheDir(), "edited_image.jpg"));
               pause = 5;
                UCrop.of(srcuri,dstnuri).start(this);

           }


        }


        if(pause == 6){
            if(requestCode == UCrop.REQUEST_CROP){
              if(resultCode == RESULT_OK){
                  Uri resulturi = UCrop.getOutput(data);
                  if(resulturi!=null){
                             resultURI = resulturi;
                      try {
                          Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resulturi);
                          uploadbit = bitmap;
                          simg.setImageBitmap(bitmap);

                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  }

              }
            }
        }

    }





    private byte[] convertBitintobyte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        return  baos.toByteArray();
    }
    private void uploadimgfire(Bitmap bitmap, String uid, String usernamme) {
        d.setTitle("Uploading...........");

        FirebaseStorage strg = FirebaseStorage.getInstance();
        StorageReference psr = strg.getReference().child("users/" + uid + "/profile.jpg");
        byte[] bytimg = convertBitintobyte(bitmap);

        psr.putBytes(bytimg)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        psr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profileurl = uri.toString();

                                UserProfile u = new UserProfile();
                                u.setMurl(profileurl);
                                u.setName(usernamme);
                                u.setUId(uid);

                                saveuserinfotoDatabase(u);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                d.dismiss(); // Dismiss dialog on failure
                                Log.e("Firebase storage", "Failed to retrieve download URL", e);
                                Toast.makeText(Pf.this, "Failed to retrieve image URL", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float per = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        d.setMessage("Loading " + per + "%");
                        d.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        d.dismiss(); // Dismiss dialog on failure
                        Log.e("Firebase storage", "Image upload failed", e);
                        Toast.makeText(Pf.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveuserinfotoDatabase(UserProfile uspd) {
        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        String uid = uspd.getUId();
        DatabaseReference dr = fbd.getReference("Customer/").child("UserDetails/"+uid);


            dr.setValue(uspd)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            d.dismiss();
                            Toast.makeText(Pf.this, "Welcome", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Pf.this, DemoDesign.class);
                            i.putExtra("UID", uspd.getUId());
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            d.dismiss(); // Dismiss dialog on failure
                            Log.e("Firebase Database", "Failed to save user data", e);
                            Toast.makeText(Pf.this, "Failed to save user data", Toast.LENGTH_LONG).show();
                        }
                    });

    }







    @Override
    protected void onStart() {
        super.onStart();
        pause = 6;

    }

    private void loaduser(String loguser) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
      //  Toast.makeText(Pf.this,"Yaha rak aa gae",Toast.LENGTH_LONG).show();

        String uid = loguser;

        if(uid!=null){
           //Toast.makeText(Pf.this,"User null nahi hai\n"+uid,Toast.LENGTH_LONG).show();
            DatabaseReference dr = db.getReference("Customer").child("UserDetails/"+uid);
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() & snapshot!=null){
                      //  Toast.makeText(Pf.this,"    data fetched bro \n"+uid,Toast.LENGTH_LONG).show();
                        String uusername = snapshot.child("name").getValue(String.class);
                        username.setText(uusername);
                       String  profileimage = snapshot.child("murl").getValue(String.class);
                        if(profileimage!=null){
                           // Toast.makeText(Pf.this,"loadprofile called\n",Toast.LENGTH_LONG).show();
                            photourlbrother  = profileimage;
                            loadProfile(profileimage);
                        }else{
                            Glide.with(getApplicationContext()).load(R.drawable.progile).into(simg);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(Pf.this,"failed to load data",Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    private void loadProfile(String profileimage) {

        Glide.with(this).load(profileimage)
                .into(simg);

    }


}
