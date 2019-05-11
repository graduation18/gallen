package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_filter_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class filters_BottomSheetFragment extends BottomSheetDialogFragment implements pass_filter_data {
    private View view;
    private Button insurance_company,degree;
    private RadioButton selected_gender;
    private RecyclerView dialog_list;
    private insurance_SpinAdapter adapter;
    private List<patient_insurance_model> insurance_model_list = new ArrayList<>();
    private List<patient_insurance_model> filtered_insurance_model_list = new ArrayList<>();
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private patient_insurance_model insurance_model;
    private List<search_result_list_model> contact_list = new ArrayList<>();
    private List<search_result_list_model> filtered_contact_list = new ArrayList<>();
    private pass_filter_data mListener;



    public filters_BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        insurance_company=(Button)view.findViewById(R.id.insurance_company);
        degree=(Button)view.findViewById(R.id.degree);
        final Button filter_it=(Button)view.findViewById(R.id.filters);
        RadioGroup group=(RadioGroup)view.findViewById(R.id.radioGroup);
        final EditText fee_from=(EditText)view.findViewById(R.id.fee_from);
        final EditText fee_to=(EditText)view.findViewById(R.id.fee_to);
        mprogressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male) {
                    selected_gender=(RadioButton)view.findViewById(R.id.male);

                } else if(checkedId == R.id.female) {

                    selected_gender=(RadioButton)view.findViewById(R.id.female);

                }
            }
        });
        insurance_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText search=(EditText)dialog.findViewById(R.id.search_edt);
                adapter=new insurance_SpinAdapter(getActivity(),insurance_model_list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (search.getText().length()>0) {
                            insurance_model = filtered_insurance_model_list.get(position);
                            insurance_company.setText(insurance_model.name);
                            dialog.dismiss();
                        }else {
                            insurance_model = insurance_model_list.get(position);
                            insurance_company.setText(insurance_model.name);
                            dialog.dismiss();
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
                dialog.show();
                mprogressBar.setVisibility(View.VISIBLE);
                get_insurance_data();
            }
        });

        filter_it.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             filtered_contact_list.clear();
                                             if (fee_from.getText().toString().length() > 0 && fee_to.getText().toString().length() > 0) {

                                                 filtered_contact_list.clear();
                                                 for (search_result_list_model model : contact_list) {
                                                     if (model.doctor_model.doctor_fee <= Integer.parseInt(fee_to.getText().toString()) && model.doctor_model.doctor_fee >= Integer.parseInt(fee_from.getText().toString())) {
                                                         filtered_contact_list.add(model);
                                                     }
                                                 }
                                                 mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);


                                             } else if (fee_from.getText().toString().length() > 0 && fee_to.getText().toString().length() == 0) {


                                                 filtered_contact_list.clear();
                                                 for (search_result_list_model model : contact_list) {
                                                     if (model.doctor_model.doctor_fee >= Integer.parseInt(fee_from.getText().toString())) {
                                                         filtered_contact_list.add(model);

                                                     }
                                                 }

                                                 Log.w("dssdasda", fee_from.getText().toString() + filtered_contact_list.size());
                                                 mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                                             } else if (fee_to.getText().toString().length() > 0 && fee_from.getText().toString().length() == 0) {

                                                 filtered_contact_list.clear();
                                                 for (search_result_list_model model : contact_list) {
                                                     if (model.doctor_model.doctor_fee <= Integer.parseInt(fee_to.getText().toString())) {
                                                         filtered_contact_list.add(model);
                                                     }
                                                 }
                                                 mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);

                                             } else {
                                                 mListener.notify_adapter(filters_BottomSheetFragment.this,contact_list);
                                             }
                                             if (insurance_model!=null) {
                                                 if (filtered_contact_list.size() > 0) {
                                                     for (search_result_list_model model : filtered_contact_list) {
                                                         if (model.patient_insurance_model.id != insurance_model.id) {
                                                             filtered_contact_list.remove(model);
                                                         }
                                                     }
                                                     mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                                                 } else {
                                                     for (search_result_list_model model : contact_list) {

                                                         if (model.patient_insurance_model.id == insurance_model.id) {
                                                             filtered_contact_list.add(model);
                                                         }

                                                     }
                                                     mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                                                 }
                                             }
                                             if (selected_gender != null) {
                                                 Log.w("sdljhdsakjd", selected_gender.getText().toString());
                                                 if (filtered_contact_list.size() > 0) {
                                                     for (search_result_list_model model : filtered_contact_list) {
                                                         if (!model.doctor_model.doctor_gender.equals(selected_gender.getText().toString())) {
                                                             Log.w("sdljhdsakjd", model.doctor_model.doctor_gender + "  " + selected_gender.getText().toString());
                                                             filtered_contact_list.remove(model);
                                                         }
                                                     }
                                                     mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                                                 } else {
                                                     for (search_result_list_model model : contact_list) {

                                                         if (model.doctor_model.doctor_gender.equals(selected_gender.getText().toString())) {
                                                             filtered_contact_list.add(model);
                                                         }

                                                     }
                                                    mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                                                 }
                                             }


                                         }
                                     });





                // Inflate the layout for this fragment
        return view;
    }
    private void filter(String text)
    {
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

        adapter.filterList(filtered_insurance_model_list);
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

    @Override
    public void pass_data(List<search_result_list_model> contact_list,pass_filter_data listner) {
        this.contact_list=contact_list;
        this.mListener=listner;
    }

    @Override
    public void notify_adapter(pass_filter_data listner,List<search_result_list_model> contact_list ) {

    }
}