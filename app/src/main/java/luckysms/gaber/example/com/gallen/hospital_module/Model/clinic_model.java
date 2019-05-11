package luckysms.gaber.example.com.gallen.hospital_module.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class clinic_model implements Serializable {
    public String  name,address,phone,website,email,image_url;
    public int  id;
    public boolean active;
    public String hospital,gov,city;
    public String insurance_company_list,doctor_list,nurse_list;
    public double latitude,longitude;

    public clinic_model(String name,String address,String phone,String website,String email,String image_url
    ,int id,boolean active,String hospital, String gov,String city
    ,String insurance_company_list,String doctor_list, String nurse_list,double latitude,double longitude){
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.website=website;
        this.email=email;
        this.image_url=image_url;
        this.id=id;
        this.active=active;
        this.hospital=hospital;
        this.gov=gov;
        this.city=city;
        this.insurance_company_list=insurance_company_list;
        this.doctor_list=doctor_list;
        this.nurse_list=nurse_list;
        this.latitude=latitude;
        this.longitude=longitude;

    }
}

