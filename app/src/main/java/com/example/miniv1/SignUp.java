package com.example.miniv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.miniv1.MainActivity;
import com.example.miniv1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUp extends AppCompatActivity {

    Dialog loadingDialog;

    Button signUp_btn, back_btn;

    EditText RegEmail_et, RegPwd_et, RegConPwd_et;
    EditText RegFname_et, RegLname_et;

    String imageURL = "https://firebasestorage.googleapis.com/v0/b/miniv1.appspot.com/o/Profile_Pictures%2FDefault%2F1578210714754img.jpg?alt=media&token=371f0542-4fb0-451e-ab3a-df4e33ed46a8";

    private FirebaseAuth Reg_FirebaseAuth;


    RelativeLayout relativeLayout1, relativeLayout2;
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        loadingDialog = new Dialog(this);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.rel_lay1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.rel_lay2);

        handler.postDelayed(runnable, 2000);

        ScrollView signupScroller = (ScrollView) findViewById(R.id.signup_scroller);
        YoYo.with(Techniques.DropOut).duration(1500).repeat(0).playOn(signupScroller);


        //FULL SCREEN: HIF+DING THE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Reg_FirebaseAuth = FirebaseAuth.getInstance();


        signUp_btn = (Button) findViewById(R.id.btn_signUp);
        back_btn = (Button) findViewById(R.id.btn_back);

        RegEmail_et = (EditText) findViewById(R.id.et_email);
        RegPwd_et = (EditText) findViewById(R.id.et_pwd);
        RegConPwd_et = (EditText) findViewById(R.id.et_cnf_pwd);

        RegFname_et = (EditText) findViewById(R.id.et_fName);
        RegLname_et = (EditText) findViewById(R.id.et_lName);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_regEmail = RegEmail_et.getText().toString().trim();
                final String str_regPwd = RegPwd_et.getText().toString().trim();
                final String str_regConPwd = RegConPwd_et.getText().toString().trim();
                final String str_regFname = RegFname_et.getText().toString().trim();
                final String str_regLname = RegLname_et.getText().toString().trim();

                if (TextUtils.isEmpty(str_regEmail)) {
                    Toast.makeText(SignUp.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(str_regPwd) || str_regPwd.length() < 8) {
                    Toast.makeText(SignUp.this, "Password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(str_regFname) || TextUtils.isEmpty(str_regLname)) {
                    Toast.makeText(SignUp.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (str_regPwd.equals(str_regConPwd)) {

                    showLoadingDialog(view);
                    Reg_FirebaseAuth.createUserWithEmailAndPassword(str_regEmail, str_regPwd)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

//                                        UploadImages();

                                        signUpFormFB userInfo = new signUpFormFB(str_regFname, str_regLname, str_regEmail, str_regPwd, imageURL, "2010-10-10".toString());


                                        FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseAuth.getInstance().signOut();
                                                Toast.makeText(SignUp.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        });

                                    } else {

                                        Toast.makeText(SignUp.this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }

                                }
                            });

                }
            }
        });

    }

    public void showLoadingDialog(View v) {

        loadingDialog.setContentView(R.layout.loading_dialogbox);

        Button cover_btn = (Button) loadingDialog.findViewById(R.id.btn_cover);
        ImageView img_loading = (ImageView) loadingDialog.findViewById(R.id.img_loading);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        img_loading.startAnimation(animation);

        cover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUp.this, "Signing Up.", Toast.LENGTH_SHORT).show();
            }
        });

        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }
}
