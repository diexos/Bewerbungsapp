package com.example.sbaar.bewerbungsapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.webkit.WebView




class ApplyFragment : Fragment() {

    val TAG = "ApplyFragment"

    override fun onAttach(context: Context?) {
        Log.d(TAG,"onAttach")
        super.onAttach(context)
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)


    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCrateView")

        val v = inflater.inflate(R.layout.apply, container, false)
        var mWebView: WebView =  v.findViewById(R.id.wV_Info)
        mWebView.loadUrl(getString(R.string.website_url))

        // Enable Javascript
        val webSettings = mWebView.getSettings()

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(WebViewClient())

        return v



    }




}
