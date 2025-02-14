package com.example.ecommerce;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {



@GET("products")
    Call<List<ecomproducts>> get(@Query("fields") String fields);

@GET
    Call<List<ecomproducts>> getcat(@Url String url , @Query("fields") String fields);

@POST("products")
    Call<ecomproducts> post(@Field("id")int id, @Field("title") String title);
}
