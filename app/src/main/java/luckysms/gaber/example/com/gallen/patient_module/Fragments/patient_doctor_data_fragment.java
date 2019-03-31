package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluehomestudio.progresswindow.ProgressWindow;
import com.bluehomestudio.progresswindow.ProgressWindowConfiguration;
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
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_available_appointments_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_reviews_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.DataPassListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.reviews_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class patient_doctor_data_fragment extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,favorite
            ,name,speciality,discount_code,rating_ratio,doctor_fee,doctor_info,graduated_from;
    private Button vistors_reviews;
    private ImageView image;
    private RatingBar rating;
    private RecyclerView available_appointments_recycler;
    private List<available_appointments_list_model> contact_list = new ArrayList<>();
    private patient_doctor_available_appointments_list_adapter data_adapter;
    DataPassListener mCallback;
    private RequestQueue queue;
    private ProgressWindow progressWindow ;
    private int id,clinic_id,hospital_id,speciallity_id;
    private String doctor_name,doctor_speciality,doctor_availabilty,doctor_graduated,doctor_location,doctor_image,clinic_name,hospital_name;
    private boolean doctor_accept_discount;
    private Float doctor_rating;
    private double doctor_fees;






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        id=args.getInt("doctor_id");
        doctor_availabilty=args.getString("doctor_availabilty");
        doctor_graduated=args.getString("doctor_graduated");
        doctor_image=args.getString("doctor_image");
        doctor_location=args.getString("doctor_location");
        doctor_name=args.getString("doctor_name");
        doctor_speciality=args.getString("doctor_speciality");
        doctor_accept_discount=args.getBoolean("doctor_accept_discount");
        doctor_fees=args.getDouble("doctor_fee");
        doctor_rating=args.getFloat("doctor_rating");
        speciallity_id=args.getInt("doctor_speciality_id");

    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_doctor_data_fragment, container, false);
        progressConfigurations();
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        favorite=(TextView)view.findViewById(R.id.favorite);
        name=(TextView)view.findViewById(R.id.name);
        speciality=(TextView)view.findViewById(R.id.speciality);
        discount_code=(TextView)view.findViewById(R.id.discount_code);
        rating_ratio=(TextView)view.findViewById(R.id.rating_ratio);
        doctor_fee=(TextView)view.findViewById(R.id.doctor_fee);
        doctor_info=(TextView)view.findViewById(R.id.doctor_info);
        graduated_from=(TextView)view.findViewById(R.id.graduated_from);
        image=(ImageView)view.findViewById(R.id.image);
        rating=(RatingBar)view.findViewById(R.id.rating);
        vistors_reviews=(Button)view.findViewById(R.id.vistors_reviews);
        available_appointments_recycler = view.findViewById(R.id.available_appointments_recycler);
        data_adapter = new patient_doctor_available_appointments_list_adapter(getActivity(), contact_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        available_appointments_recycler.setLayoutManager(layoutManager);
        available_appointments_recycler.setItemAnimator(new DefaultItemAnimator());
        available_appointments_recycler.setAdapter(data_adapter);
        available_appointments_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), available_appointments_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Bundle args = new Bundle();
                args.putString("day",contact_list.get(position).day);
                args.putString("from",contact_list.get(position).from);
                args.putString("to",contact_list.get(position).to);
                args.putInt("day_id",contact_list.get(position).day_id);
                args.putInt("from_id",contact_list.get(position).from_id);
                args.putInt("to_id",contact_list.get(position).to_id);
                args.putInt("speciallity_id",speciallity_id);
                args.putString("hospital_name",hospital_name);
                args.putString("clinic_name",clinic_name);

                args.putInt("clinic_id",clinic_id);
                args.putInt("hospital_id",hospital_id);


                args.putInt("doctor_id",id);
                args.putString("doctor_availabilty",doctor_availabilty);
                args.putString("doctor_graduated",doctor_graduated);
                args.putString("doctor_image",doctor_image);
                args.putString("doctor_location",doctor_location);
                args.putString("doctor_name",doctor_name);
                args.putString("doctor_speciality",doctor_speciality);
                args.putBoolean("doctor_accept_discount",doctor_accept_discount);
                args.putDouble("doctor_fee",doctor_fees);
                args.putFloat("doctor_rating",doctor_rating);
                Fragment fragment=new patient_confirm_reservation_fragment();
                fragment.setArguments(args);
                go_to(fragment);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_results_fragment();
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vistors_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search_results_fragment();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });

        showProgress();
        get_doctor_data();
        get_doctor_available_appintments(id);
        return view;
    }


    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void show_dialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.patient_doctor_rating_fragment);
        Button cancel=(Button)dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView reviews_recycler;
        List<reviews_list_model> reviews_list = new ArrayList<>();
        patient_doctor_reviews_list_adapter data_adapter;
        reviews_recycler = view.findViewById(R.id.reviews_recycler);
        data_adapter = new patient_doctor_reviews_list_adapter(getActivity(), reviews_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        reviews_recycler.setLayoutManager(mLayoutManager);
        reviews_recycler.setItemAnimator(new DefaultItemAnimator());
        reviews_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 5));
        reviews_recycler.setAdapter(data_adapter);


        dialog.show();

    }
    private void get_doctor_data()
    {

        name.setText(doctor_name);
        speciality.setText(doctor_speciality);
        graduated_from.setText(doctor_location);
        if (doctor_accept_discount){
            discount_code.setVisibility(View.VISIBLE);
        }
        rating.setRating(doctor_rating);
        rating_ratio.setText(String .valueOf(doctor_rating));
        doctor_fee.setText(doctor_fee.getText().toString()+" "+String .valueOf(doctor_fees));
        Picasso.with(getActivity())
                .load(doctor_image)
                .placeholder(R.drawable.locations_map)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override public void onError() {
                        Toast.makeText(getActivity(),"error loading image",Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void get_doctor_available_appintments(final int id){

        try {
            String url = "http://microtec1.egytag.com:30001/api/clinics/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(getActivity());
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
                            Toast.makeText(getActivity(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);

                                    JSONArray shift_list=object.getJSONArray("shift_list");
                                    for (int j=0;j<shift_list.length();j++){
                                        JSONObject shift=shift_list.getJSONObject(j);

                                    }
                                    JSONArray doctor_list=object.getJSONArray("doctor_list");
                                    JSONObject doctor=doctor_list.getJSONObject(0);
                                    JSONObject doc=doctor.getJSONObject("doctor");
                                    if (doc.getInt("id")==id){
                                        String _id=object.getString("_id");
                                        int id=object.getInt("id");
                                        String image_url=object.getString("image_url");
                                        String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                        clinic_name=name;
                                        clinic_id=id;
                                        JSONObject hospital=object.getJSONObject("hospital");
                                        String hospital_name=new String(hospital.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                        int hospital_id=hospital.getInt("id");
                                        patient_doctor_data_fragment.this.hospital_id=hospital_id;
                                        patient_doctor_data_fragment.this.hospital_name=hospital_name;


                                        JSONObject shift=doctor.getJSONObject("shift");
                                        JSONArray times=shift.getJSONArray("times_list");
                                        for (int v=0;v<times.length();v++){
                                            JSONObject appointment=times.getJSONObject(v);
                                            JSONObject day=appointment.getJSONObject("day");
                                            JSONObject from=appointment.getJSONObject("from");
                                            JSONObject to=appointment.getJSONObject("to");
                                            String day_name=day.getString("name");
                                            String from_name=from.getString("name");
                                            String to_name=to.getString("name");
                                            int day_id=day.getInt("id");
                                            int from_id=from.getInt("id");
                                            int to_id=to.getInt("id");

                                            contact_list.add(new available_appointments_list_model(day_name,from_name,to_name,day_id,from_id,to_id));
                                        }

                                    }



                                    }

                                    data_adapter.notifyDataSetChanged();


                                }


                            }


                    } catch(JSONException e){
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+ getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
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

    private void progressConfigurations(){
        progressWindow = ProgressWindow.getInstance(getActivity());
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

}
