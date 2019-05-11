package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.search_city_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.cities_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.governorates_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_city_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_gov_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class search_gov_BottomSheetFragment extends BottomSheetDialogFragment implements pass_gov_data {
    private View view;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private patient_gov_model gov_model;
    RecyclerView dialog_list;
    private ArrayList<patient_gov_model> filtered_govs_List = new ArrayList<>();
    private ArrayList<patient_gov_model>govs=new ArrayList<>();
    private gov_SpinAdapter adapter;
    private pass_gov_data mListener_gov;
    private int gov_id;
    private BottomSheetBehavior mBehavior;

    public search_gov_BottomSheetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            gov_id=getArguments().getInt("city_id");
        }
    }
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
        adapter=new gov_SpinAdapter(getActivity(),govs);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dialog_list.setLayoutManager(mLayoutManager);
        dialog_list.setItemAnimator(new DefaultItemAnimator());
        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        dialog_list.setAdapter(adapter);
        dialog_list.setNestedScrollingEnabled(false);
        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                if (searchh.getText().length()>0){
                    gov_model=filtered_govs_List.get(position);
                    mListener_gov.pass_data(gov_model,search_gov_BottomSheetFragment.this);
                    dismiss();
                }else if (gov_id!=0){
                    gov_model=filtered_govs_List.get(position);
                    mListener_gov.pass_data(gov_model,search_gov_BottomSheetFragment.this);
                    dismiss();
                }else {
                    gov_model=govs.get(position);
                    mListener_gov.pass_data(gov_model,search_gov_BottomSheetFragment.this);
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

                filter_govs(s.toString());


            }
        });

        mprogressBar.setVisibility(View.VISIBLE);
        get_goves_data();


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void get_goves_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/goves/all";
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
                                govs.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_gov_model city=  new patient_gov_model(_id,image_url,name,id);
                                    govs.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                adapter.notifyDataSetChanged();
                                if (gov_id!=0){
                                    filter_gov_by_city(gov_id);
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

    private void filter_govs(String text) {
        filtered_govs_List.clear();
        for (patient_gov_model item : govs) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_govs_List.add(item);
                }
            }

        }

        adapter.filterList(filtered_govs_List);
    }
    private void filter_gov_by_city(int id) {
        filtered_govs_List.clear();
        for (patient_gov_model item : govs) {
            if (!item.name.isEmpty()){
                if (item.id==id) {
                    filtered_govs_List.add(item);
                }
            }else {
                if (item.id==id) {
                    filtered_govs_List.add(item);
                }
            }

        }

        adapter.filterList(filtered_govs_List);
    }

    @Override
    public void pass_data(patient_gov_model speciality_model, pass_gov_data listner) {
        mListener_gov=listner;
    }
}