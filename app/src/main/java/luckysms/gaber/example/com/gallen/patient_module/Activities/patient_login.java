package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;

public class patient_login extends AppCompatActivity {
    private EditText mobile_number_email_address,password;
    private Button sign_in,see_password;
    private boolean seen;
    private RequestQueue queue;
    private ProgressWindow progressWindow ;
    private TextView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        progressConfigurations();
        mobile_number_email_address=(EditText)findViewById(R.id.mobile_number_email_address);
        password=(EditText)findViewById(R.id.password);
        see_password=(Button)findViewById(R.id.see_password);
        sign_in=(Button)findViewById(R.id.sign_in);
        back=(TextView)findViewById(R.id.back);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_number_email_address_s=mobile_number_email_address.getText().toString();
                String password_s=password.getText().toString();
                if (mobile_number_email_address_s.length()>0&&password_s.length()>0){
                    login(mobile_number_email_address_s,password_s);
                }else {
                    Toast.makeText(patient_login.this,getResources().getString(R.string.login_error),Toast.LENGTH_LONG).show();
                }


            }
        });
        see_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seen){
                    seen=false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }else {
                    seen=true;
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    password.setSelection(password.getText().length());
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patient_start_screen=new Intent(patient_login.this,patient_start_screen.class);
                startActivity(patient_start_screen);
                finish();
            }
        });
    }
    public void forget_password(View view) {
        Intent forget_password=new Intent(this,patient_forget_password.class);
        startActivity(forget_password);
    }
    private void login(final String mobile_number_email_address_s, final String password_s)
    {


        try {
            String url = "http://microtec1.egytag.com/api/user/login";
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
                        if (res.has("error")) {
                            if (res.getString("error").equals("email not set")){
                                Toast.makeText(patient_login.this,getResources().getString(R.string.email_not_set),Toast.LENGTH_LONG).show();

                            }else if (res.getString("error").equals("email or password error")){
                                Toast.makeText(patient_login.this,getResources().getString(R.string.email_or_password_error),Toast.LENGTH_LONG).show();

                            }

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject user = res.getJSONObject("user");
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("_id", user.getString("_id"))
                                        .putString("email", user.getString("email"))
                                        .putString("password", password_s)
                                        .putBoolean("state",true)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .commit();
                                Intent not_now=new Intent(patient_login.this,patient_main_screen.class);
                                startActivity(not_now);
                                finish();
                                Toast.makeText(patient_login.this,getResources().getString(R.string.welcome),Toast.LENGTH_LONG).show();

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
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
    private void progressConfigurations(){
        progressWindow = ProgressWindow.getInstance(this);
        ProgressWindowConfiguration progressWindowConfiguration = new ProgressWindowConfiguration();
        progressWindowConfiguration.backgroundColor = Color.parseColor("#32000000") ;
        progressWindowConfiguration.progressColor = Color.WHITE ;
        progressWindow.setConfiguration(progressWindowConfiguration);
    }
    public void showProgress(){
        progressWindow.showProgress();
    }
    public void hideProgress(){
        progressWindow.hideProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();

    }
}
