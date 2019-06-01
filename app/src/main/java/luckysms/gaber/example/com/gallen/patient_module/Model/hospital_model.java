package luckysms.gaber.example.com.gallen.patient_module.Model;

import java.io.Serializable;

public class hospital_model implements Serializable {
    public String  hospital__id,hospital_image_url,hospital_name,hospital_address,hospital_phone,hospital_mobile,hospital_latitude,hospital_longitude;
    public int  hospital_id;

    public hospital_model(String hospital__id,String hospital_image_url,String hospital_name,String hospital_address
            ,String hospital_phone,String hospital_mobile,int  hospital_id,String hospital_latitude,String hospital_longitude){
        this.hospital__id=hospital__id;
        this.hospital_image_url=hospital_image_url;
        this.hospital_name=hospital_name;
        this.hospital_address=hospital_address;
        this.hospital_phone=hospital_phone;
        this.hospital_mobile=hospital_mobile;
        this.hospital_id=hospital_id;
        this.hospital_latitude=hospital_latitude;
        this.hospital_longitude=hospital_longitude;
    }
}
