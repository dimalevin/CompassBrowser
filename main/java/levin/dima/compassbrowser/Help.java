package levin.dima.compassbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_window);

        Button button = findViewById(R.id.help_ExitButton);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });

        TextView helpTxt =  findViewById(R.id.helpText);
        helpTxt.setMovementMethod(new ScrollingMovementMethod());

        //window resize
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);  //get screen measure

        //set new size
        getWindow().setLayout((int)(dm.widthPixels*.8), (int)(dm.heightPixels*.7));
    }
}
