package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_confirm_reservation_fragment;

public class complete_booking extends AppCompatActivity {
    private TextView number_of_notifications,notifications;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_complete_the_booking_fragment);

        number_of_notifications=(TextView)findViewById(R.id.number_of_notifications);
        notifications=(TextView)findViewById(R.id.notifications);
        confirm=(Button)findViewById(R.id.confirm);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main=new Intent(complete_booking.this,patient_main_screen.class);
                startActivity(go_main);
                finish();

            }
        });
    }
}
