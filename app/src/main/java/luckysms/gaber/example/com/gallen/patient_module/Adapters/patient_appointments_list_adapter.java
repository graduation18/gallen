package luckysms.gaber.example.com.gallen.patient_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.approval_list_model;

public class patient_appointments_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<appointments_list_model> contact_list;



    public class MyViewHolder_finshed extends RecyclerView.ViewHolder {
        public TextView name,speciality,date,time;
        public ImageView image;
        public Button detect,details;
        public MyViewHolder_finshed(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            speciality=(TextView) view.findViewById(R.id.speciality);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            image=(ImageView)view.findViewById(R.id.image);
            detect=(Button)view.findViewById(R.id.detect);
            details=(Button)view.findViewById(R.id.details);

        }
    }
    public class MyViewHolder_not_finshed extends RecyclerView.ViewHolder {
        public TextView name,speciality,date,time;
        public ImageView image;
        public Button map_location,cancel;
        public MyViewHolder_not_finshed(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            speciality=(TextView) view.findViewById(R.id.speciality);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            image=(ImageView)view.findViewById(R.id.image);
            map_location=(Button)view.findViewById(R.id.map_location);
            cancel=(Button)view.findViewById(R.id.cancel);

        }
    }
    public class MyViewHolder_cancelled extends RecyclerView.ViewHolder {
        public TextView name,speciality,date,time;
        public ImageView image;
        public MyViewHolder_cancelled(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            speciality=(TextView) view.findViewById(R.id.speciality);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            image=(ImageView)view.findViewById(R.id.image);


        }
    }



    public patient_appointments_list_adapter(Context context, List<appointments_list_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public int getItemViewType(int position) {

        if (contact_list.get(position).type.equals("finshed")){
            return 0;
        }else if (contact_list.get(position).type.equals("not finshed")){
            return 1;
        }else if (contact_list.get(position).type.equals("cancelled")){
            return 2;
        }
            return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_appointments_list_item_finshed, parent, false);
            return new MyViewHolder_finshed(itemView);
        }else if (viewType==1){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_appointments_list_item_not_finshed, parent, false);
            return new MyViewHolder_not_finshed(itemView);
        }else if (viewType==2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_appointments_list_item_cancelled, parent, false);
            return new MyViewHolder_cancelled(itemView);
        }
        return null;
    }




    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        appointments_list_model data = contact_list.get(position);
        if (holder.getItemViewType()==0){
            MyViewHolder_finshed finshed=(MyViewHolder_finshed)holder;
            finshed.name.setText(data.name);
            finshed.date.setText(data.date);
            finshed.time.setText(new Date(data.time).toString());
            finshed.speciality.setText(data.speciality);
            Picasso.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.pharmcy)
                    .into(finshed.image, new Callback() {
                        @Override
                        public void onSuccess() {}
                        @Override public void onError() {
                            Toast.makeText(context,"error loading image",Toast.LENGTH_LONG).show();
                        }
                    });




        }else if (holder.getItemViewType()==1){
            MyViewHolder_not_finshed not_finshed=(MyViewHolder_not_finshed)holder;
            not_finshed.name.setText(data.name);
            not_finshed.date.setText(data.date);
            not_finshed.time.setText(new Date(data.time).toString());
            not_finshed.speciality.setText(data.speciality);
            Picasso.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.pharmcy)
                    .into(not_finshed.image, new Callback() {
                        @Override
                        public void onSuccess() {}
                        @Override public void onError() {
                            Toast.makeText(context,"error loading image",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (holder.getItemViewType()==2){
            MyViewHolder_cancelled cancelled=(MyViewHolder_cancelled)holder;
            cancelled.name.setText(data.name);
            cancelled.date.setText(data.date);
            cancelled.time.setText(new Date(data.time).toString());
            cancelled.speciality.setText(data.speciality);
            Picasso.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.pharmcy)
                    .into(cancelled.image, new Callback() {
                        @Override
                        public void onSuccess() {}
                        @Override public void onError() {
                            Toast.makeText(context,"error loading image",Toast.LENGTH_LONG).show();
                        }
                    });
        }



    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


