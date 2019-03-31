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

public class patient_doctor_available_appointments_list_adapter extends RecyclerView.Adapter<patient_doctor_available_appointments_list_adapter.MyViewHolder>  {

    private Context context;
    private List<available_appointments_list_model> contact_list;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,time;
        public Button book_now;
        public MyViewHolder(View view) {
            super(view);
            date=(TextView) view.findViewById(R.id.date);
            time=(TextView) view.findViewById(R.id.time);
            book_now=(Button) view.findViewById(R.id.book_now);

        }
    }



    public patient_doctor_available_appointments_list_adapter(Context context, List<available_appointments_list_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_doctor_available_appointments_item, parent, false);
        return new MyViewHolder(itemView);


    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        available_appointments_list_model data = contact_list.get(position);
        holder.date.setText(data.day+"\n"+data.from);

        holder.time.setText(data.to);





    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


