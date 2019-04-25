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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Custom.RecyclerTouchListener;
import luckysms.gaber.example.com.gallen.patient_module.Custom.appointment_Listener;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointments_list_model;

public class patient_appointments_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<appointments_list_model> contact_list;
    private RecyclerTouchListener touchListener;
    private appointment_Listener mListener;



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
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.details(getAdapterPosition());
                }
            });
            detect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.detect(getAdapterPosition());
                }
            });
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

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.cancel(getAdapterPosition());
                }
            });
            map_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.map_location(getAdapterPosition());
                }
            });
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



    public patient_appointments_list_adapter(Context context, List<appointments_list_model> contact_list,appointment_Listener mListener) {
        this.context = context;
        this.contact_list = contact_list;
        this.mListener =mListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (contact_list.get(position).status_id==2){
            return 0;
        }else if (contact_list.get(position).status_id==1){
            return 1;
        }else if (contact_list.get(position).status_id==3){
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
            finshed.name.setText(data.selected_doctor_name);
            Date date=new Date(data.date);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String date_s = df2.format(date);
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
            String time_s = df1.format(date);
            finshed.date.setText(date_s);
            finshed.time.setText(time_s);
            finshed.speciality.setText(data.selected_specialty_name);
            Picasso.with(context)
                    .load(data.image_url)
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
            not_finshed.name.setText(data.selected_doctor_name);
            Date date=new Date(data.date);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String date_s = df2.format(date);
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
            String time_s = df1.format(date);
            not_finshed.date.setText(date_s);
            not_finshed.time.setText(time_s);
            not_finshed.speciality.setText(data.selected_specialty_name);
            Picasso.with(context)
                    .load(data.image_url)
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
            cancelled.name.setText(data.selected_doctor_name);
            Date date=new Date(data.date);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String date_s = df2.format(date);
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm a");
            String time_s = df1.format(date);
            cancelled.date.setText(date_s);
            cancelled.time.setText(time_s);
            cancelled.speciality.setText(data.selected_specialty_name);
            Picasso.with(context)
                    .load(data.image_url)
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


