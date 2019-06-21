package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.complete_booking;
import luckysms.gaber.example.com.gallen.patient_module.Custom.AsyncTaskLoadImage;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.hospital_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_confirm_reservation_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications
            ,name,speciality,discount_code,rating_ratio,location,fees,date,time,doctor_info;
    private Button map_location,confirm_booking,enter_discount_code;
    private ImageView image,favorite;
    private RatingBar rating;
    private int patient_id;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private available_appointments_list_model ticket;
    private patient_speciality_model speciality_model;
    private doctor_model doctor_model;
    private hospital_model hospital_model;
    private patient_insurance_model insurance_model;
    private patient_gov_model gov_model;
    private patient_city_model city_model;
    private String patient_mobile,patient_name,patient_email,patient_birth_date,clinic_name,_name;
    private int clinic_id;
    private JSONObject patient_insurance;
    private boolean fav_added;
    private JSONObject user_info;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null) {
            Bundle args = getArguments();
            if (args != null) {
                    city_model = (patient_city_model) args.getSerializable("city");
                    gov_model = (patient_gov_model) args.getSerializable("governorate");
                    _name = args.getString("name");
                    ticket=(available_appointments_list_model)args.getSerializable("ticket");
                    doctor_model= (doctor_model) args.getSerializable("doctor");
                    hospital_model= (hospital_model) args.getSerializable("hospital");
                    insurance_model= (patient_insurance_model) args.getSerializable("insurance_company");
                    speciality_model= (patient_speciality_model) args.getSerializable("speciality");
                    clinic_name=args.getString("clinic_name");
                    clinic_id=args.getInt("clinic_id");
                    fav_added=args.getBoolean("fav_added");
                    patient_id=getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0);
            }
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_doctor_confirm_reservation_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        favorite=(ImageView)view.findViewById(R.id.favorite);
        name=(TextView)view.findViewById(R.id.name);
        speciality=(TextView)view.findViewById(R.id.speciality);
        discount_code=(TextView)view.findViewById(R.id.discount_code);
        rating_ratio=(TextView)view.findViewById(R.id.rating_ratio);
        location=(TextView)view.findViewById(R.id.location);
        fees=(TextView)view.findViewById(R.id.fees);
        date=(TextView)view.findViewById(R.id.date);
        time=(TextView)view.findViewById(R.id.time);
        doctor_info=(TextView)view.findViewById(R.id.doctor_info);
        map_location=(Button)view.findViewById(R.id.map_location);
        confirm_booking=(Button)view.findViewById(R.id.confirm_booking);
        enter_discount_code=(Button)view.findViewById(R.id.enter_discount_code);
        image=(ImageView)view.findViewById(R.id.image);
        rating=(RatingBar)view.findViewById(R.id.rating);


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                get_patient_data( doctor_model,getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));

            }
        });



        map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr=" + hospital_model.hospital_latitude + "," + hospital_model.hospital_longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args=new Bundle();
                if (doctor_model!=null) {
                    args.putSerializable("doctor", doctor_model);
                }
                if (hospital_model!=null) {
                    args.putSerializable("hospital", hospital_model);

                }
                if (insurance_model!=null) {
                    args.putSerializable("insurance_company",insurance_model);
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

                args.putString("name", _name);
                Fragment fragment=new patient_doctor_data_fragment();
                fragment.setArguments(args);
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                JSONObject selected_doctor=new JSONObject();
                JSONObject patient=new JSONObject();
                JSONObject selected_time=new JSONObject();
                JSONObject selected_hospital=new JSONObject();
                JSONObject selected_specialty=new JSONObject();
                JSONObject selected_clinic=new JSONObject();
                JSONObject status=new JSONObject();
                JSONArray drugs_list=new JSONArray();
                JSONArray scans_list=new JSONArray();
                JSONArray analyses_list=new JSONArray();
                JSONArray operation_list=new JSONArray();
                JSONObject selected_shift=new JSONObject();
                String image_url = "";
                JSONObject ticket_date =new JSONObject();





                try {
                    selected_doctor.put("id",doctor_model.id);
                    selected_doctor.put("name",doctor_model.doctor_name);
                    JSONObject day_ob=new JSONObject();
                    day_ob.put("id",ticket.day_id);
                    day_ob.put("en",ticket.day);
                    JSONObject from_obj=new JSONObject();
                    from_obj.put("id",ticket.from_id);
                    from_obj.put("en",ticket.from);
                    JSONObject to_obj=new JSONObject();
                    to_obj.put("id",ticket.to_id);
                    to_obj.put("en",ticket.to);
                    selected_time.put("day",day_ob);
                    selected_time.put("from",from_obj);
                    selected_time.put("to",to_obj);

                    selected_hospital.put("id",hospital_model.hospital_id);
                    selected_hospital.put("name",hospital_model.hospital_name);
                    selected_hospital.put("latitude",hospital_model.hospital_latitude);
                    selected_hospital.put("longitude",hospital_model.hospital_longitude);

                    selected_specialty.put("id",speciality_model.id);
                    selected_specialty.put("name",speciality_model.name);



                    patient.put("mobile",patient_mobile);
                    patient.put("name",patient_name);
                    patient.put("email",patient_email);
                    patient.put("birth_date",patient_birth_date);
                    patient.put("insurance_company",patient_insurance);
                    patient.put("id",patient_id);
                    patient.put("_id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("_id",""));
                    patient.put("image_url","ss");



                    status.put("id",1);
                    status.put("en","waiting");
                    status.put("ar","انتظار");
                    status.put("name","waiting");

                    image_url=doctor_model.doctor_image;

                    selected_clinic.put("id",clinic_id);
                    selected_clinic.put("name",clinic_name);

                    ticket_date.put("date",new SimpleDateFormat("MM/dd/yyyy").format(new Date()));







                } catch (JSONException e) {
                    e.printStackTrace();
                }
                add_ticket(patient,status,selected_hospital,ticket_date,image_url);

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Bundle args=new Bundle();
                    if (doctor_model!=null) {
                        args.putSerializable("doctor", doctor_model);
                    }
                    if (hospital_model!=null) {
                        args.putSerializable("hospital", hospital_model);

                    }
                    if (insurance_model!=null) {
                        args.putSerializable("insurance_company",insurance_model);
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

                    args.putString("name", _name);
                    Fragment fragment=new patient_doctor_data_fragment();
                    fragment.setArguments(args);
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        if (fav_added) {
            favorite.setImageResource(R.drawable.doctor_favourite_fill);
        }
        mprogressBar.setVisibility(View.VISIBLE);
        get_data();
        get_doctor_data();

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }
    private void get_doctor_data()
    {

        name.setText(doctor_model.doctor_name);
        speciality.setText(speciality_model.name);
        try {
            location.setText(new String(hospital_model.hospital_address.getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (doctor_model.doctor_accept_discount){
            discount_code.setVisibility(View.VISIBLE);
        }
        rating.setRating(doctor_model.doctor_rating);
        rating_ratio.setText(String .valueOf(Math.round(doctor_model.doctor_rating)));
        fees.setText(fees.getText().toString()+" "+String .valueOf(doctor_model.doctor_fee));
        String url2 =doctor_model.doctor_image;
        new AsyncTaskLoadImage(image).execute(url2);
        date.setText(ticket.day);
        time.setText(ticket.from);
        doctor_info.setText(doctor_model.doctor_notes);

    }
    private void add_ticket(final JSONObject patient, final JSONObject status ,final JSONObject selected_hospital
                            ,final JSONObject ticket_date,final String image_url) {


        try {
            final int []counter={0};
            String url = "http://intmicrotec.neat-url.com:6566/api/tickets/update";
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
                           // Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
                    if (counter[0]<4) {
                        add_ticket(patient,status,selected_hospital,ticket_date,image_url);
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
                        object.put("id",ticket.ticket_id);
                        object.put("patient",patient);
                        object.put("status",status);
                        object.put("selected_hospital",selected_hospital);
                        object.put("ticket_date",ticket_date);
                        object.put("image_url",image_url);

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
    private void get_data()
    {


        try {
            final int []counter={0};
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
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            //Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject doc=res.getJSONObject("doc");
                                patient_mobile=doc.getString("mobile");
                                patient_name=doc.getString("name");
                                patient_email=doc.getString("email");
                                patient_birth_date=doc.getString("birth_date");

                                patient_insurance=doc.getJSONObject("insurance_company");
                                patient_id=getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0);
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
                        get_data();
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



    private void favourite_update(final doctor_model doctor_model, final int id)
    {


        try {
            final int []counter={0};
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/update";
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
                                Toast.makeText(getActivity(),getResources().getString(R.string.added_to_favourite),Toast.LENGTH_LONG).show();
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
                                JSONObject speciality=new JSONObject();
                                speciality.put("name",speciality_model.name);
                                speciality.put("id",speciality_model.id);
                                speciality.put("_id",speciality_model._id);
                                speciality.put("image_url",speciality_model.image_url);
                                doctor.put("specialty",speciality);
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
                                    fav_added=false;
                                    favorite.setImageResource( R.drawable.doctor_favourite);
                                }else {
                                    list.put(doctor);
                                    favorite.setImageResource( R.drawable.doctor_favourite_fill);
                                    fav_added=true;
                                }

                                getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("favourite_list",new String(list.toString()
                                                .getBytes("ISO-8859-1"), "UTF-8"))
                                        .commit();

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
                        favourite_update(doctor_model,id);
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
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }



                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", id);
                        object.put("user_info",user_info.getJSONObject("user_info"));
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
                        JSONObject speciality=new JSONObject();
                        speciality.put("name",speciality_model.name);
                        speciality.put("id",speciality_model.id);
                        speciality.put("_id",speciality_model._id);
                        speciality.put("image_url",speciality_model.image_url);
                        doctor.put("specialty",speciality);
                        JSONObject hospital=new JSONObject();
                        hospital.put("name",hospital_model.hospital_name);
                        hospital.put("id",hospital_model.hospital_id);
                        hospital.put("phone",hospital_model.hospital_phone);
                        hospital.put("mobile",hospital_model.hospital_mobile);
                        hospital.put("image_url",hospital_model.hospital_image_url);
                        hospital.put("latitude",hospital_model.hospital_latitude);
                        hospital.put("longitude",hospital_model.hospital_longitude);
                        hospital.put("_id",hospital_model.hospital__id);
                        hospital.put("address",hospital_model.hospital_address);



                        doctor.put("hospital",hospital);

                        String favs=getActivity().getSharedPreferences("personal_data", MODE_PRIVATE)
                                .getString("favourite_list","");
                        Log.w("favsfavs",favs);
                        JSONArray list = new JSONArray();
                        if (favs.length()>0) {

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
                            } else {
                                list.put(doctor);
                            }
                        } else {

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
            final int []counter={0};
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

                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            // Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                user_info=res.getJSONObject("doc");
                                if (user_info.has("favourite_list")){
                                    getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                            .putString("favourite_list",new String(user_info.getJSONArray("favourite_list").toString()
                                                    .getBytes("ISO-8859-1"), "UTF-8"))
                                            .commit();

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
                    if (counter[0]<4) {
                        get_patient_data(doctor_model,id);
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
}
