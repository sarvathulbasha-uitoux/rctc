package com.google.firebase.example.fireeats;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DeepLinkMain extends AppCompatActivity {

    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link_main);
        bt1 = findViewById(R.id.bt1);

        bt1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                UPI();
            }
        });

    }

    private String getUPIString(String payeeAddress, String payeeName, String payeeMCC, String trxnID, String trxnRefId,
                                String trxnNote, String payeeAmount, String currencyCode, String refUrl) {
        String UPI = "upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                + "&mc=" + payeeMCC + "&tid=" + trxnID + "&tr=" + trxnRefId
                + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                + "&refUrl=" + refUrl;
        return UPI.replace(" ", "+");
    }


    @SuppressLint("RestrictedApi")
    public void UPI() {
        Intent intent = new Intent();
        if (intent != null) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getUPIString("gowthambellan@okaxis", "gowthambellan@okaxis", "gowthambellan@okaxis", "121212", "23127632",
                    "payfromDues", "1", "INR", "")));
            Intent chooser = Intent.createChooser(intent, "Pay with...");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivityForResult(chooser, 1, null);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Log.e("UPI REQUEST CODE-->", "" + requestCode);
            Log.e("UPI RESULT CODE-->", "" + resultCode);
            Log.e("UPI DATA-->", "" + data);


            if (resultCode == -1) {
                // 200 Success

            } else {
                // 400 Failed
            }


        } catch (Exception e) {
            Log.e("Error in UPI", "" + e.getMessage());
        }
    }

}
