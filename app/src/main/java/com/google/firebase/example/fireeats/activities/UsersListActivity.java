package com.google.firebase.example.fireeats.activities;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.example.fireeats.R;
import com.google.firebase.example.fireeats.interfaces.OnLoadMoreListener;
import com.google.firebase.example.fireeats.models.Users;

import java.util.ArrayList;


public class UsersListActivity extends AppCompatActivity {

    RecyclerView rv1;
    ArrayList<Users> arrayList;
    ArrayList<Users> arrayList_get;
    UserAdapter mUserAdapter;
    AppBarLayout appBarLayout;
    RelativeLayout rl_back_arrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        appBarLayout = findViewById(R.id.appBarLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 0.1f));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }

        rv1 = findViewById(R.id.rv1);
        rl_back_arrow = findViewById(R.id.rl_back_arrow);
        arrayList_get = new ArrayList<>();
        arrayList_get = getIntent().getParcelableArrayListExtra("array");
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(UsersListActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv1.setLayoutManager(linearLayoutManager1);
        initialLoading(arrayList_get);

        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void initialLoading(final ArrayList<Users> arrayListLoader) {

        arrayList = new ArrayList<>();

        if (!(arrayListLoader.size() == 0)) {
            if (arrayListLoader.size() > 4) {
                for (int a = 0; a < 5; a++) {
                    Users invoiceDetailsModel = arrayListLoader.get(a);
                    arrayList.add(invoiceDetailsModel);
                }
            } else {
                for (int a = 0; a < arrayListLoader.size(); a++) {
                    Users invoiceDetailsModel = arrayListLoader.get(a);
                    arrayList.add(invoiceDetailsModel);
                }
            }
        }
        Log.e("arrayList size", "--------------------------------------------------" + arrayList.size());
        mUserAdapter = new UserAdapter();
        rv1.setAdapter(mUserAdapter);// set adapter on recyclerview
        mUserAdapter.notifyDataSetChanged();
        mUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("haint", "Load More");
                arrayList.add(null);
                mUserAdapter.notifyItemInserted(arrayList.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("haint", "Load More 2");


                        //Remove loading item
                        arrayList.remove(arrayList.size() - 1);
                        mUserAdapter.notifyItemRemoved(arrayList.size());

                        //Load data
                        int index = arrayList.size();
                        int end = index + 5;
                        if (arrayListLoader.size() > index) {

                            if (end > arrayListLoader.size()) {
                                end = arrayListLoader.size();
                            }

                            for (int i = index; i < end; i++) {
                                Users invoiceDetailsModel = arrayListLoader.get(i);
                                arrayList.add(invoiceDetailsModel);
                            }
                            mUserAdapter.notifyDataSetChanged();
                            mUserAdapter.setLoaded();
                        }
                    }
                }, 4000);
            }
        });

    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;

        UserAdapter() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv1.getLayoutManager();
            rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return arrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return arrayList == null ? 0 : arrayList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(UsersListActivity.this).inflate(R.layout.row_item_members, parent, false);
                return new ItemHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(UsersListActivity.this).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof ItemHolder) {
                ItemHolder itemHolder = (ItemHolder) holder;

                Users usersModel = arrayList.get(position);
                itemHolder.tv_name.setText(usersModel.getName());
                itemHolder.tv_desg.setText(usersModel.getDesg());
                itemHolder.tv_email.setText(usersModel.getMail());

            }


        }

        public void setLoaded() {
            isLoading = false;
        }
    }

    public class ItemHolder extends ViewHolder implements View.OnClickListener {

        TextView tv_name, tv_email, tv_desg;

        ItemHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv_name);
            tv_email = v.findViewById(R.id.tv_email);
            tv_desg = v.findViewById(R.id.tv_desg);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
