package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import luckysms.gaber.example.com.gallen.R;

public class patient_update_data extends Fragment {
    private View view;
    private Button confirm;
    private TextView back,number_of_notifications,notifications;
    private EditText full_name,mobile_number,email_address,date_of_birth,insurance_company;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_edit_profile_fragment, container, false);
        confirm=(Button)view.findViewById(R.id.confirm);
        full_name=(EditText)view.findViewById(R.id.full_name);
        mobile_number=(EditText) view.findViewById(R.id.mobile_number);
        date_of_birth=(EditText) view.findViewById(R.id.date_of_birth);
        email_address=(EditText) view.findViewById(R.id.email_address);
        insurance_company=(EditText) view.findViewById(R.id.insurance_company);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
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
                    Fragment fragment=new patient_settings();
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
