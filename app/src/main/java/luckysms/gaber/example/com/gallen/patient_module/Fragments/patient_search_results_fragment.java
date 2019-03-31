package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.cities_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.governorates_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_approval_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.DataPassListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_search_results_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality,insurance_companies;
    private RecyclerView search_result_recycler;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private patient_search_result_list_adapter data_adapter;
    private JSONObject specialty=new JSONObject(),gov=new JSONObject();

    DataPassListener mCallback;
    private RequestQueue queue;
    private ProgressWindow progressWindow ;
    private int city_s,governorate_s,speciality_s,insurance_company_s;
    private String name;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args!=null) {
            city_s = args.getInt("city");
            governorate_s = args.getInt("governorate");
            speciality_s = args.getInt("speciality");
            insurance_company_s = args.getInt("insurance_company");
            name=args.getString("name");
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

        progressConfigurations();

        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
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
                Bundle args = new Bundle();
                args.putInt("doctor_id",contact_list.get(position).id);
                args.putString("doctor_availabilty",contact_list.get(position).doctor_availabilty);
                args.putString("doctor_graduated",contact_list.get(position).doctor_graduated);
                args.putString("doctor_image",contact_list.get(position).doctor_image);
                args.putString("doctor_location",contact_list.get(position).doctor_location);
                args.putString("doctor_name",contact_list.get(position).doctor_name);
                args.putString("doctor_speciality",contact_list.get(position).doctor_speciality);
                args.putInt("doctor_speciality_id",speciality_s);
                args.putBoolean("doctor_accept_discount",contact_list.get(position).doctor_accept_discount);
                args.putDouble("doctor_fee",contact_list.get(position).doctor_fee);
                args.putFloat("doctor_rating",contact_list.get(position).doctor_rating);



                Fragment doctor_profile=new patient_doctor_data_fragment();
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
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        showProgress();

        if (city_s>0){
            get_cities_data(city_s);
        }

        if (governorate_s>0){
            get_goves_data(governorate_s);
        }
        search();

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void search()
    {


        try {
            String url = "http://microtec1.egytag.com/api/doctors/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    hideProgress();
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

                                    JSONObject doc=list.getJSONObject(i);
                                    String doctor_name=new String(doc.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String doctor_speciality=new String(doc.getJSONObject("specialty").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String doctor_availabilty="available";
                                    String doctor_graduated="Ain Shams";
                                    String doctor_location=new String ((doc.getJSONObject("gov").getString("name")
                                            +doc.getJSONObject("city").getString("name")
                                            +doc.getString("address")).getBytes("ISO-8859-1"),"UTF-8");
                                    String doctor_image= "http://microtec1.egytag.com"+doc.getString("image_url");
                                    boolean doctor_accept_discount=true;
                                    Float doctor_rating=4f;
                                    double doctor_fee=200;
                                    int id=doc.getInt("id");



                                    search_result_list_model doctor=new search_result_list_model(doctor_name,doctor_speciality,doctor_availabilty
                                            ,doctor_graduated,doctor_location,doctor_image,doctor_accept_discount,doctor_rating,doctor_fee,id);

                                    contact_list.add(doctor);
                                    Log.w("dsakjbsdahk",doctor_name);

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
                    hideProgress();
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
                            name_object.put("name", name);
                            object.put("where", name_object);
                        }



                        if(speciality_s>0||city_s>0||governorate_s>0||insurance_company_s>0){
                            JSONObject where=new JSONObject();
                            if (speciality_s>0){
                                JSONObject speciality_object=new JSONObject();
                                speciality_object.put("id", speciality_s);
                                where.put("specialty", speciality_object);

                            }

                            if (city_s>0){
                                JSONObject city_object=new JSONObject();
                                city_object.put("id", city_s);
                                where.put("city", city_object);

                            }



                            if (insurance_company_s>0){
                                JSONObject insurance_company_object=new JSONObject();
                                insurance_company_object.put("id", insurance_company_s);
                                where.put("insurance", insurance_company_object);

                            }
                            if (governorate_s>0){
                                JSONObject governorate_object=new JSONObject();
                                governorate_object.put("id", governorate_s);
                                where.put("gov", governorate_object);
                                Log.w("dsldsajdaslk",object.toString());

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
    private void get_cities_data(final int id)
    {


        try {
            String url = "http://microtec1.egytag.com/api/cities/all";
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
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String gov_id=object.getJSONObject("gov").getString("_id");
                                    String gov_name=new String( object.getJSONObject("gov").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    int govid=object.getJSONObject("gov").getInt("id");
                                    location.setText(location.getText().toString()+name);

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
                    JSONObject where=new JSONObject();

                    try {





                        if (city_s>0){
                            JSONObject governorate_object=new JSONObject();
                            governorate_object.put("id", id);
                            where.put("city", governorate_object);
                            Log.w("dsldsajdaslk",object.toString());
                        }
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
    private void get_goves_data(final int id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/goves/all";
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
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    location.setText(location.getText().toString()+name);



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
                    JSONObject where=new JSONObject();

                    try {





                            if (governorate_s>0){
                                JSONObject governorate_object=new JSONObject();
                                governorate_object.put("id", id);
                                where.put("gov", governorate_object);
                                Log.w("dsldsajdaslk",object.toString());
                            }
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
