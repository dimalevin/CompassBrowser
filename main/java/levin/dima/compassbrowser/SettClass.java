package levin.dima.compassbrowser;

import android.content.SharedPreferences;
import android.webkit.WebView;

public class SettClass {

    static SettClass pointer;

    private WebView webView;
    private boolean opt_vibrate;    //vibrate flag
    private boolean opt_js;         //javascript flag
    private SharedPreferences shrd;

    //CONSTR.
    public SettClass(SharedPreferences shrd) {

        this.shrd = shrd;
        loadSetts();        //load settings

        pointer = this; //ugly-smart solution for not to use Serializable interface
    }

    //SET
    public void setOpt_vibrate(boolean opt_vibrate) {
        this.opt_vibrate = opt_vibrate;
    }

    public void setOpt_js(boolean opt_js) {
        this.opt_js = opt_js;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    //GET
    public boolean isOpt_vibrate() {
        return opt_vibrate;
    }

    public boolean isOpt_js() {
        return opt_js;
    }

    //METHODS

    //clear cache
    public void clearCache(){
        webView.clearCache(true);
    }

    //clear history
    public void clearHistory(){
        webView.clearHistory();
    }

    //clear all
    public void clearAll(){
        //webview
        clearCache();
        clearHistory();

        //shared pref.
        SharedPreferences.Editor editor = shrd.edit();
        editor.clear();
        editor.apply();

        //load settings
        loadSetts();
    }

    //set javascript
    public void setJs(){
        webView.getSettings().setJavaScriptEnabled(opt_js);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(opt_js);
    }

    //save settings
    public void saveSettings(){
        SharedPreferences.Editor editor = shrd.edit();

        //save states
        editor.putBoolean("vibrate", opt_vibrate);
        editor.putBoolean("js", opt_js);
        editor.apply(); //imm. write to ram
    }

    //load settings
    public void loadSetts(){
        opt_vibrate = shrd.getBoolean("vibrate", false);
        opt_js = shrd.getBoolean("js", false);
    }

    //apply settings
    public void applySettings(){
        setJs();
    }
}
