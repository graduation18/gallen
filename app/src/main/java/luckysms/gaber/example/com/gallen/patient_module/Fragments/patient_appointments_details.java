package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_appointments_drug_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointment_drugs_list_model;

public class patient_appointments_details extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality,insurance_companies;
    private RecyclerView drugs_recycler;
    private List<appointment_drugs_list_model> contact_list = new ArrayList<>();
    private patient_appointments_drug_list_adapter data_adapter;
    private String loc,spe,ins,drugs_list;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            loc=getArguments().getString("location");
            spe=getArguments().getString("speciality");
            ins=getArguments().getString("insurance_companies");
            drugs_list=getArguments().getString("drugs_list");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_appointment_details, container, false);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        back=(TextView)view.findViewById(R.id.back);
        drugs_recycler = view.findViewById(R.id.drugs_recycler);
        data_adapter = new patient_appointments_drug_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        drugs_recycler.setLayoutManager(mLayoutManager);
        drugs_recycler.setItemAnimator(new DefaultItemAnimator());
        drugs_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        drugs_recycler.setAdapter(data_adapter);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
                go_to(fragment);
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_settings();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });


        location.setText(loc);
        speciality.setText(spe);
        insurance_companies.setText(ins);

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void parse_drugs(String drugs){
        try {
            JSONArray drugs_list=new JSONArray(drugs);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
