package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_defintions;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;

public class hospital_create_doctor_account extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_clinic_definition, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);

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
        return view;
    }

}
