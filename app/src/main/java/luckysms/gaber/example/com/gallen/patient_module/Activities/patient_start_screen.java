package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_screen);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    public void sign_in(View view) {
        Intent sign_in=new Intent(this,patient_login.class);
        startActivity(sign_in);
    }

    public void sign_up(View view) {
        Intent sign_up=new Intent(this,patient_sign_up.class);
        mprogressBar.setVisibility(View.VISIBLE);
        login("13322","h1",sign_up);
    }

    public void not_now(View view) {
        mprogressBar.setVisibility(View.VISIBLE);
        Intent not_now=new Intent(patient_start_screen.this,patient_main_screen.class);
        not_now.putExtra("visitor",true);
        login("13322","h1",not_now);
    }


    private void login(final String mobile_number_email_address_s, final String password_s, final Intent not_now)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/user/login";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                .putString("accessToken",res.getString("accessToken"))
                                .commit();
                        mprogressBar.setVisibility(View.INVISIBLE);

                        startActivity(not_now);


                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w("dsnnasdjlas",error);
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("email", mobile_number_email_address_s);
                    pars.put("password", password_s);
                    return pars;
                }
            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

}
