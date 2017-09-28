package com.misapps.oscarruiz.managingusers.interfaces;

import com.misapps.oscarruiz.managingusers.models.User;
import com.misapps.oscarruiz.managingusers.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public interface ApiEndpoints {

    //list all users
    @GET(Constants.ENDPOINTS.GET_ALL)
    Call<ArrayList<User>> getUsersList();

    @GET(Constants.ENDPOINTS.GET+"/{id}")
    Call<User> getUser(@Path("id") int userId);

    //Create user
    @POST(Constants.ENDPOINTS.CREATE)
    Call<User> createUser(@Body User user);

    @POST(Constants.ENDPOINTS.UPDATE)
    Call<User> updateUser(@Body User user);

    //Delete user
    @GET(Constants.ENDPOINTS.REMOVE+"/{id}")
    Call<Void> deleteUser(@Path("id") int userID);
}
