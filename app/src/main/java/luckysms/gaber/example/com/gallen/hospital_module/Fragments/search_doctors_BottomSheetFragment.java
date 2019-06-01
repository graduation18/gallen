package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.clinic_SpinAdapter;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.hospital_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_clinic_data;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_doctor_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class search_doctors_BottomSheetFragment extends BottomSheetDialogFragment  implements pass_doctor_data {
    private View view;

    private RequestQueue queue;
    private ArrayList<search_doctor_name_model>doctor_name_list=new ArrayList<>();
    private ArrayList<search_doctor_name_model>doctor_name_filteredList=new ArrayList<>();
    private hospital_search_result_list_adapter doctor_adapter;
    private ProgressBar mprogressBar;
    private RecyclerView dialog_list;
    private pass_doctor_data mListener;
    private BottomSheetBehavior mBehavior;
    private search_doctor_name_model search_doctor_name_model;
    private int speciality_id=0;

    public search_doctors_BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            speciality_id=getArguments().getInt("speciality_id",0);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.dialog_list, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);




        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        dialog_list= view.findViewById(R.id.dialog_list);
        final EditText searchh=(EditText)view.findViewById(R.id.search_edt);
        doctor_adapter=new hospital_search_result_list_adapter(getActivity(),doctor_name_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dialog_list.setLayoutManager(mLayoutManager);
        dialog_list.setItemAnimator(new DefaultItemAnimator());
        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        dialog_list.setAdapter(doctor_adapter);
        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                if (searchh.getText().length()>0||speciality_id>0){
                    search_doctor_name_model=doctor_name_filteredList.get(position);
                    mListener.pass_data(search_doctor_name_model,search_doctors_BottomSheetFragment.this);
                    dismiss();
                }else {
                    search_doctor_name_model=doctor_name_list.get(position);
                    mListener.pass_data(search_doctor_name_model,search_doctors_BottomSheetFragment.this);
                    dismiss();
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



        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
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
                                    if (doctor.has("code")){doctor_code=doctor.getString("code");}
                                    Log.w("fasfaasfas",doctor_code);
                                    String  doctor_phone=doctor.getString("phone");
                                    String  doctor_gender=doctor.getString("gender");
                                    double  doctor_fee=doctor.getDouble("fee");
                                    String doctor_name=new String (doctor.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String doctor_image= doctor.getString("image_url");
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
                                            clinic.getDouble("longitude"),
                                            specialty_name,specialty_id
                                    );
                                    doctor_name_list.add(new search_doctor_name_model(doctor_model,speciality_model,clinic_model));
                                }

                                doctor_adapter.notifyDataSetChanged();
                                if (speciality_id>0){
                                    doctor_filter_by_speciality(speciality_id);
                                }


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
    private void doctor_filter_by_speciality(int speciality_id) {
        doctor_name_filteredList.clear();
        for (search_doctor_name_model item : doctor_name_list) {
            if (!item.doctor_model.doctor_name.isEmpty()){
                if (item.speciality_model.id==speciality_id) {
                    doctor_name_filteredList.add(item);
                }
            }else {
                if (item.speciality_model.id==speciality_id) {
                    doctor_name_filteredList.add(item);
                }
            }

        }

        doctor_adapter.filterList(doctor_name_filteredList);
    }


    @Override
    public void pass_data(search_doctor_name_model clinic_model, pass_doctor_data listner) {
        this.mListener=listner;
    }
}