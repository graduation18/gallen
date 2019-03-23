package luckysms.gaber.example.com.gallen.patient_module.Model;

public class appointments_list_model {
    public String name,image,date,speciality,type;
    public long time;
    appointments_list_model(String name, long time, String date, String image, String speciality,String type){
        this.name=name;
        this.date=date;
        this.time=time;
        this.image=image;
        this.speciality=speciality;
        this.type=type;
    }

}
