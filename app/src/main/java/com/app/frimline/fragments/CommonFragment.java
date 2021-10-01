package com.app.frimline.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.app.frimline.Common.CONSTANT;
import com.app.frimline.R;
import com.app.frimline.databinding.FragmentCommonBinding;

import java.util.Observable;


public class CommonFragment extends BaseFragment {

    private FragmentCommonBinding binding;

    public CommonFragment() {
    }

    public CommonFragment(String targetURL) {
        this.targetURL = targetURL;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common, parent, false);
        loadData();
        return binding.getRoot();
    }

    String targetURL = "";

    @SuppressLint("SetTextI18n")
    public void setTitle(String targetURL) {
        this.targetURL = targetURL;

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadData() {

        binding.staticPagesWebView.setWebChromeClient(new MyWebChromeClient());
        binding.staticPagesWebView.setWebViewClient(new webClient());
        binding.staticPagesWebView.getSettings().setLoadWithOverviewMode(true);
        binding.staticPagesWebView.getSettings().setSupportZoom(true);
        binding.staticPagesWebView.getSettings().setJavaScriptEnabled(true);

        String url = "";
        if (targetURL.equalsIgnoreCase("about_us")) {
            url = CONSTANT.ABOUT_US;
        }
        if (targetURL.equalsIgnoreCase("shipping_policy")) {
            url = CONSTANT.SHIPPING_POLICY;
        }
        if (targetURL.equalsIgnoreCase("privacy_policy")) {
            url = CONSTANT.PRIVACY_POLICY;
        }
        if (targetURL.equalsIgnoreCase("contact_us")) {
            url = CONSTANT.CONTACT_US;
        }


        binding.staticPagesWebView.loadUrl(url);
        binding.status.setVisibility(View.GONE);
        binding.screenLoader.getIndeterminateDrawable().setColorFilter(Color.parseColor(pref.getThemeColor()), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.screenLoader.setProgressTintList(ColorStateList.valueOf(Color.parseColor(pref.getThemeColor())));
    }

    public class MyWebChromeClient extends WebChromeClient {

        public void onProgressChanged(WebView view, int newProgress) {

            binding.screenLoader.setVisibility(View.VISIBLE);
            binding.staticPagesWebView.setVisibility(View.GONE);
            binding.screenLoader.setProgress(newProgress);


        }
    }

    public class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.screenLoader.setVisibility(View.GONE);

            binding.staticPagesWebView.setVisibility(View.VISIBLE);
            binding.staticPagesWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
    }
}