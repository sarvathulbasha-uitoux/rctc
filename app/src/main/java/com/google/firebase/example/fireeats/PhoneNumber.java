package com.google.firebase.example.fireeats;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class PhoneNumber extends AppCompatActivity implements NotificationAdapter.NotificationInterface {
    RecyclerView rv1;
    ArrayList<NotificationModal> notificationModalArrayList = new ArrayList<>();
    NotificationModal notificationModal;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        rv1 = findViewById(R.id.rv1);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(PhoneNumber.this);
        rv1.setLayoutManager(mLayoutManager);

        notificationAdapter = new NotificationAdapter(PhoneNumber.this, notificationModalArrayList);
        rv1.setAdapter(notificationAdapter);


        notificationModal = new NotificationModal();
        notificationModal.setNumber("");
        notificationModalArrayList.add(notificationModal);

        notificationAdapter.notifyDataSetChanged();

    }


    @Override
    public void add(final String position) {
        try {
            final Handler handler = new Handler();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            sleep(1000);
                            handler.post(this);
                            Log.e("size add", "-=-=-" + notificationModalArrayList.size());
                            notificationModal.setNumber("");
                            notificationModalArrayList.add(notificationModal);
                            notificationAdapter.notifyItemInserted(Integer.parseInt(position + 1));
                            Log.e("size add", "-=-=-" + notificationModalArrayList.size());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();


        } catch (Exception e) {

        }
    }

    @Override
    public void addByEdiText(String position) {
        try {
            Log.e("size edit", "-=-=-" + notificationModalArrayList.size());
            if (notificationModalArrayList.size() <= Integer.parseInt(position) + 1) {
                notificationModal.setNumber("");
                notificationModalArrayList.add(notificationModal);
                Log.e("size edit", "-=-=-" + notificationModalArrayList.size());
//                notificationAdapter.notifyDataSetChanged();
                notificationAdapter.notifyItemInserted(Integer.parseInt(position + 1));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void delete(String position) {
        try {
            Log.e("size delete", "-=-=-" + notificationModalArrayList.size() + "--==-" + position);
//            int a= Integer.valueOf(position)+1;
            notificationModalArrayList.remove(Integer.parseInt(position));
            notificationAdapter.notifyItemRemoved(Integer.parseInt(position));
            Log.e("size delete", "-=-=-" + notificationModalArrayList.size());
//            notificationAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationAdapter.onBindListener(PhoneNumber.this);
    }
}
