package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import luckysms.gaber.example.com.gallen.R;

public class patient_sign_up extends AppCompatActivity {
    private RequestQueue queue;
    private TextView code;
    private EditText full_name,mobile_number,email_address,password,confirm_password,date_of_birth,insurance_company;
    private Button register,see_password,see_confirm_password;
    private RadioGroup group;
    private RadioButton selected_gender;
    private boolean seen_pass,seen_confirm_pass;
    private ProgressWindow progressWindow ;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        progressConfigurations();
        code=(TextView)findViewById(R.id.code);
        full_name=(EditText)findViewById(R.id.full_name);
        mobile_number=(EditText)findViewById(R.id.mobile_number);
        email_address=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        date_of_birth=(EditText)findViewById(R.id.date_of_birth);
        insurance_company=(EditText)findViewById(R.id.insurance_company);
        see_password=(Button)findViewById(R.id.see_password);
        see_confirm_password=(Button)findViewById(R.id.see_confirm_password);
        register=(Button)findViewById(R.id.register);
        back=(TextView)findViewById(R.id.back);
        group=(RadioGroup)findViewById(R.id.radioGroup);
        selected_gender=(RadioButton)findViewById(R.id.male);


        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male) {
                    selected_gender=(RadioButton)findViewById(R.id.male);

                } else if(checkedId == R.id.female) {

                    selected_gender=(RadioButton)findViewById(R.id.female);

                }
            }
        });

        code.setText(random_code());


        see_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seen_pass){
                    seen_pass=false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }else {
                    seen_pass=true;
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    password.setSelection(password.getText().length());
                }

            }
        });
        see_confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seen_confirm_pass){
                    seen_confirm_pass=false;
                    confirm_password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirm_password.setSelection(password.getText().length());
                }else {
                    seen_confirm_pass=true;
                    confirm_password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    confirm_password.setSelection(password.getText().length());
                }

            }
        });





        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code_s=code.getText().toString();
                String full_name_s=full_name.getText().toString();
                String mobile_number_s=mobile_number.getText().toString();
                String email_address_s=email_address.getText().toString();
                String password_s=password.getText().toString();
                String confirm_password_s=confirm_password.getText().toString();
                String date_of_birth_s=date_of_birth.getText().toString();
                String insurance_company_s=insurance_company.getText().toString();
                String gender=selected_gender.getText().toString();
               if (code_s.length()>0&&full_name_s.length()>0&&mobile_number_s.length()>0&&email_address_s.length()>0&&password_s.length()>0&&
                       confirm_password_s.length()>0&&date_of_birth_s.length()>0&&insurance_company_s.length()>0){
                   if (confirm_password_s.equals(password_s)) {
                       showProgress();
                       sign_up(code_s, full_name_s, mobile_number_s, email_address_s, password_s, date_of_birth_s, insurance_company_s, gender);
                   }else {
                       Toast.makeText(patient_sign_up.this,getResources().getText(R.string.password_not_matched),Toast.LENGTH_LONG).show();
                   }
               }else {
                   Toast.makeText(patient_sign_up.this,getResources().getString(R.string.login_error),Toast.LENGTH_LONG).show();

               }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patient_start_screen=new Intent(patient_sign_up.this,patient_start_screen.class);
                startActivity(patient_start_screen);
                finish();
            }
        });
    }
    private void sign_up(final String code_s, final String full_name_s, final String mobile_number_s, final String email_address_s
            , final String password_s, final String date_of_birth_s, final String insurance_company_s, final String gender)
    {


        try {
            String url = "http://microtec1.egytag.com/api/register/add";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    hideProgress();
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("patient_id",code_s)
                                        .putString("patient_password", password_s)
                                        .putString("patient_name",full_name_s)
                                        .putString("patient_gender",gender)
                                        .putString("patient_insurance_company",insurance_company_s)
                                        .putString("patient_date_of_birth",date_of_birth_s)
                                        .putString("patient_email_address",email_address_s)
                                        .putString("patient_mobile_number",mobile_number_s)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .commit();

                                Intent not_now=new Intent(patient_sign_up.this,patient_main_screen.class);
                                startActivity(not_now);
                                finish();
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.welcome),Toast.LENGTH_LONG).show();

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
                    pars.put("patient_name", full_name_s);
                    pars.put("patient_mobile", mobile_number_s);
                    pars.put("patient_user_name", email_address_s);
                    pars.put("patient_password", password_s);
                    pars.put("patient_date_of_birth", date_of_birth_s);
                    pars.put("patient_insurance_company", insurance_company_s);
                    pars.put("patient_gender", gender);

                    return pars;
                }
            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }
    private void progressConfigurations(){
        progressWindow = ProgressWindow.getInstance(this);
        ProgressWindowConfiguration progressWindowConfiguration = new ProgressWindowConfiguration();
        progressWindowConfiguration.backgroundColor = Color.parseColor("#32000000") ;
        progressWindowConfiguration.progressColor = Color.WHITE ;
        progressWindow.setConfiguration(progressWindowConfiguration);
    }
    public void showProgress(){
        progressWindow.showProgress();
    }
    public void hideProgress(){
        progressWindow.hideProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgress();

    }

    public String  random_code() {
        byte[] array = new byte[8]; // length is bounded by 8
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        return generatedString;
    }
    private void show_date(){
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date_of_birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}
