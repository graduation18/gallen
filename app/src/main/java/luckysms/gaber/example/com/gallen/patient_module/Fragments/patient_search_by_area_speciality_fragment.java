package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import static android.content.Context.MODE_PRIVATE;

public class patient_search_by_area_speciality_fragment extends Fragment  {
    private View view;
    private Button search;
    private TextView back,number_of_notifications,notifications;
    private Button speciality;
    private String state,speciality_s;
    private RequestQueue queue;
    private ArrayList<patient_speciality_model>specialities=new ArrayList<>();
    private ArrayList<patient_speciality_model> filteredList = new ArrayList<>();
    private speciality_SpinAdapter adapter;
    private int speciality_id;
    private boolean visitor;
    RecyclerView dialog_list;
    private ProgressBar mprogressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            visitor=getArguments().getBoolean("visitor");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_search_by_area_and_speciality_fragment, container, false);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        speciality=(Button) view.findViewById(R.id.speciality);
        search=(Button)view.findViewById(R.id.search);
        back=(TextView)view.findViewById(R.id.back);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search();
                Bundle args=new Bundle();
                if (visitor){
                    args.putBoolean("visitor",true);
                }
                fragment.setArguments(args);
                go_to(fragment);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        speciality.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              final Dialog dialog=new Dialog(getActivity());
                                              dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                              dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                              dialog.setContentView(R.layout.dialog_list);
                                              dialog_list= dialog.findViewById(R.id.dialog_list);
                                              final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                                              adapter=new speciality_SpinAdapter(getActivity(),specialities);
                                              RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                              dialog_list.setLayoutManager(mLayoutManager);
                                              dialog_list.setItemAnimator(new DefaultItemAnimator());
                                              dialog_list.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
                                              dialog_list.setAdapter(adapter);
                                              dialog_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), dialog_list, new RecyclerTouchListener.ClickListener() {
                                                  @Override
                                                  public void onClick(View v, final int position) {
                                                      if (searchh.getText().length()>0){
                                                          speciality_id=filteredList.get(position).id;
                                                          speciality_s=filteredList.get(position).name;
                                                          speciality.setText(speciality_s);
                                                          dialog.dismiss();
                                                      }else {
                                                          speciality_id=specialities.get(position).id;
                                                          speciality_s=specialities.get(position).name;
                                                          speciality.setText(speciality_s);
                                                          dialog.dismiss();
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

                                              get_specialties_data();
                                              dialog.show();

                                          }
                                      });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_search();
                    Bundle args=new Bundle();
                    if (visitor){
                        args.putBoolean("visitor",true);
                    }
                    fragment.setArguments(args);
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_search_by_area_fragment();
                Bundle args = new Bundle();
                args.putInt("speciality",speciality_id);
                args.putString("speciality_s",speciality_s);
                              if (visitor){
                    args.putBoolean("visitor",true);
                }
                fragment.setArguments(args);
                go_to(fragment);
            }

        });



        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_frameLayout, fragment)
                .commit();
    }



    private void get_specialties_data()
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/medical_specialties/all";
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
                                specialities.clear();
                                JSONArray list=res.getJSONArray("list");

                                for (int i=0;i<list.length();i++){
                                    JSONObject object=list.getJSONObject(i);
                                    String _id=object.getString("_id");
                                    String image_url=object.getString("image_url");
                                    String name=new String(object.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                    int id=object.getInt("id");
                                    patient_speciality_model speciality=  new patient_speciality_model(_id,image_url,name,id);
                                    specialities.add(speciality);


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
        for (patient_speciality_model item : specialities) {
            if (!item.name.isEmpty()){
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }else {
                if (item.name.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

        }

        adapter.filterList(filteredList);
    }

}
