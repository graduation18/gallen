package luckysms.gaber.example.com.gallen.patient_module.Model;

public class search_result_list_model {
    public hospital_model hospital_model;
    public doctor_model doctor_model;
    public patient_speciality_model patient_speciality_model;
    public patient_insurance_model patient_insurance_model;
    public patient_gov_model patient_gov_model;
    public patient_city_model patient_city_model;
    public nurse_model nurse_model;
    public search_result_list_model( hospital_model hospital_model,doctor_model doctor_model,
             patient_speciality_model patient_speciality_model,patient_insurance_model patient_insurance_model
            ,patient_gov_model patient_gov_model,patient_city_model patient_city_model,nurse_model nurse_model){
        this.hospital_model=hospital_model;
        this.doctor_model=doctor_model;
        this.patient_speciality_model=patient_speciality_model;
        this.patient_insurance_model=patient_insurance_model;
        this.patient_gov_model=patient_gov_model;
        this.patient_city_model=patient_city_model;
        this.nurse_model=nurse_model;

    }

}
