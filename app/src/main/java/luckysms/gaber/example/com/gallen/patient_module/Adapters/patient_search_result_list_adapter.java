package luckysms.gaber.example.com.gallen.patient_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public class patient_search_result_list_adapter extends RecyclerView.Adapter<patient_search_result_list_adapter.MyViewHolder>  {

    private Context context;
    private List<search_result_list_model> contact_list;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,speciality,schedules,discount_code,graduated_from,doctor_fee;
        public Button book_now;
        public ImageView image;
        public RatingBar rating;
        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            speciality=(TextView) view.findViewById(R.id.speciality);
            schedules=(TextView) view.findViewById(R.id.schedules);
            discount_code=(TextView) view.findViewById(R.id.discount_code);
            graduated_from=(TextView) view.findViewById(R.id.graduated_from);
            doctor_fee=(TextView) view.findViewById(R.id.doctor_fee);
            book_now=(Button)view.findViewById(R.id.book_now);
            rating=(RatingBar)view.findViewById(R.id.rating);
            image=(ImageView)view.findViewById(R.id.image);

        }
    }



    public patient_search_result_list_adapter(Context context, List<search_result_list_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_search_by_area_and_speciality_result_item, parent, false);
        return new MyViewHolder(itemView);


    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        search_result_list_model data = contact_list.get(position);
        holder.name.setText(data.doctor_name);
        holder.speciality.setText(data.doctor_speciality);
        holder.schedules.setText(data.doctor_availabilty);
        if (data.doctor_accept_discount) {
            holder.discount_code.setText(context.getResources().getText(R.string.Accepts_the_discount_code));
        }else {
            holder.discount_code.setText(context.getResources().getText(R.string.not_Accepts_the_discount_code));
        }
        holder.graduated_from.setText(data.doctor_graduated);
        holder.doctor_fee.setText(context.getResources().getText(R.string.Detection_Price)+String .valueOf(data.doctor_fee));
        holder.rating.setRating(data.doctor_rating);
        Picasso.with(context)
                .load(data.doctor_image)
                .placeholder(R.drawable.pharmcy)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override public void onError() {
                        Toast.makeText(context,"error loading image",Toast.LENGTH_LONG).show();
                    }
                });




    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


