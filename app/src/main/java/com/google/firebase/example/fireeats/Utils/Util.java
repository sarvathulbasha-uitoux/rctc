package com.google.firebase.example.fireeats.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.example.fireeats.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Util {


    public static void showSnack(Activity activity, String message) {

        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        textView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Oswald-Regular.ttf"));
//        sbView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        sbView.setBackground(activity.getResources().getDrawable(R.drawable.gradient));
        snackbar.show();

    }


    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static String generateId() {
        String transactionTrackId = "";
        try {
            long msTime = System.currentTimeMillis();
            Date curDateTime = new Date(msTime);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
            String curDate = formatter.format(curDateTime);
            Random rnd = new Random();
            int randomNumber = 100 + rnd.nextInt(900);
            transactionTrackId = curDate + randomNumber;
            Log.e("transactionTrackId", transactionTrackId);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionTrackId;
    }

}
