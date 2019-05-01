package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.fxn.pix.Pix;
import com.google.gson.JsonArray;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class hospital_doctor_definition extends Fragment {
    private View view;
    private ImageView doctor_image;
    private EditText doctor_code,full_name,doctor_phone,email_address,doctor_fee,doctor_info;
    private Spinner doctor_status,doctor_accept_code;
    private Button clinics,speciality,save,cancel;
    private TextView back,number_of_notifications,notifications;
    int PICK_IMAGE_MULTIPLE = 1;
    private RecyclerView dialog_list;
    private speciality_SpinAdapter speciality_adapter;
    private ArrayList<patient_speciality_model> specialities=new ArrayList<>();
    private ArrayList<patient_speciality_model> filteredList = new ArrayList<>();
    private patient_speciality_model selected_speciality;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private String selected_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_doctor_definition, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        doctor_code=(EditText)view.findViewById(R.id.doctor_code);
        full_name=(EditText)view.findViewById(R.id.full_name);
        doctor_phone=(EditText)view.findViewById(R.id.doctor_phone);
        email_address=(EditText)view.findViewById(R.id.email_address);
        doctor_fee=(EditText)view.findViewById(R.id.doctor_fee);
        doctor_info=(EditText)view.findViewById(R.id.doctor_info);
        doctor_status=(Spinner)view.findViewById(R.id.doctor_status);
        doctor_accept_code=(Spinner)view.findViewById(R.id.doctor_accept_code);
        clinics=(Button)view.findViewById(R.id.clinics);
        speciality=(Button)view.findViewById(R.id.speciality);
        save=(Button)view.findViewById(R.id.save);
        cancel=(Button)view.findViewById(R.id.cancel);
        doctor_image=(ImageView)view.findViewById(R.id.doctor_image);
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
                String doctor_code_s=doctor_code.getText().toString();
                String full_name_s=full_name.getText().toString();
                String doctor_phone_s=doctor_phone.getText().toString();
                String email_address_s=email_address.getText().toString();
                String doctor_fee_s=doctor_fee.getText().toString();
                String doctor_info_s=doctor_info.getText().toString();
                boolean doctor_status_s=false;
                if (doctor_status.getSelectedItem().toString().contains(getResources().getString(R.string.active))){
                    doctor_status_s=true;
                }

                boolean doctor_accept_code_s=false;
                if (doctor_accept_code.getSelectedItem().toString().contains(getResources().getString(R.string.Accepts_the_discount_code))){
                    doctor_accept_code_s=true;
                }
                JSONArray rating_list=new JSONArray();
                JSONArray review_list=new JSONArray();
                JSONObject hospital=new JSONObject();
                JSONObject speciality=new JSONObject();

                try {
                    hospital.put("id",0);
                    hospital.put("name","");
                    speciality.put("id",selected_speciality.id);
                    speciality.put("name",selected_speciality.name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (selected_image.length()>0&&doctor_code_s.length()>0&&full_name_s.length()>0&&doctor_phone_s.length()>0
                        &&email_address_s.length()>0 &&doctor_fee_s.length()>0&&doctor_info_s.length()>0
                        &&selected_speciality!=null){
                    add_doctor(full_name_s,doctor_status_s,hospital,doctor_phone_s,email_address_s,selected_image,
                            doctor_accept_code_s,rating_list,review_list, Integer.parseInt(doctor_fee_s),doctor_info_s,doctor_code_s);
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



        doctor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_images();
            }
        });
       fill_discount_code();
       fill_status();
        

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void select_images()
    {
        Pix.start(this,
                PICK_IMAGE_MULTIPLE,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (String uri:returnValue){
                try {
                    selected_image=uri;
                    Picasso.with(getActivity())
                            .load(uri)
                            .placeholder(R.drawable.user)
                            .into(doctor_image, new Callback() {
                                @Override
                                public void onSuccess() {}
                                @Override public void onError() {
                                }
                            });

                } catch (Exception e) {
                    Log.w("errrrrrrror",e.getMessage());
                }
            }
        }


    }
    private void add_doctor(final String name, final boolean active, final JSONObject hospital, final String phone
                            , final String email , final String image_url, final boolean doctor_accept_code
            , final JSONArray rating_list
            , final JSONArray review_list, final int doctor_fee_s, final String doctor_info_s, final String doctor_code_s)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/doctors/add";
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
                        object.put("phone",phone);
                        object.put("email",email);
                        object.put("image_url",image_url);
                        object.put("doctor_accept_code",doctor_accept_code);
                        object.put("rating_list",rating_list);
                        object.put("review_list",review_list);
                        object.put("doctor_fee_s",doctor_fee_s);
                        object.put("doctor_info_s",doctor_info_s);
                        object.put("doctor_code_s",doctor_code_s);

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
    private void fill_status(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(getActivity().getResources().getString(R.string.none));
        spinnerArray.add(getActivity().getResources().getString(R.string.active));
        spinnerArray.add(getActivity().getResources().getString(R.string.not_active));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctor_status.setAdapter(adapter);

    }
    private void fill_discount_code(){
        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray2.add(getActivity().getResources().getString(R.string.none));
        spinnerArray2.add(getActivity().getResources().getString(R.string.Accepts_the_discount_code));
        spinnerArray2.add(getActivity().getResources().getString(R.string.not_Accepts_the_discount_code));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray2);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctor_accept_code.setAdapter(adapter2);
    }
}
