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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.JsonArray;
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
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_defintions;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.clinic_SpinAdapter;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_clinic_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.ApiConfig;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class hospital_doctor_definition extends Fragment implements pass_speciality_data,pass_clinic_data {
    private View view;
    private ImageView doctor_image;
    private EditText doctor_code,full_name,doctor_phone,email_address,doctor_fee,doctor_info;
    private Spinner doctor_status,doctor_accept_code;
    private Button clinics,speciality,save,cancel;
    private TextView back,number_of_notifications,notifications;
    private RadioGroup group;
    private RadioButton selected_gender;

    int PICK_IMAGE_MULTIPLE = 1;
    private pass_speciality_data mListener;
    private pass_clinic_data clinic_mListener;
    private clinic_model clinic_model;
    private patient_speciality_model speciality_model;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private String selected_image_url="";

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
        group=(RadioGroup)view.findViewById(R.id.radioGroup);
        selected_gender=(RadioButton)view.findViewById(R.id.male);

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
        clinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                search_clinics_BottomSheetFragment bottomSheetFragment = new search_clinics_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                clinic_mListener=bottomSheetFragment;
                clinic_mListener.pass_data(null,hospital_doctor_definition.this);

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
                String gender=selected_gender.getText().toString();
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
                JSONObject clinic=new JSONObject();
                if (selected_image_url.length()>0&&doctor_code_s.length()>0&&full_name_s.length()>0&&doctor_phone_s.length()>0
                        &&email_address_s.length()>0 &&doctor_fee_s.length()>0&&doctor_info_s.length()>0
                        &&speciality_model!=null&&clinic_model!=null){
                try {
                    hospital.put("id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                    hospital.put("name",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("name",""));
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
                    clinic.put("city",clinic_model.city);
                    clinic.put("gov",clinic_model.gov);
                    clinic.put("hospital",clinic_model.hospital);
                    clinic.put("doctor_list",clinic_model.doctor_list);
                    clinic.put("image_url",clinic_model.image_url);
                    clinic.put("email",clinic_model.email);
                    clinic.put("insurance_company_list",clinic_model.insurance_company_list);
                    clinic.put("nurse_list",clinic_model.nurse_list);
                    clinic.put("website",clinic_model.website);
                    clinic.put("phone",clinic_model.phone);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                    add_doctor(full_name_s,doctor_status_s,hospital,doctor_phone_s,email_address_s,selected_image_url,
                            doctor_accept_code_s,rating_list,review_list
                            , Integer.parseInt(doctor_fee_s),doctor_info_s
                            ,doctor_code_s,speciality,gender,clinic);
                }
            }
        });
        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment bottomSheetFragment = new search_specilty_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(null,hospital_doctor_definition.this);


            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male) {
                    selected_gender=(RadioButton)view.findViewById(R.id.male);

                } else if(checkedId == R.id.female) {

                    selected_gender=(RadioButton)view.findViewById(R.id.female);

                }
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
                    selected_image_url=uri;
                   uploadImage(selected_image_url);

                } catch (Exception e) {
                    Log.w("errrrrrrror",e.getMessage());
                }
            }
        }


    }
    private void add_doctor(final String name, final boolean active, final JSONObject hospital, final String phone
                            , final String email , final String image_url, final boolean doctor_accept_code
                            , final JSONArray rating_list,final JSONArray review_list, final int doctor_fee_s
                            , final String doctor_info_s,final String doctor_code_s, final JSONObject speciality,
                            final String gender,final JSONObject clinic )
    {


        try {
            String url = "http://microtec1.egytag.com/api/doctors/add";
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
                                object.put("id",res.getInt("id"));
                                object.put("_id",res.getString("_id"));
                                object.put("speciality",speciality);
                                object.put("gender",gender);
                                object.put("clinic",clinic);

                                mprogressBar.setVisibility(View.VISIBLE);
                                update_clinics(new JSONArray(clinic_model.doctor_list).put(object),clinic_model.id,object);
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
                        object.put("mobile",phone);
                        object.put("email",email);
                        object.put("image_url",image_url);
                        object.put("accept_code",doctor_accept_code);
                        object.put("rating_list",rating_list);
                        object.put("review_list",review_list);
                        object.put("fee",doctor_fee_s);
                        object.put("info",doctor_info_s);
                        object.put("code",doctor_code_s);
                        object.put("speciality",speciality);
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
    private void update_clinics(final JSONArray doctor_list, final int id, final JSONObject object)
    {


        try {
            String url = "http://microtec1.egytag.com/api/clinics/update";
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
                                mprogressBar.setVisibility(View.VISIBLE);
                                JSONArray array=new JSONArray(getActivity().getSharedPreferences("personal_data",MODE_PRIVATE)
                                        .getString("doctor_list",""));
                                array.put(object);
                                update_hospital(array,getActivity().getSharedPreferences("personal_data",MODE_PRIVATE).getInt("id",0));
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
            String url = "http://microtec1.egytag.com/api/hospitals/update";
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
                                hospital_doctor_definition.this.doctor_accept_code.setSelection(0);
                                clinics.setText("");
                                speciality.setText("");
                                doctor_image.setImageResource(R.drawable.user);
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
                            .load("http://microtec1.egytag.com"+selected_image_url)
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
