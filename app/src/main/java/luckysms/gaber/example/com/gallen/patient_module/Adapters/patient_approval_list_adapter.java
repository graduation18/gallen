package luckysms.gaber.example.com.gallen.patient_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Model.approval_list_model;

public class patient_approval_list_adapter extends RecyclerView.Adapter<patient_approval_list_adapter.MyViewHolder>  {

    private Context context;
    private List<approval_list_model> contact_list;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,location,phone;
        public ImageView image;
        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            location=(TextView) view.findViewById(R.id.location);
            phone=(TextView) view.findViewById(R.id.phone);
            image=(ImageView)view.findViewById(R.id.image);

        }
    }



    public patient_approval_list_adapter(Context context, List<approval_list_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_approval_list_item, parent, false);
        return new MyViewHolder(itemView);


    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        approval_list_model data = contact_list.get(position);
        holder.name.setText(data.name);
        holder.location.setText(data.location);
        holder.phone.setText(data.phone);
        Picasso.with(context)
                .load(data.image)
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