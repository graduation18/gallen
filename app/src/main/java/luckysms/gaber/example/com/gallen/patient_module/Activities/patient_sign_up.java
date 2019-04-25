package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.pix.Pix;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.cities_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.governorates_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.speciality_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;

public class patient_sign_up extends AppCompatActivity {
    private RequestQueue queue;
    private EditText full_name,mobile_number,email_address,password,confirm_password,date_of_birth;
    private Button insurance_company;
    private Button register,see_password,see_confirm_password,select_image,country;
    private RadioGroup group;
    private RadioButton selected_gender;
    private ImageView photo;
    private boolean seen_pass,seen_confirm_pass;
    private TextView back;
    private ArrayList<patient_city_model> cities=new ArrayList<>();
    private ArrayList<patient_gov_model>govs=new ArrayList<>();
    private Button city,governorates;
    int city_id,gov_id;
    int PICK_IMAGE_MULTIPLE = 1;
    private RecyclerView dialog_list;
    private insurance_SpinAdapter insurance_adapter;
    private List<patient_insurance_model> insurance_model_list = new ArrayList<>();
    private List<patient_insurance_model> filtered_insurance_model_list = new ArrayList<>();
    private int insurace_i;
    private CountryCodePicker  cpp;
    private ArrayList<patient_gov_model> filtered_govs_List = new ArrayList<>();
    private ArrayList<patient_city_model> filtered_cities_List = new ArrayList<>();
    private gov_SpinAdapter adapter;
    private city_SpinAdapter adapter2;
    private ProgressBar mprogressBar;
    private String selected_image_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        full_name=(EditText)findViewById(R.id.full_name);
        mobile_number=(EditText)findViewById(R.id.mobile_number);
        email_address=(EditText)findViewById(R.id.email_address);
        password=(EditText)findViewById(R.id.password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        date_of_birth=(EditText)findViewById(R.id.date_of_birth);
        cpp=(CountryCodePicker)findViewById(R.id.ccp);
        insurance_company=(Button)findViewById(R.id.insurance_company);
        see_password=(Button)findViewById(R.id.see_password);
        see_confirm_password=(Button)findViewById(R.id.see_confirm_password);
        select_image=(Button)findViewById(R.id.select_image);
        register=(Button)findViewById(R.id.register);
        country=(Button)findViewById(R.id.country);
        back=(TextView)findViewById(R.id.back);
        group=(RadioGroup)findViewById(R.id.radioGroup);
        selected_gender=(RadioButton)findViewById(R.id.male);
        city=(Button)findViewById(R.id.city);
        governorates=(Button)findViewById(R.id.governorates);
        photo=(ImageView)findViewById(R.id.photo);


        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_date();
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male) {
                    selected_gender=(RadioButton)findViewById(R.id.male);

                } else if(checkedId == R.id.female) {

                    selected_gender=(RadioButton)findViewById(R.id.female);

                }
            }
        });

        see_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seen_pass){
                    seen_pass=false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }else {
                    seen_pass=true;
                    password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    password.setSelection(password.getText().length());
                }

            }
        });
        see_confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seen_confirm_pass){
                    seen_confirm_pass=false;
                    confirm_password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirm_password.setSelection(password.getText().length());
                }else {
                    seen_confirm_pass=true;
                    confirm_password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    confirm_password.setSelection(password.getText().length());
                }

            }
        });


        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        country.setText(name);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        insurance_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(patient_sign_up.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText search=(EditText)dialog.findViewById(R.id.search_edt);
                insurance_adapter=new insurance_SpinAdapter(patient_sign_up.this,insurance_model_list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(patient_sign_up.this);
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(patient_sign_up.this, LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(insurance_adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(patient_sign_up.this, dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (search.getText().length()>0) {
                            insurace_i = filtered_insurance_model_list.get(position).id;
                            insurance_company.setText(filtered_insurance_model_list.get(position).name);

                            dialog.dismiss();
                        }else {
                            insurace_i = insurance_model_list.get(position).id;
                            insurance_company.setText(insurance_model_list.get(position).name);
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
        governorates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(patient_sign_up.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                adapter=new gov_SpinAdapter(patient_sign_up.this,govs);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(patient_sign_up.this);
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(patient_sign_up.this, LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(adapter);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(patient_sign_up.this, dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            gov_id=filtered_govs_List.get(position).id;
                            governorates.setText(filtered_govs_List.get(position).name);
                            dialog.dismiss();

                        }else {
                            gov_id=govs.get(position).id;
                            governorates.setText(govs.get(position).name);
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

                        filter_govs(s.toString());


                    }
                });

                mprogressBar.setVisibility(View.VISIBLE);
                get_goves_data();
                dialog.show();
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(patient_sign_up.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_list);
                dialog_list= dialog.findViewById(R.id.dialog_list);
                final EditText searchh=(EditText)dialog.findViewById(R.id.search_edt);
                adapter2=new city_SpinAdapter(patient_sign_up.this,cities);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(patient_sign_up.this);
                dialog_list.setLayoutManager(mLayoutManager);
                dialog_list.setItemAnimator(new DefaultItemAnimator());
                dialog_list.addItemDecoration(new MyDividerItemDecoration(patient_sign_up.this, LinearLayoutManager.VERTICAL, 5));
                dialog_list.setAdapter(adapter2);
                dialog_list.addOnItemTouchListener(new RecyclerTouchListener(patient_sign_up.this, dialog_list, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View v, final int position) {
                        if (searchh.getText().length()>0){
                            city_id=filtered_cities_List.get(position).id;
                            city.setText(filtered_cities_List.get(position).name);
                            dialog.dismiss();
                        }else {
                            city_id=cities.get(position).id;
                            city.setText(cities.get(position).name);
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

                        filter_city(s.toString());


                    }
                });

                mprogressBar.setVisibility(View.VISIBLE);
                get_cities_data();
                dialog.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name_s=full_name.getText().toString();
                String mobile_number_s="+"+cpp.getSelectedCountryCode()+mobile_number.getText().toString();
                String email_address_s=email_address.getText().toString();
                String password_s=password.getText().toString();
                String confirm_password_s=confirm_password.getText().toString();
                String date_of_birth_s=date_of_birth.getText().toString();
                String insurance_company_s=insurance_company.getText().toString();
                int insurance_company_i=insurace_i;


                String gender=selected_gender.getText().toString();
                int gender_id;
                if (gender.contains("female")){
                    gender_id=2;

                }else {
                    gender_id=1;

                }
                String gov_s=governorates.getText().toString();
                String city_s=city.getText().toString();
                String country_s=country.getText().toString();
               if (full_name_s.length()>0&&mobile_number_s.length()==13&&email_address_s.length()>0&&password_s.length()>0&&
                       confirm_password_s.length()>0&&date_of_birth_s.length()>0&&insurance_company_s.length()>0&&country_s.length()>0){
                   if (confirm_password_s.equals(password_s)) {
                       mprogressBar.setVisibility(View.VISIBLE);
                       sign_up(full_name_s, mobile_number_s, email_address_s, password_s, date_of_birth_s
                               , insurance_company_s, gender,gender_id,insurance_company_i,gov_id,city_id,city_s,gov_s,country_s);
                   }else {
                       Toast.makeText(patient_sign_up.this,getResources().getText(R.string.password_not_matched),Toast.LENGTH_LONG).show();
                   }
               }else {
                   Toast.makeText(patient_sign_up.this,getResources().getString(R.string.login_error),Toast.LENGTH_LONG).show();

               }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patient_start_screen=new Intent(patient_sign_up.this,patient_start_screen.class);
                startActivity(patient_start_screen);
                finish();
            }
        });

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_images();
                select_image.setVisibility(View.INVISIBLE);
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_images();
            }
        });

    }
    private void sign_up(final String full_name_s, final String mobile_number_s, final String email_address_s
            , final String password_s, final String date_of_birth_s, final String insurance_company_s, final String gender, final int gender_id
            , final int insurance_company_i, final int gov_id, final int city_id, final String city_name, final String gov_name
    ,final String country)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/register/add";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
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
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                login(full_name_s,mobile_number_s,email_address_s,password_s,date_of_birth_s,insurance_company_s
                                        ,gender,gender_id,insurance_company_i,gov_id,city_id,city_name,gov_name,country);
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {

                        object.put("patient_name", full_name_s);
                        object.put("patient_mobile", mobile_number_s);
                        object.put("patient_user_name", email_address_s);
                        object.put("patient_password", password_s);
                        object.put("birth_date", date_of_birth_s);
                        JSONObject gen=new JSONObject();
                        gen.put("name", gender);
                        gen.put("id", gender_id);
                        object.put("gender",gen);
                        JSONObject insurance=new JSONObject();
                        insurance.put("id",insurance_company_i);
                        insurance.put("name",insurance_company_s);
                        object.put("insurance",insurance);
                        JSONObject gov=new JSONObject();
                        gov.put("id",gov_id);
                        gov.put("name",gov_name);
                        object.put("gov",gov);
                        JSONObject city=new JSONObject();
                        city.put("id",city_id);
                        city.put("name",city_name);
                        object.put("city",city);
                        JSONObject countr=new JSONObject();
                        countr.put("name",country);
                        object.put("country",countr);




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

    private void update(final String full_name_s, final String mobile_number_s, final String email_address_s
            , final String password_s, final String date_of_birth_s, final String insurance_company_s, final String gender, final int gender_id
            , final int insurance_company_i, final int gov_id, final int city_id, final String city_name, final String gov_name
            ,final String country,final int id)
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/patients/update";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
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
                            Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                Intent not_now=new Intent(patient_sign_up.this,patient_main_screen.class);
                                startActivity(not_now);
                                finish();
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.welcome),Toast.LENGTH_LONG).show();

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    mprogressBar.setVisibility(View.INVISIBLE);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", id);
                        object.put("name", full_name_s);
                        object.put("mobile", mobile_number_s);
                        object.put("user_name", email_address_s);
                        object.put("password", password_s);
                        object.put("birth_date", date_of_birth_s);
                        JSONObject gen=new JSONObject();
                        gen.put("name", gender);
                        gen.put("id", gender_id);
                        object.put("gender",gen);
                        JSONObject insurance=new JSONObject();
                        insurance.put("id",insurance_company_i);
                        insurance.put("name",insurance_company_s);
                        object.put("insurance",insurance);
                        JSONObject gov=new JSONObject();
                        gov.put("id",gov_id);
                        gov.put("name",gov_name);
                        object.put("gov",gov);
                        JSONObject city=new JSONObject();
                        city.put("id",city_id);
                        city.put("name",city_name);
                        object.put("city",city);
                        JSONObject countr=new JSONObject();
                        countr.put("name",country);
                        object.put("country",countr);




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

    private void login(final String full_name_s, final String mobile_number_s, final String email_address_s
            , final String password_s, final String date_of_birth_s, final String insurance_company_s, final String gender, final int gender_id
            , final int insurance_company_i, final int gov_id, final int city_id, final String city_name, final String gov_name
            ,final String country)
    {


        try {
            String url = "http://microtec1.egytag.com/api/user/login";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
            }
            // Request a string response from the provided URL.
            final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //do other things with the received JSONObject
                    Log.w("dsakjbsdahk", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.has("error")) {
                            if (res.getString("error").equals("email not set")){
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.email_not_set),Toast.LENGTH_LONG).show();

                            }else if (res.getString("error").equals("email or password error")){
                                Toast.makeText(patient_sign_up.this,getResources().getString(R.string.email_or_password_error),Toast.LENGTH_LONG).show();

                            }

                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {
                                JSONObject user = res.getJSONObject("user");
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putInt("id",user.getInt("id"))
                                        .putBoolean("state",true)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .putString("accessToken",res.getString("accessToken"))
                                        .commit();
                                update(full_name_s,mobile_number_s,email_address_s,password_s,date_of_birth_s,insurance_company_s
                                        ,gender,gender_id,insurance_company_i,gov_id,city_id,city_name,gov_name,country,user.getInt("id"));

                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
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
                    pars.put("email", email_address_s);
                    pars.put("password", password_s);
                    return pars;
                }
            };
            queue.add(stringReq);

        } catch (Exception e) {

        }


    }

    private void get_cities_data()
    {


        try {
            String url = "http://microtec1.egytag.com/api/cities/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(patient_sign_up.this);
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
                            Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
                                    patient_city_model  city=  new patient_city_model(_id,image_url,name,gov_id,gov_name,id,govid);
                                    cities.add(city);
                                    Log.w("dsakjbsdahk",name);


                                }
                                adapter2.notifyDataSetChanged();

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
    private void get_goves_data()
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/goves/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(patient_sign_up.this);
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
                            Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
    private void get_insurance_data()
    {


        try {
            String url = "http://microtec1.egytag.com:30001/api/medical_insurance_companies/all";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
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
                            Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

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
                    pars.put("Cookie", "access_token="+getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));
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
    private void show_date(){
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date_of_birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void select_images()
    {
        Pix.start(this,
                PICK_IMAGE_MULTIPLE,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (String uri:returnValue){
                try {

                    mprogressBar.setVisibility(View.VISIBLE);
                    SendImage(uri);

                } catch (Exception e) {
                    Log.w("errrrrrrror",e.getMessage());
                }
            }
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
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    private void SendImage(final String image)
    {



        String url = "http://microtec1.egytag.com/api/upload/image/default";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();

        if (image != null) {
            File file = new File(image);
            Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
            Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
            mpEntity.addPart("fileToUpload", new FileBody(file, "application/octet"));
        }
        httppost.setEntity(mpEntity.build());
        try {
            HttpResponse response = httpclient.execute(httppost);
            Log.w("saddsjak",response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
