package luckysms.gaber.example.com.gallen.hospital_module.Model;

import java.io.Serializable;

public class speciality_model implements Serializable {
    public String _id,image_url,name;
    public int id;
    public speciality_model(String _id, String image_url, String name, int id){
        this._id=_id;
        this.image_url=image_url;
        this.name=name;
        this.id=id;


    }

}
