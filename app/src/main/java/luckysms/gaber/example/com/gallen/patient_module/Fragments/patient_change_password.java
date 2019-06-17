package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_main_screen;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_change_password extends Fragment {
    private View view;
    private Button confirm;
    private TextView back,number_of_notifications,notifications;
    private EditText old_password,new_password;
    private RequestQueue queue;
    private String old_pass,correct_pass;
    private ProgressBar mprogressBar;
    private JSONObject user_info;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_change_password_fragment, container, false);
        confirm=(Button)view.findViewById(R.id.confirm);
        old_password=(EditText)view.findViewById(R.id.old_password);
        new_password=(EditText) view.findViewById(R.id.new_password);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        old_pass=getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("patient_password", "");
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pass=new_password.getText().toString();
                String old_pass=old_password.getText().toString();
                if (old_pass.equals(correct_pass)){
                    mprogressBar.setVisibility(View.VISIBLE);
                    update(new_pass);
                }
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_settings();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });
        mprogressBar.setVisibility(View.VISIBLE);
        get_data();
        return view;
    }


    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void update( final String password_s)
    {


        try {
            final int []counter={0};
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/update";
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
                           // Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                new_password.setText("");
                                old_password.setText("");
                                Toast.makeText(getActivity(),getResources().getText(R.string.password_reset),Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (counter[0]<4) {
                        update(password_s);
                        counter[0]++;
                    }else {
                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getContext(),
                                    "Oops. Timeout error!",
                                    Toast.LENGTH_LONG).show();
                        }
                        mprogressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }
                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0));
                        object.put("password",password_s);
                        object.put("name",user_info.getString("name"));
                        object.put("mobile",user_info.getString("mobile"));
                        object.put("username",user_info.getString("username"));
                        object.put("email",user_info.getString("email"));
                        object.put("image_url",user_info.getString("image_url"));
                        object.put("type",user_info.getString("type"));
                        object.put("user_info",user_info.getJSONObject("user_info"));

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

    private void get_data()
    {


        try {
            final int []counter={0};
            String url = "http://intmicrotec.neat-url.com:6566/api/patients/view";
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
                           // Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                user_info=res.getJSONObject("doc");
                                correct_pass=user_info.getString("password");




                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (counter[0]<4) {
                        get_data();
                        counter[0]++;
                    }else {
                        if (error instanceof NetworkError) {
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getContext(),
                                    "Oops. Timeout error!",
                                    Toast.LENGTH_LONG).show();
                        }
                        mprogressBar.setVisibility(View.INVISIBLE);
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }

                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",getActivity().getSharedPreferences("personal_data",Context.MODE_PRIVATE).getInt("id",0));




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
}
