package luckysms.gaber.example.com.gallen.patient_module.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Activities.patient_start_screen;

import static android.content.Context.MODE_PRIVATE;

public class patient_more_visitor extends Fragment {
    private View view;
    private Button settings, approval_list, contact_us, about_us, sign_out,sign_in;
    public LinearLayout more_layout;
    private RequestQueue queue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.patient_more_visitor_fragment, container, false);

        settings = (Button) view.findViewById(R.id.settings);
        approval_list = (Button) view.findViewById(R.id.approval_list);
        contact_us = (Button) view.findViewById(R.id.contact_us);
        about_us = (Button) view.findViewById(R.id.about_us);
        sign_out = (Button) view.findViewById(R.id.sign_out);
        sign_in = (Button) view.findViewById(R.id.sign_in);
        more_layout = (LinearLayout) view.findViewById(R.id.more_layout);

        more_layout.setEnabled(false);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_screen=new Intent(getActivity(),patient_start_screen.class);
                startActivity(start_screen);
                getActivity().finish();
            }
        });




        return view;
    }

}