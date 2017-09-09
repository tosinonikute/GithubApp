package com.githubapp.ui.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.githubapp.GitHubService;
import com.githubapp.R;
import com.githubapp.data.RestClient;
import com.githubapp.data.model.Item;
import com.githubapp.data.model.Users;
import com.githubapp.ui.webview.WebviewActivity;
import com.githubapp.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ProgressBar spinner;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        if(NetworkUtil.isConnected(getApplicationContext())) {
            loadUsers();
            hideOfflineSnackBar();
        } else {
            displayOfflineSnackbar();
        }

    }

    public void initializeViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        spinner =  (ProgressBar) findViewById(R.id.progress_bar);
    }


    public void loadUsers(){
        showProgress();
        RestClient.getInstance().listUsers().enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                hideProgress();
                if(response.code() == 200) {
                    Users users = response.body();
                    Log.d(TAG, users.getTotalCount().toString());
                    ArrayList<Item> items = users.getItems();
                    adapter = new ListAdapter(getApplicationContext(), items);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                hideProgress();
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void showProgress(){
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        spinner.setVisibility(View.GONE);
    }


    public void displayOfflineSnackbar(){
        snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.checkinternet), Snackbar.LENGTH_INDEFINITE);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUsers();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public void hideOfflineSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }




}
