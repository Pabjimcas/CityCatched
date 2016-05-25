package com.example.pabji.siftapplication.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pabji.siftapplication.R;

/**
 * Created by Daniel on 24/5/16.
 */
public class WebviewFragment extends Fragment {

    private WebView webView;
    private FloatingActionButton floatingActionButton;
    private String url = "";
    private String latitude = "";
    private String longitude = "";

    public static WebviewFragment newInstance(String name, String latitude, String longitude) {
        WebviewFragment webviewFragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString("description", name);
        args.putString("latitude", latitude);
        args.putString("longitude", longitude);
        webviewFragment.setArguments(args);
        return webviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        getUIControls(view);
        if (getArguments() != null) {
            url = (String) getArguments().get("description");
            latitude = (String) getArguments().get("latitude");
            longitude = (String) getArguments().get("longitude");
        }
        setWeb(view, url);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q=search/monumentos+de+salamanca"));
                startActivity(intent);
            }
        });
        return view;
    }

    public void setWeb(View view, String url) {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }


    private void getUIControls(View view){
        webView = (WebView) view.findViewById(R.id.wv_description);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
    }

}
