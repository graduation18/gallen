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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Adapters.patient_doctor_reviews_list_adapter;
import luckysms.gaber.example.com.gallen.patient_module.Custom.MyDividerItemDecoration;
import luckysms.gaber.example.com.gallen.patient_module.Model.reviews_list_model;
import static android.content.Context.MODE_PRIVATE;

public class reviews_BottomSheetFragment extends BottomSheetDialogFragment {
    private View view;
    private RequestQueue queue;
    private ProgressBar mprogressBar;
    private int id;
    private Dialog dialog;
    private List<reviews_list_model> reviews_list = new ArrayList<>();
    private RecyclerView reviews_recycler;
    private patient_doctor_reviews_list_adapter reviews_list_adapter;
    private BottomSheetBehavior mBehavior;
    private TextView text,rating_ratio,vistors;
    private RatingBar ratingBar;
    private JSONArray review_list;
    private boolean visitor;

    public reviews_BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments()!=null){
            id=getArguments().getInt("id");
            visitor=getArguments().getBoolean("visitor");

        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        view = View.inflate(getContext(), R.layout.patient_doctor_rating_fragment, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);
        mprogressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ratingBar=(RatingBar)view.findViewById(R.id.rating);
        rating_ratio=(TextView)view.findViewById(R.id.rating_ratio);
        vistors=(TextView)view.findViewById(R.id.vistors);
        text=(TextView)view.findViewById(R.id.text);
        ImageButton cancel=(ImageButton)view.findViewById(R.id.cancel);
        Button add_review=(Button)view.findViewById(R.id.add_review);
        reviews_recycler = view.findViewById(R.id.reviews_recycler);
        reviews_list_adapter = new patient_doctor_reviews_list_adapter(getActivity(), reviews_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        reviews_recycler.setLayoutManager(mLayoutManager);
        reviews_recycler.setItemAnimator(new DefaultItemAnimator());
        reviews_recycler.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 5));
        reviews_recycler.setAdapter(reviews_list_adapter);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visitor) {
                    reviews_BottomSheetFragment.this.dialog = new Dialog(getActivity());
                    reviews_BottomSheetFragment.this.dialog.setContentView(R.layout.add_review);
                    Button add_review = (Button) reviews_BottomSheetFragment.this.dialog.findViewById(R.id.add_review);
                    final RatingBar rating = (RatingBar) reviews_BottomSheetFragment.this.dialog.findViewById(R.id.rating);
                    final EditText review = (EditText) reviews_BottomSheetFragment.this.dialog.findViewById(R.id.review);
                    add_review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String review_s = review.getText().toString();
                            Float rate = rating.getRating();
                            if (review_s.length() > 2) {
                                mprogressBar.setVisibility(View.VISIBLE);
                                review_update(id, review_list, rate, review_s);
                                reviews_BottomSheetFragment.this.dialog.dismiss();
                            }
                        }
                    });
                    reviews_BottomSheetFragment.this.dialog.show();
                }else {
                    Toast.makeText(getActivity(),getResources().getText(R.string.please_sign_in_first),Toast.LENGTH_LONG).show();

                }
            }
        });


        mprogressBar.setVisibility(View.VISIBLE);
        get_data(id);



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

    private void review_update(final int doc_id, final JSONArray review_list, final float rate, final String comment)
    {


        try {
            final int[] counter = {0};
            String url = "http://intmicrotec.neat-url.com:6566/api/doctors/update";
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
                                Toast.makeText(getActivity(),getResources().getString(R.string.added_to_reviews),Toast.LENGTH_LONG).show();
                                reviews_list.add(new reviews_list_model(comment,
                                        new SimpleDateFormat("MM/dd/yyyy").format(new Date())
                                        , (int) rate
                                        , getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("image_url","")
                                , getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("name","")));
                                reviews_list_adapter.notifyDataSetChanged();
                            }
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (counter[0]<4) {
                        review_update(doc_id,review_list,rate,comment);
                        counter[0]++;
                    }else {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                        mprogressBar.setVisibility(View.INVISIBLE);
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> pars = new HashMap<String, String>();
                    pars.put("Content-Type", "application/json");
                    pars.put("Cookie", "access_token="+getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("accessToken",""));

                    return pars;
                }



                @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id", doc_id);
                        JSONArray array=new JSONArray(new String(review_list.toString().getBytes("ISO-8859-1"), "UTF-8"));
                        JSONObject review=new JSONObject();
                        review.put("comment",comment);
                        review.put("rate",rate);
                        review.put("date",new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                        review.put("patient_name",
                                getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("name",""));
                        review.put("patient_image_url",
                                getActivity().getSharedPreferences("personal_data", MODE_PRIVATE).getString("image_url",""));
                        array.put(review);
                        object.put("review_list",array);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
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
    private void get_data(final int id)
    {


        try {
            final int[] counter = {0};
            String url = "http://intmicrotec.neat-url.com:6566/api/doctors/view";
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
                                JSONObject doctor=res.getJSONObject("doc");
                                review_list=new JSONArray();
                                if (doctor.has("review_list")){
                                    review_list=doctor.getJSONArray("review_list");
                                    Float rate=0f;
                                        for (int i=0;i<review_list.length();i++){
                                            JSONObject j=review_list.getJSONObject(i);
                                            reviews_list.add(new reviews_list_model(
                                                    new String (j.getString("comment")
                                                            .getBytes("ISO-8859-1"), "UTF-8")
                                                    ,new String (j.getString("date") .getBytes("ISO-8859-1"), "UTF-8"),j.getInt("rate")
                                                    ,j.getString("patient_image_url")
                                                    ,new String(j.getString("patient_name").getBytes("ISO-8859-1"), "UTF-8")));
                                            rate=rate+j.getInt("rate");

                                        }
                                        if(reviews_list.size()>0){
                                            reviews_list_adapter.notifyDataSetChanged();
                                            reviews_recycler.setVisibility(View.VISIBLE);
                                            text.setVisibility(View.INVISIBLE);
                                        }

                                    rate=rate/review_list.length();
                                    ratingBar.setRating(rate);
                                    rating_ratio.setText(String.valueOf(Math.round(rate)));


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
                    if (counter[0]<4) {
                        get_data(id);
                        counter[0]++;
                    }else {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();
                        mprogressBar.setVisibility(View.INVISIBLE);
                    }

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
                public byte[] getBody() throws AuthFailureError {
                    JSONObject object=new JSONObject();
                    try {
                        object.put("id",id);




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

}