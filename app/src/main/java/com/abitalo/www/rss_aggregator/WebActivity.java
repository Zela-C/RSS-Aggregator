package com.abitalo.www.rss_aggregator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WebActivity extends AppCompatActivity {
    private boolean isStarred = false;
    private WebView webView = null;
    private Toolbar toolbar = null;
    private TextView title = null;
    private TextView time = null;
    private TextView author = null;
    private Date pubDate= null;
    private Date currentDate = null;
    private String url = null;
    private final DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_rss_detail);
        init_view();
    }

    private void init_view(){
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        webView = (WebView) findViewById(R.id.web_view);
        toolbar = (Toolbar) findViewById(R.id.web_tool_bar);
        author = (TextView) findViewById(R.id.author);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        HashMap item = (HashMap) intent.getSerializableExtra("item");

        url = item.get("url").toString();
        title.setText(item.get("title").toString());
        time.setText(item.get("date").toString());

        String description = (String) item.get("description");
        String encoded = (String) item.get("encoded");
        String authorstr = (String) item.get("author");

        if(null != authorstr){
            authorstr=authorstr.toString();
            author.setText("by "+authorstr);
        }
        else {
            author.setText("");
        }

        if(null != encoded)
            showWebView(encoded);
        else
            showWebView(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rss_web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (android.R.id.home == menuItem.getItemId()) {
            finish();
        }
        else if (R.id.essay_save == menuItem.getItemId()) {
            if(isStarred) {
                toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_menu_star_border);
                isStarred=false;
            }
            else{
                toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_menu_star);
                isStarred=true;
            }
        }
        else if(R.id.essay_broser == menuItem.getItemId()){
            if(null !=url) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            else {
                Snackbar.make(webView, "No Link For this essay.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showWebView(String html)
    {
        String CSS_STYLE ="<style>* {color:#808080;max-width: 100% !important;word-wrap:break-word !important;} " +
                                    "img {display: inline !important; height: auto !important; max-width: 100% !important;border-radius :6px;box-shadow: 0px 1px 3px rgba(0,0,0,0.12), 0px 1px 2px rgba(0,0,0,0.24);} " +
                                    "a {display:inline-block !important; height: auto !important;max-width: 100% !important; word-wrap:break-word !important;" +
                                    "table{ width:100%25 !important; word-wrap:break-word !important;} " +
                                    "pre{display:block !important; height: auto !important;max-width: 100% !important; word-wrap:break-word !important;}" +
                                    "td{max-width: 100% !important; word-wrap:break-word !important;</style>";
        webView.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        webView.getSettings();
        // 设置WevView要显示的网页
        webView.loadDataWithBaseURL(null, CSS_STYLE+html, "text/html", "utf-8",
                null);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
    }

}
