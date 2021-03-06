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
import luckysms.gaber.example.com.gallen.patient_module.Model.appointment_drugs_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.appointments_list_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.available_appointments_list_model;

public class patient_appointments_drug_list_adapter extends RecyclerView.Adapter<patient_appointments_drug_list_adapter.MyViewHolder>  {

    private Context context;
    private List<appointment_drugs_list_model> contact_list;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView drug_name,drug_use,dose,patient_name;
        public MyViewHolder(View view) {
            super(view);
            drug_name=(TextView) view.findViewById(R.id.drug_name);
            drug_use=(TextView) view.findViewById(R.id.drug_use);
            dose=(TextView) view.findViewById(R.id.dose);
            patient_name=(TextView) view.findViewById(R.id.patient_name);

        }
    }



    public patient_appointments_drug_list_adapter(Context context, List<appointment_drugs_list_model> contact_list) {
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
        appointment_drugs_list_model data = contact_list.get(position);
        holder.patient_name.setText(data.patient_name);
        holder.dose.setText(data.dose);
        holder.drug_name.setText(data.drug_name);
        holder.drug_use.setText(data.drug_use);






    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


