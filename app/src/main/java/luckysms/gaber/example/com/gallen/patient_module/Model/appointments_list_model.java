package luckysms.gaber.example.com.gallen.patient_module.Model;

import org.json.JSONArray;

public class appointments_list_model {
   public int selected_time_id,selected_shift_id,selected_doctor_id,selected_specialty_id
           ,selected_hospital_id,selected_clinic_id,patient_id,status_id,id;
   public String selected_time_name,selected_shift_name,selected_doctor_name,selected_specialty_name
           ,selected_hospital_name,selected_clinic_name,patient__id,patient_image_url,patient_name,patient_mobile,patient_insurance,status_ar
           ,status_en,status_name,image_url,_id;

    public JSONArray drugs_list,scans_list,analyses_list,operation_list;
   public long date;
    public appointments_list_model(
            String selected_time_name, String selected_shift_name, String selected_doctor_name, String selected_specialty_name
            , String selected_hospital_name, String selected_clinic_name, String patient__id, String patient_image_url, String patient_name
            , String patient_mobile,String patient_insurance, String status_ar, String status_en, String status_name, long date, String image_url, String _id,
            int selected_time_id, int selected_shift_id, int selected_doctor_id, int selected_specialty_id
            , int selected_hospital_id, int selected_clinic_id, int patient_id, int status_id, int id
            , JSONArray drugs_list,JSONArray scans_list,JSONArray analyses_list,JSONArray operation_list
    ){
        this.selected_time_name=selected_time_name;
        this.selected_shift_name=selected_shift_name;
        this.selected_doctor_name=selected_doctor_name;
        this.selected_specialty_name=selected_specialty_name;
        this.selected_hospital_name=selected_hospital_name;
        this.selected_clinic_name=selected_clinic_name;
        this.patient__id=patient__id;
        this.patient_image_url=patient_image_url;
        this.patient_name=patient_name;
        this.patient_mobile=patient_mobile;
        this.patient_insurance=patient_insurance;
        this.status_ar=status_ar;
        this.status_en=status_en;
        this.status_name=status_name;
        this.date=date;
        this.image_url=image_url;
        this._id=_id;
        this.selected_shift_id=selected_shift_id;
        this.selected_time_id=selected_time_id;
        this.selected_doctor_id=selected_doctor_id;
        this.selected_specialty_id=selected_specialty_id;
        this.selected_hospital_id=selected_hospital_id;
        this.selected_clinic_id=selected_clinic_id;
        this.patient_id=patient_id;
        this.status_id=status_id;
        this.id=id;
        this.drugs_list=drugs_list;
        this.scans_list=scans_list;
        this.analyses_list=analyses_list;
        this.operation_list=operation_list;

    }

}
