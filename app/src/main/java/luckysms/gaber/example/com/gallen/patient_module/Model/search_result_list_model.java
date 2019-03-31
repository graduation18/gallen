package luckysms.gaber.example.com.gallen.patient_module.Model;

public class search_result_list_model {
    public String doctor_name,doctor_speciality,doctor_availabilty,doctor_graduated,doctor_location,doctor_image;
    public boolean doctor_accept_discount;
    public Float doctor_rating;
    public int id;
    public double doctor_fee;
    public search_result_list_model(String doctor_name, String doctor_speciality, String doctor_availabilty
            , String doctor_graduated, String doctor_location, String doctor_image
            , boolean doctor_accept_discount, Float doctor_rating, double doctor_fee, int id){
        this.doctor_name=doctor_name;
        this.doctor_speciality=doctor_speciality;
        this.doctor_availabilty=doctor_availabilty;
        this.doctor_graduated=doctor_graduated;
        this.doctor_location=doctor_location;
        this.doctor_image=doctor_image;
        this.doctor_accept_discount=doctor_accept_discount;
        this.doctor_rating=doctor_rating;
        this.doctor_fee=doctor_fee;
        this.id=id;
    }

}
