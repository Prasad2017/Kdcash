package in.kdcash.Retrofit;

import java.util.List;
import java.util.Map;

import in.kdcash.Model.Area;
import in.kdcash.Model.City;
import in.kdcash.Model.Country;
import in.kdcash.Model.LoginResponse;
import in.kdcash.Model.Pincode;
import in.kdcash.Model.PostResponse;
import in.kdcash.Model.ProfileResponse;
import in.kdcash.Model.State;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("/api/api/UserMaster/Mobileno")
    Call<LoginResponse> checkMobileNumber(@Query("Mobileno") String mobileNumber);


    @GET("/api/api/General/country")
    Call<Country> getCountryList(@Query("pageNo") String pageNo,
                                 @Query("pageSize") String pageSize);


    @GET("/api/api/General/States")
    Call<State> getStateList();


    @GET("/api/api/General/Districts")
    Call<City> getCityList(@Query("pageNo") String pageNo,
                           @Query("pageSize") String pageSize,
                           @Query("state") String stateId);


    @GET("/api/api/General/Pincode")
    Call<Area> getAreaList(@Query("pageNo") String pageNo,
                           @Query("pageSize") String pageSize,
                           @Query("Taluka") String cityName);


    @Multipart
    @POST("/api/api/BankTransfer")
    Call<PostResponse> transferMoney(@HeaderMap Map<String, String> headers,
                                     @Part MultipartBody.Part file,
                                     @Part("IdFrontSide") RequestBody imageFirst,
                                     @Part MultipartBody.Part file1,
                                     @Part("IdBackSide") RequestBody imageSecond,
                                     @Part MultipartBody.Part file2,
                                     @Part("CheckFrontPhoto") RequestBody imageCheque,
                                     @Part("AgentCode") RequestBody AgentCode,
                                     @Part("SenderName") RequestBody fullName,
                                     @Part("SenderMobileNo") RequestBody mobileNumber,
                                     @Part("SenderAddress") RequestBody address,
                                     @Part("RecAccountHolder") RequestBody receiverName,
                                     @Part("RecMobileNo") RequestBody receiverNumber,
                                     @Part("Amount") RequestBody amount,
                                     @Part("AccountNumber") RequestBody accountNumber,
                                     @Part("RecBankName") RequestBody bankName,
                                     @Part("RecBranchName") RequestBody branchName,
                                     @Part("RecIfSCCode") RequestBody ifscCode,
                                     @Part("RecCountry") RequestBody receiverCountry,
                                     @Part("Paymentmode") RequestBody paymentMode,
                                     @Part("Status") RequestBody status);


    @POST("/api/api/UserMaster/CreateAgent")
    Call<PostResponse> registration(@Body ProfileResponse profileResponse);
}
