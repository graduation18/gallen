package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_available_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.AsyncTaskLoadImage;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.hospital_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.nurse_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_favourite_doctor_data_fragment extends Fragment   {
    private View view;
    private TextView back,number_of_notifications,notifications
            ,name,speciality,discount_code,rating_ratio,doctor_fee,doctor_info,graduated_from,text;
    private Button vistors_reviews;
    private ImageView image,favorite;
    private RatingBar rating;
    private RecyclerView available_appointments_recycler;
    private List<available_appointments_list_model> contact_list = new ArrayList<>();
    private patient_doctor_available_appointments_list_adapter data_adapter;
    appointment_Listener mCallback;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private boolean fav_added;
    private doctor_model doctor_model;
    private patient_gov_model gov_model;
    private patient_city_model city_model;
    private patient_speciality_model speciality_model;
    private hospital_model hospital_model;
    private patient_insurance_model insurance_model;
    private String clinic_name;
    private int clinic_id;






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null) {
            Bundle args = getArguments();
            doctor_model= (doctor_model) args.getSerializable("doctor");
        }

    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_doctor_data_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        favorite=(ImageView)view.findViewById(R.id.favorite);
        name=(TextView)view.findViewById(R.id.name);
        speciality=(TextView)view.findViewById(R.id.speciality);
        discount_code=(TextView)view.findViewById(R.id.discount_code);
        rating_ratio=(TextView)view.findViewById(R.id.rating_ratio);
        doctor_fee=(TextView)view.findViewById(R.id.doctor_fee);
        doctor_info=(TextView)view.findViewById(R.id.doctor_info);
        graduated_from=(TextView)view.findViewById(R.id.graduated_from);
        text=(TextView)view.findViewById(R.id.text);
        image=(ImageView)view.findViewById(R.id.image);
        rating=(RatingBar)view.findViewById(R.id.rating);
        vistors_reviews=(Button)view.findViewById(R.id.vistors_reviews);
        available_appointments_recycler = view.findViewById(R.id.available_appointments_recycler);
        data_adapter = new patient_doctor_available_appointments_list_adapter(getActivity(), contact_list,false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        available_appointments_recycler.setLayoutManager(layoutManager);
        available_appointments_recycler.setItemAnimator(new DefaultItemAnimator());
        available_appointments_recycler.setAdapter(data_adapter);
        available_appointments_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), available_appointments_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Bundle args = new Bundle();
                args.putString("clinic_name", clinic_name);
                args.putInt("clinic_id", clinic_id);
                args.putBoolean("fav_added", fav_added);
                if (contact_list.get(position) != null) {
                    args.putSerializable("ticket", contact_list.get(position));
                }
                if (doctor_model != null) {
                    args.putSerializable("doctor", doctor_model);
                }
                if (hospital_model != null) {
                    args.putSerializable("hospital", hospital_model);
                }
                if (insurance_model != null) {
                    args.putSerializable("insurance_company", insurance_model);
                }
                if (speciality_model != null) {
                    args.putSerializable("speciality", speciality_model);
                }
                if (gov_model != null) {
                    args.putSerializable("governorate", gov_model);
                }
                if (city_model != null) {
                    args.putSerializable("city", city_model);
                }

                Fragment fragment = new patient_confirm_reservation_fragment();
                fragment.setArguments(args);
                go_to(fragment);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_favorites();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vistors_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                s.putString("review_list",doctor_model.review_list);
                s.putFloat("doctor_rating",doctor_model.doctor_rating);
                s.putInt("id",doctor_model.id);
                reviews_BottomSheetFragment bottomSheetFragment = new reviews_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());



            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                get_patient_data(doctor_model,getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_favorites();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        mprogressBar.setVisibility(View.VISIBLE);

        search();
        return view;
    }


    public void go_to(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.favorite_frameLayout, fragment)
                .commit();
    }
    private void get_data(final int id)
    {


        try {
            final int[] counter = {0};
            String url = "http://intmicrotec.neat-url.com:6566/api/doctors/view";
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
                            //Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject doctor=res.getJSONObject("doc");
                                String doctor__id=doctor.getString("_id");
                                String doctor_name=new String(doctor.getString("name")
                                        .getBytes("ISO-8859-1"), "UTF-8");
                                int doctor_id=doctor.getInt("id");
                                boolean doctor_active=doctor.getBoolean("active");
                                boolean doctor_accept_code=doctor.getBoolean("accept_code");
                                String doctor_phone=doctor.getString("phone");
                                String doctor_info=doctor.getString("info");
                                String doctor_gender=doctor.getString("gender");
                                String doctor_code=doctor.getString("code");
                                double doctor_fee=doctor.getDouble("fee");
                                String doctor_image= "http://intmicrotec.neat-url.com:6566"+doctor.getString("image_url");
                                JSONObject specialty=doctor.getJSONObject("specialty");
                                String specialty__id=specialty.getString("_id");
                                int specialty_id=specialty.getInt("id");

                                JSONObject clinic=doctor.getJSONObject("clinic");
                                clinic_id=clinic.getInt("id");
                                clinic_name=new String (clinic.getString("name").getBytes("ISO-8859-1"), "UTF-8");

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
                                    doctor_model.review_list=
                                            new String(review_list.toString().getBytes("ISO-8859-1"), "UTF-8")
                                    ;
                                }

                                String specialty_name=new String (specialty.getString("name")
                                        .getBytes("ISO-8859-1"), "UTF-8");
                                String availability=getResources().getString(R.string.not_active);
                                if (doctor_active){
                                    availability=getResources().getString(R.string.active);
                                }




                                patient_favourite_doctor_data_fragment.this.doctor_model.doctor_name=doctor_name;
                                patient_favourite_doctor_data_fragment.this.doctor_model.doctor_rating= rate;
                                patient_favourite_doctor_data_fragment.this.doctor_model.doctor_fee=doctor_fee;
                                patient_favourite_doctor_data_fragment.this.doctor_model.doctor_image=doctor_image;
                                patient_favourite_doctor_data_fragment.this.doctor_model.doctor_notes=doctor_info;

                                name.setText(doctor_name);
                                speciality.setText(specialty_name);
                                graduated_from.setText(new String (hospital_model.hospital_address.getBytes("ISO-8859-1"), "UTF-8"));
                                if (doctor_accept_code){
                                    discount_code.setVisibility(View.VISIBLE);
                                }

                                rating.setRating(doctor_model.doctor_rating);
                                rating_ratio.setText(String.valueOf(Math.round(doctor_model.doctor_rating)));
                                patient_favourite_doctor_data_fragment.this.doctor_fee.setText(
                                        patient_favourite_doctor_data_fragment.this.doctor_fee.getText().toString()+" "+String .valueOf(doctor_model.doctor_fee));

                                String url = doctor_image;
                                new AsyncTaskLoadImage(image).execute(url);

                                patient_favourite_doctor_data_fragment.this.doctor_info.setText(doctor_model.doctor_notes);
                                check_for_favourite();



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
                        get_data(doctor_model.id);
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
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }
    private void get_doctor_available_appintments(final int id)
    {

        try {
            final int []counter={0};
            String url = "http://intmicrotec.neat-url.com:6566/api/tickets/all";
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
                            //Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++) {
                                    JSONObject object = list.getJSONObject(i);
                                    int ticket_id = object.getInt("id");
                                    JSONObject selected_time = object.getJSONObject("selected_time");
                                    JSONObject status = new JSONObject();
                                    int status_id = 1;
                                    if (object.has("status")) {
                                        status = object.getJSONObject("status");
                                        status_id =status.getInt("id") ;
                                        Log.w("dssdaasd", String.valueOf(status_id));


                                        if (status.getInt("id") == 0) {
                                            JSONObject day_ob = selected_time.getJSONObject("day");
                                            int day_id = day_ob.getInt("id");
                                            String day_name = day_ob.getString("en");
                                            JSONObject from_obj = selected_time.getJSONObject("from");
                                            int from_id = from_obj.getInt("id");
                                            String from_name = from_obj.getString("en");
                                            JSONObject to_obj = selected_time.getJSONObject("to");
                                            int to_id = to_obj.getInt("id");
                                            String to_name = to_obj.getString("en");
                                            if (object.has("selected_doctor")) {
                                                if (object.getJSONObject("selected_doctor").getInt("id") == doctor_model.id) {
                                                    contact_list.add(new available_appointments_list_model(day_name, from_name, to_name, day_id, from_id, to_id,ticket_id,status.getInt("id")));
                                                }
                                            }

                                        }
                                    }
                                }
                                data_adapter.notifyDataSetChanged();
                                if(contact_list.size()>0){
                                    available_appointments_recycler.setVisibility(View.VISIBLE);
                                    text.setVisibility(View.INVISIBLE);
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
                    if (counter[0]<4) {
                        get_doctor_available_appintments(doctor_model.id);
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
                        JSONObject where=new JSONObject();
                        where.put("status.id",0);
                        object.put("where",where);




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
    private void favourite_update(final doctor_model doctor_model, final int id)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/update";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    mprogressBar.setVisibility(View.INVISIBLE);
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            //Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                Toast.makeText(getActivity(),getResources().getString(R.string.added_to_favourite),Toast.LENGTH_LONG).show();

                                JSONObject doctor=new JSONObject();
                                doctor.put("id",doctor_model.id);
                                doctor.put("name",doctor_model.doctor_name);
                                doctor.put("image_url",doctor_model.doctor_image);
                                doctor.put("fee",doctor_model.doctor_fee);
                                doctor.put("rating",Math.round(doctor_model.doctor_rating));
                                doctor.put("accept_discount",doctor_model.doctor_accept_discount);
                                doctor.put("speciality",speciality.getText().toString());
                                JSONArray favourite_list;
                                String favs=getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("favourite_list","");
                                if (favs.length()>0){
                                    favourite_list=new JSONArray(favs);
                                    if (fav_added){
                                        for (int i=0;i<favourite_list.length();i++){
                                            JSONObject objects=favourite_list.getJSONObject(i);
                                            if (objects.getInt("id")==id){
                                                favourite_list.remove(i);
                                            }
                                        }
                                        favorite.setImageResource( R.drawable.doctor_favourite);
                                        fav_added=false;
                                    }else {
                                        favorite.setImageResource( R.drawable.doctor_favourite_fill);
                                        favourite_list.put(doctor);
                                        fav_added=true;
                                    }
                                }else {
                                    favourite_list=new JSONArray();
                                    if (fav_added){
                                        for (int i=0;i<favourite_list.length();i++){
                                            JSONObject objects=favourite_list.getJSONObject(i);
                                            if (objects.getInt("id")==id){
                                                favourite_list.remove(i);
                                            }
                                        }
                                        fav_added=false;
                                        favorite.setImageResource( R.drawable.doctor_favourite);
                                    }else {
                                        favorite.setImageResource( R.drawable.doctor_favourite_fill);
                                        favourite_list.put(doctor);
                                        fav_added=true;
                                    }
                                }
                                getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("favourite_list",favourite_list.toString())
                                        .commit();

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
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }



                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", id);
                        JSONObject doctor=new JSONObject();
                        doctor.put("id",doctor_model.id);
                        doctor.put("name",doctor_model.doctor_name);
                        doctor.put("image_url",doctor_model.doctor_image);
                        doctor.put("rating",Math.round(doctor_model.doctor_rating));
                        doctor.put("accept_discount",doctor_model.doctor_accept_discount);
                        JSONArray review_list=new JSONArray(doctor_model.review_list);
                        doctor.put("review_list",review_list);
                        doctor.put("fee",doctor_model.doctor_fee);
                        doctor.put("gender",doctor_model.doctor_gender);
                        doctor.put("notes",doctor_model.doctor_notes);
                        doctor.put("graduated",doctor_model.doctor_graduated);
                        JSONArray favourite_list;
                        String favs=getActivity().getSharedPreferences("personal_data", MODE_PRIVATE)
                                .getString("favourite_list","");
                        Log.w("favsfavs",favs);
                        JSONArray list = new JSONArray();
                        JSONArray jsonArray = new JSONArray(favs);
                        int len = jsonArray.length();
                        if (fav_added) {
                            if (jsonArray != null) {
                                for (int i = 0; i < len; i++) {
                                    JSONObject objects = jsonArray.getJSONObject(i);
                                    if (objects.getInt("id") != doctor_model.id) {
                                        list.put(jsonArray.get(i));
                                    }
                                }
                            }
                        }else {
                            list.put(doctor);
                        }
                        object.put("favourite_list",list);




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
    private void get_patient_data(final doctor_model doctor_model,final int id)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mprogressBar.setVisibility(View.INVISIBLE);
                    //do other things with the received JSONObject

                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                           // Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONArray list=res.getJSONArray("list");
                                for (int i=0;i<list.length();i++) {
                                    JSONObject doc = list.getJSONObject(i);
                                    if (doc.has("favourite_list")){
                                        getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                                .putString("favourite_list",new String(doc.getJSONArray("favourite_list").toString()
                                                        .getBytes("ISO-8859-1"), "UTF-8"))
                                                .commit();

                                    }


                                }
                                mprogressBar.setVisibility(View.VISIBLE);
                                favourite_update(doctor_model,id);
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
                        JSONObject _id=new JSONObject();
                        _id.put("id",id);
                        object.put("where",_id);

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
    private void check_for_favourite()
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

                                for (int i=0;i<favourite_list.length();i++){
                                    JSONObject object=favourite_list.getJSONObject(i);
                                    if (object.getInt("id")==doctor_model.id){
                                        favorite.setImageResource( R.drawable.doctor_favourite_fill);
                                        fav_added=true;
                                    }
                                }

                                Log.w("ksadkasdkl", String.valueOf(doctor_model.id));
                                get_doctor_available_appintments(doctor_model.id);
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (counter[0]<4) {
                        check_for_favourite();
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

    private void search()
    {


        try {
            final  int []counter={0};
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
                            //Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
                                        if (patient_favourite_doctor_data_fragment.this.doctor_model.id==doctor_id) {
                                            doctor_model = new doctor_model(doctor_name, availability
                                                    , ""
                                                    , doctor_image
                                                    , doctor_accept_code,
                                                    rate
                                                    , doctor_fee
                                                    , doctor_id,
                                                    doctor_info,
                                                    doctor_gender,
                                                    new String(review_list.toString().getBytes("ISO-8859-1"), "UTF-8"));
                                        }
                                        patient_speciality_model=new patient_speciality_model(specialty__id,
                                                "ss",specialty_name,specialty_id);
                                        search_result_list_model doctor_model_res=new search_result_list_model(
                                                hospital_model,doctor_model
                                                ,patient_speciality_model,patient_insurance_model,
                                                patient_gov_model,patient_city_model,nurse_model);

                                        //Log.w("dsakjbsdahk",doctor_model_res.doctor_model.doctor_name);



                                    }

                                    patient_favourite_doctor_data_fragment.this.speciality_model=patient_speciality_model;
                                    patient_favourite_doctor_data_fragment.this.gov_model=new patient_gov_model(gov__id,""
                                    ,gov_name,gov_id);
                                    patient_favourite_doctor_data_fragment.this.city_model=new patient_city_model(city__id,"",
                                            city_name,gov__id,gov_name,city_id,gov_id);
                                    patient_favourite_doctor_data_fragment.this.hospital_model=new hospital_model(hospital__id
                                    ,hospital_image_url,hospital_name,hospital_address,hospital_phone,hospital_mobile,hospital_id,hospital_latitude
                                    ,hospital_longitude);
                                    patient_favourite_doctor_data_fragment.this.doctor_model=doctor_model;
                                    patient_favourite_doctor_data_fragment.this.insurance_model=patient_insurance_model;




                                }
                                get_data(doctor_model.id);

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
                    if (counter[0]<4) {
                        search();
                        counter[0]++;
                    }else {
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
                        if(doctor_model!=null) {
                            JSONObject name_object=new JSONObject();
                            name_object.put("doctor_list.doctor.id", doctor_model.id);
                            object.put("where", name_object);
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


}
