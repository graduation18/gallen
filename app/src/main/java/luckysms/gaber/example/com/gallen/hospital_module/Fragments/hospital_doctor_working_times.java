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

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_defintions;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_search_by_doctor_name;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.hospital_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_clinic_data;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_doctor_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Activities.complete_booking;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;


public class hospital_doctor_working_times extends Fragment implements pass_speciality_data,pass_doctor_data {
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
    private pass_speciality_data mListener;
    private pass_doctor_data doctor_mListener;
    private patient_speciality_model speciality_model;
    private search_doctor_name_model search_doctor_name_model;


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
        start_appointment_sunday=(Spinner)view.findViewById(R.id.start_appointment_sunday);
        end_appointment_sunday=(Spinner)view.findViewById(R.id.end_appointment_sunday);
        start_appointment_monday=(Spinner)view.findViewById(R.id.start_appointment_monday);
        end_appointment_monday=(Spinner)view.findViewById(R.id.end_appointment_monday);
        start_appointment_tuesday=(Spinner)view.findViewById(R.id.start_appointment_tuesday);
        end_appointment_tuesday=(Spinner)view.findViewById(R.id.end_appointment_tuesday);
        start_appointment_wednsday=(Spinner)view.findViewById(R.id.start_appointment_wednsday);
        end_appointment_wednsday=(Spinner)view.findViewById(R.id.end_appointment_wednsday);
        start_appointment_thursday=(Spinner)view.findViewById(R.id.start_appointment_thursday);
        end_appointment_thursday=(Spinner)view.findViewById(R.id.end_appointment_thursday);
        start_appointment_friday=(Spinner)view.findViewById(R.id.start_appointment_friday);
        end_appointment_friday=(Spinner)view.findViewById(R.id.end_appointment_friday);
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
                Bundle s=new Bundle();
                if (search_doctor_name_model!=null&&speciality_model==null){
                    s.putInt("speciality_id",search_doctor_name_model.speciality_model.id);
                }
                search_doctors_BottomSheetFragment bottomSheetFragment = new search_doctors_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                doctor_mListener=bottomSheetFragment;
                doctor_mListener.pass_data(null,hospital_doctor_working_times.this);
            }
        });
        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (search_doctor_name_model!=null&&speciality_model==null){
                    s.putInt("speciality_id",search_doctor_name_model.speciality_model.id);
                }
                search_specilty_BottomSheetFragment bottomSheetFragment = new search_specilty_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(null,hospital_doctor_working_times.this);
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
                Intent intent=new Intent(getActivity(),hospital_defintions.class);
                startActivity(intent);
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
                int time_period = 0;
                if (appointment_period.getText().toString().length()>0){

                    time_period= Integer.parseInt(appointment_period.getText().toString());
                }
                if(time_period==0){
                    time_period=20;
                }
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
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
                        String date;
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                        c.set(Calendar.HOUR_OF_DAY,0);
                        c.set(Calendar.MINUTE,0);
                        c.set(Calendar.SECOND,0);
                        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                        if (df.format(c.getTime()).equals(df.format(new Date().getTime()))){
                            date=df.format(c.getTime());
                        }else {
                            c.add(Calendar.DATE,7);
                            date=df.format(c.getTime());
                        }

                        sched_tickets(from,to,time_period,date,obj);
                        times_list.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    shift.put("times_list",times_list);
                    doctor_list.put(shift);
                    if (search_doctor_name_model!=null) {
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
    private void add_ticket(final JSONObject selected_doctor, final JSONObject selected_time
            , final JSONObject selected_specialty, final JSONObject selected_hospital, final JSONObject status
            , final JSONArray drugs_list, final JSONArray scans_list, final JSONArray analyses_list
            , final JSONArray operation_list, final JSONObject selected_shift, final JSONObject image_url
            , final JSONObject selected_clinic,final String ticket_date) {


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
                public byte[] getBody() throws com.android.volley.AuthFailureError {
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
                        object.put("selected_clinic",selected_clinic);
                        object.put("date",ticket_date);




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


    private void ticket_data(String date, final JSONObject selected_time,JSONObject selected_shift){
        JSONObject selected_doctor_obj=new JSONObject();
        JSONObject selected_specialty_obj=new JSONObject();
        JSONObject selected_hospital_obj=new JSONObject();
        JSONObject status_obj=new JSONObject();
        JSONObject selected_clinic_obj =new JSONObject();
        try {
            selected_doctor_obj.put("id",search_doctor_name_model.doctor_model.id);
            selected_doctor_obj.put("name",search_doctor_name_model.doctor_model.doctor_name);
            selected_doctor_obj.put("image",search_doctor_name_model.doctor_model.doctor_image);
            selected_doctor_obj.put("fee",search_doctor_name_model.doctor_model.doctor_fee);
            selected_doctor_obj.put("discount_code",search_doctor_name_model.doctor_model.doctor_accept_discount);
            selected_doctor_obj.put("gender",search_doctor_name_model.doctor_model.doctor_gender);
            selected_doctor_obj.put("info",search_doctor_name_model.doctor_model.doctor_notes);
            selected_specialty_obj.put("id",speciality_model.id);
            selected_specialty_obj.put("name",new String (speciality_model.name.getBytes("ISO-8859-1"), "UTF-8"));
            selected_hospital_obj.put("id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
            selected_hospital_obj.put("name",new  String (getActivity().getSharedPreferences("personal_data", MODE_PRIVATE)
                    .getString("name","").getBytes("ISO-8859-1"), "UTF-8"));
            status_obj.put("name_en","available");
            status_obj.put("name_ar","متاح");
            status_obj.put("id",0);
            selected_clinic_obj.put("id",search_doctor_name_model.clinic_model.id);
            selected_clinic_obj.put("name", new String (search_doctor_name_model.clinic_model.name
                    .getBytes("ISO-8859-1"), "UTF-8"));


           add_ticket(selected_doctor_obj,selected_time,selected_specialty_obj,selected_hospital_obj,status_obj,new JSONArray(),new JSONArray(),
                   new JSONArray(),new JSONArray(),selected_shift,new JSONObject(),selected_clinic_obj,date);
            Log.w("ffddfdfssdfsfd", String.valueOf(selected_hospital_obj)+" "+selected_doctor_obj.toString()+selected_clinic_obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sched_tickets(String from, String to, int time_period, String date, JSONObject selected_shift){
        int from_h= Integer.parseInt(from.split(":")[0]);
        int to_h= Integer.parseInt(to.split(":")[0]);
        int from_m= Integer.parseInt(from.split(":")[1].split(" ")[0]);
        int to_m= Integer.parseInt(to.split(":")[1].split(" ")[0]);
        String to_aa=to.split(" ")[1];
        String from_aa=from.split(" ")[1];
        if (from_aa.contains("pm")){
            from_h=from_h+12;
        }
        if (to_aa.contains("pm")){
            to_h=to_h+12;
        }
        if (from_h>=to_h){
            to_h=to_h+24;
        }
        while (from_h<to_h){
            if (from_m+time_period>=60){
                if(from_h*60+from_m+time_period<to_h*60+to_m){
                    from_m+=time_period-60;
                    from_h++;
                    String time;
                    if (from_h>=12){
                        if (from_m==0){
                            if (from_h - 12>=12){
                                if (from_h - 12==12){
                                    time = from_h - 12 + ":" + from_m + "0 am";
                                }else {
                                    time = from_h - 24 + ":" + from_m + "0 am";
                                }
                            }else {
                                if (from_h==12){
                                    time = from_h  + ":" + from_m + "0 pm";
                                }else {
                                    time = from_h - 12 + ":" + from_m + "0 pm";
                                }
                            }
                        }else {
                            if (from_h==12){
                                time=from_h+":"+from_m+" pm";
                            }else {
                                time=from_h-12+":"+from_m+" pm";
                            }


                        }
                    }else {
                        if (from_m==0){
                            time=from_h+":"+from_m+"0 am";
                        }else {
                            time=from_h+":"+from_m+" am";

                        }
                    }
                    try {
                        Log.w("sdasddsadsa",time);
                        JSONObject selected_time=new JSONObject();
                        selected_time.put("time",time);
                        ticket_data(date,selected_time,selected_shift);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }else{

                from_m+=time_period;
                String time;
                if (from_h>=12){
                    if (from_m==0){
                        if (from_h - 12>=12){
                            if (from_h - 12==12){
                                time = from_h - 12 + ":" + from_m + "0 am";
                            }else {
                                time = from_h - 24 + ":" + from_m + "0 am";
                            }
                        }else {
                            if (from_h==12){
                                time = from_h  + ":" + from_m + "0 pm";
                            }else {
                                time = from_h - 12 + ":" + from_m + "0 pm";
                            }
                        }
                    }else {
                        if (from_h - 12>=12){
                            if (from_h - 12==12){
                                time = from_h - 12 + ":" + from_m + " am";
                            }else {
                                time = from_h - 24 + ":" + from_m + " am";
                            }
                        }else {
                            if (from_h==12){
                                time = from_h  + ":" + from_m + " pm";
                            }else {
                                time = from_h - 12 + ":" + from_m + " pm";
                            }

                        }

                    }
                }else {
                    if (from_m==0){
                        time=from_h+":"+from_m+"0 am";
                    }else {
                        time=from_h+":"+from_m+" am";

                    }
                }
                try {
                    Log.w("sdasddsadsa",time);
                    JSONObject selected_time=new JSONObject();
                    selected_time.put("time",time);
                    ticket_data(date,selected_time,selected_shift);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            if(from_h==to_h&&from_m<to_m&&from_m+time_period<to_m){

                        from_m+=time_period;
                        String time;
                        if (from_h>=12){
                            if (from_m==0){
                                if (from_h==12){
                                    time = from_h+ ":" + from_m + "0 pm";
                                }else {
                                    if (from_h - 12>=12){
                                        if (from_h - 12==12){
                                            time = from_h - 12 + ":" + from_m + "0 am";
                                        }else {
                                            time = from_h - 24 + ":" + from_m + "0 am";
                                        }
                                    }else {
                                        if (from_h==12){
                                            time = from_h  + ":" + from_m + "0 pm";
                                        }else {
                                            time = from_h - 12 + ":" + from_m + "0 pm";
                                        }

                                    }
                                }
                            }else {
                                if (from_h==12){
                                    time = from_h  + ":" + from_m + " pm";
                                }else {

                                    if (from_h - 12>=12){
                                        if (from_h - 12==12){
                                            time = from_h - 12 + ":" + from_m + " am";
                                        }else {
                                            time = from_h - 24 + ":" + from_m + " am";
                                        }
                                    }else {
                                        if (from_h==12){
                                            time = from_h  + ":" + from_m + " pm";
                                        }else {
                                            time = from_h - 12 + ":" + from_m + " pm";
                                        }
                                    }
                                }


                            }

                        }else {
                            if (from_m==0){
                                time=from_h+":"+from_m+"0 am";
                            }else {
                                time=from_h+":"+from_m+" am";

                            }

                        }
                        try {
                            Log.w("sdasddsadsa",time);
                            JSONObject selected_time=new JSONObject();
                            selected_time.put("time",time);
                            ticket_data(date,selected_time,selected_shift);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


            }
        }
    }

    @Override
    public void pass_data(patient_speciality_model speciality_model, pass_speciality_data listner) {
        this.speciality_model=speciality_model;
        speciality.setText(speciality_model.name);
    }

    @Override
    public void pass_data(search_doctor_name_model search_doctor_name_model, pass_doctor_data listner) {
        this.search_doctor_name_model=search_doctor_name_model;
        doctor_name.setText(search_doctor_name_model.doctor_model.doctor_name);
    }
}
