package com.githubapp.ui.detail;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.githubapp.R;
import com.githubapp.data.RestClient;
import com.githubapp.data.model.Item;
import com.githubapp.data.model.Users;
import com.githubapp.ui.list.ListAdapter;
import com.githubapp.util.ImageUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private String name;
    private String TAG = "DetailActivity";
    private TextView userName, followerUrl, gistsUrl, starredUrl, reposUrl;
    private ImageView userImage;
    private String shareUrl = "";
    private String shareTitle = "";
    private FloatingActionButton shareButton;
    private ProgressBar spinner;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        getBundleData();
        setupToolbar();
        initializeViews();
        getUserByName();
        shareProfile();
    }

    public void setupToolbar(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initializeViews(){
        linearLayout = (LinearLayout) findViewById(R.id.main_content);
        spinner = (ProgressBar) findViewById(R.id.progress_bar);
        shareButton = (FloatingActionButton)  findViewById(R.id.share_button);
        userImage = (ImageView) findViewById(R.id.user_image);
        userName = (TextView) findViewById(R.id.name_of_user);
        followerUrl = (TextView) findViewById(R.id.follower_url);
        gistsUrl = (TextView) findViewById(R.id.gists_url);
        starredUrl = (TextView) findViewById(R.id.starred_url);
        reposUrl = (TextView) findViewById(R.id.repos_url);
    }

    public void getBundleData(){
        name = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
        }
    }

    public void getUserByName(){
        showProgress();
        RestClient.getInstance().getUserByName(name).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                hideProgress();
                if(response.code() == 200) {
                    Users users = response.body();
                    List<Item> items = users.getItems();
                    setDetails(items);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                hideProgress();
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void setDetails(List<Item> items){
        ImageUtil.displayImage(getApplicationContext(), items.get(0).getAvatarUrl(), userImage);
        userName.setText(items.get(0).getLogin());
        followerUrl.setText(getString(R.string.follower_url) + " " + items.get(0).getFollowersUrl());
        gistsUrl.setText(getString(R.string.gists_url) + " " + items.get(0).getGistsUrl());
        starredUrl.setText(getString(R.string.starred_url) + " " + items.get(0).getStarredUrl());
        reposUrl.setText(getString(R.string.repos_url) + " " + items.get(0).getReposUrl());
        shareUrl = items.get(0).getHtmlUrl();
        shareTitle = items.get(0).getLogin();
    }

    public void shareProfile(){
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareUrl != "") {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = shareUrl;
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareTitle);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            }
        });

    }

    public void showProgress(){
        spinner.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
    }

    public void hideProgress(){
        spinner.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
