package com.misapps.oscarruiz.managingusers.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.activities.HomeActivity;
import com.misapps.oscarruiz.managingusers.controllers.NavigationController;
import com.misapps.oscarruiz.managingusers.models.User;
import com.misapps.oscarruiz.managingusers.utils.Constants;

public class DetailFragment extends Fragment {

    /**
     * Navigation Controller
     */
    private NavigationController navigationController;

    /**
     * Accept Button
     */
    private Button acceptButton;

    /**
     * User Id text view
     */
    private EditText idET;

    /**
     * User Name text view
     */
    private EditText userNameET;

    /**
     * User Birthdate text view
     */
    private EditText userBirthdateET;

    /**
     * User
     */
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        getViews(view);
        setListeners();
        setInfo();

        return view;
    }

    /**
     * Method to get views
     */
    private void getViews(View view) {
        idET = (EditText)view.findViewById(R.id.id_text_view);
        userNameET = (EditText)view.findViewById(R.id.name_edit_text);
        userBirthdateET = (EditText)view.findViewById(R.id.birthdate_edit_text);
        acceptButton = (Button)view.findViewById(R.id.accept_button);
    }

    /**
     * Method to set listeners
     */
    private void setListeners() {
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set Toolbar title
                ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.app_name));
                //Go to home fragment
                HomeFragment homeFragment = new HomeFragment();
                navigationController.changeFragment(getActivity(), homeFragment, null, Constants.APLICATION_STATES.HOME_STATE);
            }
        });
    }

    /**
     * Method to set info
     */
    private void setInfo() {
        //Init navigation controller
        navigationController = new NavigationController();

        //Hide floating action button
        ((HomeActivity)getActivity()).hideFAB();

        //Get data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable(Constants.USER_KEY);
        }
        if (user != null) {
            idET.setText(String.valueOf(user.getId()));
            userNameET.setText(user.getName());
            userBirthdateET.setText(user.getBirthdate().replace("T", " a las "));
        }
    }
}
