package luckysms.gaber.example.com.gallen.patient_module.Model;

import java.io.Serializable;

public class doctor_model implements Serializable {
    public String doctor_name,doctor_availabilty,doctor_graduated,doctor_image,doctor_notes,doctor_gender,review_list;
    public boolean doctor_accept_discount;
    public Float doctor_rating;
    public int id;
    public double doctor_fee;

    public doctor_model(String doctor_name, String doctor_availabilty
            , String doctor_graduated, String doctor_image
            , boolean doctor_accept_discount, Float doctor_rating, double doctor_fee, int id,String doctor_notes
    ,String doctor_gender,String review_list){
        this.doctor_name=doctor_name;
        this.doctor_availabilty=doctor_availabilty;
        this.doctor_graduated=doctor_graduated;
        this.doctor_image=doctor_image;
        this.doctor_accept_discount=doctor_accept_discount;
        this.doctor_rating=doctor_rating;
        this.doctor_fee=doctor_fee;
        this.id=id;
        this.doctor_notes=doctor_notes;
        this.doctor_gender=doctor_gender;
        this.review_list=review_list;
    }
}
