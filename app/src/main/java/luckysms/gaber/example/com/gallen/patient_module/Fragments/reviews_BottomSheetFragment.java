package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import luckysms.gaber.example.com.gallen.R;

public class reviews_BottomSheetFragment extends BottomSheetDialogFragment {
    public reviews_BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.patient_doctor_rating_fragment, container, false);
    }
}