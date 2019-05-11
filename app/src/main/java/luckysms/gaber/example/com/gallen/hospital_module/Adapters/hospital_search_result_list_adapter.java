package luckysms.gaber.example.com.gallen.hospital_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Model.search_doctor_name_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public class hospital_search_result_list_adapter extends RecyclerView.Adapter<hospital_search_result_list_adapter.MyViewHolder>  {

    private Context context;
    private List<search_doctor_name_model> contact_list;

    public void filterList(List<search_doctor_name_model> filteredList) {
        contact_list = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,speciality,schedules,discount_code,graduated_from,doctor_fee,hospital_name;
        public Button book_now;
        public ImageView image,hospital_image;
        public RatingBar rating;
        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            speciality=(TextView) view.findViewById(R.id.speciality);
            schedules=(TextView) view.findViewById(R.id.schedules);
            discount_code=(TextView) view.findViewById(R.id.discount_code);
            graduated_from=(TextView) view.findViewById(R.id.graduated_from);
            doctor_fee=(TextView) view.findViewById(R.id.doctor_fee);
            hospital_name=(TextView) view.findViewById(R.id.hospital_name);
            book_now=(Button)view.findViewById(R.id.book_now);
            rating=(RatingBar)view.findViewById(R.id.rating);
            image=(ImageView)view.findViewById(R.id.image);
            hospital_image=(ImageView)view.findViewById(R.id.hospital_image);

        }
    }



    public hospital_search_result_list_adapter(Context context, List<search_doctor_name_model> contact_list) {
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
        search_doctor_name_model data = contact_list.get(position);
        holder.name.setText(data.doctor_model.doctor_name);
        holder.speciality.setText(data.speciality_model.name);
        holder.schedules.setText(data.doctor_model.doctor_availabilty);
        if (data.doctor_model.doctor_accept_discount) {
            holder.discount_code.setText(context.getResources().getText(R.string.Accepts_the_discount_code));
        }else {
            holder.discount_code.setText(context.getResources().getText(R.string.not_Accepts_the_discount_code));
        }
        holder.graduated_from.setText(data.doctor_model.doctor_graduated);
        holder.doctor_fee.setText(context.getResources().getText(R.string.Detection_Price)+String .valueOf(data.doctor_model.doctor_fee));
        holder.rating.setRating(data.doctor_model.doctor_rating);
        Picasso.with(context)
                .load("http://microtec1.egytag.com"+data.doctor_model.doctor_image)
                .placeholder(R.drawable.user)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {}
                    @Override public void onError() {
                    }
                });





    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


