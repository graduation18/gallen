package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_main_screen;
import luckysms.gaber.example.com.gallen.patient_module.Custom.DataPassListener;

public class patient_settings extends Fragment {
    private View view;
    private Button update_data,change_password;
    private RadioGroup radioGroup;
    private RadioButton English,Arabic;
    private TextView back,number_of_notifications,notifications;
    public static patient_settings inst;

    @Override
    public void onStart() {
        super.onStart();
        inst=this;
    }
    public static patient_settings instance() {
        return inst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_settings_fragment, container, false);
        update_data=(Button)view.findViewById(R.id.update_data);
        change_password=(Button)view.findViewById(R.id.change_password);
        English=(RadioButton) view.findViewById(R.id.English);
        Arabic=(RadioButton) view.findViewById(R.id.Arabic);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_more();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_update_data();
                go_to(fragment);
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_change_password();
                go_to(fragment);
            }
        });


        Log.w("sa;ksdakas",Locale.getDefault().getLanguage());



        if (getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getString("language","").equals("en")){
            English.setChecked(true);
        }else if (getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getString("language","").equals("ar")){
            Arabic.setChecked(true);
        }else {
            if (Locale.getDefault().getLanguage().contains("ar")){
                Arabic.setChecked(true);
            }else {
                English.setChecked(true);
            }
        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.English) {
                    Resources res = getActivity().getResources();
// Change locale settings in the app.
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    conf.setLocale(new Locale("en")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
                    res.updateConfiguration(conf, dm);
                    getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).edit()
                            .putString("language","en")
                            .commit();

                    go_to_main();




                } else if(checkedId == R.id.Arabic) {
                    Resources res = getActivity().getResources();
// Change locale settings in the app.
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    conf.setLocale(new Locale("ar")); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
                    res.updateConfiguration(conf, dm);
                    getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).edit()
                            .putString("language","ar")
                            .commit();

                    go_to_main();



                }
            }

        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_more();
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
    public void go_to_main(){
        Intent go_to_main=new Intent(getActivity(),patient_main_screen.class);
        startActivity(go_to_main);
        getActivity().finish();
    }

}
