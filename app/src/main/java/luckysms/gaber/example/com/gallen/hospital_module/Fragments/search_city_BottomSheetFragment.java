package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import luckysms.gaber.example.com.gallen.hospital_module.Adapters.clinic_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_city_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;

public class search_city_BottomSheetFragment extends BottomSheetDialogFragment implements pass_city_data {
    private View view;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private patient_city_model city_model;
    RecyclerView dialog_list;
    private ArrayList<patient_city_model> filtered_cities_List = new ArrayList<>();
    private ArrayList<patient_city_model>cities=new ArrayList<>();
    private city_SpinAdapter adapter2;
    private int gov_id;
    private pass_city_data mListener_city;
    private BottomSheetBehavior mBehavior;


    public search_city_BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            gov_id=getArguments().getInt("gov_id");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.dialog_list, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        dialog_list= view.findViewById(R.id.dialog_list);
        final EditText searchh=(EditText)view.findViewById(R.id.search_edt);
        adapter2=new city_SpinAdapter(getActivity(),cities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dialog_list.setLayoutManager(mLayoutManager);
        dialog_list.setItemAnimator(new DefaultItemAnimator());
        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        dialog_list.setAdapter(adapter2);
        dialog_list.setNestedScrollingEnabled(false);
        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                if (searchh.getText().length()>0){
                    city_model=filtered_cities_List.get(position);
                    mListener_city.pass_data(city_model, search_city_BottomSheetFragment.this);
                    dismiss();
                }else if (gov_id!=0){
                    city_model=filtered_cities_List.get(position);
                    mListener_city.pass_data(city_model, search_city_BottomSheetFragment.this);
                    dismiss();
                }else {
                    city_model=cities.get(position);
                    mListener_city.pass_data(city_model, search_city_BottomSheetFragment.this);
                    dismiss();
                }


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        searchh.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                filter_city(s.toString());


            }
        });

        mprogressBar.setVisibility(View.VISIBLE);
        get_cities_data();


        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }



    private void get_cities_data()
    {


        try {
            String url = "http://intmicrotec.neat-url.com:6566/api/cities/all";
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
                                cities.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    String gov_id=object.getJSONObject("gov").getString("_id");
                                    String gov_name=new String( object.getJSONObject("gov").getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    int govid=object.getJSONObject("gov").getInt("id");
                                    patient_city_model city=  new patient_city_model(_id,image_url,name,gov_id,gov_name,id,govid);
                                    cities.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                adapter2.notifyDataSetChanged();
                                if (gov_id!=0) {
                                    filter_city_by_gov(gov_id);
                                }

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
                    pars.put("Content-Type", "application/x-www-form-urlencoded");
                    return pars;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();

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
    private void filter_city(String text) {
        filtered_cities_List.clear();
        for (patient_city_model item : cities) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_cities_List.add(item);
                }
            }

        }

        adapter2.filterList(filtered_cities_List);
    }
    private void filter_city_by_gov(int id) {
        filtered_cities_List.clear();
        for (patient_city_model item : cities) {
            if (!item.name.isEmpty()){
                if (item.govid==id) {
                    filtered_cities_List.add(item);
                }
            }else {
                if (item.govid==id) {
                    filtered_cities_List.add(item);
                }
            }

        }

        adapter2.filterList(filtered_cities_List);
    }

    @Override
    public void pass_data(patient_city_model speciality_model, pass_city_data listner) {
        mListener_city=listner;
    }
}