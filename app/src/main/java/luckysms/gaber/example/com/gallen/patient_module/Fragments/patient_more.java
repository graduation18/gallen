package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_main_screen;

import static android.content.Context.MODE_PRIVATE;

public class patient_more extends Fragment  {
    private View view;
    private Button settings,approval_list,contact_us,about_us,sign_out;
    public LinearLayout more_layout;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private FrameLayout sign_out_frame;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_more_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        settings=(Button)view.findViewById(R.id.settings);
        approval_list=(Button)view.findViewById(R.id.approval_list);
        contact_us=(Button)view.findViewById(R.id.contact_us);
        about_us=(Button)view.findViewById(R.id.about_us);
        sign_out=(Button)view.findViewById(R.id.sign_out);
        more_layout=(LinearLayout)view.findViewById(R.id.more_layout);
        sign_out_frame=(FrameLayout)view.findViewById(R.id.sign_out_frame);
        Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        Animation bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        settings.startAnimation(bounce);
        approval_list.startAnimation(bounce);
        contact_us.startAnimation(bounce);
        about_us.startAnimation(bounce);
        sign_out.startAnimation(fadein);
        sign_out_frame.startAnimation(fadein);
        patient_settings inst = patient_settings.instance();

        if (inst==null){
            more_layout.setVisibility(View.VISIBLE);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
                go_to(fragment);

            }
        });

        approval_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_approval_list();
                go_to(fragment);
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                logout();
            }
        });



        return view;
    }

    public void go_to(Fragment fragment) {
        more_layout.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void logout()
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/user/logout";
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
                                getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putString("_id", "")
                                        .putInt("id", 0)
                                        .putString("email","")
                                        .putString("password"," ")
                                        .putBoolean("state",false)
                                        .commit();
                                getActivity().finish();

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


                public String getBodyContentType()
                {
                    return "application/json; charset=utf-8";
                }



            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

}
