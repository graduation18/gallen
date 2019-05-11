package luckysms.gaber.example.com.gallen.hospital_module.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.hospital_module.Model.clinic_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

public class clinic_SpinAdapter extends RecyclerView.Adapter<clinic_SpinAdapter.MyViewHolder>  {

    private Context context;
    private List<clinic_model> contact_list;

    public void filterList(ArrayList<clinic_model> filteredList) {
        contact_list = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);

        }
    }



    public clinic_SpinAdapter(Context context, List<clinic_model> contact_list) {
        this.context = context;
        this.contact_list = contact_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_row, parent, false);
        return new MyViewHolder(itemView);


    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        clinic_model data = contact_list.get(position);
        holder.name.setText(data.name);




    }


    @Override
    public int getItemCount() {
        return contact_list.size();
    }





}


