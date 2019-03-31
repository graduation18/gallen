package luckysms.gaber.example.com.gallen.patient_module.Model;

public class available_appointments_list_model {
    public String day,from,to;
    public int day_id,from_id,to_id;

    public available_appointments_list_model(String day,String from,String to,int day_id,int from_id ,int to_id){
        this.day=day;
        this.from=from;
        this.to=to;
        this.day_id=day_id;
        this.from_id=from_id;
        this.to_id=to_id;
    }
}
