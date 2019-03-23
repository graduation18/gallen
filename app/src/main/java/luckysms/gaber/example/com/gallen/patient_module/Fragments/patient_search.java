package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Custom.DataPassListener;

public class patient_search extends Fragment implements DataPassListener {
    private View view;
    private Button area_speciality,doctor_name;
    private LinearLayout search_layout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_fragment, container, false);
        area_speciality=(Button)view.findViewById(R.id.area_speciality);
        doctor_name=(Button)view.findViewById(R.id.doctor_name);
        search_layout=(LinearLayout)view.findViewById(R.id.search_layout);
        search_layout.setVisibility(View.VISIBLE);

        area_speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_by_area_speciality_fragment();
                go_to(fragment);
            }
        });
        doctor_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_by_doctor_name_fragment();
                go_to(fragment);
            }
        });
        return view;
    }
    public void go_to(Fragment fragment) {
        search_layout.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    @Override
    public void passData(Fragment fragment, Bundle data) {
        fragment.setArguments(data);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
