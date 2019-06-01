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
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_city_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_gov_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class patient_search_by_area_fragment extends Fragment implements pass_gov_data,pass_city_data  {
    private View view;
    private Button search;
    private TextView back,number_of_notifications,notifications;
    private RadioGroup radioGroup;
    private RadioButton auto_location,select_governate;
    private Button city,governorates;
    private FusedLocationProviderClient mFusedLocationClient;
    private String state;
    private boolean visitor;
    private pass_city_data mListener_city;
    private pass_gov_data mListener_gov;
    private patient_city_model city_model;
    private patient_gov_model gov_model;
    private patient_speciality_model speciality_model;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            visitor=getArguments().getBoolean("visitor");
            speciality_model = (patient_speciality_model) getArguments().getSerializable("speciality");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_fragment, container, false);
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
                                        Bundle s=new Bundle();
                                        if (gov_model!=null&&city_model==null){
                                            s.putInt("gov_id",gov_model.id);
                                        }
                                        search_city_BottomSheetFragment bottomSheetFragment = new search_city_BottomSheetFragment();
                                        bottomSheetFragment.setArguments(s);
                                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                                        mListener_city=bottomSheetFragment;
                                        mListener_city.pass_data(null,patient_search_by_area_fragment.this);

                                    }
                                });
        governorates.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Bundle s=new Bundle();
                                                if (city_model!=null&&gov_model==null){
                                                    s.putInt("city_id",city_model.govid);
                                                }
                                                search_gov_BottomSheetFragment bottomSheetFragment = new search_gov_BottomSheetFragment();
                                                bottomSheetFragment.setArguments(s);
                                                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                                                mListener_gov=bottomSheetFragment;
                                                mListener_gov.pass_data(null,patient_search_by_area_fragment.this);

                                            }
                                        });


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
                if (city_model!=null){
                    args.putSerializable("city",city_model);
                }
                if (gov_model!=null){
                    args.putSerializable("governorate",gov_model);
                }
                args.putSerializable("speciality",speciality_model);
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


    @Override
    public void pass_data(patient_city_model city_model, pass_city_data listner) {
        this.city_model=city_model;
        city.setText(city_model.name);
    }

    @Override
    public void pass_data(patient_gov_model gov_model, pass_gov_data listner) {
        this.gov_model=gov_model;
        governorates.setText(gov_model.name);
    }
}
