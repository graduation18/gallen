package luckysms.gaber.example.com.gallen.patient_module.Model;

public class hospital_model {
    public String  hospital__id,hospital_image_url,hospital_name,hospital_address,hospital_phone,hospital_mobile;
    public int  hospital_id;

    public hospital_model(String hospital__id,String hospital_image_url,String hospital_name,String hospital_address
            ,String hospital_phone,String hospital_mobile,int  hospital_id){
        this.hospital__id=hospital__id;
        this.hospital_image_url=hospital_image_url;
        this.hospital_name=hospital_name;
        this.hospital_address=hospital_address;
        this.hospital_phone=hospital_phone;
        this.hospital_mobile=hospital_mobile;
        this.hospital_id=hospital_id;
    }
}
