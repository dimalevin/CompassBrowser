package levin.dima.compassbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fav_page extends AppCompatActivity {

    private ImageButton[] buttons = new ImageButton[FavClass.FAVS];
    private TextView[] labels = new TextView[FavClass.FAVS];
    private FavClass favs;
    private SharedPreferences shrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shared preferences
        shrd = getSharedPreferences("appsetts", Context.MODE_PRIVATE);

        //buttons
        Button btnClrAll = (Button) findViewById(R.id.favBtnClearAll);
        Button btnHidAll = (Button) findViewById(R.id.favBtnHideAll);

        //image buttons
        buttons[0] = (ImageButton) findViewById(R.id.fav1Btn);
        buttons[1] = (ImageButton) findViewById(R.id.fav2Btn);
        buttons[2] = (ImageButton) findViewById(R.id.fav3Btn);
        buttons[3] = (ImageButton) findViewById(R.id.fav4Btn);

        //labels
        labels[0] = (TextView) findViewById(R.id.fav1Text);
        labels[1] = (TextView) findViewById(R.id.fav2Text);
        labels[2] = (TextView) findViewById(R.id.fav3Text);
        labels[3] = (TextView) findViewById(R.id.fav4Text);


        //favorites handler
        favs = new FavClass(buttons, labels, shrd);
        favs.loadSetts();

        //ON-CLIKS

        //clear all
        btnClrAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favs.clearAll();
            }
        });

        //hide all
        btnHidAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favs.hideAll();
            }
        });


        //fav1
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(0);
            }
        });

        //fav2
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(1);
            }
        });

        //fav3
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(2);
            }
        });

        //fav4
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(3);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //back button pressed
        favs.saveSetts();
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
            startActivity(new Intent(Fav_page.this, Help.class));
            return true;
        } else if (id == R.id.itemAbout) {
            //about
            startActivity(new Intent(Fav_page.this, About.class));
            return true;
        } else if (id == R.id.itemExit) {
            favs.saveSetts();
            this.finishAffinity();  //close all
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //METHODS

    //set fav
    public void setFav(int ind) {

        //if url not empty
        if (FavClass.userUrl != null && !FavClass.userUrl.isEmpty()) {
            favs.setOneFav(ind);
            Toast.makeText(getApplicationContext(), labels[ind].getText().toString() + " is set", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "empty url", Toast.LENGTH_SHORT).show();

    }

    //show alert
    public void showAlert(final int ind){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("What to do?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "LOAD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String url = FavClass.pointer.getUrl(ind);
                        //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                        //if url is not empty
                        if(!url.isEmpty()){
                            FavClass.vw.loadUrl(url);       //load url
                            FavClass.edTxt.setText(url);    //set url field
                            onBackPressed();    //"back button"
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "url empty", Toast.LENGTH_SHORT).show();
                            //dialog.cancel();
                        }

                        dialog.cancel();    //close window

                    }
                });

        builder1.setNegativeButton(
                "SET",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setFav(ind);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
