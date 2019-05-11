package luckysms.gaber.example.com.gallen.patient_module.Custom;

import java.util.List;

import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;
import luckysms.gaber.example.com.gallen.patient_module.Model.search_result_list_model;

public interface pass_speciality_data {
    void pass_data(patient_speciality_model speciality_model, pass_speciality_data listner);

}
