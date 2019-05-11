package luckysms.gaber.example.com.gallen.doctor_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_available_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_reviews_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_confirm_reservation_fragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_search_results_fragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.reviews_list_model;

import static android.content.Context.MODE_PRIVATE;

public class doctor_data_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications
            ,name,speciality,discount_code,rating_ratio,doctor_fee,doctor_info,graduated_from;
    private Button vistors_reviews;
    private ImageView image,favorite;
    private RatingBar rating;
    private RecyclerView available_appointments_recycler,reviews_recycler;
    private List<available_appointments_list_model> contact_list = new ArrayList<>();
    private List<reviews_list_model> reviews_list = new ArrayList<>();
    private patient_doctor_available_appointments_list_adapter data_adapter;
    private patient_doctor_reviews_list_adapter reviews_list_adapter;
    appointment_Listener mCallback;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private boolean fav_added;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.doctor_data_fragment, container, false);
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
        image=(ImageView)view.findViewById(R.id.image);
        rating=(RatingBar)view.findViewById(R.id.rating);
        vistors_reviews=(Button)view.findViewById(R.id.vistors_reviews);
        available_appointments_recycler = view.findViewById(R.id.available_appointments_recycler);
        data_adapter = new patient_doctor_available_appointments_list_adapter(getActivity(), contact_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        available_appointments_recycler.setLayoutManager(layoutManager);
        available_appointments_recycler.setItemAnimator(new DefaultItemAnimator());
        available_appointments_recycler.setAdapter(data_adapter);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mprogressBar.setVisibility(View.VISIBLE);
        get_data();
        get_doctor_available_appintments();
        return view;
    }


    public void go_to(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }
    private void get_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/doctors/view";
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
                                JSONObject doc=res.getJSONObject("doc");
                                String doctor_name = new String(doc.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                String  doctor_speciality=new String(doc.getJSONObject("specialty").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                String  doctor_availabilty="available";
                                String  doctor_graduated="Ain Shams";
                                String  doctor_location=new String ((doc.getJSONObject("gov").getString("name")
                                            +doc.getJSONObject("city").getString("name")
                                            +doc.getString("address")).getBytes("ISO-8859-1"),"UTF-8");
                                String doctor_image= "http://microtec1.egytag.com"+doc.getString("image_url");
                                    boolean doctor_accept_discount=true;
                                     Float doctor_rating=4f;
                                     double doctor_fees=200;
                                JSONArray rev_list=new JSONArray();
                                if (doc.has("review_list")){
                                    rev_list=doc.getJSONArray("review_list");
                                }
                                String review_list=rev_list.toString();

                                    String notes = "";
                                    if (doc.has("notes")) {
                                         notes = new String(doc.getString("notes").getBytes("ISO-8859-1"), "UTF-8");
                                    }

                                name.setText(doctor_name);
                                speciality.setText(doctor_speciality);
                                graduated_from.setText(doctor_location);
                                if (doctor_accept_discount){
                                    discount_code.setVisibility(View.VISIBLE);
                                }
                                rating.setRating(doctor_rating);
                                rating_ratio.setText(String .valueOf(doctor_rating));
                                doctor_fee.setText(doctor_fee.getText().toString()+" "+String .valueOf(doctor_fees));
                                Picasso.with(getActivity())
                                        .load(doctor_image)
                                        .placeholder(R.drawable.locations_map)
                                        .into(image, new Callback() {
                                            @Override
                                            public void onSuccess() {}
                                            @Override public void onError() {
                                                Toast.makeText(getActivity(),"error loading image",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                doctor_info.setText(notes);




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
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));




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
    private void get_doctor_available_appintments()
    {

        try {
            String url = "http://microtec1.egytag.com/api/tickets/all";
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
                                int count=res.getInt("count");
                                if (count>0){
                                    for (int i=0;i<list.length();i++) {
                                        JSONObject object = list.getJSONObject(i);
                                        JSONObject selected_time=object.getJSONObject("selected_time");
                                        JSONObject status=new JSONObject();
                                        int status_id=0;
                                        if (object.has("status")) {
                                            status = object.getJSONObject("status");
                                            status_id=status.getInt("id");
                                        }

                                        if (status_id==0){
                                            JSONObject day_ob=selected_time.getJSONObject("day_ob");
                                            int day_id=day_ob.getInt("id");
                                            String day_name=day_ob.getString("en");
                                            JSONObject from_obj=selected_time.getJSONObject("from_obj");
                                            int from_id=from_obj.getInt("id");
                                            String from_name=from_obj.getString("en");
                                            JSONObject to_obj=selected_time.getJSONObject("to_obj");
                                            int to_id=to_obj.getInt("id");
                                            String to_name=to_obj.getString("en");
                                            contact_list.add(new available_appointments_list_model(day_name,from_name,to_name,day_id,from_id,to_id));

                                        }
                                    }
                                    data_adapter.notifyDataSetChanged();
                                }else {

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
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        JSONObject where=new JSONObject();
                        where.put("doctor_list.doctor.id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                        where.put("date",new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
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




}
