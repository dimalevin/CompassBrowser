package levin.dima.compassbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private EditText urlField;
    private Vibrator vibro;
    private SettClass settings;
    private SharedPreferences shrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);   //make icons colorful

        //buttons
        Button buttonBack = (Button) findViewById(R.id.btnBack);
        Button buttonForw = (Button) findViewById(R.id.btnForw);

        //shared preferences
        shrd = getSharedPreferences("appsetts", Context.MODE_PRIVATE);

        //settings handler class
        settings = new SettClass(shrd);


        //browser
        webView = (WebView) findViewById(R.id.browserWindow);
        settings.setWebView(webView);   //connect to settings
        webView.setWebViewClient(new WebViewClient());  //don't use ext. browser

        //webView pointer
        FavClass.vw = webView;

        //visual settings
        webView.getSettings().setLoadWithOverviewMode(true);    //loads the WebView completely zoomed out
        webView.getSettings().setUseWideViewPort(true);         //makes the Webview have a normal viewport
        webView.setBackgroundColor(Color.WHITE);

        //javaScript options
        settings.setJs();

        //url field
        urlField = (EditText) findViewById(R.id.urlField);

        FavClass.edTxt = urlField;  //pointer

        //onCLicks - what happens/occurs when a user clicks a button

        //enter keyboard button
        urlField.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the key event is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    //check connection
                    if (checkInternet()) {

                        String url = urlField.getText().toString();     //get url

                        //check url
                        if (checkUrl(url))
                            webView.loadUrl("https://" + url);  //load url
                        else {
                            vibrate(100);   //vibrate
                            Toast.makeText(getApplicationContext(), "wrong url", Toast.LENGTH_SHORT).show();
                        }

                    } else
                        noConnection();

                    return true;
                }
                return false;
            }
        });

        //back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    urlField.setText(webView.getOriginalUrl());
                } else {
                    webView.loadUrl("about:blank");
                    urlField.setText("");
                    vibrate(100);   //vibrate
                    Toast.makeText(getApplicationContext(), "no more pages", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //forward button
        buttonForw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                    urlField.setText(webView.getOriginalUrl());
                } else {
                    vibrate(100);   //vibrate
                    Toast.makeText(getApplicationContext(), "no more pages", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Get instance of Vibrator from current Context
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemHelp) {
            //help
            startActivity(new Intent(MainActivity.this, Help.class));
            return true;
        } else if (id == R.id.itemAbout) {
            //about
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        } else if (id == R.id.itemExit) {
            this.finishAffinity();  //close all
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browser) {
            //
        } else if (id == R.id.nav_favorites) {
            //FavClass.userUrl = urlField.getText().toString();
            FavClass.userUrl = webView.getOriginalUrl();
            startActivity(new Intent(MainActivity.this, Fav_page.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {
            //share function
            doShare();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //share
    public void doShare() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_info);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    //check network status
    public boolean checkInternet() {

        //instantiate an object
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //get all networks information
        NetworkInfo networkInfo[] = cm.getAllNetworkInfo();

        //checking internet connectivity
        for (int i = 0; i < networkInfo.length; ++i) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    //when no connection
    public void noConnection() {
        webView.loadUrl("file:///android_asset/no_internet.png");     //display error image

        //vibrate
        vibrate(300);
        Toast.makeText(getApplicationContext(), "Internet Not Connected!", Toast.LENGTH_LONG).show();
    }

    //vibrate function
    public void vibrate(int ms) {
        if (settings.isOpt_vibrate())
            vibro.vibrate(ms);     //vibrate
    }

    //check url
    public boolean checkUrl(String url) {
        return url.matches("[wwwWWW.][a-zA-Z0-9].*");
    }

}
