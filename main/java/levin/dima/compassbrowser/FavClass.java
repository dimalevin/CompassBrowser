package levin.dima.compassbrowser;

import android.content.SharedPreferences;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class FavClass {

    //pointers
    public static WebView vw;
    public static EditText edTxt;
    public static FavClass pointer;
    public static String userUrl;

    //finals
    public static final int FAVS = 4;
    private static final int DEFAULT_IMG = R.drawable.favorites_default;
    private static final int BLANK_IMG = R.drawable.favorites_shade;

    private SharedPreferences shrd;
    private String[] url;
    private String[] label;
    private ImageButton[] favButtons;
    private TextView[] btnText;

    //CONSTR.
    public FavClass(ImageButton[] buttons, TextView[] btnText, SharedPreferences shrd){

        url = new String[FAVS];
        label = new String[FAVS];
        this.favButtons = buttons;
        this.btnText = btnText;
        this.shrd = shrd;
        pointer = this;
    }

    //SET

    public void setIcon(ImageButton favButton, int elem) {
        favButton.setBackgroundResource(elem);
    }

    public void setUrl(int ind, String url) {
        this.url[ind] = url;
    }

    public void setFavButton(int ind ,ImageButton favButton) {
        this.favButtons[ind] = favButton;
    }

    //GET
    public String getUrl(int ind){
        return url[ind];
    }

    //METHODS

    //hide all
    public void hideAll(){

        for(int i=0; i<FAVS; i++){
            //if favorite is set
            if(url[i]!= null && !url[i].isEmpty() && !url[i].equals("empty")) {
                label[i] = "private";
                btnText[i].setText(label[i]);
            }
        }
    }

    //clear all
    public void clearAll(){
        for(int i=0; i<FAVS; i++){
            setIcon(favButtons[i], BLANK_IMG);  //icon

            //label
            label[i] = "empty";
            btnText[i].setText(label[i]);

            //url's
            url[i] = "";
        }
    }

    //set one favorite
    public void setOneFav(int ind) {

            if(!userUrl.isEmpty()){

                //icon
                setIcon(favButtons[ind], DEFAULT_IMG);

                //url
                url[ind] = userUrl;

                //label
                label[ind] = makeLabelFromUrl();
                btnText[ind].setText(label[ind]);
            }
    }

    //save settings
    public void saveSetts(){
        SharedPreferences.Editor editor = shrd.edit();

        //save states
        for (int i=0; i<FAVS; i++) {

            //url
            if(url[i] != null)
                editor.putString("favUrl"+i, url[i]);
            else
                editor.putString("favUrl"+i, "");

            //label
            if(label[i] != null)
                editor.putString("favLabel"+i, label[i]);
        }

        editor.apply(); //imm. write to ram
    }

    //load settings
    public void loadSetts(){
        //load states
        for (int i=0; i<FAVS; i++) {

            //url
            url[i] = shrd.getString("favUrl"+i, "");

            //label
            btnText[i].setText(shrd.getString("favLabel"+i, "empty"));

            //icon
            if(!url[i].isEmpty())
                setIcon(favButtons[i], DEFAULT_IMG);
            else
                setIcon(favButtons[i], BLANK_IMG);
        }
    }

    //set label from url
    private String makeLabelFromUrl() {
        return userUrl.substring(12, userUrl.indexOf('/', 10));
    }
}
