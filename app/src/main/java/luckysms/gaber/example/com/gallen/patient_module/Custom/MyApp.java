package luckysms.gaber.example.com.gallen.patient_module.Custom;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

public class MyApp extends Application {

    private static MyApp _instance;

    public static MyApp get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }



}