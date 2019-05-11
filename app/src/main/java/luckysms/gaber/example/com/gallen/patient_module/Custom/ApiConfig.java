package luckysms.gaber.example.com.gallen.patient_module.Custom;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiConfig {
    static String BASE_URL="http://microtec1.egytag.com/api/";
    @Multipart
    @POST
    Call<Object> uploadImage(@Url String url, @Part MultipartBody.Part image);
}
