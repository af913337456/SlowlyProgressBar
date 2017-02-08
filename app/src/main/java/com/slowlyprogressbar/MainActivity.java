package com.slowlyprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private SlowlyProgressBar slowlyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /** 第一种，first solution */
        slowlyProgressBar =
                new SlowlyProgressBar
                        (
                                findViewById(R.id.p),
                                getWindowManager().getDefaultDisplay().getWidth()
                        )
                .setViewHeight(3);

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                slowlyProgressBar.setProgress(newProgress);
            }

        });
        /** 第二种动画模式，another */
        /**
        slowlyProgressBar =
                new SlowlyProgressBar
                        (
                                findViewById(R.id.p)
                        );
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                slowlyProgressBar.onProgressStart();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                slowlyProgressBar.onProgressChange(newProgress);
            }
        });
        */
        webView.loadUrl("http://www.cnblogs.com/linguanh");
    }

    @Override
    public void finish() {
        super.finish();
        if(slowlyProgressBar!=null){
            slowlyProgressBar.destroy();
            slowlyProgressBar = null;
        }
    }
}
