package com.example.pabji.siftapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pabji.siftapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebFragment extends Fragment {

    @Bind(R.id.web_view_item)
    WebView webView;

    public static WebFragment newInstance(String url) {
        WebFragment webFragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        webFragment.setArguments(args);
        return webFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_web, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        String url = "";

        if (getArguments() != null) {
            url = (String) getArguments().get("url");
        }
        setWeb(url);
        super.onViewCreated(view, savedInstanceState);
    }

    public void setWeb(String url) {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}