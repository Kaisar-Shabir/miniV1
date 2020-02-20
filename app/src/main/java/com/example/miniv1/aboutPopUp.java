package com.example.miniv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class aboutPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        JustifyTextView jtv = (JustifyTextView) findViewById(R.id.text);

        YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(jtv);

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.55));
    }
}
