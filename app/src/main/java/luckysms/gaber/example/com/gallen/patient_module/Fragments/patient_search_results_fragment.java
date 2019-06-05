package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_filter_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.hospital_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.nurse_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_search_results_fragment extends Fragment implements pass_filter_data {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality,text;
    private RecyclerView search_result_recycler;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private List<search_result_list_model> filtered_contact_list = new ArrayList<>();
    private patient_search_result_list_adapter data_adapter;
    private RequestQueue queue;
    private patient_speciality_model speciality_model;
    private patient_gov_model gov_model;
    patient_city_model city_model;
    private String name;
    private boolean visitor;
    private Button filter,clearfilters;
    private ProgressBar mprogressBar;
    private pass_filter_data mListener;







    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args!=null) {
            if ((patient_city_model) args.getSerializable("city")!=null){

                city_model = (patient_city_model) args.getSerializable("city");
            }
            if ((patient_gov_model) args.getSerializable("governorate")!=null){
                gov_model = (patient_gov_model) args.getSerializable("governorate");

            }

            speciality_model = (patient_speciality_model) args.getSerializable("speciality");
            name=args.getString("name");
            visitor=args.getBoolean("visitor");

        }


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_and_speciality_result_fragment, container, false);

        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        location=(TextView)view.findViewById(R.id.location);
        speciality=(TextView)view.findViewById(R.id.speciality);
        text=(TextView)view.findViewById(R.id.text);
        filter=(Button)view.findViewById(R.id.filters);
        clearfilters=(Button)view.findViewById(R.id.clearfilters);
        search_result_recycler = view.findViewById(R.id.search_result_recycler);
        data_adapter = new patient_search_result_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        search_result_recycler.setLayoutManager(mLayoutManager);
        search_result_recycler.setItemAnimator(new DefaultItemAnimator());
        search_result_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        search_result_recycler.setAdapter(data_adapter);
        search_result_recycler.setHasFixedSize(true);
        search_result_recycler.setItemViewCacheSize(20);
        search_result_recycler.setDrawingCacheEnabled(true);
        search_result_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        search_result_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), search_result_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                    Bundle args = new Bundle();
                    if (contact_list.get(position)!=null) {
                        args.putSerializable("doctor", contact_list.get(position).doctor_model);
                        args.putSerializable("hospital", contact_list.get(position).hospital_model);
                        args.putSerializable("insurance_company", contact_list.get(position).patient_insurance_model);
                    }
                    if (gov_model!=null) {
                        args.putSerializable("governorate", gov_model);
                    }
                    if (speciality_model!=null) {
                        args.putSerializable("speciality", speciality_model);
                    }
                    if (city_model!=null) {
                        args.putSerializable("city", city_model);
                    }
                    if (visitor){
                        args.putBoolean("visitor",true);
                    }

                    args.putString("name", name);
                    Log.w("dasadsddsa",contact_list.get(position).doctor_model.review_list);



                    Fragment doctor_profile = new patient_doctor_data_fragment();
                    doctor_profile.setArguments(args);
                    go_to(doctor_profile);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search();
                Bundle args=new Bundle();
                if (visitor){
                    args.putBoolean("visitor",true);
                }
                fragment.setArguments(args);
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle s=new Bundle();
                filters_BottomSheetFragment bottomSheetFragment = new filters_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(contact_list,patient_search_results_fragment.this);

                    }
                });


        clearfilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().getSharedPreferences("filters",MODE_PRIVATE).edit()
                        .putInt("fee",0)
                        .putInt("gender",0)
                        .putInt("insurance_company_id",0)
                        .putString("insurance_company_name","")
                        .putString("insurance_company_image_url","")
                        .putString("insurance_company__id","")
                        .commit();

            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search();
                    Bundle args=new Bundle();
                    if (visitor){
                        args.putBoolean("visitor",true);
                    }
                    fragment.setArguments(args);
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        mprogressBar.setVisibility(View.VISIBLE);
        if (gov_model!=null && city_model!=null) {
            location.setText(gov_model.name + " , " + city_model.name);
        }else if (gov_model!=null && city_model==null){
            location.setText(gov_model.name);
        }else if (gov_model==null && city_model!=null){
            location.setText(city_model.gov_name + " , " + city_model.name);
        }
        if(speciality_model!=null) {
            speciality.setText(speciality_model.name);
        }


        search();

        return view;
    }
    public void go_to(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }
    private void search()
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/hospitals/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    mprogressBar.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject res = new JSONObject(response);

                        Log.w("dsakjbsdahk", res.toString());

                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                contact_list.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject hospital=list.getJSONObject(i);

                                    hospital_model hospital_model= null;
                                    doctor_model doctor_model = null;
                                    patient_speciality_model patient_speciality_model = null;
                                    patient_insurance_model patient_insurance_model= null;
                                    patient_gov_model patient_gov_model= null;
                                    patient_city_model patient_city_model= null;
                                    nurse_model nurse_model = null;



                                    //hospital
                                    String  hospital__id=hospital.getString("_id");
                                    int  hospital_id=hospital.getInt("id");
                                    String hospital_image_url=hospital.getString("image_url");
                                    String hospital_name=new String(hospital.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String hospital_address="";
                                    if (hospital.has("address")){hospital_address=hospital.getString("address");}
                                    String hospital_phone = null;
                                    if (hospital.has("phone")) {
                                         hospital_phone = hospital.getString("phone");
                                    }
                                    String hospital_mobile=hospital.getString("mobile");
                                    String hospital_latitude=hospital.getString("latitude");
                                    String hospital_longitude=hospital.getString("longitude");
                                    hospital_address = hospital.getString("address");
                                    hospital_model=new hospital_model(hospital__id,
                                            hospital_image_url,
                                            hospital_name,
                                            hospital_address,
                                            hospital_phone,
                                            hospital_mobile,
                                            hospital_id,
                                            hospital_latitude,
                                            hospital_longitude);
                                    //gov
                                    JSONObject gov=hospital.getJSONObject("gov");
                                    String gov__id="";
                                    int gov_id=gov.getInt("id");
                                    String gov_name= new String(gov.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    patient_gov_model=new patient_gov_model(gov__id,"",gov_name,gov_id);
                                    //city

                                    JSONObject city=hospital.getJSONObject("city");
                                    String city__id="";
                                    int city_id=city.getInt("id");
                                    String city_name=new String(city.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    patient_city_model=new patient_city_model(city__id,"",city_name,gov__id,gov_name,city_id,gov_id);





                                    //insurance

                                    JSONArray insurance_company_list=hospital.getJSONArray("insurance_company_list");
                                    Log.w("sadkkdsa", String.valueOf(insurance_company_list.length()));
                                    for (int j=0;j<insurance_company_list.length();j++){
                                        JSONObject insurance_company_obj=insurance_company_list.getJSONObject(j);
                                        if (insurance_company_obj.has("insurance_company")) {
                                            JSONObject insurance_company = insurance_company_obj.getJSONObject("insurance_company");
                                            String insurance_company__id = insurance_company.getString("_id");
                                            int insurance_company_id = insurance_company.getInt("id");
                                            String insurance_company_name = new String(insurance_company.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                            patient_insurance_model = new patient_insurance_model(insurance_company__id, "", insurance_company_name, insurance_company_id);
                                        }else {
                                            patient_insurance_model = new patient_insurance_model("dssd", "", "none", 0);

                                        }
                                    }

                                    //doctor_list

                                    JSONArray doctor_list=hospital.getJSONArray("doctor_list");

                                    for (int d=0;d<doctor_list.length();d++){
                                        JSONObject doctor_obj=doctor_list.getJSONObject(d);
                                        JSONObject doctor=doctor_obj.getJSONObject("doctor");
                                        String doctor__id=doctor.getString("_id");
                                        String doctor_name=new String(doctor.getString("name") .getBytes("ISO-8859-1"), "UTF-8");
                                        int doctor_id=doctor.getInt("id");
                                        boolean doctor_active=doctor.getBoolean("active");
                                        boolean doctor_accept_code=doctor.getBoolean("accept_code");
                                        String doctor_phone=doctor.getString("phone");
                                        String doctor_info=doctor.getString("info");
                                        String doctor_gender=doctor.getString("gender");
                                        String doctor_code=doctor.getString("code");
                                        double doctor_fee=doctor.getDouble("fee");
                                        String doctor_image=doctor.getString("image_url");
                                        JSONObject specialty=doctor.getJSONObject("specialty");
                                        String specialty__id=specialty.getString("_id");
                                        int specialty_id=specialty.getInt("id");
                                        Float rate=0f;
                                        JSONArray review_list=new JSONArray();
                                        if (doctor.has("review_list")){
                                            review_list=doctor.getJSONArray("review_list");
                                            Log.w("dasadsddsa",review_list.toString());

                                            for (int s=0;s<review_list.length();s++){
                                                JSONObject rates=review_list.getJSONObject(s);
                                                rate=rate+rates.getInt("rate");
                                            }
                                            rate=rate/review_list.length();

                                        }


                                        String specialty_name=new String (specialty.getString("name")
                                                .getBytes("ISO-8859-1"), "UTF-8");
                                        String availability=getResources().getString(R.string.not_active);
                                        if (doctor_active){
                                            availability=getResources().getString(R.string.active);
                                        }
                                        JSONArray nurse_list=hospital.getJSONArray("nurse_list");

                                        for (int n=0;n<nurse_list.length();n++){
                                            JSONObject nurse=nurse_list.getJSONObject(n);

                                            String nurse__id="";
                                            if (nurse.has("_id")){nurse__id=nurse.getString("_id");}
                                            int nurse_id=0;
                                            if (nurse.has("id")){nurse_id=nurse.getInt("id");}
                                            String nurse_name="";
                                            if (nurse.has("name")){nurse_name=new String (nurse.getString("name").getBytes("ISO-8859-1"), "UTF-8");}

                                            nurse_model=new nurse_model(nurse__id,nurse_name,nurse_id);
                                        }
                                        doctor_model=new doctor_model(doctor_name,availability
                                                ,""
                                                ,doctor_image
                                                ,doctor_accept_code,
                                                rate
                                                ,doctor_fee
                                                ,doctor_id,
                                                doctor_info,
                                                doctor_gender,
                                                new String(review_list.toString().getBytes("ISO-8859-1"), "UTF-8"));
                                        patient_speciality_model=new patient_speciality_model(specialty__id,
                                                "ss",specialty_name,specialty_id);
                                        search_result_list_model doctor_model_res=new search_result_list_model(
                                                hospital_model,doctor_model
                                                ,patient_speciality_model,patient_insurance_model,
                                                patient_gov_model,patient_city_model,nurse_model);

                                        Log.w("dsakjbsdahk",doctor_model_res.doctor_model.doctor_name);
                                        if (speciality_model!=null) {
                                            if (speciality_model.id == specialty_id) {
                                                contact_list.add(doctor_model_res);

                                            }
                                        }else {

                                            if (doctor_model.doctor_name.contains(name)) {
                                                contact_list.add(doctor_model_res);

                                            }
                                        }


                                    }




                                }
                                if (contact_list.size()>0) {
                                    data_adapter.notifyDataSetChanged();
                                    search_result_recycler.setVisibility(View.VISIBLE);
                                    text.setVisibility(View.GONE);
                                }else {
                                    search_result_recycler.setVisibility(View.GONE);
                                    text.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
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
                        if(name != null && !name.isEmpty()) {
                            JSONObject name_object=new JSONObject();
                            name_object.put("doctor_list.doctor.name", name);
                            object.put("where", name_object);
                        }



                        if(speciality_model!=null||city_model!=null||gov_model!=null){
                            JSONObject where=new JSONObject();
                            if (speciality_model!=null){
                                where.put("doctor_list.doctor.specialty.id", speciality_model.id);

                            }

                            if (city_model!=null){
                                where.put("city", city_model.id);

                            }

                            if (gov_model!=null){
                                where.put("gov.id", gov_model.id);
                            }
                            object.put("where",where);

                        }


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
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringReq);


        } catch (Exception e) {

        }


    }
    @Override
    public void pass_data(List<search_result_list_model> contact_list,pass_filter_data listner) {
        this.mListener=listner;
    }

    @Override
    public void notify_adapter(pass_filter_data listner,List<search_result_list_model> contact_list ) {
        data_adapter.filterList(contact_list);
        data_adapter.notifyDataSetChanged();
    }


}
