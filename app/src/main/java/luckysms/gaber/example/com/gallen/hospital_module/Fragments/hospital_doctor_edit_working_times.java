package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.hospital_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Activities.complete_booking;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import static android.content.Context.MODE_PRIVATE;


public class hospital_doctor_edit_working_times extends Fragment {
    private View view;
    private CheckBox saturaday,sunday,monday,tuesday,wednsday,thursday,friday;
    private Spinner start_appointment_saturaday,end_appointment_saturaday,start_appointment_sunday,end_appointment_sunday,start_appointment_monday
            ,end_appointment_monday,start_appointment_tuesday,end_appointment_tuesday,start_appointment_wednsday,end_appointment_wednsday
            ,start_appointment_thursday,end_appointment_thursday,start_appointment_friday,end_appointment_friday;
    private Button save,cancel,doctor_name,speciality;
    private EditText appointment_period;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private RecyclerView dialog_list;
    private speciality_SpinAdapter speciality_adapter;
    private ArrayList<patient_speciality_model> specialities=new ArrayList<>();
    private ArrayList<patient_speciality_model> filteredList = new ArrayList<>();
    private patient_speciality_model selected_speciality;
    private clinic_model selected_clinic;

    private ArrayList<search_doctor_name_model>doctor_name_list=new ArrayList<>();
    private ArrayList<search_doctor_name_model>doctor_name_filteredList=new ArrayList<>();
    private search_doctor_name_model selected_doctor;
    private hospital_search_result_list_adapter doctor_adapter;



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
        doctor_name=(Button)view.findViewById(R.id.doctor_name);
        appointment_period=(EditText)view.findViewById(R.id.appointment_period);
        speciality=(Button)view.findViewById(R.id.speciality);
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
        doctor_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                doctor_adapter=new hospital_search_result_list_adapter(getActivity(),doctor_name_list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(doctor_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            selected_doctor=doctor_name_filteredList.get(position);
                            doctor_name.setText(selected_doctor.doctor_model.doctor_name);
                            dialog.dismiss();
                        }else {
                            selected_doctor=doctor_name_list.get(position);
                            doctor_name.setText(selected_doctor.doctor_model.doctor_name);
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));


