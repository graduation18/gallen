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
                            getSharedPreferences("logged_in",MODE_PRIVATE).edit()
                                    .putBoolean("state",true)
                                    .putString("phone",phone)
                                    .commit();

                            Intent intent=new Intent(patient_confirm_code.this,patient_main_screen.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Log.w("khgj",task.getException());

                            Toast.makeText(patient_confirm_code.this,"Login unsuccessful ", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    private void login(final String mobile_number_email_address_s, final String password_s)
    {


        try {
            String url = "http://microtec1.egytag.com/api/user/login";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            if (res.getString("error").equals("email not set")){
                                Toast.makeText(patient_confirm_code.this,getResources().getString(R.string.email_not_set),Toast.LENGTH_LONG).show();

                            }else if (res.getString("error").equals("email or password error")){
                                Toast.makeText(patient_confirm_code.this,getResources().getString(R.string.email_or_password_error),Toast.LENGTH_LONG).show();

                            }

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject user = res.getJSONObject("user");
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("_id", user.getString("_id"))
                                        .putString("email", user.getString("email"))
                                        .putString("password", password_s)
                                        .putBoolean("state",true)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .commit();
                                Intent not_now=new Intent(patient_confirm_code.this,patient_main_screen.class);
                                startActivity(not_now);
                                finish();
                                Toast.makeText(patient_confirm_code.this,getResources().getString(R.string.welcome),Toast.LENGTH_LONG).show();

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("email", mobile_number_email_address_s);
                    pars.put("password", password_s);
                    return pars;
                }
            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }



}
