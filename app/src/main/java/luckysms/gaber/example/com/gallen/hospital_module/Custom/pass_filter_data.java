package luckysms.gaber.example.com.gallen.hospital_module.Custom;

import java.util.List;

import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public interface pass_filter_data {
    void pass_data(List<search_result_list_model> contact_list, pass_filter_data listner);
    void notify_adapter(pass_filter_data listner, List<search_result_list_model> contact_list);

}
