package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.AutoCompleteTextView;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.cities_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.governorates_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class patient_search_by_area_fragment extends Fragment  {
    private View view;
    private Button search;
    private TextView back,number_of_notifications,notifications;
    private RadioGroup radioGroup;
    private RadioButton auto_location,select_governate;
    private Button city,governorates;
    //private Spinner insurance_company;
    private FusedLocationProviderClient mFusedLocationClient;
    private String state;
    appointment_Listener mCallback;
    private RequestQueue queue;
    private ArrayList<patient_city_model>cities=new ArrayList<>();
    private ArrayList<patient_gov_model>govs=new ArrayList<>();
    //private ArrayList<patient_insurance_model>insurance_companies=new ArrayList<>();
    private gov_SpinAdapter adapter;
    private city_SpinAdapter adapter2;
    private cities_list_adapter cities_list_adapter;
    private governorates_list_adapter governorates_list_adapter;
    private int city_id,gov_id,insurance_id,speciality_id;
    private boolean visitor;
    private String speciality_s;
    RecyclerView dialog_list;
    private ArrayList<patient_gov_model> filtered_govs_List = new ArrayList<>();
    private ArrayList<patient_city_model> filtered_cities_List = new ArrayList<>();
    private ProgressBar mprogressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            visitor=getArguments().getBoolean("visitor");
            speciality_id = getArguments().getInt("speciality");
            speciality_s=getArguments().getString("speciality_s");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        city=(Button)view.findViewById(R.id.city);
        governorates=(Button)view.findViewById(R.id.governorates);
        //insurance_company=(Spinner)view.findViewById(R.id.insurance_company);
        search=(Button)view.findViewById(R.id.search);
        auto_location=(RadioButton) view.findViewById(R.id.auto_location);
        select_governate=(RadioButton) view.findViewById(R.id.select_governate);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
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

        city.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        final Dialog dialog=new Dialog(getActivity());
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        dialog.setContentView(R.layout.dialog_list);
                                        dialog_list= dialog.findViewById(R.id.dialog_list);
                                        final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                                        adapter2=new city_SpinAdapter(getActivity(),cities);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                        dialog_list.setLayoutManager(mLayoutManager);
                                        dialog_list.setItemAnimator(new DefaultItemAnimator());
                                        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                                        dialog_list.setAdapter(adapter2);
                                        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                                            @Override
                                            public void onClick(View v, final int position) {
                                                if (searchh.getText().length()>0){
                                                    city_id=filtered_cities_List.get(position).id;
                                                    city.setText(filtered_cities_List.get(position).name);
                                                    dialog.dismiss();
                                                }else {
                                                    city_id=cities.get(position).id;
                                                    city.setText(cities.get(position).name);
                                                    dialog.dismiss();
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

                                                filter_city(s.toString());


                                            }
                                        });

                                        mprogressBar.setVisibility(View.VISIBLE);
                                        get_cities_data();
                                        dialog.show();



                                    }
                                });
        governorates.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                final Dialog dialog=new Dialog(getActivity());
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                dialog.setContentView(R.layout.dialog_list);
                                                dialog_list= dialog.findViewById(R.id.dialog_list);
                                                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                                                adapter=new gov_SpinAdapter(getActivity(),govs);
                                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                                dialog_list.setLayoutManager(mLayoutManager);
                                                dialog_list.setItemAnimator(new DefaultItemAnimator());
                                                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                                                dialog_list.setAdapter(adapter);
                                                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                                                    @Override
                                                    public void onClick(View v, final int position) {
                                                        if (searchh.getText().length()>0){
                                                            gov_id=filtered_govs_List.get(position).id;
                                                            governorates.setText(filtered_govs_List.get(position).name);
                                                            dialog.dismiss();

                                                        }else {
                                                            gov_id=govs.get(position).id;
                                                            governorates.setText(govs.get(position).name);
                                                            dialog.dismiss();

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

                                                        filter_govs(s.toString());


                                                    }
                                                });

                                                mprogressBar.setVisibility(View.VISIBLE);
                                                get_goves_data();
                                                dialog.show();
                                            }
                                        });
        /*insurance_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                insurance_id=insurance_companies.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                insurance_id=0;
            }
        });*/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.auto_location) {
                    city.setEnabled(false);
                    check_forall_permissions();
                } else if(checkedId == R.id.select_governate) {
                    city.setEnabled(true);


                }
            }

        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search_by_area_speciality_fragment();
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




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_results_fragment();
                Bundle args = new Bundle();
                args.putInt("city",city_id);
                args.putInt("governorate",gov_id);
                args.putInt("speciality",0);
                args.putInt("insurance_company",insurance_id);
                args.putInt("speciality",speciality_id);
                args.putString("city_s",city.getText().toString());
                args.putString("governorate_s",governorates.getText().toString());
                args.putString("speciality_s",speciality_s);
                if (visitor){
                    args.putBoolean("visitor",true);
                }
                fragment.setArguments(args);
                go_to(fragment);
            }

        });




        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }
    private void check_forall_permissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                            boolean enabledGPS = service
                                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

                            if (!enabledGPS) {
                                Toast.makeText(getActivity(), getResources().getText(R.string.please_turn_on_gps), Toast.LENGTH_LONG).show();
                                RadioButton checked=(RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                                checked.setChecked(false);
                            }else {
                                request_location();
                            }

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Toast.makeText(getActivity(),getActivity().getResources().getText(R.string.location_permission_denied),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }


                })
                .onSameThread()
                .check();
    }
    @SuppressLint("MissingPermission")
    public void request_location(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener( getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                state=addresses.get(0).getAdminArea();
                                city.setText(state);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }



    private void get_cities_data()
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
                    mprogressBar.setVisibility(View.INVISIBLE);

                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);


                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                cities.clear();
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
                                     patient_city_model  city=  new patient_city_model(_id,image_url,name,gov_id,gov_name,id,govid);
                                     cities.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                adapter2.notifyDataSetChanged();

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
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();

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
    private void get_goves_data()
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
                    mprogressBar.setVisibility(View.INVISIBLE);
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);


                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                govs.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_gov_model city=  new patient_gov_model(_id,image_url,name,id);
                                    govs.add(city);
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
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();

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
   /* private void get_insurance_data()
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
                    hideProgress();
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);


                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONArray list=res.getJSONArray("list");
                                insurance_companies.add(new patient_insurance_model("","","none",0));

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_insurance_model city=  new patient_insurance_model(_id,image_url,name,id);
                                    insurance_companies.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                adapter = new insurance_SpinAdapter(getActivity(), android.R.layout.simple_spinner_item, insurance_companies);
                                insurance_company.setAdapter(adapter);


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
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
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

    */

    private void filter_govs(String text) {
        filtered_govs_List.clear();
        for (patient_gov_model item : govs) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }

        }

        adapter.filterList(filtered_govs_List);
    }
    private void filter_city(String text) {
        filtered_cities_List.clear();
        for (patient_city_model item : cities) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }

        }

        adapter2.filterList(filtered_cities_List);
    }
}
