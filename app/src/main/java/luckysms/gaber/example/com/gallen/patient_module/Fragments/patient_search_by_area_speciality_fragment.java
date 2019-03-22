package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import luckysms.gaber.example.com.gallen.R;

public class patient_search_by_area_speciality_fragment extends Fragment {
    private View view;
    private Button search;
    private TextView back,number_of_notifications,notifications;
    private RadioGroup radioGroup;
    private RadioButton auto_location,select_governate;
    private AutoCompleteTextView city,governorates;
    private Spinner speciality,insurance_company;
    private FusedLocationProviderClient mFusedLocationClient;
    private String state;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_and_speciality_fragment, container, false);
        city=(AutoCompleteTextView)view.findViewById(R.id.city);
        governorates=(AutoCompleteTextView)view.findViewById(R.id.governorates);
        speciality=(Spinner)view.findViewById(R.id.speciality);
        insurance_company=(Spinner)view.findViewById(R.id.insurance_company);
        search=(Button)view.findViewById(R.id.search);
        auto_location=(RadioButton) view.findViewById(R.id.auto_location);
        select_governate=(RadioButton) view.findViewById(R.id.select_governate);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);


        city.setEnabled(false);


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
                    Fragment fragment=new patient_search();
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
                go_to(fragment);
            }
        });

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
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
                            request_location();
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
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
