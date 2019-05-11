package luckysms.gaber.example.com.gallen.hospital_module.Fragments;

import android.app.Dialog;
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
import luckysms.gaber.example.com.gallen.hospital_module.Custom.pass_clinic_data;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_speciality_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

import static android.content.Context.MODE_PRIVATE;

public class search_clinics_BottomSheetFragment extends BottomSheetDialogFragment  implements pass_clinic_data  {
    private View view;

    private RequestQueue queue;
    private clinic_SpinAdapter adapter;
    private ArrayList<clinic_model> clinics_list=new ArrayList<>();
    private ArrayList<clinic_model> clinics_filteredList = new ArrayList<>();
    private ProgressBar mprogressBar;
    private clinic_model clinic_model;
    RecyclerView dialog_list;
    private pass_clinic_data mListener;
    private BottomSheetBehavior mBehavior;

    public search_clinics_BottomSheetFragment() {
        // Required empty public constructor
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
        adapter=new clinic_SpinAdapter(getActivity(),clinics_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        dialog_list.setLayoutManager(mLayoutManager);
        dialog_list.setItemAnimator(new DefaultItemAnimator());
        dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        dialog_list.setAdapter(adapter);
        dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View v, final int position) {
                if (searchh.getText().length()>0){
                    clinic_model=clinics_filteredList.get(position);
                    mListener.pass_data(clinic_model,search_clinics_BottomSheetFragment.this);
                    dismiss();
                }else {
                    clinic_model=clinics_list.get(position);
                    mListener.pass_data(clinic_model,search_clinics_BottomSheetFragment.this);
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

                filter(s.toString());


            }
        });



        mprogressBar.setVisibility(View.VISIBLE);

        get_clinics_data();


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



    private void filter(String text)
    {
        clinics_filteredList.clear();
        for (clinic_model item : clinics_list) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    clinics_filteredList.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    clinics_filteredList.add(item);
                }
            }

        }

        adapter.filterList(clinics_filteredList);
    }

    private void get_clinics_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/clinics/all";
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
                                clinics_list.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    boolean active=object.getBoolean("active") ;
                                    JSONObject hospital=object.getJSONObject("hospital");
                                    JSONObject gov=object.getJSONObject("gov");
                                    JSONObject city=object.getJSONObject("city");
                                    String  address = "";
                                            if(object.has("address")){address=object.getString("address");}
                                    String phone=object.getString("phone");
                                    String website=object.getString("website");
                                    String email=object.getString("email");
                                    JSONArray insurance_company_list = new JSONArray();
                                    if (object.has("insurance_company_list")) {
                                        insurance_company_list = object.getJSONArray("insurance_company_list");
                                    }
                                    JSONArray doctor_list=object.getJSONArray("doctor_list");
                                    JSONArray nurse_list=object.getJSONArray("nurse_list");
                                    double latitude=0;
                                    if (object.has("latitude")) {
                                        latitude= Double.parseDouble(object.getString("latitude"));
                                    }
                                    double longitude=0;
                                    if (object.has("longitude")){
                                        longitude= Double.parseDouble(object.getString("longitude"));
                                    }
                                    Log.w("sdaddasadsdsaas",name);

                                    clinic_model speciality=  new clinic_model( name, address, phone, website, email, image_url
                                            , id, active, hospital.toString(),  gov.toString(), city.toString()
                                            , insurance_company_list.toString(),
                                            doctor_list.toString(),  nurse_list.toString(), latitude, longitude);
                                    clinics_list.add(speciality);


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
                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        JSONObject where=new JSONObject();
                        where.put("hospital.id",getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getInt("id",0));
                        object.put("where",where);



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

    @Override
    public void pass_data(clinic_model city_model, pass_clinic_data listner) {
        this.mListener=listner;
    }
}