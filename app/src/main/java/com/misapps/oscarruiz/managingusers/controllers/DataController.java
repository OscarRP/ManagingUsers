package com.misapps.oscarruiz.managingusers.controllers;

import android.content.Context;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.interfaces.ApiEndpoints;
import com.misapps.oscarruiz.managingusers.interfaces.AppInterfaces;
import com.misapps.oscarruiz.managingusers.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public class DataController {

    /**
     * Method to get all users
     */
    public void getUsers(final Context context, final AppInterfaces.IGetUsers getUsersListener) {
        //create api connection
        ApiEndpoints apiInterfaces = ApiClient.getClient().create(ApiEndpoints.class);

        Call call = apiInterfaces.getUsersList();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    //Get users list
                    ArrayList<User> users = (ArrayList<User>) response.body();
                    getUsersListener.getUsers(users);
                } else {
                    //Show error message
                    getUsersListener.error(context.getString(R.string.error_loading_users));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getUsersListener.error(t.getMessage().toString());
            }
        });
    }

    /**
     * Method to save user
     */
    public void saveUser(User user, final AppInterfaces.ISaveUser saveUserListener) {
        //create api connection
        ApiEndpoints apiInterfaces = ApiClient.getClient().create(ApiEndpoints.class);

        Call call = apiInterfaces.createUser(user);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    saveUserListener.saveUser(response);
                } else {
                    //show error message
                    saveUserListener.saveUser(response);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                saveUserListener.error(t.getMessage());
            }
        });
    }

    /**
     * Method to edit user
     */
    public void editUser(User user, final AppInterfaces.IEditUser editUserListener) {
        //create api connection
        ApiEndpoints apiInterfaces = ApiClient.getClient().create(ApiEndpoints.class);

        Call call = apiInterfaces.updateUser(user);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    editUserListener.editUser(response);
                } else {
                    //show error message
                    editUserListener.editUser(response);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                editUserListener.error(t.getMessage());
            }
        });
    }

    /**
     * Method to delete user
     */
    public void deleteUser(int id, final AppInterfaces.IDeleteUser deleteListener) {
        //create api connection
        ApiEndpoints apiInterfaces = ApiClient.getClient().create(ApiEndpoints.class);

        Call call = apiInterfaces.deleteUser(id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    deleteListener.deleteUser(response);
                } else {
                    //show error message
                    deleteListener.deleteUser(response);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                deleteListener.error(t.getMessage());
            }
        });
    }
}
