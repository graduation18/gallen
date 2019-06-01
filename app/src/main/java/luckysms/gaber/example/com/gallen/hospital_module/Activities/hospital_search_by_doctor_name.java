package luckysms.gaber.example.com.gallen.hospital_module.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.hospital_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_doctor_data_fragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

public class hospital_search_by_doctor_name extends AppCompatActivity {
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private EditText doctor_name;
    private ProgressBar mprogressBar;
    private ArrayList<search_doctor_name_model>doctor_name_list=new ArrayList<>();
    private ArrayList<search_doctor_name_model>filteredList=new ArrayList<>();
    private RecyclerView search_result_recycler;
    private hospital_search_result_list_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_search_by_doctor_name);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        back=(TextView)findViewById(R.id.back);
        number_of_notifications=(TextView)findViewById(R.id.number_of_notifications);
        notifications=(TextView)findViewById(R.id.notifications);
        doctor_name=(EditText)findViewById(R.id.doctor_name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(hospital_search_by_doctor_name.this,hospital_start_activity.class);
                startActivity(back);
            }
        });
        doctor_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                filter(s.toString());


            }
        });
        search_result_recycler = findViewById(R.id.search_result_recycler);
        adapter=new hospital_search_result_list_adapter(this,doctor_name_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        search_result_recycler.setLayoutManager(mLayoutManager);
        search_result_recycler.setItemAnimator(new DefaultItemAnimator());
        search_result_recycler.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 5));
        search_result_recycler.setAdapter(adapter);
        search_result_recycler.addOnItemTouchListener(new RecyclerTouchListener(this, search_result_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        mprogressBar.setVisibility(View.VISIBLE);
        get_doctor_data(getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));

    }
    private void get_doctor_data(final int id)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/hospitals/view";
            if (queue == null) {
                queue = Volley.newRequestQueue(hospital_search_by_doctor_name.this);
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


                        if (res.has("error")) {
                            Toast.makeText(hospital_search_by_doctor_name.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                doctor_name_list.clear();
                                JSONObject doc=res.getJSONObject("doc");
                                JSONArray doctor_list=doc.getJSONArray("doctor_list");

                                for (int d=0;d<doctor_list.length();d++){
                                    JSONObject doctor=doctor_list.getJSONObject(d);

                                    String doctor__id="";
                                    if (doctor.has("_id")){doctor__id=doctor.getString("_id");}
                                    int doctor_id=doctor.getInt("id");
                                    String  doctor_email=doctor.getString("email");
                                    String  doctor_info=doctor.getString("info");
                                    boolean  doctor_active=doctor.getBoolean("active");
                                    boolean  doctor_accept_code=doctor.getBoolean("accept_code");

                                    String doctor_availabilty=getResources().getString(R.string.not_active);
                                    if (doctor_active){
                                        doctor_availabilty=getResources().getString(R.string.active);
                                    }
                                    String  doctor_code="";
                                    if (doctor.has("code")){doctor.getString("code");}
                                    String  doctor_phone=doctor.getString("phone");
                                    String  doctor_gender=doctor.getString("gender");
                                    double  doctor_fee=doctor.getDouble("fee");
                                    String doctor_name=new String (doctor.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String doctor_image= "http://intmicrotec.neat-url.com:6566"+doctor.getString("image_url");
                                    JSONObject specialty=doctor.getJSONObject("specialty");
                                    String specialty__id="";
                                    if (specialty.has("_id")){specialty.getString("_id");}
                                    int specialty_id=specialty.getInt("id");
                                    JSONArray review_list=new JSONArray();
                                    if (doctor.has("review_list")){
                                        review_list=doctor.getJSONArray("review_list");
                                    }
                                    JSONObject clinic=new JSONObject();
                                    if (doctor.has("clinic")){
                                        clinic=doctor.getJSONObject("clinic");
                                    }
                                    double rating=0;
                                    JSONArray rating_list=new JSONArray();
                                    if (doctor.has("rating_list")){
                                        rating_list=doctor.getJSONArray("rating_list");
                                        for (int a=0;a<rating_list.length();a++){
                                            JSONObject rate=rating_list.getJSONObject(a);
                                            rating=rating+rate.getInt("rate");
                                        }
                                        rating=rating/rating_list.length();
                                    }
                                    String specialty_name=new String (specialty.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    doctor_model doctor_model=new doctor_model(doctor_name,doctor_availabilty,
                                            "",doctor_image,doctor_accept_code
                                            ,Float.parseFloat(String.valueOf(rating)),doctor_fee
                                            ,doctor_id,doctor_info,doctor_gender,
                                            review_list.toString(),doctor_code,doctor_email,doctor_phone,doctor__id);
                                    patient_speciality_model speciality_model=new patient_speciality_model(specialty__id,"ss",specialty_name,specialty_id);
                                    clinic_model clinic_model=new clinic_model(
                                            clinic.getString("name"), clinic.getString("address"), clinic.getString("phone")
                                            , clinic.getString("website"),clinic.getString("email"),clinic.getString("image_url"),
                                            clinic.getInt("id"),clinic.getBoolean("active"),clinic.getJSONObject("hospital").toString(),
                                            clinic.getJSONObject("gov").toString(),clinic.getJSONObject("city").toString(),
                                            clinic.getJSONArray("insurance_company_list").toString(),
                                            clinic.getJSONArray("doctor_list").toString(), clinic.getJSONArray("nurse_list").toString()
                                            ,clinic.getDouble("latitude"),
                                            clinic.getDouble("longitude"),specialty_name,specialty_id
                                    );
                                    doctor_name_list.add(new search_doctor_name_model(doctor_model,speciality_model,clinic_model));
                                }

                                adapter.notifyDataSetChanged();


                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mprogressBar.setVisibility(View.INVISIBLE);

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
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",id);
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
            stringReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

    private void filter(String text) {
        filteredList.clear();
        for (search_doctor_name_model item : doctor_name_list) {
            if (!item.doctor_model.doctor_name.isEmpty()){
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }else {
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

        }

        adapter.filterList(filteredList);
        if (filteredList.size()>0){
            search_result_recycler.setVisibility(View.VISIBLE);
        }
    }
}
