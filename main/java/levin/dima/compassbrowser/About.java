package levin.dima.compassbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_window);

        Button button = findViewById(R.id.about_okButton);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        //window resize
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  //get screen measure

        //set new size
        getWindow().setLayout((int)(dm.widthPixels*.7), (int)(dm.heightPixels*.5));
    }
}
