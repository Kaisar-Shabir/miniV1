package com.example.miniv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class mail_popup extends AppCompatActivity {


    Button send_btn;
    private EditText composeEmail_et, subject_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.45));


        subject_et = (EditText) findViewById(R.id.et_subject);
        composeEmail_et = (EditText) findViewById(R.id.et_composeEmail);
        send_btn = (Button) findViewById(R.id.btn_sendMail);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub = subject_et.getText().toString();
                String mail = composeEmail_et.getText().toString();
                String myMailId = "dr.dermo.mini.project@gmail.com , kaisarshabirdar@gmail.com";
                String[] emails = myMailId.split(",");

                Intent sendMail = new Intent(Intent.ACTION_SEND);


                sendMail.putExtra(Intent.EXTRA_EMAIL, emails);
                sendMail.putExtra(Intent.EXTRA_SUBJECT, sub);
                sendMail.putExtra(Intent.EXTRA_TEXT, mail);
                sendMail.setType("message/rfc822");
                sendMail.setPackage("com.google.android.gm");

                startActivity(sendMail);
            }
        });


    }
}
