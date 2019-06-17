package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_favourite_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.favourite;
import luckysms.gaber.example.com.gallen.patient_module.Model.hospital_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_favorites extends Fragment {
    private View view;
    private TextView number_of_notifications,notifications,location,speciality,insurance_companies,text;
    private RecyclerView favorite_recycler;
    private List<favourite> contact_list = new ArrayList<>();
    private patient_favourite_list_adapter data_adapter;
    private ProgressBar mprogressBar;
    private RequestQueue queue;
    private LinearLayout favourite_layout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_favourites_doctors_fragment, container, false);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        text=(TextView)view.findViewById(R.id.text);
        mprogressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        favourite_layout=(LinearLayout)view.findViewById(R.id.favourite_layout);
        favorite_recycler = view.findViewById(R.id.favorite_recycler);
        data_adapter = new patient_favourite_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favorite_recycler.setLayoutManager(mLayoutManager);
        favorite_recycler.setItemAnimator(new DefaultItemAnimator());
        favorite_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 10));
        favorite_recycler.setAdapter(data_adapter);
        favorite_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), favorite_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                Bundle args = new Bundle();
                args.putSerializable("doctor", contact_list.get(position).doctor_model);
                Fragment doctor_profile = new patient_favourite_doctor_data_fragment();
                doctor_profile.setArguments(args);
                go_to(doctor_profile);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0)!=0) {
            mprogressBar.setVisibility(View.VISIBLE);
            get_patient_data();
        }

        return view;
    }

    private void get_patient_data()
    {


        try {
            final int[] counter = {0};
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/view";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    mprogressBar.setVisibility(View.INVISIBLE);
                    contact_list.clear();
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject doc=res.getJSONObject("doc");
                                JSONArray favourite_list=doc.getJSONArray("favourite_list");
                                for (int i=0;i<favourite_list.length();i++) {
                                    JSONObject doctor = favourite_list.getJSONObject(i);
                                    int id=doctor.getInt("id");
                                    String name=new String (doctor.getString("name")
                                            .getBytes("ISO-8859-1"), "UTF-8");
                                    String image_url=doctor.getString("image_url");
                                    Float rating=Float.parseFloat(String.valueOf(doctor.getDouble("rating")));
                                    boolean accept_discount= doctor.getBoolean("accept_discount");
                                    String review_list=new String(doctor.getJSONArray("review_list").toString().getBytes("ISO-8859-1"), "UTF-8");
                                    int fee=doctor.getInt("fee");
                                    String gender=doctor.getString("gender");
                                    String notes=new String(doctor.getString("notes").getBytes("ISO-8859-1"), "UTF-8");;
                                    JSONObject hospital=doctor.getJSONObject("hospital");
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
                                    hospital_model hospital_model=new hospital_model(hospital__id,
                                            hospital_image_url,
                                            hospital_name,
                                            hospital_address,
                                            hospital_phone,
                                            hospital_mobile,
                                            hospital_id,
                                            hospital_latitude,
                                            hospital_longitude);

                                    JSONObject specialty=doctor.getJSONObject("specialty");
                                    String specialty__id=specialty.getString("_id");
                                    int specialty_id=specialty.getInt("id");
                                    String specialty_name=new String (specialty.getString("name")
                                            .getBytes("ISO-8859-1"), "UTF-8");
                                    patient_speciality_model patient_speciality_model=new patient_speciality_model(specialty__id,
                                            "ss",specialty_name,specialty_id);
                                    doctor_model doctor_model=new doctor_model(name,"active","",image_url,accept_discount
                                            ,rating,fee,id,notes,gender,review_list);
                                    contact_list.add(new favourite(hospital_model,doctor_model,patient_speciality_model));
                                }
                                data_adapter.notifyDataSetChanged();
                                if (contact_list.size()>0){
                                    favorite_recycler.setVisibility(View.VISIBLE);
                                    text.setVisibility(View.INVISIBLE);
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
                    if (counter[0]<4) {
                        get_patient_data();
                        counter[0]++;
                    }else {
                        Log.w("sadkjsdkjlljksda", error.getMessage());
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                        mprogressBar.setVisibility(View.INVISIBLE);
                    }

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
                        object.put("id",getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0));




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
    public void go_to(Fragment fragment) {
        favourite_layout.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.favorite_frameLayout, fragment)
                .commit();
    }


}
