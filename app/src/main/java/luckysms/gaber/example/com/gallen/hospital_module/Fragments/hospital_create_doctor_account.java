package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_defintions;
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_doctor_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_main_screen;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_specilty_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class hospital_create_doctor_account extends Fragment implements pass_speciality_data,pass_doctor_data{
    private View view;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private EditText doctor_phone,email_address,password,confirm_password;
    private pass_speciality_data mListener;
    private pass_doctor_data doctor_mListener;
    private patient_speciality_model speciality_model;
    private search_doctor_name_model search_doctor_name_model;
    private Button doctor_name,speciality,register;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_doctor_account, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        doctor_name=(Button)view.findViewById(R.id.doctor_name);
        speciality=(Button)view.findViewById(R.id.speciality);
        register=(Button)view.findViewById(R.id.register);
        doctor_phone=(EditText)view.findViewById(R.id.doctor_phone);
        email_address=(EditText)view.findViewById(R.id.email_address);
        password=(EditText)view.findViewById(R.id.password);
        confirm_password=(EditText)view.findViewById(R.id.confirm_password);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),hospital_defintions.class);
                startActivity(intent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        doctor_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (speciality_model!=null&&search_doctor_name_model==null){
                    s.putInt("speciality_id",speciality_model.id);
                }
                search_doctors_BottomSheetFragment bottomSheetFragment = new search_doctors_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                doctor_mListener=bottomSheetFragment;
                doctor_mListener.pass_data(null,hospital_create_doctor_account.this);
            }
        });
        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (search_doctor_name_model!=null&&speciality_model==null){
                    s.putInt("speciality_id",search_doctor_name_model.speciality_model.id);
                }
                search_specilty_BottomSheetFragment bottomSheetFragment = new search_specilty_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener=bottomSheetFragment;
                mListener.pass_data(null,hospital_create_doctor_account.this);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=password.getText().toString();
                String pass_confirm=confirm_password.getText().toString();
                String email=email_address.getText().toString();
                if (search_doctor_name_model!=null&&speciality_model!=null&&pass.equals(pass_confirm)&&email.length()>0){
                  update(search_doctor_name_model.doctor_model.id,email,pass);
                }else {

                }
            }
        });

        return view;
    }

    private void update(final int id , final String username, final String password)
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/doctors/update";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    mprogressBar.setVisibility(View.INVISIBLE);
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                speciality_model=null;
                                speciality.setText("");
                                search_doctor_name_model=null;
                                doctor_name.setText("");
                                confirm_password.setText("");
                                hospital_create_doctor_account.this.password.setText("");
                                email_address.setText("");
                                doctor_phone.setText("");

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", id);
                        object.put("username", username);
                        object.put("password", password);
                        } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.w("sadkjsdkjlljksda",object.toString());
                    return object.toString().getBytes();

                };

                public String getBodyContentType()
                {
                    return "application/json; charset=utf-8";
                }






            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

    @Override
    public void pass_data(patient_speciality_model speciality_model, pass_speciality_data listner) {
        this.speciality_model=speciality_model;
        speciality.setText(speciality_model.name);
    }

    @Override
    public void pass_data(search_doctor_name_model search_doctor_name_model, pass_doctor_data listner) {
        this.search_doctor_name_model=search_doctor_name_model;
        doctor_name.setText(search_doctor_name_model.doctor_model.doctor_name);
        email_address.setText(search_doctor_name_model.doctor_model.doctor_email);
        doctor_phone.setText(search_doctor_name_model.doctor_model.doctor_phone);

    }
}
