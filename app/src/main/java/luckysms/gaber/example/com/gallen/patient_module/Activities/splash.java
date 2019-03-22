package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Locale;

import luckysms.gaber.example.com.gallen.R;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        check_forall_permissions();

    }
    private void check_forall_permissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            change_language();
                            next_activity();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }


                })
                .onSameThread()
                .check();
    }
    private void next_activity(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getSharedPreferences("personal_data",MODE_PRIVATE).getBoolean("state",false)){
                    Intent main=new Intent(splash.this,patient_main_screen.class);
                    startActivity(main);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                }else {
                    Intent mobile_authentication=new Intent(splash.this,patient_start_screen.class);
                    startActivity(mobile_authentication);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }

            }
        }, 2500);
    }
    @SuppressLint("NewApi")
    private void change_language(){
        if (getSharedPreferences("personal_data",Context.MODE_PRIVATE).getString("language","").equals("")) {
            Resources res = getResources();
// Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale(Locale.getDefault().getLanguage())); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
            res.updateConfiguration(conf, dm);
            Log.w("sa;ksdakas",Locale.getDefault().getLanguage());
            getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                    .putString("language",Locale.getDefault().getLanguage())
                    .commit();
        }
    }
}
