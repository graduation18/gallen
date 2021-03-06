package luckysms.gaber.example.com.gallen.patient_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Custom.AsyncTaskLoadImage;
import luckysms.gaber.example.com.gallen.patient_module.Model.approval_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.reviews_list_model;

public class patient_doctor_reviews_list_adapter extends RecyclerView.Adapter<patient_doctor_reviews_list_adapter.MyViewHolder>  {

    private Context context;
    private List<reviews_list_model> contact_list;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,date_time,patient_name;
        public RatingBar rating;
        public ImageView patient_image_url;
        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            date_time=(TextView) view.findViewById(R.id.date_time);
            patient_name=(TextView) view.findViewById(R.id.patient_name);
            rating=(RatingBar)view.findViewById(R.id.rating);
            patient_image_url=(ImageView)view.findViewById(R.id.patient_image_url);

        }
    }



    public patient_doctor_reviews_list_adapter(Context context, List<reviews_list_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_review_list_item, parent, false);
        return new MyViewHolder(itemView);


    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        reviews_list_model data = contact_list.get(position);
        holder.name.setText(data.name);
        holder.date_time.setText(data.date_time);
        holder.rating.setRating(data.rating);
        holder.patient_name.setText(data.patient_name);
        new AsyncTaskLoadImage(holder.patient_image_url).execute("http://intmicrotec.neat-url.com:6566"+data.patient_image_url);



    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


