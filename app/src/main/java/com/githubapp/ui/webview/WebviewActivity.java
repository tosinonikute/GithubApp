package com.githubapp.ui.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.githubapp.R;

/**
 * @author Tosin Onikute.
 */

public class WebviewActivity extends AppCompatActivity {

    public String EXTRA_URL = "";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getStringExtra(EXTRA_URL);
        spinner = (ProgressBar) findViewById(R.id.progress_bar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("EXTRA_URL");
        }

        if(url != null) {
            loadWebView(url);
        }

    }

    public void loadWebView(String url){

        spinner.setVisibility(View.VISIBLE);
        setTitle(url);
        final WebView webview= (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                displayLongToast(getApplicationContext(), description);
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }
            public void onPageFinished(WebView view, String url) {
                spinner.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }
        });
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);

    }

    public void displayLongToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
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
