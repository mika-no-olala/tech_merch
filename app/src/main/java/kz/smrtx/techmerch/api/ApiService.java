package kz.smrtx.techmerch.api;

import kz.smrtx.techmerch.items.reqres.JsonResponse;
import kz.smrtx.techmerch.items.reqres.LoginResponse;
import kz.smrtx.techmerch.items.reqres.StringResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("ExecQuery")
    Call<JsonResponse> getUser(@Query("token") String token, @Query("query") String query);

    @POST("ExecQuery")
    Call<StringResponse> query(@Query("token") String token, @Query("query") String query);

    @FormUrlEncoded
    @POST("Login")
    Call<LoginResponse> getToken(@Field("login") String login, @Field("password") String password);

}
