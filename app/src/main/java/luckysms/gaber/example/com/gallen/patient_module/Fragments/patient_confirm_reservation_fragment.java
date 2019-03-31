package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;

import static android.content.Context.MODE_PRIVATE;

public class patient_confirm_reservation_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,favorite
            ,name,speciality,discount_code,rating_ratio,location,fees,date,time,doctor_info;
    private Button map_location,confirm_booking,enter_discount_code;
    private ImageView image;
    private RatingBar rating;
    private int id,patient_id,clinic_id,hospital_id,day_id,from_id,to_id,speciallity_id;
    private String doctor_name,doctor_speciality,doctor_availabilty,doctor_graduated,doctor_location,doctor_image
            , day,from,to,hospital_name,clinic_name, patient_mobile,patient_name,patient_email,patient_birth_date,patient_insurance;
    private boolean doctor_accept_discount;
    private Float doctor_rating;
    private double doctor_fees;
    private RequestQueue queue;
    private ProgressWindow progressWindow ;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args!=null) {
            day = args.getString("day");
            from = args.getString("from");
            to = args.getString("to");
            id = args.getInt("doctor_id");
            day_id = args.getInt("day_id");
            from_id = args.getInt("from_id");
            to_id = args.getInt("to_id");
            doctor_availabilty = args.getString("doctor_availabilty");
            doctor_graduated = args.getString("doctor_graduated");
            doctor_image = args.getString("doctor_image");
            doctor_location = args.getString("doctor_location");
            doctor_name = args.getString("doctor_name");
            doctor_speciality = args.getString("doctor_speciality");
            doctor_accept_discount = args.getBoolean("doctor_accept_discount");
            doctor_fees = args.getDouble("doctor_fee");
            doctor_rating = args.getFloat("doctor_rating");
            hospital_name=args.getString("hospital_name");
            clinic_name=args.getString("clinic_name");
            clinic_id= args.getInt("clinic_id");
            hospital_id=args.getInt("hospital_id");
            speciallity_id=args.getInt("speciallity_id");
        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_doctor_confirm_reservation_fragment, container, false);
        progressConfigurations();
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        favorite=(TextView)view.findViewById(R.id.favorite);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_results_fragment();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject selected_doctor=new JSONObject();
                JSONObject patient=new JSONObject();
                JSONObject selected_time=new JSONObject();
                JSONObject selected_hospital=new JSONObject();
                JSONObject selected_specialty=new JSONObject();


                try {
                    selected_doctor.put("id",id);
                    selected_doctor.put("name",doctor_name);
                    JSONObject day_ob=new JSONObject();
                    day_ob.put("id",day_id);
                    day_ob.put("en",day);
                    JSONObject from_obj=new JSONObject();
                    from_obj.put("id",from_id);
                    from_obj.put("en",from);
                    JSONObject to_obj=new JSONObject();
                    to_obj.put("id",to_id);
                    to_obj.put("en",to);
                    selected_hospital.put("id",hospital_id);
                    selected_hospital.put("name",hospital_name);
                    selected_specialty.put("id",speciallity_id);
                    selected_specialty.put("name",doctor_speciality);

                    patient.put("mobile",patient_mobile);
                    patient.put("name",patient_name);
                    patient.put("email",patient_email);
                    patient.put("birth_date",patient_birth_date);
                    patient.put("insurance",patient_insurance);
                    patient.put("id",patient_id);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                add_ticket(patient,selected_doctor,selected_time,selected_specialty,selected_hospital);

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search_results_fragment();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        showProgress();
        get_data();
        get_doctor_data();
        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void get_doctor_data()
    {

        name.setText(doctor_name);
        speciality.setText(doctor_speciality);
        location.setText(doctor_location);
        if (doctor_accept_discount){
            discount_code.setVisibility(View.VISIBLE);
        }
        rating.setRating(doctor_rating);
        rating_ratio.setText(String .valueOf(doctor_rating));
        fees.setText(fees.getText().toString()+" "+String .valueOf(doctor_fees));
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

    }

    private void add_ticket(final JSONObject patient, final JSONObject selected_doctor, final JSONObject selected_time, final JSONObject selected_specialty, final JSONObject selected_hospital)
    {


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
                    hideProgress();
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {

                                Toast.makeText(getActivity(),getResources().getString(R.string.booking_successful),Toast.LENGTH_LONG).show();
                                Fragment fragment=new patient_complete_booking_fragment();
                                go_to(fragment);

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
                        object.put("patient",patient);
                        object.put("selected_specialty",selected_specialty);
                        object.put("selected_hospital",selected_hospital);
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
    private void progressConfigurations(){
        progressWindow = ProgressWindow.getInstance(getActivity());
        ProgressWindowConfiguration progressWindowConfiguration = new ProgressWindowConfiguration();
        progressWindowConfiguration.backgroundColor = Color.parseColor("#32000000") ;
        progressWindowConfiguration.progressColor = Color.WHITE ;
        progressWindow.setConfiguration(progressWindowConfiguration);
    }
    public void showProgress(){
        progressWindow.showProgress();
    }
    public void hideProgress(){
        progressWindow.hideProgress();
    }
    @Override
    public void onPause() {
        super.onPause();
        hideProgress();

    }
    private void get_data()
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/patients/view";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    hideProgress();
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject doc=res.getJSONObject("doc");
                                patient_mobile=doc.getString("mobile");
                                patient_name=doc.getString("name");
                                patient_email=doc.getString("email");
                                patient_birth_date=doc.getString("birth_date");
                                patient_insurance=doc.getString("insurance");
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
                    hideProgress();
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
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
