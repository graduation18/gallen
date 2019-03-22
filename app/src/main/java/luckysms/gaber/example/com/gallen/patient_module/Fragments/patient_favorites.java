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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public class patient_favorites extends Fragment {
    private View view;
    private TextView number_of_notifications,notifications,location,speciality,insurance_companies;
    private RecyclerView favorite_recycler;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private patient_search_result_list_adapter data_adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_favourites_doctors_fragment, container, false);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        favorite_recycler = view.findViewById(R.id.favorite_recycler);
        data_adapter = new patient_search_result_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favorite_recycler.setLayoutManager(mLayoutManager);
        favorite_recycler.setItemAnimator(new DefaultItemAnimator());
        favorite_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        favorite_recycler.setAdapter(data_adapter);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}
