package com.example.ecommerce;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {
    Button done;
    FloatingActionButton addd;
    ShapeableImageView simg;
TextView cancel;
    Bitmap uploadbit;
    Uri resultURI;
    EditText username;
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
        setContentView(R.layout.activity_edit_profile);

        simg = findViewById(R.id.simg);
        addd = findViewById(R.id.addd);
        username = findViewById(R.id.usern);
        cancel = findViewById(R.id.cancel);
        done = findViewById(R.id.done);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, Account.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        d = new ProgressDialog(this);
        SharedPreferences s = getSharedPreferences("useriddetail",MODE_PRIVATE);
      String uid =  s.getString("signuserid","null hai");

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
              //  Toast.makeText(EditProfile.this, uid, Toast.LENGTH_SHORT).show();
               if(!Username.isEmpty() && uploadbit!=null){
                   uploadimgfire(uploadbit,uid,Username);
                 //  Toast.makeText(EditProfile.this, Username, Toast.LENGTH_SHORT).show();
               } else if (uploadbit == null) {
                 UserProfile u = new UserProfile();
                 u.setMurl(null);
                 u.setUId(uid);
                 u.setName(Username);
                 saveuserinfotoDatabase(u);

               } else{
                   Toast.makeText(EditProfile.this, "user null", Toast.LENGTH_SHORT).show();
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


    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        }else{

            showImageSourceDialog();
        }
    }
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
                Uri srcuri = data.getData();
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
      //  Toast.makeText(EditProfile.this,"byte ke andar", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        return  baos.toByteArray();

    }
    private void uploadimgfire(Bitmap bitmap, String uid, String usernamme) {
        d.setTitle("Uploading...........");
        FirebaseStorage strg = FirebaseStorage.getInstance();
        StorageReference psr = strg.getReference().child("users/" + uid + "/profile.jpg");


        byte[] bytimg = convertBitintobyte(bitmap);
      //  Toast.makeText(EditProfile.this,"uploadkeandar", Toast.LENGTH_SHORT).show();
        psr.putBytes(bytimg)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(EditProfile.this,"successkeandar", Toast.LENGTH_SHORT).show();
                        psr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profileurl = uri.toString();
                               // Toast.makeText(EditProfile.this,"successfull", Toast.LENGTH_SHORT).show();
                                UserProfile u = new UserProfile();
                                u.setMurl(profileurl);
                                u.setName(usernamme);
                                u.setUId(uid);

                                saveuserinfotoDatabase(u);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                d.dismiss();
                              // Dismiss dialog on failure
                                Log.e("Firebase storage", "Failed to retrieve download URL", e);
                                Toast.makeText(EditProfile.this, "Failed to retrieve image URL", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                        d.dismiss();
                       // Dismiss dialog on failure
                        Log.e("Firebase storage", "Image upload failed", e);
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(EditProfile.this, "Changed", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EditProfile.this, Account.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        d.dismiss();
                   // Dismiss dialog on failure
                        Log.e("Firebase Database", "Failed to save user data", e);
                        Toast.makeText(EditProfile.this, "Failed to save user data", Toast.LENGTH_LONG).show();
                    }
                });

    }







    @Override
    protected void onStart() {
        super.onStart();
        pause = 6;

    }



}