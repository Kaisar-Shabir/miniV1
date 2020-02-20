package com.example.miniv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.firebase.storage.internal.SmartHandler;

public class message_feedback extends AppCompatActivity {

    EditText et_msg;
    Button btn_sendMsg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_feedback);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.31));

        et_msg = (EditText) findViewById(R.id.et_composeMsg);
        btn_sendMsg = (Button) findViewById(R.id.btn_sendMsg);

        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(message_feedback.this, Manifest.permission.SEND_SMS);

                if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    messageDil();
                    finish();
                }
                else {
                    ActivityCompat.requestPermissions(message_feedback.this, new String[] {
                            Manifest.permission.SEND_SMS}, 0);
                }
            }
        });

    }





    private void messageDil() {

        String pNumber = "7006605443";
        String msgStr = et_msg.getText().toString().trim();

        if(msgStr.equals("")) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(pNumber, null, msgStr, null, null);
            Toast.makeText(this, "Message Send.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case 0:

                if(grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    messageDil();
                    finish();
                }
                else{
                    Toast.makeText(this, "Please grant permissions.", Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }
}
