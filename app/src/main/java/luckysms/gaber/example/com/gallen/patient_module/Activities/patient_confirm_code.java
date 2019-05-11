package luckysms.gaber.example.com.gallen.patient_module.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.doctor_module.Activities.basic_activity;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_start_activity;


/**
 * Created by gaber on 11/30/2018.
 */

public class patient_confirm_code extends AppCompatActivity {
    FirebaseAuth auth;
    String vervication_id,phone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private OtpTextView otpTextView;
    private RequestQueue queue;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.patient_confirm_code_fragment);
        auth=FirebaseAuth.getInstance();
        otpTextView = findViewById(R.id.otp_view);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code=credential.getSmsCode();
                otpTextView.setOTP(code);
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String mverificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                vervication_id = mverificationId;

            }
        };
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                sign_in(vervication_id,otpTextView.getOTP());
            }
        });

        phone=getIntent().getStringExtra("phone_number");
        get_verfiy_code(phone);


    }
    private void get_verfiy_code(String phone_number)
    {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);// OnVerificationStateChangedCallbacks

    }
    private void sign_in(String vervication_id,String code)
    {
        PhoneAuthCredential authCredential= PhoneAuthProvider.getCredential(vervication_id,code);
        signInWithPhoneAuthCredential(authCredential);
    }
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential)
    {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                    .putBoolean("state",true)
                                    .putString("phone",phone)
                                    .commit();
                            if (getSharedPreferences("personal_data",MODE_PRIVATE).getString("type","").equals("hospital")){
                                Intent intent=new Intent(patient_confirm_code.this,hospital_start_activity.class);
                                startActivity(intent);
                                finish();
                            }else if (getSharedPreferences("personal_data",MODE_PRIVATE).getString("type","").equals("patient")){
                                Intent intent=new Intent(patient_confirm_code.this,patient_main_screen.class);
                                startActivity(intent);
                                finish();
                            }else if (getSharedPreferences("personal_data",MODE_PRIVATE).getString("type","").equals("doctor")){
                                Intent intent=new Intent(patient_confirm_code.this,basic_activity.class);
                                startActivity(intent);
                                finish();
                            }



                        }else {
                            Log.w("khgj",task.getException());

                            Toast.makeText(patient_confirm_code.this,"Login unsuccessful ", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }




}
