package luckysms.gaber.example.com.gallen.patient_module.Model;

public class favourite {
    public hospital_model hospital_model;
    public doctor_model doctor_model;
    public patient_speciality_model patient_speciality_model;
    public favourite( hospital_model hospital_model,doctor_model doctor_model,
                                     patient_speciality_model patient_speciality_model) {
        this.hospital_model = hospital_model;
        this.doctor_model = doctor_model;
        this.patient_speciality_model = patient_speciality_model;
    }
}
