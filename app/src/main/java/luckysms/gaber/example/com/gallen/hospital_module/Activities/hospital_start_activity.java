package luckysms.gaber.example.com.gallen.hospital_module.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import luckysms.gaber.example.com.gallen.R;

public class hospital_start_activity extends AppCompatActivity {
    ProgressBar bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_homepage);
        bar=(ProgressBar)findViewById(R.id.progressBar);

    }

    public void dashboard(View view) {
        Intent dashboard=new Intent(hospital_start_activity.this,hospital_search_by_doctor_name.class);
        startActivity(dashboard);

    }

    public void create_doctor_account(View view) {
        Intent create_doctor_account=new Intent(hospital_start_activity.this,hospital_search_by_doctor_name.class);
        startActivity(create_doctor_account);
    }

    public void definition_and_addition(View view) {
        Intent definition_and_addition=new Intent(hospital_start_activity.this,hospital_search_by_doctor_name.class);
        startActivity(definition_and_addition);
    }
}
