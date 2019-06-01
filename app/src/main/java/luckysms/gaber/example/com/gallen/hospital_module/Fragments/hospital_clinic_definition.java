package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rilixtech.CountryCodePicker;

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
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_defintions;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.LocationFinder;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_city_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_gov_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_insurance_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_search_by_area_speciality_fragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_city_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_gov_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_insurance_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class hospital_clinic_definition extends Fragment implements pass_gov_data,pass_city_data,pass_insurance_data,pass_speciality_data {
    private View view;
    private EditText hospital_code,full_name,hospital_address,email_address,website,hospital_phone;
    private Button speciality,governorates,area,insurance_company,save;
    private TextView back,number_of_notifications,notifications;
    private CountryCodePicker cpp;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private pass_speciality_data mListener;
    private pass_insurance_data mListener_insurance;
    private pass_city_data mListener_city;
    private pass_gov_data mListener_gov;
    private patient_city_model city_model;
    private patient_gov_model gov_model;
    private patient_insurance_model insurance_model;
    private patient_speciality_model speciality_model;
    private double latitude ;
    private double longitude;
    private String address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_clinic_definition, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        hospital_code=(EditText)view.findViewById(R.id.hospital_code);
        full_name=(EditText)view.findViewById(R.id.full_name);
        hospital_address=(EditText)view.findViewById(R.id.hospital_address);
        email_address=(EditText)view.findViewById(R.id.email_address);
        website=(EditText)view.findViewById(R.id.website);
        hospital_phone=(EditText)view.findViewById(R.id.hospital_phone);
        speciality=(Button)view.findViewById(R.id.speciality);
        governorates=(Button)view.findViewById(R.id.governorates);
        area=(Button)view.findViewById(R.id.area);
        insurance_company=(Button)view.findViewById(R.id.insurance_company);
        save=(Button)view.findViewById(R.id.save);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        cpp=(CountryCodePicker)view.findViewById(R.id.ccp);
        check_forall_permissions();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(),hospital_defintions.class);
               startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hospital_code_s=hospital_code.getText().toString();
                String full_name_s=full_name.getText().toString();
                String hospital_address_s=hospital_address.getText().toString();
                String email_address_s=email_address.getText().toString();
                String website_s=website.getText().toString();
                String hospital_phone_s="+"+cpp.getSelectedCountryCode()+hospital_phone.getText().toString();
                JSONObject hospital=new JSONObject();
                JSONObject gov=new JSONObject();
                JSONObject city=new JSONObject();
                JSONArray insurance_company_list=new JSONArray();
                JSONObject specialty=new JSONObject();


                if (hospital_code_s.length()>0&&full_name_s.length()>0&&
                        email_address_s.length()>0&&hospital_phone_s.length()==13&&city_model!=null
                        &&speciality_model!=null&&gov_model!=null&&insurance_model!=null) {

                    try {
                        Log.w("sdasdsd", String.valueOf(latitude)+" "+speciality_model.name+" "+city_model.name+" "+insurance_model.name+" "+gov_model.name);

                        hospital.put("id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                        hospital.put("name",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("name",""));
                        gov.put("id",gov_model.id);
                        gov.put("name",gov_model.name);
                        city.put("id",city_model.id);
                        city.put("name",city_model.name);

                        specialty.put("name",speciality_model.name);
                        specialty.put("id",speciality_model.id);

                        JSONObject insurance_company =new JSONObject();
                        insurance_company.put("id",insurance_model.id);
                        insurance_company.put("name",insurance_model.name);
                        insurance_company.put("_id",insurance_model._id);
                        insurance_company.put("image_url",insurance_model.image_url);
                        insurance_company_list.put(insurance_company);
                            mprogressBar.setVisibility(View.VISIBLE);
                            add_clinic(full_name_s,true,hospital,gov,city
                                    ,address,hospital_phone_s,website_s,email_address_s,getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("image_url", "hospital"),
                                    insurance_company_list,new JSONArray(),new JSONArray(),specialty);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    if (speciality_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_speciality_first),Toast.LENGTH_LONG).show();
                    }
                    else if (gov_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_government_first),Toast.LENGTH_LONG).show();
                    }
                    else if (city_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_city_first),Toast.LENGTH_LONG).show();
                    }
                    else if (insurance_model==null){
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_select_insurance_company_first),Toast.LENGTH_LONG).show();
                    }
                    else if (hospital_phone_s.length()!=13){
                        if (hospital_phone_s.length()<13){
                            Toast.makeText(getActivity(),getResources().getString(R.string.number_is_too_long),Toast.LENGTH_LONG).show();
                        }else if (hospital_phone_s.length()>13){
                            Toast.makeText(getActivity(),getResources().getString(R.string.number_is_too_short),Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.please_fill_data_fields),Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment bottomSheetFragment = new search_specilty_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(null,hospital_clinic_definition.this);

            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (gov_model!=null&&city_model==null){
                    s.putInt("gov_id",gov_model.id);
                }
                luckysms.gaber.example.com.gallen.patient_module.Fragments.search_city_BottomSheetFragment bottomSheetFragment = new search_city_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener_city=bottomSheetFragment;
                mListener_city.pass_data(null,hospital_clinic_definition.this);
            }
        });
        governorates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (city_model!=null&&gov_model==null){
                    s.putInt("city_id",city_model.govid);
                }
                luckysms.gaber.example.com.gallen.patient_module.Fragments.search_gov_BottomSheetFragment bottomSheetFragment = new search_gov_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener_gov=bottomSheetFragment;
                mListener_gov.pass_data(null,hospital_clinic_definition.this);

            }
        });
        insurance_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                luckysms.gaber.example.com.gallen.patient_module.Fragments.search_insurance_BottomSheetFragment bottomSheetFragment = new search_insurance_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener_insurance=bottomSheetFragment;
                mListener_insurance.pass_data(null,hospital_clinic_definition.this);
            }
        });




        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void add_clinic(final String name, final boolean active, final JSONObject hospital, final JSONObject gov, final JSONObject city, final String address, final String phone
            , final String website, final String email , final String image_url , final JSONArray insurance_company_list,
                            final JSONArray doctor_list , final JSONArray nurse_list,final JSONObject specialty)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/clinics/add";
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
                        if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                hospital_clinic_definition.this.hospital_code.setText("");
                                hospital_clinic_definition.this.full_name.setText("");
                                hospital_clinic_definition.this.hospital_address.setText("");
                                hospital_clinic_definition.this.email_address.setText("");
                                hospital_clinic_definition.this.website.setText("");
                                hospital_clinic_definition.this.hospital_phone.setText("");
                                hospital_clinic_definition.this.speciality.setText("");
                                hospital_clinic_definition.this.insurance_company.setText("");
                                hospital_clinic_definition.this.area.setText("");

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
            }){
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
                        object.put("name",name);
                        object.put("active",active);
                        object.put("hospital",hospital);
                        object.put("gov",gov);
                        object.put("city",city);
                        object.put("address",address);
                        object.put("phone",phone);
                        object.put("website",website);
                        object.put("email",email);
                        object.put("image_url",image_url);
                        object.put("specialty",specialty);
                        object.put("insurance_company_list",insurance_company_list);
                        object.put("doctor_list",doctor_list);
                        object.put("nurse_list",nurse_list);
                        object.put("latitude",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("latitude",""));
                        object.put("longitude",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("longitude",""));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.w("sadkjsdkjlljksda",object.toString());
                    return object.toString().getBytes();

                }

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

    private void get_location(){
        LocationFinder finder;
        finder = new LocationFinder(getActivity());
        if (finder.canGetLocation()) {
            latitude = finder.getLatitude();
            longitude = finder.getLongitude();
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);
                hospital_address.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            finder.showSettingsAlert();
        }
    }

    @Override
    public void pass_data(patient_city_model city_model, pass_city_data listner) {
        this.city_model=city_model;
        area.setText(city_model.name);
    }
    @Override
    public void pass_data(patient_gov_model gov_model, pass_gov_data listner) {
        this.gov_model=gov_model;
        governorates.setText(gov_model.name);
    }
    @Override
    public void pass_data(patient_insurance_model insurance_model, pass_insurance_data listner) {
        this.insurance_model=insurance_model;
        insurance_company.setText(insurance_model.name);
    }
    @Override
    public void pass_data(patient_speciality_model speciality_model, pass_speciality_data listner) {
        this.speciality_model=speciality_model;
        speciality.setText(speciality_model.name);
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

                                get_location();


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
}
