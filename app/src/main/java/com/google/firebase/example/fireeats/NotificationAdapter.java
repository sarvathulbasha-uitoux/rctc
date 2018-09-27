package com.google.firebase.example.fireeats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public static NotificationInterface listener;
    Context context;
    ArrayList<NotificationModal> arraylist;


    public NotificationAdapter(Context contex, ArrayList<NotificationModal> arrayList) {
        context = contex;
        arraylist = arrayList;

    }

    public static void onBindListener(NotificationInterface addInterface) {
        listener = addInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, null);
        return new ItemHolder(itemLayoutView);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ItemHolder itemHolder = (ItemHolder) holder;
        final NotificationModal notificationModal = arraylist.get(position);

        if (position == 0) {
            itemHolder.tv1.setVisibility(View.VISIBLE);
            itemHolder.et1.setVisibility(View.VISIBLE);
            itemHolder.iv1.setVisibility(View.VISIBLE);
            itemHolder.iv2.setVisibility(View.GONE);
        } else {
            itemHolder.tv1.setVisibility(View.VISIBLE);
            itemHolder.et1.setVisibility(View.VISIBLE);
            itemHolder.iv1.setVisibility(View.VISIBLE);
            itemHolder.iv2.setVisibility(View.VISIBLE);
        }


        itemHolder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.add(String.valueOf(position));
                }
            }
        });

        itemHolder.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.delete(String.valueOf(position));
                }
            }
        });

        itemHolder.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    if (listener != null) {
                        listener.addByEdiText(String.valueOf(position));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public interface NotificationInterface {
        void add(String position);

        void addByEdiText(String position);

        void delete(String position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ItemHolder extends ViewHolder implements View.OnClickListener {
        public TextView tv1;
        public EditText et1;
        public ImageView iv1, iv2;

        public ItemHolder(View v) {
            super(v);

            tv1 = v.findViewById(R.id.tv1);
            et1 = v.findViewById(R.id.et1);
            iv1 = v.findViewById(R.id.iv1);
            iv2 = v.findViewById(R.id.iv2);

        }
    }


}