                searchh.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        doctor_filter(s.toString());


                    }
                });



                mprogressBar.setVisibility(View.VISIBLE);

                get_doctor_data(getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                dialog.show();

            }
        });
        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                speciality_adapter=new speciality_SpinAdapter(getActivity(),specialities);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(speciality_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            selected_speciality=filteredList.get(position);
                            speciality.setText(selected_speciality.name);
                            dialog.dismiss();
                        }else {
                            selected_speciality=specialities.get(position);
                            speciality.setText(selected_speciality.name);
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));


                searchh.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        speciality_filter(s.toString());


                    }
                });



                mprogressBar.setVisibility(View.VISIBLE);

                get_specialties_data();
                dialog.show();

            }
        });

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
                    if (selected_doctor!=null) {

                    }else {
                        Toast.makeText(getActivity(),getResources().getText(R.string.please_select_doctor_first),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fill_hours();
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
            String url = "http://intmicrotec.neat-url.com:6566/api/clinics/update";
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

    private void get_specialties_data()
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/medical_specialties/all";
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


                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                specialities.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_speciality_model speciality=  new patient_speciality_model(_id,image_url,name,id);
                                    specialities.add(speciality);


                                }
                                speciality_adapter.notifyDataSetChanged();


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
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
                    return pars;
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
    private void speciality_filter(String text) {
        filteredList.clear();
        for (patient_speciality_model item : specialities) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

        }

        speciality_adapter.filterList(filteredList);
    }
    private void doctor_filter(String text) {
        doctor_name_filteredList.clear();
        for (search_doctor_name_model item : doctor_name_list) {
            if (!item.doctor_model.doctor_name.isEmpty()){
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())) {
                    doctor_name_filteredList.add(item);
                }
            }else {
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())) {
                    doctor_name_filteredList.add(item);
                }
            }

        }

        doctor_adapter.filterList(doctor_name_filteredList);
    }
    private void add_ticket(final JSONObject selected_doctor, final JSONObject selected_time
            , final JSONObject selected_specialty, final JSONObject selected_hospital, final JSONObject status
            , final JSONArray drugs_list, final JSONArray scans_list, final JSONArray analyses_list
            , final JSONArray operation_list, final JSONObject selected_shift, final JSONObject image_url
            , final JSONObject selected_clinic,final JSONObject ticket_date) {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/tickets/add";
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
                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {

                                Toast.makeText(getActivity(),getResources().getString(R.string.booking_successful),Toast.LENGTH_LONG).show();
                                Intent c=new Intent(getActivity(),complete_booking.class);
                                startActivity(c);


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
            }) {
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
                        object.put("selected_doctor",selected_doctor);
                        object.put("selected_time",selected_time);
                        object.put("selected_specialty",selected_specialty);
                        object.put("selected_hospital",selected_hospital);
                        object.put("drugs_list",drugs_list);
                        object.put("operation_list",operation_list);
                        object.put("scans_list",scans_list);
                        object.put("analyses_list",analyses_list);
                        object.put("selected_shift",selected_shift);
                        object.put("status",status);
                        object.put("image_url",image_url);
                        object.put("date",new Date().getTime());
                        object.put("selected_clinic",selected_clinic);
                        object.put("ticket_date",ticket_date);




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
    private void fill_hours(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("7:00 am");
        spinnerArray.add("7:30 am");
        spinnerArray.add("8:00 am");
        spinnerArray.add("8:30 am");
        spinnerArray.add("9:00 am");
        spinnerArray.add("9:30 am");
        spinnerArray.add("10:00 am");
        spinnerArray.add("10:30 am");
        spinnerArray.add("11:00 am");
        spinnerArray.add("11:30 am");
        spinnerArray.add("12:00 pm");
        spinnerArray.add("12:30 pm");
        spinnerArray.add("1:00 pm");
        spinnerArray.add("1:30 pm");
        spinnerArray.add("2:00 pm");
        spinnerArray.add("2:30 pm");
        spinnerArray.add("3:00 pm");
        spinnerArray.add("3:30 pm");
        spinnerArray.add("4:00 pm");
        spinnerArray.add("4:30 pm");
        spinnerArray.add("5:00 pm");
        spinnerArray.add("5:30 pm");
        spinnerArray.add("6:00 pm");
        spinnerArray.add("6:30 pm");
        spinnerArray.add("7:00 pm");
        spinnerArray.add("7:30 pm");
        spinnerArray.add("8:00 pm");
        spinnerArray.add("8:30 pm");
        spinnerArray.add("9:00 pm");
        spinnerArray.add("9:30 pm");
        spinnerArray.add("10:00 pm");
        spinnerArray.add("10:30 pm");
        spinnerArray.add("11:00 pm");
        spinnerArray.add("11:30 pm");
        spinnerArray.add("12:00 am");
        spinnerArray.add("12:30 am");
        spinnerArray.add("1:00 am");
        spinnerArray.add("1:30 am");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_appointment_saturaday.setAdapter(adapter);
        end_appointment_saturaday.setAdapter(adapter);
        start_appointment_sunday.setAdapter(adapter);
        end_appointment_sunday.setAdapter(adapter);
        start_appointment_monday.setAdapter(adapter);
        end_appointment_monday.setAdapter(adapter);
        start_appointment_tuesday.setAdapter(adapter);
        end_appointment_tuesday.setAdapter(adapter);
        start_appointment_wednsday.setAdapter(adapter);
        end_appointment_wednsday.setAdapter(adapter);
        start_appointment_thursday.setAdapter(adapter);
        end_appointment_thursday.setAdapter(adapter);
        start_appointment_friday.setAdapter(adapter);
        end_appointment_friday.setAdapter(adapter);


    }

    private void get_doctor_data(final int id)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/hospitals/view";
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


                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
                                            clinic.getJSONObject("gov").toString(),clinic.getJSONObject("city").toString()
                                            ,clinic.getJSONArray("insurance_company_list").toString(),clinic.getJSONArray("doctor_list").toString()
                                            ,clinic.getJSONArray("nurse_list").toString(),clinic.getDouble("latitude"),
                                            clinic.getDouble("longitude"),specialty_name,specialty_id
                                    );
                                    doctor_name_list.add(new search_doctor_name_model(doctor_model,speciality_model,clinic_model));
                                }

                                doctor_adapter.notifyDataSetChanged();


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
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ticket_data(String date, final JSONObject selected_time,JSONObject selected_shift){
        JSONObject selected_doctor_obj=new JSONObject();
        JSONObject selected_specialty_obj=new JSONObject();
        JSONObject selected_hospital_obj=new JSONObject();
        JSONObject status_obj=new JSONObject();
        JSONObject ticket_date =new JSONObject();
        JSONObject selected_clinic_obj =new JSONObject();
        try {
            selected_doctor_obj.put("id",selected_doctor.doctor_model.id);
            selected_doctor_obj.put("name",selected_doctor.doctor_model.doctor_name);
            selected_doctor_obj.put("image",selected_doctor.doctor_model.doctor_image);
            selected_doctor_obj.put("fee",selected_doctor.doctor_model.doctor_fee);
            selected_doctor_obj.put("rating",new JSONArray(selected_doctor.doctor_model.doctor_rating));
            selected_doctor_obj.put("discount_code",selected_doctor.doctor_model.doctor_accept_discount);
            selected_doctor_obj.put("review_list",new JSONArray(selected_doctor.doctor_model.review_list));
            selected_doctor_obj.put("gender",selected_doctor.doctor_model.doctor_gender);
            selected_doctor_obj.put("info",selected_doctor.doctor_model.doctor_notes);
            selected_specialty_obj.put("id",selected_speciality.id);
            selected_specialty_obj.put("name",selected_speciality.name);
            selected_hospital_obj.put("id",1);
            selected_hospital_obj.put("name","مستشفى الامل");
            status_obj.put("name_en","available");
            status_obj.put("name_ar","متاح");
            status_obj.put("id",0);
            ticket_date.put("date",date);
            selected_clinic_obj.put("id",selected_clinic.id);
            selected_clinic_obj.put("name",selected_clinic.name);

            add_ticket(selected_doctor_obj,selected_time,selected_specialty_obj,selected_hospital_obj,status_obj,
                    new JSONArray(),new JSONArray(),new JSONArray(),new JSONArray(),selected_shift,new JSONObject(),selected_clinic_obj,ticket_date);





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
