package com.example.miniv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.miniv1.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Dialog forgotPwdDialog;
    Dialog loadingDialog;

    String imageURL = "https://firebasestorage.googleapis.com/v0/b/miniv1.appspot.com/o/Profile_Pictures%2FDefault%2F1578210714754img.jpg?alt=media&token=371f0542-4fb0-451e-ab3a-df4e33ed46a8";

    private FirebaseAuth firebaseAuth_forgotPwd;

    RelativeLayout relativeLayout1, relativeLayout2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
        }
    };

    Button LoginButton, SignUpButton;
    //ForgotPwdButton;
    EditText LoginEmail_et, LoginPwd_et;


    //GOOOGLE LOGIN
    SignInButton GLoginButton;

    //FACEBOOK LOGIN
    private LoginButton FBLoginButton;
    private CallbackManager callbackManager;
    //private CircleImageView circleImageView;
    //private TextView txtName,txtEmail;


    private FirebaseAuth Login_FirebaseAuth;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
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
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Login_FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, homePage.class));
            finish();
        }
        //updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new Dialog(this);
        forgotPwdDialog = new Dialog(this);

        //FULL SCREEN: HIF+DING THE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.rel_lay1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.rel_lay2);

        handler.postDelayed(runnable, 2000);

        LoginButton = findViewById(R.id.btn_login);
        SignUpButton = findViewById(R.id.btn_signUp);
        //ForgotPwdButton = findViewById(R.id.btn_forgotPsw);

        LoginEmail_et = findViewById(R.id.et_email);
        LoginPwd_et = findViewById(R.id.et_pwd);

        Login_FirebaseAuth = FirebaseAuth.getInstance();


        GLoginButton = (SignInButton) findViewById(R.id.btn_g_login);
        FBLoginButton = (LoginButton) findViewById(R.id.btn_fb_login);


        ScrollView loginScroller = (ScrollView) findViewById(R.id.login_scroller);
        YoYo.with(Techniques.DropOut).duration(1500).repeat(0).playOn(loginScroller);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(v);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });


        callbackManager = CallbackManager.Factory.create();
//        txtName = findViewById(R.id.profile_name);
//        txtEmail = findViewById(R.id.profile_email);
        //circleImageView = findViewById(R.id.profile_pic);
        FBLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        //checkLoginStatus();

        FBLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        //login for an already registered user
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //to home page on successful login
            public void onClick(final View v) {
                String str_loginEmail = LoginEmail_et.getText().toString().trim();
                String str_LoginPwd = LoginPwd_et.getText().toString().trim();

                if(TextUtils.isEmpty(str_loginEmail)) {
                    Toast.makeText(MainActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(str_LoginPwd) || str_LoginPwd.length() < 8) {
                    Toast.makeText(MainActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                showLoadingDialog(v);
                Login_FirebaseAuth.signInWithEmailAndPassword(str_loginEmail, str_LoginPwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(), homePage.class));
                                    Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    loadingDialog.dismiss();

                                } else {

                                    Toast.makeText(MainActivity.this, "Login failed. Please Check your email and password. Check if you are connected to internet", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(MainActivity.this, SignUp.class));
                                    loadingDialog.dismiss();
                                    return;
                                }
                                loadingDialog.dismiss();
                            }
                        });

            }
        });




        //to sign up page
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
                finish();
            }
        });


//        ForgotPwdButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, forgotPwd.class));
//                finish();
//            }
//        });

    }


    public void showDialog(View v) {
        final EditText email_et;
        Button sendLink_btn;
        TextView close_tv;

        forgotPwdDialog.setContentView(R.layout.forgot_pwd_dialog_box);

        email_et = (EditText) forgotPwdDialog.findViewById(R.id.et_forgot_email);
        close_tv = (TextView) forgotPwdDialog.findViewById(R.id.tv_close);
        sendLink_btn = (Button) forgotPwdDialog.findViewById(R.id.btn_sendLink);

        close_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPwdDialog.dismiss();
            }
        });

        firebaseAuth_forgotPwd = FirebaseAuth.getInstance();

        sendLink_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strForgotPwdEmail = email_et.getText().toString();

                if(TextUtils.isEmpty(strForgotPwdEmail)) {
                    Toast.makeText(MainActivity.this, "Fill the EMAIL field.", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth_forgotPwd.sendPasswordResetEmail(strForgotPwdEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Please check your inbox for passeord reset link.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "An error occured. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });




        forgotPwdDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgotPwdDialog.show();

    }


    public void showLoadingDialog(View v) {

        loadingDialog.setContentView(R.layout.loading_dialogbox);

        ImageView img_loading = (ImageView) loadingDialog.findViewById(R.id.img_loading);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        img_loading.startAnimation(animation);


        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }


    //To reccieve information from phone os
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Login_FirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = Login_FirebaseAuth.getCurrentUser();

                            //If login success jump to home activity

                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                            if(user != null) {

                                //share the information to homeactivity with intent
                                String personName = acct.getDisplayName();
                                String personEmail = acct.getEmail();
                                String personId = acct.getId();
                                Uri personPhoto = acct.getPhotoUrl();

                                String[] arrOfStr = personName.split(" ", 2);

                                signUpFormFB userInfo = new signUpFormFB(arrOfStr[0], arrOfStr[1], personEmail, "G ID: " + personId, imageURL, "2010-10-10".toString());

                                FirebaseDatabase.getInstance().getReference("userInformation").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Intent intent = new Intent(MainActivity.this, homePage.class);
                                startActivity(intent);

                                finish();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Sign in using google account failed.", Toast.LENGTH_SHORT).show();

                        }

                        loadingDialog.dismiss();

                        // ...
                    }
                });
    }



//    @Override
//    protected void (int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if(currentAccessToken==null)
            {
//                txtName.setText("");
//                txtEmail.setText("");
                //circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
            }
            else {
                //loadUserProfile(currentAccessToken);

                startActivity(new Intent(getApplicationContext(), homePage.class));
                Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    //String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";


//                    txtEmail.setText(email);
//                    txtName.setText(first_name +" "+last_name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    //Glide.with(MainActivity.this).load(image_url).into(circleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus()
    {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}
