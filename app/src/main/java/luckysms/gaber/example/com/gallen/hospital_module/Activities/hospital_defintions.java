package luckysms.gaber.example.com.gallen.hospital_module.Activities;

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
    }

    public void block_patient(View view) {
    }

    public void view_edit_working_time(View view) {
    }

    public void define_working_time(View view) {
    }

    public void edit_delete_doctor(View view) {
    }

    public void doctor_registration(View view) {
    }

    public void clinic_definition(View view) {
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
