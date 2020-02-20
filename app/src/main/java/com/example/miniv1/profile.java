package com.example.miniv1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class profile extends AppCompatActivity {

    EditText et_fName, et_lName;
    TextView tv_email;

    ImageView img_view;

    Button btn_changePwd, btn_update;

    DatabaseReference DB_profileRef;

    Dialog loadingDialog;

    StorageReference fireRef;
    StorageReference fireRef2;
    private StorageReference mStorageRef;
    public String imageURL = "";
    int Code = 0;

    Uri selectedImage;


    int flag = 0;
    String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        et_fName = (EditText) findViewById(R.id.et_profilePage_fname);
        et_lName = (EditText) findViewById(R.id.et_profilePage_lname);
        tv_email = (TextView) findViewById(R.id.tv_profilePage_email);

        img_view = (ImageView) findViewById(R.id.img_profile);

        btn_changePwd = (Button) findViewById(R.id.btn_changePwd);
        btn_update = (Button) findViewById(R.id.btn_updateProfile);

        DB_profileRef = FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loadingDialog = new Dialog(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();


        DB_profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("fName").getValue().toString();
                String lname = dataSnapshot.child("lName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String profile_url = dataSnapshot.child("url").getValue().toString();
                date = dataSnapshot.child("date").getValue().toString();
                tv_email.setText(email);
                et_fName.setText(fname);
                et_lName.setText(lname);

                Picasso.get().load(profile_url).placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(img_view, new Callback(){

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(profile.this, "Failed to load profile picture.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, "DB error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Image choose code using and url generation;
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)
                        && !Environment.getExternalStorageState().equals(
                        Environment.MEDIA_CHECKING)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    Code = 1;
                    startActivityForResult(intent, Code);
                } else
                    Toast.makeText(profile.this, "No gallery found.", Toast.LENGTH_SHORT).show();
            }
        });





        btn_changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change pwd code using popup
//                showLoadingDialog(v);
//                DB_profileRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        final String fname = dataSnapshot.child("fName").getValue().toString();
//                        final String lname = dataSnapshot.child("lName").getValue().toString();
//                        String email = dataSnapshot.child("email").getValue().toString();
//                        String password = dataSnapshot.child("psw").getValue().toString();
//                        final String profile_url = dataSnapshot.child("url").getValue().toString();
//
//                        if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname)) {
//                            Toast.makeText(profile.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        signUpFormFB userInfo = new signUpFormFB(fname, lname, email, password, profile_url, "2012-10-10");
//
//                        FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(profile.this, "Update successfully.", Toast.LENGTH_SHORT).show();
//                                loadingDialog.dismiss();
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(profile.this, "DB error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
//                        loadingDialog.dismiss();
//                    }
//                });
//
//                Toast.makeText(profile.this, "Write and enable change password", Toast.LENGTH_SHORT).show();

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update profile code
                Date ddate = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(ddate);
                showLoadingDialog(v);
                if(flag == 0 || strDate.equals(date)) {
                    if (flag == 1)
                        Toast.makeText(profile.this, "Profile picture can be updated only once in a day", Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                }
                else
                    UploadImages();
                flag = 0;
            }
        });
    }


    public void showLoadingDialog(View v) {

        loadingDialog.setContentView(R.layout.loading_dialogbox);

        ImageView img_loading = (ImageView) loadingDialog.findViewById(R.id.img_loading);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        img_loading.startAnimation(animation);


        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }

    public void  updateUserInfo() {
        DB_profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String fname = et_fName.getText().toString().trim();
                final String lname = et_lName.getText().toString().trim();
                String email = dataSnapshot.child("email").getValue().toString();
                String password = dataSnapshot.child("psw").getValue().toString();
                String profile_url = dataSnapshot.child("url").getValue().toString();
                String date = dataSnapshot.child("date").getValue().toString();

                if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname)) {
                    Toast.makeText(profile.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUpFormFB userInfo = new signUpFormFB(fname, lname, email, password, profile_url, date);

                FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismiss();
                        return;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, "DB error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Code) {
                Code = 0;
                Bitmap originBitmap = null;
                selectedImage = data.getData();
                InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(
                            selectedImage);
                    originBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (originBitmap != null) {
                    {
                        flag = 1;
                        this.img_view.setImageBitmap(originBitmap);
                        img_view.setVisibility(View.VISIBLE);
                    }
                } else
                    selectedImage = null;
            }
        }
    }


    public void UploadImages() {
        try {
            String strFileName = "img.jpg";

            Uri file = selectedImage;

            fireRef = mStorageRef.child("images/Profile_Pictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid().toString() + "/" + System.currentTimeMillis() + strFileName);
            fireRef2 = mStorageRef.child("Profile_PicturesBackup" + "/" + System.currentTimeMillis() + strFileName);

            UploadTask uploadTask = fireRef.putFile(file);
            Log.e("Fire Path", fireRef.toString());
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fireRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Log.e("Image URL", downloadUri.toString());

                        selectedImage = null;
                        imageURL = downloadUri.toString();
                        //Toast.makeText(SignUp.this, "Image url" + imageURL, Toast.LENGTH_SHORT).show();
                        DB_profileRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String fname = et_fName.getText().toString().trim();
                                final String lname = et_lName.getText().toString().trim();
                                String email = dataSnapshot.child("email").getValue().toString();
                                String password = dataSnapshot.child("psw").getValue().toString();
                                String profile_url = imageURL;

                                if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname)) {
                                    Toast.makeText(profile.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Date date = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = dateFormat.format(date);

                                Toast.makeText(profile.this, "Date is: " + strDate, Toast.LENGTH_SHORT).show();

                                signUpFormFB userInfo = new signUpFormFB(fname, lname, email, password, profile_url, strDate);

                                FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(profile.this, "Update successfully.", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(profile.this, "DB error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        });

                    } else {
                        Toast.makeText(profile.this, "Image upload unsuccessful. Please try again."
                                , Toast.LENGTH_LONG).show();
                    }
                }
            });

//            UploadTask uploadTask2 = fireRef2.putFile(file);
//            Log.e("Fire Path", fireRef2.toString());
//            Task<Uri> urlTask2 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    return fireRef2.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        Log.e("Image URL", downloadUri.toString());
//                        selectedImage = null;
//
//                    } else {
//                        Toast.makeText(profile.this, "Image upload unsuccessful. Please try again."
//                                , Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
        } catch (Exception ex) {
            Toast.makeText(profile.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();

        }


    }

}
