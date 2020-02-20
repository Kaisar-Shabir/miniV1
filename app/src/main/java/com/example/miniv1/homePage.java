package com.example.miniv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.InputStream;
import java.net.URL;

public class homePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navDrawer;
    TextView head_name, head_email;
    ImageView head_image;
    DatabaseReference DB_profileRef;

    boolean doubleBackToExitPressedOnce = false;

    Dialog profileDialog;
    public String imageURL = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.homepage_toolbar);
        setSupportActionBar(toolbar);

        navDrawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigationView);

        //      updating navigation header
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);




        head_email = (TextView) headView.findViewById(R.id.header_layout_email);
        head_name = (TextView) headView.findViewById(R.id.header_layout_name);
        head_image = (ImageView) headView.findViewById(R.id.header_layout_image);




        head_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        DB_profileRef = FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getUid());

        DB_profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fName").getValue().toString() + " " + dataSnapshot.child("lName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String profile_url = dataSnapshot.child("url").getValue().toString();
                imageURL = profile_url;

                head_email.setText(  "Email : " + email);
                head_name.setText(   "Name : " + name);

               Picasso.get().load(profile_url).placeholder(R.mipmap.ic_launcher)
                       .error(R.mipmap.ic_launcher)
                       .into(head_image, new Callback(){

                           @Override
                           public void onSuccess() {

                           }

                           @Override
                           public void onError(Exception e) {

                           }
                       });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(homePage.this, "DB error: " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });



        head_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawer.closeDrawer((GravityCompat.START));
                startActivity(new Intent(homePage.this, profile.class));
            }
        });


        profileDialog = new Dialog(homePage.this);

        head_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                profileDialog.setContentView(R.layout.profile_dialogbox);

                CircularImageView cimv = (CircularImageView) profileDialog.findViewById(R.id.profile_pic);

                try {
                    Picasso.get().load(imageURL)
                            .placeholder(R.drawable.ic_search_black_24dp)
                            .into(cimv);
                }
                catch (Exception e) {

                }

                Animation animation = AnimationUtils.loadAnimation(homePage.this, R.anim.animation1);
                cimv.startAnimation(animation);


                profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                profileDialog.show();

                return false;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new homeFragment())
                    .commit();

            navigationView.setCheckedItem(R.id.nav_home);
        }

    }


    @Override
    public void onBackPressed() {

        if(navDrawer.isDrawerOpen(GravityCompat.START)){
            navDrawer.closeDrawer(GravityCompat.START);
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new homeFragment())
                        .commit();
                break;

            case R.id.nav_checkup:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new checkUpFragment())
                        .commit();
                break;

            case R.id.nav_reports:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new reportsFragment())
                        .commit();
                break;

            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new searchFragment())
                        .commit();
                break;

            case R.id.nav_hospitals:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new hospitalsFragment())
                        .commit();
                break;

            case R.id.nav_mail:
                startActivity(new Intent(homePage.this, mail_popup.class));
                break;

            case R.id.nav_phone:
                makeCall();
                break;

            case R.id.nav_message:
                startActivity(new Intent(homePage.this, message_feedback.class));
                break;

            case R.id.nav_chatbot:
                startActivity(new Intent(homePage.this, chatBot.class));
                Toast.makeText(this, "Chat bot will be aviliable soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(homePage.this);
                builder.setMessage("Do you want to logout?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(homePage.this, MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null);
                AlertDialog alert = builder.create();
                alert.show();
                break;

            case R.id.nav_about:
                startActivity(new Intent(homePage.this, aboutPopUp.class));
                break;

            case R.id.nav_download:
                clicked_btn("https://drive.google.com/open?id=1ffg7YX-4u9BSizErWNx-0-TS04bzo1ab");
                //clicked_btn("https://drive.google.com/open?id=1YEksjM93k8bRnz658tFfgxT21ybt_qbl");
                break;
        }

        navDrawer.closeDrawer((GravityCompat.START));

        return true;
    }

    private void makeCall() {
        String pNumber = "tel:7006605443";
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse(pNumber));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show();
            requestPermission();

        }
        else {
            startActivity(intent);
        }
    }


    public void clicked_btn(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(homePage.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
    }



    private void loadImageFromUrl() {



    }



}
