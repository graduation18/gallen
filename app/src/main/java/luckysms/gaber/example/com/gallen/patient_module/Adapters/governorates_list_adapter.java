package luckysms.gaber.example.com.gallen.patient_module.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import luckysms.gaber.example.com.gallen.R;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_city_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.patient_gov_model;

/**
 * Created by gaber on 12/08/2018.
 */

public class governorates_list_adapter extends ArrayAdapter<patient_gov_model> {
private ArrayList<patient_gov_model> items;
private ArrayList<patient_gov_model> itemsAll;
private ArrayList<patient_gov_model> suggestions;
private int viewResourceId;

public governorates_list_adapter(Context context, int viewResourceId, ArrayList<patient_gov_model> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<patient_gov_model>) items.clone();
        this.suggestions = new ArrayList<patient_gov_model>();
        this.viewResourceId = viewResourceId;
        }

public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(viewResourceId, null);
        }
        patient_gov_model customer = items.get(position);
        if (customer != null) {
        TextView city_name = (TextView) v.findViewById(R.id.text_view_name);
        if (city_name != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                city_name.setText(customer.name);
        }
        }
        return v;
        }

@Override
public Filter getFilter() {
        return nameFilter;
        }

        Filter nameFilter = new Filter() {
@Override
public String convertResultToString(Object resultValue) {
        String str = ((patient_gov_model)(resultValue)).name;
        return str;
        }
@Override
protected FilterResults performFiltering(CharSequence constraint) {
        if(constraint != null) {
        suggestions.clear();
        for (patient_gov_model city : itemsAll) {
        if(city.name.toLowerCase().startsWith(constraint.toString().toLowerCase())){
        suggestions.add(city);
        }
        }
        FilterResults filterResults = new FilterResults();
        filterResults.values = suggestions;
        filterResults.count = suggestions.size();
        return filterResults;
        } else {
        return new FilterResults();
        }
        }
@Override
protected void publishResults(CharSequence constraint, FilterResults results) {
        ArrayList<patient_gov_model> filteredList = (ArrayList<patient_gov_model>) results.values;
        if(results != null && results.count > 0) {
        clear();
        for (patient_gov_model c : filteredList) {
        add(c);
        }
        notifyDataSetChanged();
        }
        }
        };

        }