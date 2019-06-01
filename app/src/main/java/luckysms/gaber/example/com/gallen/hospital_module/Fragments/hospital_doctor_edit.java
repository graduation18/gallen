package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_clinic_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.ApiConfig;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class hospital_doctor_edit extends Fragment implements pass_speciality_data,pass_clinic_data {
    private View view;
    private ImageView doctor_image;
    private EditText doctor_code,full_name,doctor_phone,email_address,doctor_fee,doctor_info;
    private Spinner doctor_status,doctor_accept_code;
    private Button clinics,speciality,save,cancel;
    private TextView back,number_of_notifications,notifications;
    int PICK_IMAGE_MULTIPLE = 1;
    private pass_speciality_data mListener;
    private pass_clinic_data clinic_mListener;
    private clinic_model clinic_model;
    private patient_speciality_model speciality_model;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private String _id,selected_image_url,doctor_name_s,doctor_availabilty_s,doctor_gender_s
            ,doctor_graduated_s,doctor_image_s,doctor_notes_s,dotor_code_s,review_list_s,doctor_email_s,doctor_phone_s;
    private boolean doctor_accept_discount_b;
    private double doctor_fee_d;
    private Float doctor_rating_f;
    private int id_i;
    List<String> status_Array =  new ArrayList<String>();
    List<String> discount_code_Array =  new ArrayList<String>();
    private boolean majorchange;
    private clinic_model old_clinic;





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null) {
            doctor_name_s=getArguments().getString("doctor_name");
            doctor_availabilty_s=getArguments().getString("doctor_availabilty");
            doctor_gender_s=getArguments().getString("doctor_gender");
            doctor_graduated_s=getArguments().getString("doctor_graduated");
            doctor_image_s= getArguments().getString("doctor_image");
            selected_image_url=doctor_image_s;
            doctor_notes_s=getArguments().getString("doctor_notes");
            dotor_code_s=getArguments().getString("dotor_code");
            doctor_phone_s=getArguments().getString("doctor_phone");
            doctor_email_s=getArguments().getString("doctor_email");
            review_list_s= getArguments().getString("review_list");
            speciality_model= (patient_speciality_model) getArguments().getSerializable("speciality");
            clinic_model=(clinic_model)getArguments().getSerializable("clinic");
            old_clinic=clinic_model;
            doctor_accept_discount_b= getArguments().getBoolean("doctor_accept_discount");
            doctor_fee_d= getArguments().getDouble("doctor_fee");
            doctor_rating_f=getArguments().getFloat("doctor_rating");
            id_i=getArguments().getInt("id");
            _id=getArguments().getString("_id");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_doctor_definition_edit, container, false);
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
                Bundle bundle=new Bundle();
                bundle.putString("doctor_name",doctor_name_s);
                bundle.putString("doctor_availabilty",doctor_availabilty_s);
                bundle.putString("doctor_gender",doctor_gender_s);
                bundle.putString("doctor_graduated",doctor_graduated_s);
                bundle.putString("doctor_image",doctor_image_s);
                bundle.putString("_id",_id);
                bundle.putString("doctor_notes",doctor_notes_s);
                bundle.putString("dotor_code",dotor_code_s);
                bundle.putString("doctor_phone",doctor_phone_s);
                bundle.putString("review_list",review_list_s);
                bundle.putString("doctor_email",doctor_email_s);
                bundle.putSerializable("speciality",speciality_model);
                bundle.putSerializable("clinic",clinic_model);
                bundle.putBoolean("doctor_accept_discount",doctor_accept_discount_b);
                bundle.putDouble("doctor_fee",doctor_fee_d);
                bundle.putFloat("doctor_rating",doctor_rating_f);
                bundle.putInt("id",id_i);
                bundle.putString("_id",_id);
                Fragment fragment=new hospital_doctor_edit_delete();
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
                String gender=doctor_gender_s;
                boolean doctor_status_s=false;
                if (doctor_status.getSelectedItem().toString().equals(getResources().getString(R.string.active))){
                    doctor_status_s=true;
                }

                boolean doctor_accept_code_s=false;
                if (doctor_accept_code.getSelectedItem().toString().equals(getResources().getString(R.string.Accepts_the_discount_code))){
                    doctor_accept_code_s=true;
                }
                JSONArray rating_list=new JSONArray();
                JSONArray review_list=new JSONArray();
                JSONObject hospital=new JSONObject();
                JSONObject speciality=new JSONObject();
                JSONObject clinic=new JSONObject();
                if (selected_image_url.length()>0&&doctor_code_s.length()>0&&full_name_s.length()>0&&doctor_phone_s.length()>0
                        &&email_address_s.length()>0 &&doctor_fee_s.length()>0
                        &&speciality_model!=null&&clinic_model!=null){
                    try {
                        hospital.put("id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                        hospital.put("name",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("name",""));
                        hospital.put("doctor_list",new JSONArray(getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("doctor_list","")));
                        speciality.put("id",speciality_model.id);
                        speciality.put("name",speciality_model.name);
                        speciality.put("_id",speciality_model._id);
                        speciality.put("image_url",speciality_model.image_url);
                        clinic.put("name",clinic_model.name);
                        clinic.put("id",clinic_model.id);
                        clinic.put("active",clinic_model.active);
                        clinic.put("latitude",clinic_model.latitude);
                        clinic.put("longitude",clinic_model.longitude);
                        clinic.put("address",clinic_model.address);
                        clinic.put("city",new JSONObject(clinic_model.city));
                        clinic.put("gov",new JSONObject(clinic_model.gov));
                        clinic.put("hospital",new JSONObject(clinic_model.hospital));
                        clinic.put("doctor_list",new JSONArray(clinic_model.doctor_list));
                        clinic.put("image_url",clinic_model.image_url);
                        clinic.put("email",clinic_model.email);
                        clinic.put("insurance_company_list",new JSONArray(clinic_model.insurance_company_list));
                        clinic.put("nurse_list",new JSONArray(clinic_model.nurse_list));
                        clinic.put("website",clinic_model.website);
                        clinic.put("phone",clinic_model.phone);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    update_doctor(id_i,_id,full_name_s,doctor_status_s,hospital,doctor_phone_s,email_address_s,selected_image_url,
                            doctor_accept_code_s,rating_list,review_list
                            , Double.parseDouble(doctor_fee_s),doctor_info_s
                            ,doctor_code_s,speciality,gender,clinic);
                }else {
                    if (speciality_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_speciality_first),Toast.LENGTH_LONG).show();
                    }
                    else if (clinic_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_clinic_first),Toast.LENGTH_LONG).show();
                    }
                    else if (doctor_phone_s.length()!=13){
                        if (doctor_phone_s.length()<13){
                            Toast.makeText(getActivity(),getResources().getString(R.string.number_is_too_long),Toast.LENGTH_LONG).show();
                        }else if (doctor_phone_s.length()>13){
                            Toast.makeText(getActivity(),getResources().getString(R.string.number_is_too_short),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_fill_data_fields),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                search_specilty_BottomSheetFragment bottomSheetFragment = new search_specilty_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(null,hospital_doctor_edit.this);
            }
        });
        clinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                search_clinics_BottomSheetFragment bottomSheetFragment = new search_clinics_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                clinic_mListener=bottomSheetFragment;
                clinic_mListener.pass_data(null,hospital_doctor_edit.this);
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

        set_data();
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
                    selected_image_url=uri;
                    mprogressBar.setVisibility(View.VISIBLE);
                    uploadImage(selected_image_url);

                } catch (Exception e) {
                    Log.w("errrrrrrror",e.getMessage());
                }
            }
        }


    }
    private void update_doctor(final int id,final String _id,final String name, final boolean active, final JSONObject hospital, final String phone
            , final String email , final String image_url, final boolean doctor_accept_code
            , final JSONArray rating_list,final JSONArray review_list, final double doctor_fee_s
            , final String doctor_info_s,final String doctor_code_s, final JSONObject speciality,
                               final String gender,final JSONObject clinic)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/doctors/update";
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
                                    JSONObject object=new JSONObject();
                                    object.put("name",name);
                                    object.put("active",active);
                                    object.put("hospital",hospital);
                                    object.put("phone",phone);
                                    object.put("email",email);
                                    object.put("image_url",image_url);
                                    object.put("accept_code",doctor_accept_code);
                                    object.put("rating_list",rating_list);
                                    object.put("review_list",review_list);
                                    object.put("fee",doctor_fee_s);
                                    object.put("info",doctor_info_s);
                                    object.put("code",doctor_code_s);
                                    object.put("id",id);
                                    object.put("_id",_id);
                                    object.put("specialty",speciality);
                                    object.put("gender",gender);
                                    object.put("clinic",clinic);


                                    if (old_clinic.id!=clinic_model.id) {
                                        JSONArray list = new JSONArray();
                                        JSONArray jsonArray = new JSONArray(old_clinic.doctor_list);
                                        int len = jsonArray.length();
                                        if (jsonArray != null) {
                                            for (int i = 0; i < len; i++) {
                                                JSONObject d = new JSONArray(old_clinic.doctor_list).getJSONObject(i);
                                                //Excluding the item at position
                                                if (!d.getString("name").equals(name)) {
                                                    list.put(jsonArray.get(i));
                                                }
                                            }
                                        }
                                        mprogressBar.setVisibility(View.VISIBLE);
                                        update_clinics_remove(list, old_clinic.id);
                                        update_clinics(new JSONArray(clinic_model.doctor_list).put(object), clinic_model.id,object);
                                    }else {
                                        JSONArray list = new JSONArray();
                                        JSONArray jsonArray = new JSONArray(clinic_model.doctor_list);
                                        int len = jsonArray.length();
                                        if (jsonArray != null) {
                                            for (int i = 0; i < len; i++) {
                                                JSONObject d = new JSONArray(clinic_model.doctor_list).getJSONObject(i);
                                                Log.w("dsakjbsdahk", d.toString());
                                                //Excluding the item at position
                                                if (!d.getString("name").equals(name)) {
                                                    list.put(jsonArray.get(i));
                                                }
                                            }
                                        }
                                        mprogressBar.setVisibility(View.VISIBLE);
                                        update_clinics(list.put(object), clinic_model.id,object);
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
                        object.put("id",id);
                        object.put("name",name);
                        object.put("active",active);
                        object.put("hospital",hospital);
                        object.put("phone",phone);
                        object.put("mobile",phone);
                        object.put("email",email);
                        object.put("image_url",image_url);
                        object.put("accept_code",doctor_accept_code);
                        object.put("rating_list",rating_list);
                        object.put("review_list",review_list);
                        object.put("fee",doctor_fee_s);
                        object.put("info",doctor_info_s);
                        object.put("code",doctor_code_s);
                        object.put("specialty",speciality);
                        object.put("gender",gender);
                        object.put("clinic",clinic);

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
    private void update_clinics_remove(final JSONArray doctor_list,final int id)
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
                            if (res.getBoolean("done")) { }
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
                        object.put("id",id);
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
    private void update_clinics(final JSONArray doctor_list, final int id, final JSONObject jsonObject)
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
                                JSONArray list = new JSONArray();
                                JSONArray jsonArray = new JSONArray(getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("doctor_list",""));
                                int len = jsonArray.length();
                                if (jsonArray != null) {
                                    for (int i = 0; i < len; i++) {
                                        JSONObject d = new JSONArray(getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("doctor_list","")).getJSONObject(i);
                                        //Excluding the item at position
                                        if (!d.getString("name").equals(doctor_name_s)) {
                                            list.put(jsonArray.get(i));
                                        }
                                    }
                                }
                                Log.w("dsakjbsdahk", jsonObject.toString());
                                mprogressBar.setVisibility(View.VISIBLE);
                                update_hospital(list.put(jsonObject),getActivity().getSharedPreferences("personal_data",MODE_PRIVATE).getInt("id",0));
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
                        object.put("id",id);
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
    private void update_hospital(final JSONArray doctor_list,final int id)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/hospitals/update";
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
                                doctor_code.setText("");
                                full_name.setText("");
                                doctor_phone.setText("");
                                email_address.setText("");
                                doctor_fee.setText("");
                                doctor_info.setText("");
                                doctor_status.setSelection(0);
                                hospital_doctor_edit.this.doctor_accept_code.setSelection(0);
                                clinics.setText("");
                                speciality.setText("");
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
                        object.put("id",id);
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

    private void fill_status(){
        status_Array.add(getActivity().getResources().getString(R.string.none));
        status_Array.add(getActivity().getResources().getString(R.string.active));
        status_Array.add(getActivity().getResources().getString(R.string.not_active));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, status_Array);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctor_status.setAdapter(adapter);

    }
    private void fill_discount_code(){
        discount_code_Array.add(getActivity().getResources().getString(R.string.none));
        discount_code_Array.add(getActivity().getResources().getString(R.string.Accepts_the_discount_code));
        discount_code_Array.add(getActivity().getResources().getString(R.string.not_Accepts_the_discount_code));

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, discount_code_Array);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctor_accept_code.setAdapter(adapter2);
    }
    private void set_data(){
        doctor_code.setText(dotor_code_s);
        full_name.setText(doctor_name_s);
        doctor_phone.setText(doctor_phone_s);
        email_address.setText(doctor_email_s);
        doctor_fee.setText(String .valueOf(doctor_fee_d));
        doctor_info.setText(doctor_notes_s);
        Log.w("sfasafs",doctor_image_s+" "+dotor_code_s);
        Picasso.with(getActivity())
                .load("http://intmicrotec.neat-url.com:6566"+doctor_image_s)
                .placeholder(R.drawable.user)
                .into(doctor_image, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override public void onError() {
                    }
                });
        doctor_status.setSelection(status_Array.indexOf(doctor_availabilty_s));
        if (doctor_accept_discount_b) {
            doctor_accept_code.setSelection(1);
        }else {
            doctor_accept_code.setSelection(2);
        }

        clinics.setText(clinic_model.name);
        speciality.setText(speciality_model.name);
    }
    private void uploadImage(String imagePath) {
        //creating a file
        File file = new File(imagePath);
        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);
//        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        Log.e("requestFile",requestFile.toString());
        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //creating our api
        ApiConfig api = retrofit.create(ApiConfig.class);
        //creating a call and calling the upload image method
        Call call = api.uploadImage("upload/image/default",body);
        //finally performing the call
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {

                mprogressBar.setVisibility(View.INVISIBLE);
                Log.w("response",new Gson().toJson(response.body()));
                try {
                    JSONObject object=new JSONObject(new Gson().toJson(response.body()));
                    selected_image_url=object.getString("image_url");
                    Picasso.with(getActivity())
                            .load("http://intmicrotec.neat-url.com:6566"+selected_image_url)
                            .placeholder(R.drawable.user)
                            .into(doctor_image, new Callback() {
                                @Override
                                public void onSuccess() {}
                                @Override public void onError() {
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mprogressBar.setVisibility(View.INVISIBLE);
            }


        });
    }
    @Override
    public void pass_data(patient_speciality_model speciality_model, pass_speciality_data listner) {
        this.speciality_model=speciality_model;
        speciality.setText(speciality_model.name);
    }
    @Override
    public void pass_data(luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model clinic_model, pass_clinic_data listner) {
        this.clinic_model=clinic_model;
        clinics.setText(clinic_model.name);
    }


}
