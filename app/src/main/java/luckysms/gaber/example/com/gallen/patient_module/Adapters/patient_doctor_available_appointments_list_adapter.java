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

import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Model.approval_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;

public class patient_doctor_available_appointments_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<available_appointments_list_model> contact_list;
    private boolean doctor;



    public class MyViewHolder_booked extends RecyclerView.ViewHolder {
        public TextView date,time;
        public Button book_now;
        public MyViewHolder_booked(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            book_now=(Button) view.findViewById(R.id.book_now);

        }
    }
    public class MyViewHolder_free extends RecyclerView.ViewHolder {
        public TextView date,time;
        public Button book_now;
        public MyViewHolder_free(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            book_now=(Button) view.findViewById(R.id.book_now);

        }
    }
    public class MyViewHolder_cancelled extends RecyclerView.ViewHolder {
        public TextView date,time;
        public Button book_now;
        public MyViewHolder_cancelled(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            book_now=(Button) view.findViewById(R.id.book_now);

        }
    }
    public class MyViewHolder_completed extends RecyclerView.ViewHolder {
        public TextView date,time;
        public Button book_now;
        public MyViewHolder_completed(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            book_now=(Button) view.findViewById(R.id.book_now);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return contact_list.get(position).status_id;
    }

    public patient_doctor_available_appointments_list_adapter(Context context, List<available_appointments_list_model> contact_list,boolean doctor) {
        this.context = context;
        this.contact_list = contact_list;
        this.doctor = doctor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0) {
            if (doctor) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.patient_doctor_available_appointments_free_item, parent, false);
                return new MyViewHolder_free(itemView);
            }else {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.patient_doctor_available_appointments_item, parent, false);
                return new MyViewHolder_free(itemView);
            }
        }else if (viewType==1){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_doctor_available_appointments_booked_item, parent, false);
            return new MyViewHolder_booked(itemView);
        }else if (viewType==2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_doctor_available_appointments_cancelled_item, parent, false);
            return new MyViewHolder_cancelled(itemView);
        }else if (viewType==3){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_doctor_available_appointments_completed_item, parent, false);
            return new MyViewHolder_completed(itemView);
        }else {
            return null;
        }



    }




    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        available_appointments_list_model data = contact_list.get(position);
        if (holder.getItemViewType()==0) {
            MyViewHolder_free free=(MyViewHolder_free)holder;
            free.date.setText(data.day+"\n"+data.from);
            free.time.setText(data.to);
        }else if (holder.getItemViewType()==1){
            MyViewHolder_booked booked =(MyViewHolder_booked)holder;
            booked .date.setText(data.day+"\n"+data.from);
            booked .time.setText(data.to);
        }else if (holder.getItemViewType()==2){
            MyViewHolder_cancelled cancelled=(MyViewHolder_cancelled)holder;
            cancelled.date.setText(data.day+"\n"+data.from);
            cancelled.time.setText(data.to);
        }else if (holder.getItemViewType()==3){
            MyViewHolder_completed completed=(MyViewHolder_completed)holder;
            completed.date.setText(data.day+"\n"+data.from);
            completed.time.setText(data.to);
        }






    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


