package omninos.com.dynailsty.Retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import omninos.com.dynailsty.model.GetServiceListModel;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.model.MyAppointmentModel;
import omninos.com.dynailsty.model.PaypalTokenModel;
import omninos.com.dynailsty.model.StaffDetailModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    @FormUrlEncoded
    @POST("userRegister")
    Call<LoginRegisterModel> UserRegister(@Field("name") String name,
                                          @Field("email") String email,
                                          @Field("phone") String phone,
                                          @Field("password") String password,
                                          @Field("device_type") String device_type,
                                          @Field("reg_id") String reg_id);

    @FormUrlEncoded
    @POST("matchVerificationToken")
    Call<LoginRegisterModel> MatchVerification(@Field("id") String id,
                                               @Field("token") String token);


    @FormUrlEncoded
    @POST("resendVerificationToken")
    Call<LoginRegisterModel> ResendVerification(@Field("id") String id);

    @FormUrlEncoded
    @POST("userLogin")
    Call<LoginRegisterModel> UserLogin(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("reg_id") String reg_id,
                                       @Field("device_type") String device_type);

    @GET("getServiceAndCategory")
    Call<GetServiceListModel> getServices();

    @FormUrlEncoded
    @POST("userBooking")
    Call<Map> userbooking(@Field("userId") String userId,
                          @Field("serviceId") String serviceId,
                          @Field("bookingDate") String bookingDate,
                          @Field("bookingTime") String bookingTime,
                          @Field("paymentStatus") String paymentStatus,
                          @Field("amount") String amount,
                          @Field("barber_id") String barber_id);

    @FormUrlEncoded
    @POST("myAppointments")
    Call<MyAppointmentModel> getAppointmentList(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("getStaffDetails")
    Call<StaffDetailModel> getStaffDetail(@Field("apointmentDate") String apointmentDate);


    @FormUrlEncoded
    @POST("checkSocialId")
    Call<LoginRegisterModel> socailIdCheck(@Field("social_id") String social_id);

    @FormUrlEncoded
    @POST("UserSocialLogin")
    Call<LoginRegisterModel> socialLoginAPI(@Field("social_id") String social_id,
                                            @Field("email") String email,
                                            @Field("phone") String phone,
                                            @Field("name") String name,
                                            @Field("reg_id") String reg_id,
                                            @Field("image") String image,
                                            @Field("device_type") String device_type,
                                            @Field("login_type") String login_type);


    @Multipart
    @POST("userUpdateProfile")
    Call<LoginRegisterModel> updateProfile(@Part("name") RequestBody name,
                                           @Part("phone") RequestBody phone,
                                           @Part("userId") RequestBody userId,
                                           @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("changePassword")
    Call<Map> UpdatePass(@Field("userId") String userId,
                         @Field("old_password") String old_pass,
                         @Field("new_password") String new_password);


    //payment
    @FormUrlEncoded
    @POST("userPaymentAcceptance")
    Call<Map> userPaymentAcceptance(@Field("paymentMethod") String paymentMethod,
                                    @Field("userId") String userId,
                                    @Field("barber_id") String barber_id,
                                    @Field("serviceId") String serviceId,
                                    @Field("bookingDate") String bookingDate,
                                    @Field("bookingTime") String bookingTime,
                                    @Field("amount") String amount,
                                    @Field("token") String token);

    @GET("genrateToken")
    Call<PaypalTokenModel> getToken();
}



