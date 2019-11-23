package levin.dima.compassbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SettClass settings;
    Button buttonCache, buttonHist, buttonAll;      //buttons
    Switch swVibro, swJs;                           //switches

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //settings class
        settings = SettClass.pointer;

        //buttons
        buttonCache = (Button) findViewById(R.id.btnSettClearCache);
        buttonHist = (Button) findViewById(R.id.btnSettClearHistory);
        buttonAll = (Button) findViewById(R.id.btnSettClearAll);

        //switches
        swVibro = (Switch) findViewById(R.id.swSettVibro);
        swJs = (Switch) findViewById(R.id.swSettJs);

        //set buttons state
        loadButtonsState();

        //ON-CLICKS

        //cache button
        buttonCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.clearCache();
                Toast.makeText(getApplicationContext(),"cache cleaned",Toast.LENGTH_SHORT).show();
            }
        });

        //history button
        buttonHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.clearHistory();
                Toast.makeText(getApplicationContext(),"history cleaned",Toast.LENGTH_SHORT).show();
            }
        });

        //all button
        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.clearAll();
                loadButtonsState();     //set buttons state
                Toast.makeText(getApplicationContext(),"data cleaned",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveButtonsState();
        settings.applySettings();   //apply first (if error occurs -won't save)
        settings.saveSettings();
        super.onBackPressed();
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
            startActivity(new Intent(SettingsActivity.this, Help.class));
            return true;
        }

        else if (id == R.id.itemAbout) {
            //about
            startActivity(new Intent(SettingsActivity.this, About.class));
            return true;
        }

        else if (id == R.id.itemExit) {
            saveButtonsState();
            settings.applySettings();   //apply first (if error occurs -won't save)
            settings.saveSettings();
            this.finishAffinity();  //close all
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //load state of a buttons
    private void loadButtonsState(){
        swVibro.setChecked(settings.isOpt_vibrate());
        swJs.setChecked(settings.isOpt_js());
    }

    //save buttons state
    private void saveButtonsState(){
        settings.setOpt_vibrate(swVibro.isChecked());
        settings.setOpt_js(swJs.isChecked());
    }
}
