package luckysms.gaber.example.com.gallen.hospital_module.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import luckysms.gaber.example.com.gallen.R;

public class hospital_defintions extends AppCompatActivity {
    private RequestQueue queue;
    private ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_defintions);
        mprogressBar = (ProgressBar)findViewById(R.id.progressBar);

    }

    public void sign_out(View view) {
        mprogressBar.setVisibility(View.VISIBLE);
        logout();
    }

    public void create_doctor_account(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","create_doctor_account");
        startActivity(basic_activity);
    }

    public void block_patient(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","block_patient");
        startActivity(basic_activity);
    }

    public void view_edit_working_time(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","view_edit_working_time");
        startActivity(basic_activity);
    }

    public void define_working_time(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","define_working_time");
        startActivity(basic_activity);
    }

    public void edit_delete_doctor(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","edit_delete_doctor");
        startActivity(basic_activity);
    }

    public void doctor_registration(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","doctor_registration");
        startActivity(basic_activity);
    }

    public void clinic_definition(View view) {
        Intent basic_activity=new Intent(this,basic_activity.class);
        basic_activity.putExtra("fragment","clinic_definition");
        startActivity(basic_activity);
    }
    private void logout()
    {


        try {
            String url = "http://microtec1.egytag.com/api/user/logout";
            if (queue == null) {
                queue = Volley.newRequestQueue(hospital_defintions.this);
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
                                hospital_defintions.this.getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("_id", "")
                                        .putString("email","")
                                        .putString("password"," ")
                                        .putBoolean("state",false)
                                        .commit();
                                hospital_defintions.this.finish();

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(hospital_defintions.this, "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            });
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }
}
