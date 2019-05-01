package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;

import static android.content.Context.MODE_PRIVATE;

public class hospital_doctor_working_times extends Fragment {
    private View view;
    private ImageView doctor_image;
    private EditText doctor_code,full_name,doctor_phone,email_address,doctor_fee,doctor_info;
    private CheckBox saturaday,sunday,monday,tuesday,wednsday,thursday,friday;
    private Spinner start_appointment_saturaday,end_appointment_saturaday,start_appointment_sunday,end_appointment_sunday,start_appointment_monday
            ,end_appointment_monday,start_appointment_tuesday,end_appointment_tuesday,start_appointment_wednsday,end_appointment_wednsday
            ,start_appointment_thursday,end_appointment_thursday,start_appointment_friday,end_appointment_friday;
    private Button save,cancel;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_doctor_working_times, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        save=(Button)view.findViewById(R.id.save);
        cancel=(Button)view.findViewById(R.id.cancel);
        saturaday=(CheckBox)view.findViewById(R.id.saturaday);
        sunday=(CheckBox)view.findViewById(R.id.sunday);
        monday=(CheckBox)view.findViewById(R.id.monday);
        tuesday=(CheckBox)view.findViewById(R.id.tuesday);
        wednsday=(CheckBox)view.findViewById(R.id.wednsday);
        thursday=(CheckBox)view.findViewById(R.id.thursday);
        friday=(CheckBox)view.findViewById(R.id.friday);
        start_appointment_saturaday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_saturaday=(Spinner)view.findViewById(R.id.end_appointment_saturaday);
        start_appointment_sunday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_sunday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        start_appointment_monday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_monday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        start_appointment_tuesday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_tuesday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        start_appointment_wednsday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_wednsday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        start_appointment_thursday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_thursday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        start_appointment_friday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        end_appointment_friday=(Spinner)view.findViewById(R.id.start_appointment_saturaday);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        start_appointment_saturaday.setEnabled(false);
        end_appointment_saturaday.setEnabled(false);
        start_appointment_sunday.setEnabled(false);
        end_appointment_sunday.setEnabled(false);
        start_appointment_monday.setEnabled(false);
        end_appointment_monday.setEnabled(false);
        start_appointment_tuesday.setEnabled(false);
        end_appointment_tuesday.setEnabled(false);
        start_appointment_wednsday.setEnabled(false);
        end_appointment_wednsday.setEnabled(false);
        start_appointment_thursday.setEnabled(false);
        end_appointment_thursday.setEnabled(false);
        start_appointment_friday.setEnabled(false);
        end_appointment_friday.setEnabled(false);
        saturaday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    start_appointment_saturaday.setEnabled(true);
                    end_appointment_saturaday.setEnabled(true);
                }else {
                    start_appointment_saturaday.setEnabled(false);
                    end_appointment_saturaday.setEnabled(false);
                }

            }
        });
        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    start_appointment_sunday.setEnabled(true);
                    end_appointment_sunday.setEnabled(true);
                }else {
                    start_appointment_sunday.setEnabled(false);
                    end_appointment_sunday.setEnabled(false);
                }
            }
        });
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    start_appointment_monday.setEnabled(true);
                    end_appointment_monday.setEnabled(true);
                }else {
                    start_appointment_monday.setEnabled(false);
                    end_appointment_monday.setEnabled(false);
                }

            }
        });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    start_appointment_tuesday.setEnabled(true);
                    end_appointment_tuesday.setEnabled(true);
                }else {
                    start_appointment_tuesday.setEnabled(false);
                    end_appointment_tuesday.setEnabled(false);
                }

            }
        });
        wednsday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    start_appointment_wednsday.setEnabled(true);
                    end_appointment_wednsday.setEnabled(true);
                }else {
                    start_appointment_wednsday.setEnabled(false);
                    end_appointment_wednsday.setEnabled(false);
                }

            }
        });
        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    start_appointment_thursday.setEnabled(true);
                    end_appointment_thursday.setEnabled(true);
                }else {
                    start_appointment_thursday.setEnabled(false);
                    end_appointment_thursday.setEnabled(false);
                }


            }
        });

        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    start_appointment_friday.setEnabled(true);
                    end_appointment_friday.setEnabled(true);
                }else {
                    start_appointment_friday.setEnabled(false);
                    end_appointment_friday.setEnabled(false);
                }
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray doctor_list=new JSONArray();
                JSONObject shift=new JSONObject();
                JSONArray times_list=new JSONArray();
                if (saturaday.isChecked()){
                    String from=start_appointment_saturaday.getSelectedItem().toString();
                    String to=end_appointment_saturaday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",0);
                        day.put("name","saturaday");
                        day.put("en","saturaday");
                        day.put("ar","السبت");
                        from_obj.put("id",start_appointment_saturaday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_saturaday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (sunday.isChecked()){
                    String from=start_appointment_sunday.getSelectedItem().toString();
                    String to=end_appointment_sunday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",1);
                        day.put("name","sunday");
                        day.put("en","sunday");
                        day.put("ar","الأحد");
                        from_obj.put("id",start_appointment_sunday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_sunday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (monday.isChecked()){
                    String from=start_appointment_monday.getSelectedItem().toString();
                    String to=end_appointment_monday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",2);
                        day.put("name","monday");
                        day.put("en","monday");
                        day.put("ar","الأثنين");
                        from_obj.put("id",start_appointment_monday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_monday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (tuesday.isChecked()){
                    String from=start_appointment_tuesday.getSelectedItem().toString();
                    String to=end_appointment_tuesday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",3);
                        day.put("name","tuesday");
                        day.put("en","tuesday");
                        day.put("ar","الثلاثاء");
                        from_obj.put("id",start_appointment_tuesday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_tuesday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (wednsday.isChecked()){
                    String from=start_appointment_wednsday.getSelectedItem().toString();
                    String to=end_appointment_wednsday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",4);
                        day.put("name","wednsday");
                        day.put("en","wednsday");
                        day.put("ar","الاربعاء");
                        from_obj.put("id",start_appointment_wednsday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_wednsday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (thursday.isChecked()){
                    String from=start_appointment_thursday.getSelectedItem().toString();
                    String to=end_appointment_thursday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",5);
                        day.put("name","thursday");
                        day.put("en","thursday");
                        day.put("ar","الخميس");
                        from_obj.put("id",start_appointment_thursday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_thursday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (friday.isChecked()){
                    String from=start_appointment_friday.getSelectedItem().toString();
                    String to=end_appointment_friday.getSelectedItem().toString();
                    JSONObject day=new JSONObject();
                    JSONObject from_obj=new JSONObject();
                    JSONObject to_obj=new JSONObject();
                    JSONObject obj=new JSONObject();
                    try {
                        day.put("id",6);
                        day.put("name","friday");
                        day.put("en","friday");
                        day.put("ar","الجمعة");
                        from_obj.put("id",start_appointment_friday.getSelectedItemId());
                        from_obj.put("name",from);
                        from_obj.put("en",from);
                        from_obj.put("ar",from);
                        to_obj.put("id",end_appointment_friday.getSelectedItemId());
                        to_obj.put("name",to);
                        to_obj.put("en",to);
                        to_obj.put("ar",to);
                        obj.put("day",day);
                        obj.put("from",from_obj);
                        obj.put("to",to_obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    shift.put("times_list",times_list);
                    doctor_list.put(shift);
                    update_clinic(5,doctor_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void update_clinic(final int clinic_id, final JSONArray doctor_list)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/clinics/update";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
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
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
                    return pars;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",clinic_id);
                        object.put("doctor_list",doctor_list);


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
    private void get_clinics(final int hospital_id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/clinics/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
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
                                JSONArray list=res.getJSONArray("list");
                                for (int i=0;i<list.length();i++) {
                                    JSONObject clinic=list.getJSONObject(i);
                                    clinic.getInt("id");
                                    clinic.getString("name");
                                    clinic.getBoolean("active");
                                    clinic.getJSONObject("hospital");
                                    clinic.getJSONObject("gov");
                                    clinic.getJSONObject("city");
                                    clinic.getString("address");
                                    clinic.getString("phone");
                                    clinic.getString("website");
                                    clinic.getString("email");
                                    clinic.getString("image_url");
                                    clinic.getJSONArray("insurance_company_list");
                                    clinic.getJSONArray("doctor_list");
                                    clinic.getJSONArray("nurse_list");
                                    clinic.getDouble("latitude");
                                    clinic.getDouble("longitude");
                                }
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
                    return pars;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        JSONObject where=new JSONObject();
                        where.put("hospital.id",hospital_id);
                        object.put("where",where);


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
}
