package com.example.miniv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class chatBot extends AppCompatActivity {

    ImageView loadingLogo;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        loadingLogo = (ImageView) findViewById(R.id.img_loading);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation);

        loadingLogo.startAnimation(animation);

    }
}
