package kz.smrtx.techmerch.api;

import io.reactivex.Observable;
import kz.smrtx.techmerch.items.reqres.JsonResponse;
import kz.smrtx.techmerch.items.reqres.LoginResponse;
import kz.smrtx.techmerch.items.reqres.StringResponse;
import kz.smrtx.techmerch.items.reqres.synctables.SyncTables;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("services-manager/api/ExecQuery")
    Call<JsonResponse> getUser(@Query("token") String token, @Query("query") String query);

    @POST("services-manager/api/ExecQuery")
    Call<StringResponse> query(@Query("token") String token, @Query("query") String query);

    @FormUrlEncoded
    @POST("services-manager/api/Login")
    Call<LoginResponse> getToken(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("services-sync-manager/getsynctables")
    Call<SyncTables> getSyncTables(@Field("USE_CODE") String usecode);

    @FormUrlEncoded
    @POST("services-sync-manager/getsyncdata")
    Observable<ResponseBody> getSyncData(@Field("MVL_VIEW_NAME") String viewName, @Field("USE_CODE") String useCode, @Field("MVL_REFERENCE") String reference);

}
