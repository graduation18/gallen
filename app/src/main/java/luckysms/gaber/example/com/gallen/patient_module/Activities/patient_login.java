package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Context;
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
import android.widget.ProgressBar;
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

import org.json.JSONArray;
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
    private TextView back;
    private ProgressBar mprogressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                    mprogressBar.setVisibility(View.VISIBLE);
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
        /*if (mobile_number_email_address_s.equals("mousab")&&password_s.equals("123")){
            mprogressBar.setVisibility(View.INVISIBLE);
            getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                    .putInt("id",12)
                    .putString("password", password_s)
                    .putString("language",Locale.getDefault().getLanguage())
                    .putString("accessToken","eefc4a709d47408ca92377639e2721c9")
                    .putString("type","patient")
                    .putString("image_url","/api/images/Patients.png")
                    .putString("name","mousab")
                    .putBoolean("state",true)
                    .putString("phone","+201286387994")
                    .commit();
            Intent intent=new Intent(patient_login.this,patient_main_screen.class);
            finishAffinity();
            startActivity(intent);

        }else {
            mprogressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(patient_login.this,getResources().getString(R.string.email_or_password_error),Toast.LENGTH_LONG).show();
        }*/

       try {
            String url = "http://intmicrotec.neat-url.com:6566/api/user/login";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mprogressBar.setVisibility(View.INVISIBLE);
                    //do other things with the received JSONObject
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            if (res.getString("error").equals("email not set")){
                                Toast.makeText(patient_login.this,getResources().getString(R.string.email_not_set),Toast.LENGTH_LONG).show();

                            }else if (res.getString("error").equals("email or password error")){
                                Toast.makeText(patient_login.this,getResources().getString(R.string.email_or_password_error),Toast.LENGTH_LONG).show();

                            }else if (res.getString("error").contains("Login Error , You Are Loged")){
                                Log.w("njhjhkjhk","jkjjhhjkhkkj");
                                mprogressBar.setVisibility(View.VISIBLE);
                                logout(mobile_number_email_address_s,password_s);
                            }

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject user = res.getJSONObject("user");
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putInt("id",user.getJSONObject("ref_info").getInt("id"))
                                        .putString("password", password_s)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .putString("accessToken",res.getString("accessToken"))
                                        .putString("type",user.getString("type"))
                                        .putString("image_url",user.getJSONObject("profile").getString("image_url"))
                                        .putString("name",user.getString("name"))
                                        .commit();
                                mprogressBar.setVisibility(View.VISIBLE);
                                Intent got_confirm_code = new Intent(patient_login.this, patient_confirm_code.class);
                                Log.w("dsaldj", user.getString("mobile"));
                                got_confirm_code.putExtra("phone_number", user.getString("mobile"));
                                startActivity(got_confirm_code);
                                finish();

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
                    mprogressBar.setVisibility(View.INVISIBLE);
                }
            })
           {
               @Override
               public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> pars = new HashMap<String, String>();
               pars.put("Content-Type", "application/json");
               pars.put("Cookie", "access_token="+getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

               return pars;
           }
               @Override
               public byte[] getBody() throws com.android.volley.AuthFailureError {
               JSONObject object=new JSONObject();
               try {
                   object.put("password",password_s);
                   object.put("email",mobile_number_email_address_s);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               Log.w("sadkjsdkjlljksda",object.toString());
               return object.toString().getBytes();
           };

               public String getBodyContentType()
               {
                   return "application/json; charset=utf-8";
               }



           };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }
    private void logout(final String mobile_number_email_address_s, final String password_s)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/user/logout";
            if (queue == null) {
                queue = Volley.newRequestQueue(patient_login.this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    mprogressBar.setVisibility(View.INVISIBLE);

                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("_id", "")
                                        .putInt("id", 0)
                                        .putString("email","")
                                        .putString("password"," ")
                                        .putBoolean("state",false)
                                        .commit();
                                mprogressBar.setVisibility(View.VISIBLE);
                                login(mobile_number_email_address_s,password_s);

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(patient_login.this, "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }


                public String getBodyContentType()
                {
                    return "application/json; charset=utf-8";
                }



            };;
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }



}
