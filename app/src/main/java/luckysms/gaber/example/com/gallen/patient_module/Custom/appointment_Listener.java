package luckysms.gaber.example.com.gallen.patient_module.Custom;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface appointment_Listener {
    public void detect(int pos);
    public void cancel(int pos);
    public void details(int pos);
    public void map_location(int pos);
}
