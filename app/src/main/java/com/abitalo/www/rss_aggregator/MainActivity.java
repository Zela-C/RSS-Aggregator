package com.abitalo.www.rss_aggregator;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.abitalo.www.rss_aggregator.constants.Conf;
import com.abitalo.www.rss_aggregator.view.NavDiscoveryView;
import com.abitalo.www.rss_aggregator.view.RSSListView;
import com.abitalo.www.rss_aggregator.view.WelcomeNav;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    //用于作为参数传给子线程，子线程使用该handler异步更新主线程UI或向主线程传送消息
    Handler handler = new Handler();
    //维护一个fragment回退栈，栈顶显示到content_main中
    //具体的UI都写在单独的Fragment里面
    FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        //SDK 初始化
        Bmob.initialize(this, Conf.APP_ID);

        fragmentManager = getSupportFragmentManager();
        initialView();

        /*使用维护的栈来更新content,事务要加入回退栈
        fragmentManager.beginTransaction().add(R.id.fragment_content,new Fragment(),"tag").addToBackStack(null).commit();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("tag")).addToBackStack(null).commit();
        */
    }

    void initialView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager.beginTransaction().add(R.id.nav_account, new WelcomeNav(), "account_nav").commit();
        fragmentManager.beginTransaction().add(R.id.discovery_nav_view, new NavDiscoveryView(), "discovery_view").commit();

//        fragmentManager.beginTransaction().add(R.id.fragment_content, new RSSListView(), "fragment_view").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dragger_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
