package com.misapps.oscarruiz.managingusers.interfaces;

import com.misapps.oscarruiz.managingusers.models.User;

import java.util.ArrayList;

import retrofit2.Response;


/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public class AppInterfaces {

    /**
     * Interface to get all users
     */
    public interface IGetUsers {
        public abstract void getUsers(ArrayList<User> usersList);
        public abstract void error(String error);
    }

    /**
     * Interface to save user
     */
    public interface ISaveUser {
        public abstract void saveUser(Response response);
        public abstract void error (String error);
    }

    /**
     * Interface to edit user
     */
    public interface IEditUser {
        public abstract void editUser(Response response);
        public abstract void error (String error);
    }

    /**
     * Interface to delete user
     */
    public interface IDeleteUser {
        public abstract void deleteUser(Response response);
        public abstract void error(String error);
    }

    /**
     * Interface to set date
     */
    public interface ISetDate {
        public abstract void setDate(String date);
    }

    /**
     * Interface to set time
     */
    public interface ISetTime {
        public abstract void setTime(String time);
    }

    /**
     * Interface for dialog
     */
    public interface IBackButtonDialog{
        public abstract void pressBack();
    }
}
