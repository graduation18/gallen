package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Activities.hospital_search_by_doctor_name;
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.hospital_search_result_list_adapter;
import luckysms.gaber.example.com.gallen.hospital_module.Model.doctor_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.hospital_module.Model.speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;

import static android.content.Context.MODE_PRIVATE;

public class hospital_search_edit_doctor extends Fragment {
    private View view;
    private EditText doctor_code,doctor_name;
    private Button confirm;
    private TextView back,number_of_notifications,notifications;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private ArrayList<search_doctor_name_model> doctor_name_list=new ArrayList<>();
    private ArrayList<search_doctor_name_model>filteredList=new ArrayList<>();
    private RecyclerView search_result_recycler;
    private hospital_search_result_list_adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.hospital_search_by_doctor_name_fragment, container, false);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        confirm=(Button)view.findViewById(R.id.confirm);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        doctor_name=(EditText)view.findViewById(R.id.doctor_name);
        doctor_code=(EditText)view.findViewById(R.id.doctor_code);
        search_result_recycler = view.findViewById(R.id.search_result_recycler);




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
        doctor_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                filter(s.toString());


            }
        });
        adapter=new hospital_search_result_list_adapter(getActivity(),doctor_name_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        search_result_recycler.setLayoutManager(mLayoutManager);
        search_result_recycler.setItemAnimator(new DefaultItemAnimator());
        search_result_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        search_result_recycler.setAdapter(adapter);
        search_result_recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), search_result_recycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Bundle bundle=new Bundle();
                bundle.putString("doctor_name",filteredList.get(position).doctor_model.doctor_name);
                bundle.putString("doctor_availabilty",filteredList.get(position).doctor_model.doctor_availabilty);
                bundle.putString("doctor_gender",filteredList.get(position).doctor_model.doctor_gender);
                bundle.putString("doctor_graduated",filteredList.get(position).doctor_model.doctor_graduated);
                bundle.putString("doctor_image",filteredList.get(position).doctor_model.doctor_image);
                bundle.putString("doctor_notes",filteredList.get(position).doctor_model.doctor_notes);
                bundle.putString("dotor_code",filteredList.get(position).doctor_model.dotor_code);
                bundle.putString("doctor_phone",filteredList.get(position).doctor_model.doctor_phone);
                bundle.putString("review_list",filteredList.get(position).doctor_model.review_list);
                bundle.putString("doctor_email",filteredList.get(position).doctor_model.doctor_email);
                bundle.putString("speciality_name",filteredList.get(position).speciality_model.name);
                bundle.putBoolean("doctor_accept_discount",filteredList.get(position).doctor_model.doctor_accept_discount);
                bundle.putDouble("doctor_fee",filteredList.get(position).doctor_model.doctor_fee);
                bundle.putFloat("doctor_rating",filteredList.get(position).doctor_model.doctor_rating);
                bundle.putInt("id",filteredList.get(position).doctor_model.id);
                Fragment hospital_doctor_edit_delete=new hospital_doctor_edit_delete();
                hospital_doctor_edit_delete.setArguments(bundle);
                go_to(hospital_doctor_edit_delete);



            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        mprogressBar.setVisibility(View.VISIBLE);
        get_doctor_data();

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void get_doctor_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/hospitals/view";
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
                                doctor_name_list.clear();
                                JSONObject doc=res.getJSONObject("doc");
                                JSONArray doctor_list=doc.getJSONArray("doctor_list");

                                for (int d=0;d<doctor_list.length();d++){
                                    JSONObject doctor_obj=doctor_list.getJSONObject(d);
                                    JSONObject doctor=doctor_obj.getJSONObject("doctor");
                                    String doctor__id=doctor.getString("_id");
                                    int doctor_id=doctor.getInt("id");
                                    String  doctor_email=doctor.getString("email");
                                    String  doctor_code=doctor.getString("code");
                                    String  doctor_phone=doctor.getString("phone");
                                    String doctor_name=new String (doctor.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String doctor_image= "http://microtec1.egytag.com"+doctor.getString("image_url");
                                    JSONObject specialty=doctor.getJSONObject("specialty");
                                    String specialty__id=specialty.getString("_id");
                                    int specialty_id=specialty.getInt("id");
                                    JSONArray review_list=new JSONArray();
                                    if (doctor_obj.has("review_list")){
                                        review_list=doctor_obj.getJSONArray("review_list");
                                    }
                                    String specialty_name=new String (specialty.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    doctor_model doctor_model=new doctor_model(doctor_name,"","","",false,4.5f,200
                                            ,doctor_id,"","Male",review_list.toString(),doctor_code,doctor_email,doctor_phone);
                                    speciality_model speciality_model=new speciality_model(specialty__id,"ss",specialty_name,specialty_id);
                                    doctor_name_list.add(new search_doctor_name_model(doctor_model,speciality_model));
                                }

                                adapter.notifyDataSetChanged();


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
                    mprogressBar.setVisibility(View.INVISIBLE);

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
    private void filter(String text) {
        filteredList.clear();
        for (search_doctor_name_model item : doctor_name_list) {
            if (!item.doctor_model.doctor_name.isEmpty()){
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())||item.doctor_model.dotor_code.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }else {
                if (item.doctor_model.doctor_name.toLowerCase().contains(text.toLowerCase())||item.doctor_model.dotor_code.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

        }

        adapter.filterList(filteredList);
    }
    
}
