package luckysms.gaber.example.com.gallen.patient_module.Custom;

import java.net.InetAddress;

public class checkconnectivity {
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
