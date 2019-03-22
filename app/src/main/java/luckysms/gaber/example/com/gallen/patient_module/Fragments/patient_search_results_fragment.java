package luckysms.gaber.example.com.gallen.patient_module.Fragments;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_approval_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Model.approval_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public class patient_search_results_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality,insurance_companies;
    private RecyclerView search_result_recycler;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private patient_search_result_list_adapter data_adapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_and_speciality_result_fragment, container, false);
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

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
