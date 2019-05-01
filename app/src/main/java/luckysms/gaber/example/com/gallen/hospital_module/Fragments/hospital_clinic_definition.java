package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.AppLocationService;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class hospital_clinic_definition extends Fragment {
    private View view;
    private EditText hospital_code,full_name,hospital_address,email_address,website,hospital_phone;
    private Button speciality,governorates,area,insurance_company,save;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private RecyclerView dialog_list;
    private speciality_SpinAdapter speciality_adapter;
    private ArrayList<patient_speciality_model> specialities=new ArrayList<>();
    private ArrayList<patient_speciality_model> filteredList = new ArrayList<>();
    private patient_speciality_model selected_speciality;
    private ArrayList<patient_gov_model>govs=new ArrayList<>();
    private ArrayList<patient_gov_model> filtered_govs_List = new ArrayList<>();
    private patient_gov_model selected_gov;
    private ArrayList<patient_city_model>cities=new ArrayList<>();
    private ArrayList<patient_city_model> filtered_cities_List = new ArrayList<>();
    private patient_city_model selected_city;
    private List<patient_insurance_model> insurance_model_list = new ArrayList<>();
    private List<patient_insurance_model> filtered_insurance_model_list = new ArrayList<>();
    private patient_insurance_model selected_insurance_company;
    private gov_SpinAdapter gov_adapter;
    private city_SpinAdapter city_adapter;
    private insurance_SpinAdapter insurance_company_adapter;
    private AppLocationService appLocationService;
    private double latitude ;
    private double longitude;
    private String address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_clinic_definition, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        hospital_code=(EditText)view.findViewById(R.id.hospital_code);
        full_name=(EditText)view.findViewById(R.id.full_name);
        hospital_address=(EditText)view.findViewById(R.id.hospital_address);
        email_address=(EditText)view.findViewById(R.id.email_address);
        website=(EditText)view.findViewById(R.id.website);
        hospital_phone=(EditText)view.findViewById(R.id.hospital_phone);
        speciality=(Button)view.findViewById(R.id.speciality);
        governorates=(Button)view.findViewById(R.id.governorates);
        area=(Button)view.findViewById(R.id.area);
        insurance_company=(Button)view.findViewById(R.id.insurance_company);
        save=(Button)view.findViewById(R.id.save);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        get_location();
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
                String hospital_code_s=hospital_code.getText().toString();
                String full_name_s=full_name.getText().toString();
                String hospital_address_s=hospital_address.getText().toString();
                String email_address_s=email_address.getText().toString();
                String website_s=website.getText().toString();
                String hospital_phone_s=hospital_phone.getText().toString();
                JSONObject hospital=new JSONObject();
                JSONObject gov=new JSONObject();
                JSONObject city=new JSONObject();
                JSONArray insurance_company_list=new JSONArray();
                try {
                    hospital.put("id",0);
                    hospital.put("name","");
                    gov.put("id",selected_gov.id);
                    gov.put("name",selected_gov.name);
                    city.put("id",selected_city.id);
                    city.put("name",selected_city.name);

                    JSONObject insurance_company =new JSONObject();
                    insurance_company.put("id",selected_insurance_company.id);
                    insurance_company.put("name",selected_insurance_company.name);
                    insurance_company_list.put(insurance_company);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (hospital_code_s.length()>0&&full_name_s.length()>0&&hospital_address_s.length()>0&&
                        email_address_s.length()>0&&website_s.length()>0&&hospital_phone_s.length()>0&&selected_city!=null
                        &&selected_speciality!=null&&selected_gov!=null&&selected_insurance_company!=null&&latitude!=0&&longitude!=0&&address.length()>0) {
                    add_clinic(full_name_s,true,hospital,gov,city
                            ,address,hospital_phone_s,website_s,email_address_s,"def",
                            insurance_company_list,new JSONArray(),new JSONArray(),latitude,longitude);
                }

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

                        filter(s.toString());


                    }
                });



                mprogressBar.setVisibility(View.VISIBLE);

                get_specialties_data();
                dialog.show();

            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                city_adapter=new city_SpinAdapter(getActivity(),cities);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(city_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            selected_city=filtered_cities_List.get(position);
                            area.setText(selected_city.name);
                            dialog.dismiss();
                        }else {
                            selected_city=cities.get(position);
                            area.setText(selected_city.name);
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

                        filter_city(s.toString());


                    }
                });

                mprogressBar.setVisibility(View.VISIBLE);
                get_cities_data();
                dialog.show();



            }
        });
        governorates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                gov_adapter=new gov_SpinAdapter(getActivity(),govs);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(gov_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            selected_gov=filtered_govs_List.get(position);
                            governorates.setText(selected_gov.name);
                            dialog.dismiss();

                        }else {
                            selected_gov=govs.get(position);
                            governorates.setText(selected_gov.name);
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

                        filter_govs(s.toString());


                    }
                });

                mprogressBar.setVisibility(View.VISIBLE);
                get_goves_data();
                dialog.show();
            }
        });
        insurance_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText search=(EditText)dialog.findViewById(R.id.search_edt);
                insurance_company_adapter=new insurance_SpinAdapter(getActivity(),insurance_model_list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(insurance_company_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (search.getText().length()>0) {
                            selected_insurance_company = filtered_insurance_model_list.get(position);
                            insurance_company.setText(selected_insurance_company.name);
                            dialog.dismiss();
                        }else {
                            selected_insurance_company = insurance_model_list.get(position);
                            insurance_company.setText(selected_insurance_company.name);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));


                search.addTextChangedListener(new TextWatcher() {

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
                dialog.show();
                mprogressBar.setVisibility(View.VISIBLE);
                get_insurance_data();
            }
        });




        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void add_clinic(final String name, final boolean active, final JSONObject hospital, final JSONObject gov, final JSONObject city, final String address, final String phone
            , final String website, final String email , final String image_url , final JSONArray insurance_company_list,
                            final JSONArray doctor_list , final JSONArray nurse_list, final double latitude, final double longitude)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/clinics/add";
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
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("name",name);
                        object.put("active",active);
                        object.put("hospital",hospital);
                        object.put("gov",gov);
                        object.put("city",city);
                        object.put("address",address);
                        object.put("phone",phone);
                        object.put("website",website);
                        object.put("email",email);
                        object.put("image_url",image_url);
                        object.put("insurance_company_list",insurance_company_list);
                        object.put("doctor_list",doctor_list);
                        object.put("nurse_list",nurse_list);
                        object.put("latitude",latitude);
                        object.put("longitude",longitude);


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
            String url = "http://microtec1.egytag.com:30001/api/medical_specialties/all";
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
    private void filter(String text) {
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
    private void filter_govs(String text) {
        filtered_govs_List.clear();
        for (patient_gov_model item : govs) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }

        }

        gov_adapter.filterList(filtered_govs_List);
    }
    private void filter_city(String text) {
        filtered_cities_List.clear();
        for (patient_city_model item : cities) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }

        }

        city_adapter.filterList(filtered_cities_List);
    }
    private void get_cities_data() {


        try {
            String url = "http://microtec1.egytag.com/api/cities/all";
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
                                cities.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String gov_id=object.getJSONObject("gov").getString("_id");
                                    String gov_name=new String( object.getJSONObject("gov").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    int govid=object.getJSONObject("gov").getInt("id");
                                    patient_city_model  city=  new patient_city_model(_id,image_url,name,gov_id,gov_name,id,govid);
                                    cities.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                gov_adapter.notifyDataSetChanged();

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
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();

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
    private void get_goves_data() {


        try {
            String url = "http://microtec1.egytag.com:30001/api/goves/all";
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
                                govs.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_gov_model city=  new patient_gov_model(_id,image_url,name,id);
                                    govs.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                city_adapter.notifyDataSetChanged();
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
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();

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
    private void get_insurance_data()
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/medical_insurance_companies/all";
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
                                JSONArray list=res.getJSONArray("list");
                                insurance_model_list.clear();
                                insurance_model_list.add(new patient_insurance_model("","","none",0));

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_insurance_model city=  new patient_insurance_model(_id,image_url,name,id);
                                    insurance_model_list.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                insurance_company_adapter.notifyDataSetChanged();



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
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
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
    private void get_location(){
        Location gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        if (gpsLocation != null) {
            latitude = gpsLocation.getLatitude();
            longitude = gpsLocation.getLongitude();
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showSettingsAlert("GPS");
        }
    }
    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
