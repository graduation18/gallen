package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rilixtech.CountryCodePicker;

import luckysms.gaber.example.com.gallen.R;

public class patient_forget_password extends AppCompatActivity {
    private TextView back;
    private EditText mobile_number;
    private Button send;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_patient_forget_password);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        back=(TextView)findViewById(R.id.back);
        mobile_number=(EditText)findViewById(R.id.mobile_number);
        send=(Button)findViewById(R.id.send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patient_start_screen=new Intent(patient_forget_password.this,patient_login.class);
                startActivity(patient_start_screen);
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_number_s=mobile_number.getText().toString();
                if (mobile_number_s.charAt(0)=='0'){
                    mobile_number_s=mobile_number_s.substring(1);
                }
                Log.w("dsaldj","+"+ccp.getSelectedCountryCode()+mobile_number_s);

                if (("+"+ccp.getSelectedCountryCode()+mobile_number_s).length()==13){
                    Intent got_confirm_code=new Intent(patient_forget_password.this,patient_confirm_code.class);
                    Log.w("dsaldj","+"+ccp.getSelectedCountryCode()+mobile_number_s);
                    got_confirm_code.putExtra("phone_number","+"+ccp.getSelectedCountryCode()+mobile_number_s);
                    startActivity(got_confirm_code);
                    finish();
                }else {
                    Toast.makeText(patient_forget_password.this,getResources().getText(R.string.wrong_mobile_number),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
