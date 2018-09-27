package com.google.firebase.example.fireeats.activities;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.example.fireeats.R;
import com.google.firebase.example.fireeats.Utils.Util;
import com.google.firebase.example.fireeats.models.Users;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar appBarLayout;

    Button view, add;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    Util util;
    Handler handler;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout = findViewById(R.id.toolbar);
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 0.1f));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }

        add = findViewById(R.id.bt_add);
        view = findViewById(R.id.bt_view);
        db = FirebaseFirestore.getInstance();
        handler = new Handler();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        usersArrayList = new ArrayList<>();


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(DashBoardActivity.this)) {
                    new GetAll().execute();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    public class GetAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DashBoardActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            usersArrayList = new ArrayList<>();
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Users user = document.toObject(Users.class);
                            usersArrayList.add(user);
                        }

                    }
                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(DashBoardActivity.this, UsersListActivity.class);
                    intent.putParcelableArrayListExtra("array", usersArrayList);
                    startActivity(intent);
                }
            }, 1000);
            return null;
        }
    }
}
