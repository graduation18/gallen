package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;

public class patient_start_screen extends AppCompatActivity {
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private Button sign_in,sign_up,not_now;
    private ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_screen);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_up = (Button) findViewById(R.id.sign_up);
        not_now = (Button) findViewById(R.id.not_now);
        splash=(ImageView)findViewById(R.id.splash);
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        sign_in.startAnimation(bounce);
        sign_up.startAnimation(bounce);
        not_now.startAnimation(bounce);
        splash.startAnimation(fadein);



    }

    public void sign_in(View view) {
        Intent sign_in=new Intent(this,patient_login.class);
        startActivity(sign_in);
    }

    public void sign_up(View view) {
        Intent sign_up=new Intent(this,patient_sign_up.class);
        mprogressBar.setVisibility(View.VISIBLE);
        startActivity(sign_up);
    }

    public void not_now(View view) {
        mprogressBar.setVisibility(View.VISIBLE);
        Intent not_now=new Intent(patient_start_screen.this,patient_main_screen.class);
        not_now.putExtra("visitor",true);
        startActivity(not_now);

    }

}
