package com.example.pabji.siftapplication.description;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


    public static WebviewFragment newInstance(String name) {
        WebviewFragment webviewFragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString("description", name);
        webviewFragment.setArguments(args);
        return webviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        String url = "";

        if (getArguments() != null) {
            url = (String) getArguments().get("description");
        }
        setWeb(view, url);
        return view;
    }

    public void setWeb(View view, String url) {
        WebView webView = (WebView) view.findViewById(R.id.wv_description);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}
