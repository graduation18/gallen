package luckysms.gaber.example.com.gallen.patient_module.Model;

import java.io.Serializable;

public class patient_gov_model implements Serializable {
    public String _id,image_url,name;
    public int id;
    public patient_gov_model(String _id, String image_url, String name,int id){
        this._id=_id;
        this.image_url=image_url;
        this.name=name;
        this.id=id;

    }

}
