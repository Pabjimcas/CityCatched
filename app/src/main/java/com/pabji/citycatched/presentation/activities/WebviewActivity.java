package com.pabji.citycatched.presentation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pabji.citycatched.R;
import com.pabji.citycatched.presentation.activities.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Jiménez Casado on 04/12/2016.
 */

public class WebviewActivity extends BaseActivity {

    @BindView(R.id.wb_page)
    WebView webView;

    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("buildingUrl");
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, WebviewActivity.class);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
