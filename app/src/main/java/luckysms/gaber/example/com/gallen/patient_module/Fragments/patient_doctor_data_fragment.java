package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import luckysms.gaber.example.com.gallen.patient_module.Activities.complete_booking;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_confirm_code;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_login;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_main_screen;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_available_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_reviews_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.reviews_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_doctor_data_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,favorite
            ,name,speciality,discount_code,rating_ratio,doctor_fee,doctor_info,graduated_from;
    private Button vistors_reviews;
    private ImageView image;
    private RatingBar rating;
    private RecyclerView available_appointments_recycler,reviews_recycler;
    private List<available_appointments_list_model> contact_list = new ArrayList<>();
    private List<reviews_list_model> reviews_list = new ArrayList<>();
    private patient_doctor_available_appointments_list_adapter data_adapter;
    private patient_doctor_reviews_list_adapter reviews_list_adapter;
    appointment_Listener mCallback;
    private RequestQueue queue;
    private int id,clinic_id,hospital_id,speciallity_id;
    private String doctor_name,doctor_speciality,doctor_availabilty,doctor_graduated
            ,doctor_location,doctor_image,clinic_name,hospital_name,doctor_notes;
    private boolean doctor_accept_discount;
    private Float doctor_rating;
    private double doctor_fees;
    private int city_i,governorate_i,speciality_i,insurance_company_i;
    private String _name, city_s,governorate_s,speciality_s,insurance_company_s,review_list;
    private ProgressBar mprogressBar;







    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        id=args.getInt("doctor_id");
        speciallity_id=args.getInt("doctor_speciality_id");
        doctor_notes=args.getString("doctor_notes");
        city_i = args.getInt("city");
        governorate_i = args.getInt("governorate");
        speciality_i = args.getInt("speciality");
        insurance_company_i= args.getInt("insurance_company");
        city_s = args.getString("city_s");
        governorate_s = args.getString("governorate_s");
        speciality_s = args.getString("speciality_s");
        insurance_company_s = args.getString("insurance_company_s");
        _name=args.getString("name");
        review_list=args.getString("review_list");

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
        favorite=(TextView)view.findViewById(R.id.favorite);
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
        available_appointments_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), available_appointments_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Bundle args = new Bundle();
                args.putString("day",contact_list.get(position).day);
                args.putString("from",contact_list.get(position).from);
                args.putString("to",contact_list.get(position).to);
                args.putInt("day_id",contact_list.get(position).day_id);
                args.putInt("from_id",contact_list.get(position).from_id);
                args.putInt("to_id",contact_list.get(position).to_id);
                args.putInt("speciallity_id",speciallity_id);
                args.putString("hospital_name",hospital_name);
                args.putString("clinic_name",clinic_name);

                args.putInt("clinic_id",clinic_id);
                args.putInt("hospital_id",hospital_id);


                args.putInt("doctor_id",id);
                args.putString("doctor_availabilty",doctor_availabilty);
                args.putString("doctor_graduated",doctor_graduated);
                args.putString("doctor_image",doctor_image);
                args.putString("doctor_location",doctor_location);
                args.putString("doctor_name",doctor_name);
                args.putString("doctor_speciality",doctor_speciality);
                args.putBoolean("doctor_accept_discount",doctor_accept_discount);
                args.putDouble("doctor_fee",doctor_fees);
                args.putFloat("doctor_rating",doctor_rating);
                Fragment fragment=new patient_confirm_reservation_fragment();
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
                Fragment fragment=new patient_search_results_fragment();
                Bundle args = getArguments();
                args.putInt("insurance_company",insurance_company_i);
                args.putInt("governorate", governorate_i);
                args.putInt("speciality", speciality_i);
                args.putInt("city",city_i);
                args.putString("city_s", city_s);
                args.putString("governorate_s",governorate_s);
                args.putString("speciality_s", speciality_s);
                args.putString("insurance_company_s", insurance_company_s);
                args.putString("name", _name);
                fragment.setArguments(args);
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
                View view = getLayoutInflater().inflate(R.layout.patient_doctor_rating_fragment, null);

                final BottomSheetDialog dialog=new BottomSheetDialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                RatingBar ratingBar=(RatingBar)dialog.findViewById(R.id.rating);
                TextView rating_ratio=(TextView)dialog.findViewById(R.id.rating_ratio);
                TextView vistors=(TextView)dialog.findViewById(R.id.vistors);
                Button cancel=(Button)dialog.findViewById(R.id.cancel);
                Button add_review=(Button)dialog.findViewById(R.id.add_review);
                reviews_recycler = view.findViewById(R.id.reviews_recycler);
                reviews_list_adapter = new patient_doctor_reviews_list_adapter(getActivity(), reviews_list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                reviews_recycler.setLayoutManager(mLayoutManager);
                reviews_recycler.setItemAnimator(new DefaultItemAnimator());
                reviews_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 5));
                reviews_recycler.setAdapter(reviews_list_adapter);
                ratingBar.setRating(doctor_rating);
                rating_ratio.setText(String.valueOf(doctor_rating));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                add_review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog=new Dialog(getActivity());
                        dialog.setContentView(R.layout.add_review);
                        Button add_review=(Button)dialog.findViewById(R.id.add_review);
                        final RatingBar rating=(RatingBar)dialog.findViewById(R.id.rating);
                        final EditText review=(EditText)dialog.findViewById(R.id.review);
                        add_review.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String review_s=review.getText().toString();
                                Float rate=rating.getRating();
                                if (review_s.length()>2) {
                                    review_update(id, review_list, rate, review_s);
                                }
                            }
                        });
                        dialog.show();
                    }
                });






                dialog.show();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_patient_data( new doctor_model(doctor_name,  doctor_availabilty
                        ,  doctor_graduated,  doctor_image
                        ,  doctor_accept_discount,  doctor_rating,  doctor_fees,  id, doctor_notes
                        ,"",review_list),getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search_results_fragment();
                    Bundle args = getArguments();
                    args.putInt("insurance_company",insurance_company_i);
                    args.putInt("governorate", governorate_i);
                    args.putInt("speciality", speciality_i);
                    args.putInt("city",city_i);
                    args.putString("city_s", city_s);
                    args.putString("governorate_s",governorate_s);
                    args.putString("speciality_s", speciality_s);
                    args.putString("insurance_company_s", insurance_company_s);
                    args.putString("name", _name);
                    fragment.setArguments(args);
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        mprogressBar.setVisibility(View.VISIBLE);
        get_data(id);
        get_doctor_available_appintments(id);
        return view;
    }


    public void go_to(Fragment fragment)
    {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }



    private void get_data(final int id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/doctors/view";
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
                                     doctor_name=new String(doc.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                     doctor_speciality=new String(doc.getJSONObject("specialty").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                     doctor_availabilty="available";
                                     doctor_graduated="Ain Shams";
                                     doctor_location=new String ((doc.getJSONObject("gov").getString("name")
                                            +doc.getJSONObject("city").getString("name")
                                            +doc.getString("address")).getBytes("ISO-8859-1"),"UTF-8");
                                     doctor_image= "http://microtec1.egytag.com"+doc.getString("image_url");
                                     doctor_accept_discount=true;
                                     doctor_rating=4f;
                                     doctor_fees=200;
                                     patient_doctor_data_fragment.this.id=doc.getInt("id");
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
                                doctor_info.setText(doctor_notes);




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
            String url = "http://microtec1.egytag.com:30001/api/tickets/all";
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
                                        JSONObject status=object.getJSONObject("status");
                                        int status_id= status.getInt("id");
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
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        JSONObject where=new JSONObject();
                        where.put("doctor_list.doctor.id",id);
                        where.put("ticket_date",new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
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
            String url = "http://microtec1.egytag.com:30001/api/patients/update";
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
                                Toast.makeText(getActivity(),getResources().getString(R.string.added_to_favourite),Toast.LENGTH_LONG).show();
                                favorite.setCompoundDrawablesWithIntrinsicBounds( R.drawable.doctor_favourite_fill, 0, 0, 0);

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
                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", id);
                        JSONObject doctor=new JSONObject();
                        doctor.put("id",doctor_model.id);
                        doctor.put("name",doctor_model.doctor_name);
                        doctor.put("image_url",doctor_model.doctor_image);
                        doctor.put("fee",doctor_model.doctor_fee);
                        doctor.put("rating",doctor_model.doctor_rating);
                        doctor.put("accept_discount",doctor_model.doctor_accept_discount);
                        doctor.put("speciality",speciality_s);
                        JSONArray favourite_list;
                        String favs=getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("favourite_list","");
                        if (favs.length()>0){
                             favourite_list=new JSONArray(favs);
                            favourite_list.put(doctor);
                        }else {
                             favourite_list=new JSONArray();
                            favourite_list.put(doctor);
                        }
                        object.put("favourite_list",favourite_list);




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
            String url = "http://microtec1.egytag.com:30001/api/patients/all";
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
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONArray list=res.getJSONArray("list");
                                for (int i=0;i<list.length();i++) {
                                    JSONObject doc = list.getJSONObject(i);
                                    if (doc.has("favourite_list")){
                                        getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                                .putString("favourite_list",doc.getJSONArray("favourite_list").toString())
                                                .commit();
                                        favourite_update(doctor_model,id);
                                    }

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
    private void review_update(final int doc_id, final String review_list, final float rate, final String comment)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/doctors/update";
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
                                Toast.makeText(getActivity(),getResources().getString(R.string.added_to_favourite),Toast.LENGTH_LONG).show();

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
                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", doc_id);
                        JSONArray reviews_list=new JSONArray(review_list);
                        JSONObject review=new JSONObject();
                        review.put("comment",comment);
                        review.put("rate",rate);

                        reviews_list.put(review);


                        object.put("review_list",review_list);




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
    private void add_ticket(final JSONObject selected_doctor, final JSONObject selected_time
            , final JSONObject selected_specialty, final JSONObject selected_hospital, final JSONObject status
            , final JSONArray drugs_list, final JSONArray scans_list, final JSONArray analyses_list
            , final JSONArray operation_list, final JSONObject selected_shift, final JSONObject image_url
            , final JSONObject selected_clinic,final JSONObject ticket_date) {


        try {
            String url = "http://microtec1.egytag.com:30001/api/tickets/add";
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

                                Toast.makeText(getActivity(),getResources().getString(R.string.booking_successful),Toast.LENGTH_LONG).show();
                                Intent c=new Intent(getActivity(),complete_booking.class);
                                startActivity(c);


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
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }

                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();


                    try {
                        object.put("selected_doctor",selected_doctor);
                        object.put("selected_time",selected_time);
                        object.put("selected_specialty",selected_specialty);
                        object.put("selected_hospital",selected_hospital);
                        object.put("drugs_list",drugs_list);
                        object.put("operation_list",operation_list);
                        object.put("scans_list",scans_list);
                        object.put("analyses_list",analyses_list);
                        object.put("selected_shift",selected_shift);
                        object.put("status",status);
                        object.put("image_url",image_url);
                        object.put("date",new Date().getTime());
                        object.put("selected_clinic",selected_clinic);
                        object.put("ticket_date",ticket_date);




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


}
