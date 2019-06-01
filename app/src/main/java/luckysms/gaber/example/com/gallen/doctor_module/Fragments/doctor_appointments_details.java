package luckysms.gaber.example.com.gallen.doctor_module.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_sign_up;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_appointments_drug_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.ApiConfig;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Fragments.patient_settings;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointment_drugs_list_model;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class doctor_appointments_details extends Fragment {
    private View view;
    private TextView back,number_of_notifications,notifications,location,speciality,insurance_companies;
    private RecyclerView drugs_recycler;
    private List<appointment_drugs_list_model> contact_list = new ArrayList<>();
    private patient_appointments_drug_list_adapter data_adapter;
    private String loc,spe,ins,drugs_list;
    int PICK_IMAGE_MULTIPLE = 1;
    private ProgressBar mprogressBar;
    private String selected_image_url;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            loc=getArguments().getString("location");
            spe=getArguments().getString("speciality");
            ins=getArguments().getString("insurance_companies");
            drugs_list=getArguments().getString("drugs_list");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_appointment_details, container, false);
        number_of_notifications=(TextView)view.findViewById(R.id.number_of_notifications);
        notifications=(TextView)view.findViewById(R.id.notifications);
        back=(TextView)view.findViewById(R.id.back);
        drugs_recycler = view.findViewById(R.id.drugs_recycler);
        data_adapter = new patient_appointments_drug_list_adapter(getActivity(), contact_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        drugs_recycler.setLayoutManager(mLayoutManager);
        drugs_recycler.setItemAnimator(new DefaultItemAnimator());
        drugs_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 5));
        drugs_recycler.setAdapter(data_adapter);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new patient_settings();
                go_to(fragment);
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Fragment fragment=new patient_settings();
                    go_to(fragment);
                    return true;
                }
                return false;
            }
        });


        location.setText(loc);
        speciality.setText(spe);
        insurance_companies.setText(ins);

        return view;
    }
    public void go_to(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void parse_drugs(String drugs){
        try {
            JSONArray drugs_list=new JSONArray(drugs);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void select_images()
    {
        Pix.start(this,
                PICK_IMAGE_MULTIPLE,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    /*Picasso.with(getActivity())
                            .load("http://intmicrotec.neat-url.com:6566"+selected_image_url)

                            .placeholder(R.drawable.user)
                            .into(doctor_image, new Callback() {
                                @Override
                                public void onSuccess() {}
                                @Override public void onError() {
                                }
                            });
                            */
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
}
