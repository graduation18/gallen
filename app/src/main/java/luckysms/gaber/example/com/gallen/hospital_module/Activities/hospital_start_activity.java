package luckysms.gaber.example.com.gallen.hospital_module.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_start_screen;

public class hospital_start_activity extends AppCompatActivity {
    ProgressBar bar;
    private RequestQueue queue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_homepage);
        bar=(ProgressBar)findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        getdata();
    }

    public void dashboard(View view) {
        Intent dashboard=new Intent(hospital_start_activity.this,hospital_search_by_doctor_name.class);
        startActivity(dashboard);

    }

    public void create_doctor_account(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","create_doctor_account");
        startActivity(basic_activity);
    }

    public void definition_and_addition(View view) {
        Intent definition_and_addition=new Intent(hospital_start_activity.this,hospital_defintions.class);
        startActivity(definition_and_addition);
    }
    private void getdata()
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/hospitals/view";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    //bar.setVisibility(View.INVISIBLE);
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            if (res.getString("error").equals("email not set")){
                                Toast.makeText(hospital_start_activity.this,getResources().getString(R.string.email_not_set),Toast.LENGTH_LONG).show();
                                bar.setVisibility(View.INVISIBLE);

                            }

                        }else if (res.has("done")){
                            bar.setVisibility(View.INVISIBLE);
                            JSONObject doc=res.getJSONObject("doc");
                            try {
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("name", new String (doc.getString("name").getBytes("ISO-8859-1"), "UTF-8"))
                                        .putString("image_url", doc.getString("image_url"))
                                        .putString("latitude",doc.getString("latitude"))
                                        .putString("longitude",doc.getString("longitude"))
                                        .putString("doctor_list",new String(doc.getJSONArray("doctor_list").toString().getBytes("ISO-8859-1"), "UTF-8"))
                                .commit();

                                Log.w("dsakjbsdahk", new String(doc.getJSONArray("doctor_list").toString()
                                        .getBytes("ISO-8859-1"), "UTF-8"));
                                //Log.w("dsakjbsdahk", doc.getJSONArray("doctor_list").toString());


                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }




                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w("dsnnasdjlas",error);
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.INVISIBLE);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
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
}
