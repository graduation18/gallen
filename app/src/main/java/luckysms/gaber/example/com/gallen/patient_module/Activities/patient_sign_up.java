package luckysms.gaber.example.com.gallen.patient_module.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.city_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.gov_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.insurance_SpinAdapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.ApiConfig;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_city_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_filter_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_gov_data;
import luckysms.gaber.example.com.gallen.patient_module.Custom.pass_insurance_data;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_search_by_area_fragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_city_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_gov_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.search_insurance_BottomSheetFragment;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class patient_sign_up extends AppCompatActivity implements pass_gov_data,pass_city_data,pass_insurance_data {
    private RequestQueue queue;
    private EditText full_name,mobile_number,email_address,password,confirm_password,date_of_birth;
    private Button insurance_company;
    private Button register,see_password,see_confirm_password,country;
    private RadioGroup group;
    private RadioButton selected_gender;
    private ImageView doctor_image;
    private boolean seen_pass,seen_confirm_pass;
    private TextView back;
    private Button city,governorates;
    int PICK_IMAGE_MULTIPLE = 1;
    private CountryCodePicker  cpp;
    private ProgressBar mprogressBar;
    private String selected_image_url;
    private pass_insurance_data mListener_insurance;
    private pass_city_data mListener_city;
    private pass_gov_data mListener_gov;
    private patient_city_model city_model;
    private patient_gov_model gov_model;
    private patient_insurance_model insurance_model;



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
        register=(Button)findViewById(R.id.register);
        country=(Button)findViewById(R.id.country);
        back=(TextView)findViewById(R.id.back);
        group=(RadioGroup)findViewById(R.id.radioGroup);
        selected_gender=(RadioButton)findViewById(R.id.male);
        city=(Button)findViewById(R.id.city);
        governorates=(Button)findViewById(R.id.governorates);
        doctor_image=(ImageView)findViewById(R.id.doctor_image);


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
                Bundle s=new Bundle();
                search_insurance_BottomSheetFragment bottomSheetFragment = new search_insurance_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                mListener_insurance=bottomSheetFragment;
                mListener_insurance.pass_data(null,patient_sign_up.this);
            }
        });
        governorates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (city_model!=null&&gov_model==null){
                    s.putInt("city_id",city_model.govid);
                }
                search_gov_BottomSheetFragment bottomSheetFragment = new search_gov_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                mListener_gov=bottomSheetFragment;
                mListener_gov.pass_data(null,patient_sign_up.this);

            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle s=new Bundle();
                if (gov_model!=null&&city_model==null){
                    s.putInt("gov_id",gov_model.id);
                }
                search_city_BottomSheetFragment bottomSheetFragment = new search_city_BottomSheetFragment();
                bottomSheetFragment.setArguments(s);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                mListener_city=bottomSheetFragment;
                mListener_city.pass_data(null,patient_sign_up.this);
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
                Log.w("khdsddsaasdk",date_of_birth_s);
                String insurance_company_s=insurance_company.getText().toString();
                int insurance_company_i=0;
                if (insurance_model!=null) {
                     insurance_company_i = insurance_model.id;
                }

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
                       confirm_password_s.length()>0&&date_of_birth_s.length()>0&&country_s.length()>0){
                   if (confirm_password_s.equals(password_s)) {
                       mprogressBar.setVisibility(View.VISIBLE);
                       sign_up(full_name_s, mobile_number_s, email_address_s, password_s, date_of_birth_s
                               , insurance_company_s, gender,gender_id,insurance_company_i,gov_model.id,city_model.id,city_model.name,gov_model.name,country_s);
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

        doctor_image.setOnClickListener(new View.OnClickListener() {
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
            String url = "http://intmicrotec.neat-url.com:6566/api/register/add";
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

                                if (res.getString("error").contains(" User Exists")){
                                    Toast.makeText(patient_sign_up.this,getResources().getString(R.string.user_exists),Toast.LENGTH_LONG).show();

                                }else {
                                    Toast.makeText(patient_sign_up.this,getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

                                }
                        } else if (res.has("done")) {
                            if (res.getBoolean("done")) {

                                JSONObject user = res.getJSONObject("user");
                                getSharedPreferences("personal_data", MODE_PRIVATE).edit()
                                        .putInt("id",user.getJSONObject("ref_info").getInt("id"))
                                        .putString("image_url",user.getString("image_url"))
                                        .putString("name",user.getString("name"))
                                        .putBoolean("state",true)
                                        .putString("language",Locale.getDefault().getLanguage())
                                        .commit();
                                Intent got_confirm_code = new Intent(patient_sign_up.this, patient_confirm_code.class);
                                Log.w("dsaldj",mobile_number_s);
                                got_confirm_code.putExtra("phone_number", mobile_number_s);
                                startActivity(got_confirm_code);
                                finish();
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
                        object.put("birth_date_day", date_of_birth_s);
                        JSONObject gen=new JSONObject();
                        gen.put("name", gender);
                        gen.put("id", gender_id);
                        object.put("gender",gen);
                        JSONObject insurance=new JSONObject();
                        insurance.put("id",insurance_company_i);
                        insurance.put("name",insurance_company_s);
                        object.put("insurance_company",insurance);
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
                        object.put("image_url",selected_image_url);



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

                        date_of_birth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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
                    Log.w("kdasjasdjls",uri);
                    mprogressBar.setVisibility(View.VISIBLE);
                    uploadImage(uri);


                } catch (Exception e) {
                    Log.w("errrrrrrror",e.getMessage());
                }
            }
        }


    }
    private void uploadImage(String imagePath) {
        //creating a file
        File file = new File(imagePath);
        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);
//        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        Log.e("requestFile",requestFile.toString());
        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //creating our api
        ApiConfig api = retrofit.create(ApiConfig.class);
        //creating a call and calling the upload image method
        Call call = api.uploadImage("upload/image/default",body);
        //finally performing the call
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {

                mprogressBar.setVisibility(View.INVISIBLE);
                    Log.w("response",new Gson().toJson(response.body()));
                try {
                    JSONObject object=new JSONObject(new Gson().toJson(response.body()));
                    selected_image_url=object.getString("image_url");
                    Picasso.with(patient_sign_up.this)
                            .load("http://intmicrotec.neat-url.com:6566"+selected_image_url)
                            .placeholder(R.drawable.user)
                            .into(doctor_image, new Callback() {
                                @Override
                                public void onSuccess() {}
                                @Override public void onError() {
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mprogressBar.setVisibility(View.INVISIBLE);
            }


    });
}
    @Override
    public void pass_data(patient_city_model city_model, pass_city_data listner) {
        this.city_model=city_model;
        city.setText(city_model.name);
    }
    @Override
    public void pass_data(patient_gov_model gov_model, pass_gov_data listner) {
        this.gov_model=gov_model;
        governorates.setText(gov_model.name);
    }
    @Override
    public void pass_data(patient_insurance_model insurance_model, pass_insurance_data listner) {
        this.insurance_model=insurance_model;
        insurance_company.setText(insurance_model.name);
    }
}
