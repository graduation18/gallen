package luckysms.gaber.example.com.gallen.patient_module.Model;

public class reviews_list_model {
    public String name,date_time,patient_image_url,patient_name;
    public int rating;
    public reviews_list_model(String name,String date_time,int rating,String patient_image_url,String patient_name){
        this.name=name;
        this.date_time=date_time;
        this.rating=rating;
        this.patient_image_url=patient_image_url;
        this.patient_name=patient_name;
    }
}
