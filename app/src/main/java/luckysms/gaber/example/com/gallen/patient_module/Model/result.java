package luckysms.gaber.example.com.gallen.patient_module.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class result {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;
    public String getMessage() {
        return message;
    }
    public boolean getSuccess() {
        return success;
    }
}
