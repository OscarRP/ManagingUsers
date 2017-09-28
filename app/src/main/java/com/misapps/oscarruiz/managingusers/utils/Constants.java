package com.misapps.oscarruiz.managingusers.utils;

/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public class Constants {

    /**
     * Application states interface
     */
    public interface APLICATION_STATES {
        public static final int SPLASH_STATE = 0;
        public static final int HOME_STATE = SPLASH_STATE + 1;
        public static final int ADD_USER_STATE = HOME_STATE + 1;
        public static final int USER_DETAIL_STATE = ADD_USER_STATE + 1;
    }

    /**
     * Api URL
     */
    public static final String URL = "http://hello-world.innocv.com/api/user/";

    /**
     * Endpoints
     */
    public interface ENDPOINTS {
        public static final String GET_ALL = "getall";
        public static final String GET = "get";
        public static final String CREATE = "create";
        public static final String UPDATE = "update";
        public static final String REMOVE = "remove";
    }

    /**
     * Key to put user in bundle
     */
    public static final String USER_KEY = "user";

    /**
     * Key to put isEditing in bundle
     */
    public static final String IS_EDITING_KEY = "is_editing";

}
