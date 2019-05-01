package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.pix.Pix;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class hospital_doctor_edit_delete extends Fragment {
    private View view;
    private ImageView doctor_image;
    private TextView doctor_code,full_name,doctor_phone,email_address,doctor_fee,doctor_info,doctor_accept_code,speciality;
    private Button edit_doctor,delete_doctor;
    private TextView back,number_of_notifications,notifications;
    int PICK_IMAGE_MULTIPLE = 1;
    private RecyclerView dialog_list;
    private speciality_SpinAdapter speciality_adapter;
    private ArrayList<patient_speciality_model> specialities=new ArrayList<>();
    private ArrayList<patient_speciality_model> filteredList = new ArrayList<>();
    private patient_speciality_model selected_speciality;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private String selected_image,doctor_name_s,doctor_availabilty_s,doctor_gender_s
            ,doctor_graduated_s,doctor_image_s,doctor_notes_s,dotor_code_s,review_list_s,speciality_name_s,doctor_email_s,doctor_phone_s;
    private boolean doctor_accept_discount_b;
    private double doctor_fee_d;
    private Float doctor_rating_f;
    private int id_i;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null) {
            doctor_name_s=getArguments().getString("doctor_name");
            doctor_availabilty_s=getArguments().getString("doctor_availabilty");
            doctor_gender_s=getArguments().getString("doctor_gender");
            doctor_graduated_s=getArguments().getString("doctor_graduated");
            doctor_image_s= getArguments().getString("doctor_image");
            doctor_notes_s=getArguments().getString("doctor_notes");
            dotor_code_s=getArguments().getString("dotor_code");
            doctor_phone_s=getArguments().getString("doctor_phone");
            doctor_email_s=getArguments().getString("doctor_email");
            review_list_s= getArguments().getString("review_list");
            speciality_name_s= getArguments().getString("speciality_name");
            doctor_accept_discount_b= getArguments().getBoolean("doctor_accept_discount");
            doctor_fee_d= getArguments().getDouble("doctor_fee");
            doctor_rating_f=getArguments().getFloat("doctor_rating");
            id_i=getArguments().getInt("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_doctor_view, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        mprogressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        doctor_code=(TextView)view.findViewById(R.id.doctor_code);
        full_name=(TextView)view.findViewById(R.id.full_name);
        doctor_phone=(TextView)view.findViewById(R.id.doctor_phone);
        email_address=(TextView)view.findViewById(R.id.email_address);
        doctor_fee=(TextView)view.findViewById(R.id.doctor_fee);
        doctor_info=(TextView)view.findViewById(R.id.doctor_info);
        doctor_accept_code=(TextView)view.findViewById(R.id.doctor_accept_code);
        speciality=(TextView)view.findViewById(R.id.speciality);
        delete_doctor=(Button)view.findViewById(R.id.delete_doctor);
        edit_doctor=(Button)view.findViewById(R.id.edit_doctor);
        doctor_image=(ImageView)view.findViewById(R.id.doctor_image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        delete_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_doctor(id_i);
            }
        });
        edit_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("doctor_name",speciality_name_s);
                bundle.putString("doctor_availabilty",doctor_availabilty_s);
                bundle.putString("doctor_gender",doctor_gender_s);
                bundle.putString("doctor_graduated",doctor_graduated_s);
                bundle.putString("doctor_image",doctor_image_s);
                bundle.putString("doctor_notes",doctor_notes_s);
                bundle.putString("dotor_code",dotor_code_s);
                bundle.putString("doctor_phone",doctor_phone_s);
                bundle.putString("review_list",review_list_s);
                bundle.putString("doctor_email",doctor_email_s);
                bundle.putString("speciality_name",speciality_name_s);
                bundle.putBoolean("doctor_accept_discount",doctor_accept_discount_b);
                bundle.putDouble("doctor_fee",doctor_fee_d);
                bundle.putFloat("doctor_rating",doctor_rating_f);
                bundle.putInt("id",id_i);
                Fragment hospital_doctor_edit_delete=new hospital_doctor_edit_delete();
                hospital_doctor_edit_delete.setArguments(bundle);
                go_to(hospital_doctor_edit_delete);

            }
        });

        set_data();

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void set_data(){
        doctor_code.setText(dotor_code_s);
        full_name.setText(doctor_name_s);
        doctor_phone.setText(doctor_phone_s);
        email_address.setText(doctor_email_s);
        doctor_fee.setText(String .valueOf(doctor_fee_d));
        doctor_info.setText(doctor_notes_s);
        if (doctor_accept_discount_b) {
            doctor_accept_code.setText(getResources().getText(R.string.Accepts_the_discount_code));
        }else {
            doctor_accept_code.setText(getResources().getText(R.string.not_Accepts_the_discount_code));

        }
        speciality.setText(speciality_name_s);
    }
    private void delete_doctor(final int id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/doctors/delete";
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
                        if (res.has("done")) {
                            if (res.getBoolean("done")) {
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
                    return pars;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",id);


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
            stringReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

}