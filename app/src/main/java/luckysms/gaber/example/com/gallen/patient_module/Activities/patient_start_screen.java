package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import luckysms.gaber.example.com.gallen.R;

public class patient_start_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_screen);
    }

    public void sign_in(View view) {
        Intent sign_in=new Intent(this,patient_login.class);
        startActivity(sign_in);
    }

    public void sign_up(View view) {
        Intent sign_up=new Intent(this,patient_sign_up.class);
        startActivity(sign_up);
    }

    public void not_now(View view) {
        Intent not_now=new Intent(this,patient_main_screen.class);
        startActivity(not_now);
        finish();
    }
}
