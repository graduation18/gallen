package luckysms.gaber.example.com.gallen.hospital_module.Model;

import org.json.JSONArray;
import org.json.JSONObject;

public class clinic_model {
    public String  name,address,phone,website,email,image_url;
    public int  id;
    public boolean active;
    public JSONObject hospital,gov,city;
    public JSONArray insurance_company_list,doctor_list,nurse_list;
    public double latitude,longitude;

    public clinic_model(String name,String address,String phone,String website,String email,String image_url
    ,int id,boolean active,JSONObject hospital, JSONObject gov,JSONObject city
    ,JSONArray insurance_company_list,JSONArray doctor_list, JSONArray nurse_list,double latitude,double longitude){
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

