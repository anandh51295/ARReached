package com.tekbeast.reached;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("sms.php")
    Call<User> performSend(@Query("user") String user, @Query("pass") String pass, @Query("num") String num, @Query("msg") String msg);


}
