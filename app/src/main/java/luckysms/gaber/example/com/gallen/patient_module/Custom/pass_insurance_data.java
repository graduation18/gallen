package luckysms.gaber.example.com.gallen.patient_module.Custom;

import java.util.List;

import luckysms.gaber.example.com.gallen.patient_module.Model.patient_insurance_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public interface pass_insurance_data {
    void pass_data(patient_insurance_model insurance_model, pass_insurance_data listner);

}
