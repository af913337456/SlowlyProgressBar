package com.slowlyprogressbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private SlowlyProgressBar slowlyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        WebView webView = (WebView) findViewById(R.id.webview);

        /** 第一种，first solution */
//        slowlyProgressBar =
//                new SlowlyProgressBar
//                        (
//                                findViewById(R.id.p),
//                                getWindowManager().getDefaultDisplay().getWidth()
//                        )
//                .setViewHeight(3);
//
//        webView.setWebChromeClient(new WebChromeClient(){
//
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                slowlyProgressBar.setProgress(newProgress);
//            }
//
//        });
        /** 第二种动画模式，another solution */
        findViewById(R.id.p).setVisibility(View.GONE);
        slowlyProgressBar =
                new SlowlyProgressBar
                        (
                                (ProgressBar) findViewById(R.id.ProgressBar)
                        );
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                slowlyProgressBar.onProgressStart();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                slowlyProgressBar.onProgressChange(newProgress);
            }
        });

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
