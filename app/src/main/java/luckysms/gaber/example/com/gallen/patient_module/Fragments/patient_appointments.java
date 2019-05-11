package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_appointments extends Fragment implements appointment_Listener {
    private View view;
    private TextView number_of_notifications,notifications,location,speciality,insurance_companies,text;
    private RecyclerView appointments_recycler;
    private List<appointments_list_model> contact_list = new ArrayList<>();
    private patient_appointments_list_adapter data_adapter;
    private RequestQueue queue;
    private LinearLayout appointments_layout;
    private ProgressBar mprogressBar;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_appointments_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        text=(TextView)view.findViewById(R.id.text);
        appointments_recycler = view.findViewById(R.id.appointments_recycler);
        data_adapter = new patient_appointments_list_adapter(getActivity(), contact_list,this);
        appointments_layout=(LinearLayout)view.findViewById(R.id.appointments_layout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        appointments_recycler.setLayoutManager(mLayoutManager);
        appointments_recycler.setItemAnimator(new DefaultItemAnimator());
        appointments_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        appointments_recycler.setAdapter(data_adapter);


        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mprogressBar.setVisibility(View.VISIBLE);
        get_appointments(getActivity().getSharedPreferences("personal_data",MODE_PRIVATE).getInt("id",0));
        return view;
    }

    private void get_appointments(final int patient_id)
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

                                    JSONObject appointment=list.getJSONObject(i);
                                    String _id=appointment.getString("_id");
                                    int id=appointment.getInt("id");
                                    String image_url = appointment.getString("image_url");
                                    long date=appointment.getInt("date");
                                    JSONObject status=appointment.getJSONObject("status");
                                    int status_id=status.getInt("id");
                                    String status_name=new String (status.getString("name").getBytes("ISO-8859-1"),"UTF-8");
                                    String status_en=status.getString("en");
                                    String status_ar=status.getString("ar");


                                    JSONArray drugs_list=appointment.getJSONArray("drugs_list");
                                    JSONArray scans_list=appointment.getJSONArray("scans_list");
                                    JSONArray analyses_list=appointment.getJSONArray("analyses_list");
                                    JSONArray operation_list=appointment.getJSONArray("operation_list");


                                    JSONObject patient=appointment.getJSONObject("patient");
                                    String  patient__id=patient.getString("_id");
                                    int patient_id=patient.getInt("id");
                                    String  patient_image_url=patient.getString("image_url");
                                    String  patient_name=new String (patient.getString("name").getBytes("ISO-8859-1"),"UTF-8");
                                    String  patient_mobile=patient.getString("mobile");
                                    String patient_insurance="";
                                    if (patient.has("insurance_company")) {
                                         patient_insurance = patient.getJSONObject("insurance_company").getString("name");
                                    }
                                    JSONObject selected_clinic=appointment.getJSONObject("selected_clinic");
                                    int selected_clinic_id=selected_clinic.getInt("id");
                                    String  selected_clinic_name=new String (selected_clinic.getString("name").getBytes("ISO-8859-1"),"UTF-8");


                                    JSONObject selected_hospital=appointment.getJSONObject("selected_hospital");
                                    int selected_hospital_id=selected_hospital.getInt("id");
                                    String  selected_hospital_name=new String (selected_hospital.getString("name").getBytes("ISO-8859-1"),"UTF-8");

                                    JSONObject selected_specialty=appointment.getJSONObject("selected_specialty");
                                    int selected_specialty_id=selected_specialty.getInt("id");
                                    String  selected_specialty_name=new String (selected_specialty.getString("name").getBytes("ISO-8859-1"),"UTF-8");

                                    JSONObject selected_doctor=appointment.getJSONObject("selected_doctor");
                                    int selected_doctor_id=selected_doctor.getInt("id");
                                    String  selected_doctor_name=new String (selected_doctor.getString("name").getBytes("ISO-8859-1"),"UTF-8");

                                    JSONObject selected_shift=appointment.getJSONObject("selected_shift");
                                    int selected_shift_id=0;
                                    if (selected_shift.has("id")) {
                                         selected_shift_id = selected_shift.getInt("id");
                                    }
                                    String  selected_shift_name="";
                                    if (selected_shift.has("name")) {
                                          selected_shift_name=new String (selected_shift.getString("name").getBytes("ISO-8859-1"),"UTF-8");

                                    }


                                    JSONObject selected_time=appointment.getJSONObject("selected_time");
                                    int selected_time_id=0;
                                    if (selected_time.has("id")) {
                                         selected_time_id=selected_time.getInt("id");

                                    }
                                    String  selected_time_name=new String (selected_time.getString("day").getBytes("ISO-8859-1"),"UTF-8");

                                    appointments_list_model doctor=new appointments_list_model(selected_time_name,selected_shift_name,selected_doctor_name,selected_specialty_name,selected_hospital_name
                                    ,selected_clinic_name,patient__id,patient_image_url,patient_name,patient_mobile,patient_insurance,status_ar,status_en,status_name,date,
                                            image_url,_id,selected_time_id,selected_shift_id,selected_doctor_id
                                            ,selected_specialty_id,selected_hospital_id,selected_clinic_id,patient_id,status_id,id,drugs_list,scans_list,analyses_list,operation_list);


                                    Log.w("dkljasdlasjkd",patient_name);
                                    contact_list.add(doctor);

                                }
                                data_adapter.notifyDataSetChanged();
                                if (contact_list.size()>0){
                                    appointments_recycler.setVisibility(View.VISIBLE);
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
                        JSONObject where=new JSONObject();
                        where.put("patient.id",patient_id);
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
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringReq);


        } catch (Exception e) {

        }


    }

    public void go_to(Fragment fragment) {
        appointments_layout.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void cancelled(final int ticket_id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/tickets/update";
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                get_appointments(getActivity().getSharedPreferences("personal_data",MODE_PRIVATE).getInt("id",0));

                            }
                        }
                    } catch (JSONException e) {
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
                        JSONObject status=new JSONObject();
                        status.put("id",3);
                        status.put("ar","تم الغاء الحجز");
                        status.put("en","cancelled");
                        status.put("name","cancelled");
                        object.put("id",ticket_id);
                        object.put("status",status);




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
    private void ticket_details(int position)
    {

        Fragment fragment=new patient_appointments_details();
        Bundle args=new Bundle();
        args.putString("location",contact_list.get(position).selected_clinic_name+contact_list.get(position).selected_hospital_name);
        args.putString("speciality",contact_list.get(position).selected_specialty_name);
        args.putString("insurance_companies",contact_list.get(position).patient_insurance);
        args.putString("drug_list",contact_list.get(position).drugs_list.toString());
        fragment.setArguments(args);
        go_to(fragment);

    }

    private void open_map(int position){
        String url = "http://maps.google.com/maps?daddr=" + contact_list.get(position).latitude + "," + contact_list.get(position).longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public void detect(int pos) {

    }

    @Override
    public void cancel(int pos) {
        cancelled(contact_list.get(pos).id);
    }

    @Override
    public void details(int pos) {
        ticket_details(pos);

    }

    @Override
    public void map_location(int pos) {
        open_map(pos);
    }
}
