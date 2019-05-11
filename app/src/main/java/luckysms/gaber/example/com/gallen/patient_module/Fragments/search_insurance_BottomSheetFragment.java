package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.app.Dialog;
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
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Fragments.search_city_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_insurance_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class search_insurance_BottomSheetFragment extends BottomSheetDialogFragment  implements pass_insurance_data  {
    private View view;

    private RequestQueue queue;
    private ProgressBar mprogressBar;
    RecyclerView dialog_list;
    private pass_insurance_data mListener;
    private insurance_SpinAdapter insurance_adapter;
    private List<patient_insurance_model> insurance_model_list = new ArrayList<>();
    private List<patient_insurance_model> filtered_insurance_model_list = new ArrayList<>();
    private patient_insurance_model insurance_model;
    private BottomSheetBehavior mBehavior;


    public search_insurance_BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final EditText search=(EditText)view.findViewById(R.id.search_edt);
        dialog_list= view.findViewById(R.id.dialog_list);
        insurance_adapter=new insurance_SpinAdapter(getActivity(),insurance_model_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dialog_list.setLayoutManager(mLayoutManager);
        dialog_list.setItemAnimator(new DefaultItemAnimator());
        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        dialog_list.setAdapter(insurance_adapter);
        dialog_list.setNestedScrollingEnabled(false);
        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                if (search.getText().length()>0) {
                    insurance_model = filtered_insurance_model_list.get(position);
                    mListener.pass_data(insurance_model,search_insurance_BottomSheetFragment.this);
                    dismiss();
                }else {
                    insurance_model = insurance_model_list.get(position);
                    mListener.pass_data(insurance_model,search_insurance_BottomSheetFragment.this);
                    dismiss();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        search.addTextChangedListener(new TextWatcher() {

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
        mprogressBar.setVisibility(View.VISIBLE);
        get_insurance_data();



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

    private void get_insurance_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/medical_insurance_companies/all";
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
                                JSONArray list=res.getJSONArray("list");
                                insurance_model_list.clear();
                                insurance_model_list.add(new patient_insurance_model("","","none",0));

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_insurance_model city=  new patient_insurance_model(_id,image_url,name,id);
                                    insurance_model_list.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                insurance_adapter.notifyDataSetChanged();



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
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
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
        filtered_insurance_model_list.clear();
        for (patient_insurance_model item : insurance_model_list) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_insurance_model_list.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filtered_insurance_model_list.add(item);
                }
            }

        }

        insurance_adapter.filterList(filtered_insurance_model_list);
    }

    @Override
    public void pass_data(patient_insurance_model insurance_model, pass_insurance_data listner) {
        this.mListener=listner;
    }
}