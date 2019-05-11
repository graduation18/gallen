package luckysms.gaber.example.com.gallen.hospital_module.Model;

import java.io.Serializable;

import luckysms.gaber.example.com.gallen.patient_module.Model.patient_speciality_model;

public class search_doctor_name_model  {
    public doctor_model doctor_model;
    public patient_speciality_model speciality_model;
    public clinic_model clinic_model;
    public search_doctor_name_model( doctor_model doctor_model,patient_speciality_model speciality_model,clinic_model clinic_model){
        this.doctor_model=doctor_model;
        this.speciality_model=speciality_model;
        this.clinic_model=clinic_model;
    }
}
