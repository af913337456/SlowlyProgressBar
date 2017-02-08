# SlowlyProgressBar

###功能(Function)
<pre>
&emsp;&emsp;1.模仿微信网页加载时，顶部进度条的效果。
&emsp;&emsp;&emsp;第一种方法是移动布局；
&emsp;&emsp;&emsp;第二种方法是采用动画；
</pre>

###怎样使用(How to use)

>使用方法</br>

```java

public class MainActivity extends AppCompatActivity {

    private SlowlyProgressBar slowlyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        
        /** 第二种动画模式，建议使用更加平滑，，another */
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
        /** 销毁 */
        if(slowlyProgressBar!=null){
            slowlyProgressBar.destroy();
            slowlyProgressBar = null;
        }
    }
}

```

