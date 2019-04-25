package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.hospital_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.nurse_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_search_results_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality;
    private RecyclerView search_result_recycler;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private List<search_result_list_model> filtered_contact_list = new ArrayList<>();
    private patient_search_result_list_adapter data_adapter;
    private RequestQueue queue;
    private int city_i,governorate_i,speciality_i,insurance_company_i;
    private String name, city_s,governorate_s,speciality_s,insurance_company_s;
    private boolean visitor;
    private Button filter, insurance_company,degree;
    private RadioButton selected_gender;
    private RecyclerView dialog_list;
    private insurance_SpinAdapter adapter;
    private List<patient_insurance_model> insurance_model_list = new ArrayList<>();
    private List<patient_insurance_model> filtered_insurance_model_list = new ArrayList<>();
    private ProgressBar mprogressBar;







    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args!=null) {
            city_i = args.getInt("city");
            governorate_i = args.getInt("governorate");
            speciality_i = args.getInt("speciality");
            insurance_company_i= args.getInt("insurance_company");
            city_s = args.getString("city_s");
            governorate_s = args.getString("governorate_s");
            speciality_s = args.getString("speciality_s");
            insurance_company_s = args.getString("insurance_company_s");
            name=args.getString("name");
            visitor=args.getBoolean("visitor");
            Log.w("gggggg", String.valueOf(governorate_s));
            Log.w("cccccccccc", String.valueOf(city_s));
            Log.w("sssssssssss", String.valueOf(speciality_s));
            Log.w("iiiiiiiiii", String.valueOf(insurance_company_s));
            Log.w("nnnnnnnnn", String.valueOf(name));

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
        filter=(Button)view.findViewById(R.id.filters);
        search_result_recycler = view.findViewById(R.id.search_result_recycler);
        data_adapter = new patient_search_result_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        search_result_recycler.setLayoutManager(mLayoutManager);
        search_result_recycler.setItemAnimator(new DefaultItemAnimator());
        search_result_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        search_result_recycler.setAdapter(data_adapter);
        search_result_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), search_result_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (!visitor) {
                    Bundle args = new Bundle();
                    args.putInt("doctor_id", contact_list.get(position).doctor_model.id);
                    args.putString("doctor_availabilty", contact_list.get(position).doctor_model.doctor_availabilty);
                    args.putString("doctor_graduated", contact_list.get(position).doctor_model.doctor_graduated);
                    args.putString("doctor_image", contact_list.get(position).doctor_model.doctor_image);
                    args.putString("doctor_location", contact_list.get(position).patient_gov_model.name+contact_list.get(position).patient_city_model.name);
                    args.putString("doctor_name", contact_list.get(position).doctor_model.doctor_name);
                    args.putString("doctor_speciality", contact_list.get(position).patient_speciality_model.name);
                    args.putInt("doctor_speciality_id", speciality_i);
                    args.putBoolean("doctor_accept_discount", contact_list.get(position).doctor_model.doctor_accept_discount);
                    args.putDouble("doctor_fee", contact_list.get(position).doctor_model.doctor_fee);
                    args.putFloat("doctor_rating", contact_list.get(position).doctor_model.doctor_rating);
                    args.putString("doctor_notes", contact_list.get(position).doctor_model.doctor_notes);

                    args.putInt("insurance_company",insurance_company_i);
                    args.putInt("governorate", governorate_i);
                    args.putInt("speciality", speciality_i);
                    args.putInt("city",city_i);
                    args.putString("city_s", city_s);
                    args.putString("governorate_s",governorate_s);
                    args.putString("speciality_s", speciality_s);
                    args.putString("insurance_company_s", insurance_company_s);
                    args.putString("name", name);
                    args.putString("review_list", contact_list.get(position).doctor_model.review_list);



                    Fragment doctor_profile = new patient_doctor_data_fragment();
                    doctor_profile.setArguments(args);
                    go_to(doctor_profile);
                }else {
                    Toast.makeText(getActivity(),getResources().getText(R.string.please_sign_in_first),Toast.LENGTH_LONG).show();
                }
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

                View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

                final BottomSheetDialog dialog=new BottomSheetDialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                insurance_company=(Button)dialog.findViewById(R.id.insurance_company);
                degree=(Button)dialog.findViewById(R.id.degree);
                final Button filter_it=(Button)dialog.findViewById(R.id.filters);
                RadioGroup group=(RadioGroup)dialog.findViewById(R.id.radioGroup);
                final EditText fee_from=(EditText)dialog.findViewById(R.id.fee_from);
                final EditText fee_to=(EditText)dialog.findViewById(R.id.fee_to);
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.male) {
                            selected_gender=(RadioButton)dialog.findViewById(R.id.male);

                        } else if(checkedId == R.id.female) {

                            selected_gender=(RadioButton)dialog.findViewById(R.id.female);

                        }
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
                        adapter=new insurance_SpinAdapter(getActivity(),insurance_model_list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        dialog_list.setLayoutManager(mLayoutManager);
                        dialog_list.setItemAnimator(new DefaultItemAnimator());
                        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                        dialog_list.setAdapter(adapter);
                        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View v, final int position) {
                                if (search.getText().length()>0) {
                                    insurance_company_i = filtered_insurance_model_list.get(position).id;
                                    speciality_s = filtered_insurance_model_list.get(position).name;
                                    speciality.setText(speciality_s);
                                    dialog.dismiss();
                                }else {
                                    insurance_company_i = insurance_model_list.get(position).id;
                                    speciality_s = insurance_model_list.get(position).name;
                                    speciality.setText(speciality_s);
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

                filter_it.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filtered_contact_list.clear();
                        if (fee_from.getText().toString().length()>0&&fee_to.getText().toString().length()>0) {

                            filtered_contact_list.clear();
                                for (search_result_list_model model:contact_list){
                                    if (model.doctor_model.doctor_fee <= Integer.parseInt(fee_to.getText().toString())&&model.doctor_model.doctor_fee >= Integer.parseInt(fee_from.getText().toString())) {
                                        filtered_contact_list.add(model);
                                    }
                                }
                                data_adapter.filterList(filtered_contact_list);


                        } else if (fee_from.getText().toString().length()>0&&fee_to.getText().toString().length()==0){


                                    filtered_contact_list.clear();
                                for (search_result_list_model model : contact_list) {
                                    if (model.doctor_model.doctor_fee >= Integer.parseInt(fee_from.getText().toString())) {
                                        filtered_contact_list.add(model);

                                    }
                                }

                                Log.w("dssdasda", fee_from.getText().toString()+filtered_contact_list.size());
                                data_adapter.filterList(filtered_contact_list);
                        }else if (fee_to.getText().toString().length()>0&&fee_from.getText().toString().length()==0){

                                        filtered_contact_list.clear();
                                        for (search_result_list_model model:contact_list){
                                            if (model.doctor_model.doctor_fee <= Integer.parseInt(fee_to.getText().toString())) {
                                                filtered_contact_list.add(model);
                                            }
                                        }
                                        data_adapter.filterList(filtered_contact_list);

                            }else{
                            data_adapter.filterList(contact_list);
                        }
                        if (insurance_company_i>0){
                            if (filtered_contact_list.size()>0) {
                                for (search_result_list_model model : filtered_contact_list) {
                                    if (model.patient_insurance_model.id != insurance_company_i) {
                                        filtered_contact_list.remove(model);
                                    }
                                }
                                data_adapter.filterList(filtered_contact_list);
                            }else {
                                for (search_result_list_model model : contact_list) {

                                        if (model.patient_insurance_model.id == insurance_company_i) {
                                            filtered_contact_list.add(model);
                                        }

                                }
                                data_adapter.filterList(filtered_contact_list);
                            }
                        }
                        if (selected_gender!=null){
                            Log.w("sdljhdsakjd",selected_gender.getText().toString());
                            if (filtered_contact_list.size()>0) {
                                for (search_result_list_model model : filtered_contact_list) {
                                    if (!model.doctor_model.doctor_gender.equals(selected_gender.getText().toString())) {
                                        Log.w("sdljhdsakjd",model.doctor_model.doctor_gender+"  "+selected_gender.getText().toString());
                                        filtered_contact_list.remove(model);
                                    }
                                }
                                data_adapter.filterList(filtered_contact_list);
                            }else {
                                for (search_result_list_model model : contact_list) {

                                    if (model.doctor_model.doctor_gender.equals(selected_gender.getText().toString())) {
                                        filtered_contact_list.add(model);
                                    }

                                }
                                data_adapter.filterList(filtered_contact_list);
                            }
                        }









                        dialog.dismiss();
                    }
                });




                dialog.show();






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

       location.setText(governorate_s+" , "+city_s);
       speciality.setText(speciality_s);



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
            String url = "http://microtec1.egytag.com:30001/api/hospitals/all";
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
                                    String hospital_image_url="http://microtec1.egytag.com:30001"+hospital.getString("image_url");
                                    String hospital_name=new String(hospital.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String hospital_address=hospital.getString("address");
                                    String hospital_phone = null;
                                    if (hospital.has("phone")) {
                                         hospital_phone = hospital.getString("phone");
                                    }
                                    String hospital_mobile=hospital.getString("mobile");
                                    hospital_model=new hospital_model(hospital__id,hospital_image_url,hospital_name,hospital_address,hospital_phone,hospital_mobile,hospital_id);
                                    //gov
                                    JSONObject gov=hospital.getJSONObject("gov");
                                    String gov__id=gov.getString("_id");
                                    int gov_id=gov.getInt("id");
                                    String gov_name= new String(gov.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    patient_gov_model=new patient_gov_model(gov__id,"",gov_name,gov_id);
                                    //city

                                    JSONObject city=hospital.getJSONObject("city");
                                    String city__id=city.getString("_id");
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
                                        int doctor_id=doctor.getInt("id");
                                        String doctor_name=new String (doctor.getString("name").getBytes("ISO-8859-1"), "UTF-8");
//                                        String doctor_image= "http://microtec1.egytag.com"+doctor.getString("image_url");
                                        JSONObject specialty=doctor.getJSONObject("specialty");
                                        String specialty__id=specialty.getString("_id");
                                        int specialty_id=specialty.getInt("id");
                                        JSONArray review_list=new JSONArray();
                                        if (doctor_obj.has("review_list")){
                                            review_list=doctor_obj.getJSONArray("review_list");
                                        }
                                        String specialty_name=new String (specialty.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                        doctor_model=new doctor_model(doctor_name,"","","",false,4.5f,200
                                        ,doctor_id,"","Male",review_list.toString());
                                        patient_speciality_model=new patient_speciality_model(specialty__id,"ss",specialty_name,specialty_id);
                                    }

                                    JSONArray nurse_list=hospital.getJSONArray("nurse_list");

                                    for (int n=0;n<nurse_list.length();n++){
                                        JSONObject nurse_obj=nurse_list.getJSONObject(n);
                                        JSONObject nurse=nurse_obj.getJSONObject("nurse");
                                        String nurse__id=nurse.getString("_id");
                                        int nurse_id=nurse.getInt("id");
                                        String nurse_name=new String (nurse.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                        JSONObject specialty=nurse.getJSONObject("specialty");
                                        String specialty__id=specialty.getString("_id");
                                        int specialty_id=specialty.getInt("id");
                                        String specialty_name=new String (specialty.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                        nurse_model=new nurse_model(nurse__id,nurse_name,nurse_id);
                                    }

                                    search_result_list_model doctor=new search_result_list_model(hospital_model,doctor_model
                                            ,patient_speciality_model,patient_insurance_model,
                                            patient_gov_model,patient_city_model,nurse_model);

                                    contact_list.add(doctor);


                                }
                                data_adapter.notifyDataSetChanged();

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



                        if(speciality_i>0||city_i>0||governorate_i>0||insurance_company_i>0){
                            JSONObject where=new JSONObject();
                            if (speciality_i>0){
                                where.put("doctor_list.doctor.specialty.id", speciality_i);

                            }

                            if (city_i>0){
                                where.put("city", city_i);

                            }



                            if (insurance_company_i>0){
                                where.put("insurance_company_list.insurance_company.id", insurance_company_i);

                            }
                            if (governorate_i>0){
                                where.put("gov.id", governorate_i);
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
    private void filter(String text)
    {
        filtered_insurance_model_list.clear();
        for (patient_insurance_model item : insurance_model_list) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_insurance_model_list.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_insurance_model_list.add(item);
                }
            }

        }

        adapter.filterList(filtered_insurance_model_list);
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


}
