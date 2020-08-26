package in.kdcash.Retrofit;

import java.util.List;

import in.kdcash.Model.Area;
import in.kdcash.Model.City;
import in.kdcash.Model.Country;
import in.kdcash.Model.State;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/api/General/country")
    Call<Country> getCountryList(@Query("pageNo") String pageNo,
                                 @Query("pageSize") String pageSize);


    @GET("/api/api/General/States")
    Call<State> getStateList();


    @GET("/api/api/General/Districts")
    Call<City> getCityList(@Query("pageNo") String pageNo,
                           @Query("pageSize") String pageSize,
                           @Query("state") String stateId);


    @GET("")
    Call<Area> getAreaList(@Query("") String cityId);
}
