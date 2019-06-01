package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import com.mohammedalaa.seekbar.RangeSeekBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_reviews_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_filter_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_insurance_data;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

import static android.content.Context.MODE_PRIVATE;

public class filters_BottomSheetFragment extends BottomSheetDialogFragment implements pass_filter_data,pass_insurance_data {
    private View view;
    private Button insurance_company;
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
    private BottomSheetBehavior mBehavior;
    private pass_insurance_data mListener_insurance;




    public filters_BottomSheetFragment() {
        // Required empty public constructor
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        view = View.inflate(getContext(), R.layout.bottom_sheet, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        insurance_company=(Button)view.findViewById(R.id.insurance_company);
        final Button filter_it=(Button)view.findViewById(R.id.filters);
        RadioGroup group=(RadioGroup)view.findViewById(R.id.radioGroup);
        final RangeSeekBarView rangeSeekbar = (RangeSeekBarView)view.findViewById(R.id.range_seekbar);
        rangeSeekbar.setAnimated(true,3000L);

        SharedPreferences getdata=getActivity().getSharedPreferences("filters",MODE_PRIVATE);
        rangeSeekbar.setValue(getdata.getInt("fee",rangeSeekbar.getValue()));

        if (getdata.getInt("gender", 0)!=0){
            selected_gender=(RadioButton)view.findViewById(getdata.getInt("gender", 0));
            selected_gender.setChecked(true);
        }

        if (getdata.getInt("insurance_company_id", 0)!=0) {
            int insurance_company_id= getdata.getInt("insurance_company_id", 0);
            String insurance_company_name=getdata.getString("insurance_company_name", "");
            String insurance_company_image_url=getdata.getString("insurance_company_image_url", "");
            String insurance_company__id=getdata.getString("insurance_company__id", "");
            insurance_model=new patient_insurance_model(insurance_company__id,insurance_company_image_url,insurance_company_name,insurance_company_id);
            insurance_company.setText(insurance_model.name);
        }


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
                Bundle s=new Bundle();
                search_insurance_BottomSheetFragment bottomSheetFragment = new search_insurance_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                mListener_insurance=bottomSheetFragment;
                mListener_insurance.pass_data(null,filters_BottomSheetFragment.this);
            }
        });

        filter_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filtered_contact_list.clear();
                if (  rangeSeekbar.getValue()> 0 ) {


                    filtered_contact_list.clear();
                    for (search_result_list_model model : contact_list) {
                        if (model.doctor_model.doctor_fee >= rangeSeekbar.getValue()) {
                            filtered_contact_list.add(model);

                        }
                    }

                    Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"فلتر");
                    mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                } else {
                    Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"مش فلتر");
                    mListener.notify_adapter(filters_BottomSheetFragment.this,contact_list);
                }
                if (insurance_model!=null) {
                    if (filtered_contact_list.size() > 0) {
                        for (search_result_list_model model : filtered_contact_list) {
                            if (model.patient_insurance_model.id != insurance_model.id) {
                                filtered_contact_list.remove(model);
                            }
                        }
                        Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"فلتر");
                        mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                    } else {
                        for (search_result_list_model model : contact_list) {

                            if (model.patient_insurance_model.id == insurance_model.id) {
                                filtered_contact_list.add(model);
                            }

                        }
                        Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"فلتر");
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
                        Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"فلتر");
                        mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                    } else {
                        for (search_result_list_model model : contact_list) {

                            if (model.doctor_model.doctor_gender.equals(selected_gender.getText().toString())) {
                                filtered_contact_list.add(model);
                            }

                        }
                        Log.w("dssdasda", String.valueOf(filtered_contact_list.size())+"فلتر");
                        mListener.notify_adapter(filters_BottomSheetFragment.this,filtered_contact_list);
                    }
                }


                    SharedPreferences.Editor edito=getActivity().getSharedPreferences("filters",MODE_PRIVATE).edit();
                    edito.putInt("fee",rangeSeekbar.getValue());
                    if (selected_gender!=null) {
                        edito.putInt("gender", selected_gender.getId());
                    }
                    if (insurance_model!=null) {
                        edito.putInt("insurance_company_id", insurance_model.id);
                        edito.putString("insurance_company_name", insurance_model.name);
                        edito.putString("insurance_company_image_url", insurance_model.image_url);
                        edito.putString("insurance_company__id", insurance_model._id);
                    }
                    edito.commit();

                dismiss();


            }
        });

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
    public void pass_data(List<search_result_list_model> contact_list,pass_filter_data listner) {
        this.contact_list=contact_list;
        this.mListener=listner;
    }

    @Override
    public void notify_adapter(pass_filter_data listner,List<search_result_list_model> contact_list ) {

    }
    @Override
    public void pass_data(patient_insurance_model insurance_model, pass_insurance_data listner) {
        this.insurance_model=insurance_model;
        insurance_company.setText(insurance_model.name);
    }
}