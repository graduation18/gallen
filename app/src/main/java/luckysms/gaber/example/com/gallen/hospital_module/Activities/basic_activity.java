package luckysms.gaber.example.com.gallen.hospital_module.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_clinic_definition;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_create_doctor_account;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_doctor_definition;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_doctor_edit_working_times;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_doctor_working_times;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.hospital_search_edit_doctor;

public class basic_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_activity);
        String fragment=getIntent().getStringExtra("fragment");
        if (fragment.equals("create_doctor_account")){
            Fragment goto_fragment=new hospital_create_doctor_account();
            loadFragment(goto_fragment);
        }else if (fragment.equals("block_patient")){
            Fragment goto_fragment=new hospital_clinic_definition();
            loadFragment(goto_fragment);
        }else if (fragment.equals("view_edit_working_time")){
            Fragment goto_fragment=new hospital_doctor_edit_working_times();
            loadFragment(goto_fragment);
        }else if (fragment.equals("define_working_time")){
            Fragment goto_fragment=new hospital_doctor_working_times();
            loadFragment(goto_fragment);
        }else if (fragment.equals("edit_delete_doctor")){
            Fragment goto_fragment=new hospital_search_edit_doctor();
            loadFragment(goto_fragment);
        }else if (fragment.equals("doctor_registration")){
            Fragment goto_fragment=new hospital_doctor_definition();
            loadFragment(goto_fragment);
        }else if (fragment.contains("clinic_definition")){
            Fragment goto_fragment=new hospital_clinic_definition();
            loadFragment(goto_fragment);
        }
    }
    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();    }
}
