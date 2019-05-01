package luckysms.gaber.example.com.gallen.hospital_module.Model;

public class search_doctor_name_model {
    public doctor_model doctor_model;
    public speciality_model speciality_model;
    public search_doctor_name_model( doctor_model doctor_model,speciality_model speciality_model){
        this.doctor_model=doctor_model;
        this.speciality_model=speciality_model;
    }
}
