package com.misapps.oscarruiz.managingusers.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.activities.HomeActivity;
import com.misapps.oscarruiz.managingusers.controllers.DataController;
import com.misapps.oscarruiz.managingusers.controllers.NavigationController;
import com.misapps.oscarruiz.managingusers.dialogs.Dialogs;
import com.misapps.oscarruiz.managingusers.interfaces.AppInterfaces;
import com.misapps.oscarruiz.managingusers.models.User;
import com.misapps.oscarruiz.managingusers.utils.Constants;
import com.misapps.oscarruiz.managingusers.utils.Utils;

import retrofit2.Response;


public class AddFragment extends Fragment {

    /**
     * Boolean to know if is editing user
     */
    private boolean isEditing;

    /**
     * Dialog instance
     */
    private Dialogs dialog;

    /**
     * User Instance
     */
    private User user;

    /**
     * User birtdate with time
     */
    private String userBirthdate;

    /**
     * Utils instance
     */
    private Utils utils;

    /**
     * Navigation Controller
     */
    private NavigationController navigationController;

    /**
     * Data Controller
     */
    private DataController dataController;

    /**
     * Select date ImageButton
     */
    private ImageButton selectDateButton;

    /**
     * Select time ImageButton
     */
    private ImageButton selectTimeButton;

    /**
     * Accept button
     */
    private Button acceptButton;

    /**
     * Cancel Button
     */
    private Button cancelButton;

    /**
     * BirthTime Edit text
     */
    private EditText birthtimeET;

    /**
     * Birthdate Edit text
     */
    private EditText birthdateET;

    /**
     * Name edit text
     */
    private EditText nameET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        getViews(view);
        setListeners();
        getInfo();

        return view;
    }

    /**
     * Method to get views
     */
    private void getViews(View view) {
        selectDateButton = (ImageButton)view.findViewById(R.id.select_date_button);
        selectTimeButton = (ImageButton)view.findViewById(R.id.select_time_button);
        acceptButton = (Button)view.findViewById(R.id.accept_button);
        cancelButton = (Button)view.findViewById(R.id.cancel_button);
        birthdateET = (EditText)view.findViewById(R.id.birthdate_edit_text);
        birthtimeET = (EditText)view.findViewById(R.id.birth_time_edit_text);
        nameET = (EditText)view.findViewById(R.id.name_edit_text);
    }

    /**
     * Method to set listeners
     */
    private void setListeners() {
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.setDate(new AppInterfaces.ISetDate() {
                    @Override
                    public void setDate(String date) {
                        birthdateET.setText(date);
                    }
                });
            }
        });

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.setTime(new AppInterfaces.ISetTime() {
                    @Override
                    public void setTime(String time) {
                        birthtimeET.setText(time);
                    }
                });
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if all fields are filled
                if (checkFields()) {
                    //Create final birthdate
                    userBirthdate = birthdateET.getText().toString() + "T" + birthtimeET.getText().toString();
                    if (isEditing) {
                        //Update user
                        editUser();
                    } else {
                        //Save user
                        user = new User(nameET.getText().toString(), userBirthdate);
                        dialog.showLoadingDialog(getContext());
                        dataController.saveUser(user, new AppInterfaces.ISaveUser() {
                            @Override
                            public void saveUser(final Response response) {
                                dialog.hideLoadingDialog();
                                String result;
                                if (response.isSuccessful()) {
                                    result = getString(R.string.user_created);
                                } else {
                                    result = response.message();
                                }
                                //Show result
                                dialog.showResultDialog(getActivity(), result, response.isSuccessful(), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Hide dialog
                                        dialog.hideResultDialog();
                                        if (response.isSuccessful()) {
                                            //Go to home
                                            goHome();
                                        }
                                    }
                                }, new AppInterfaces.IBackButtonDialog() {
                                    @Override
                                    public void pressBack() {
                                        //Hide dialog
                                        dialog.hideResultDialog();
                                        if (response.isSuccessful()) {
                                            //Go to home
                                            goHome();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void error(String error) {
                                dialog.hideLoadingDialog();
                                dialog.showResultDialog(getActivity(), getString(R.string.user_creation_error), false, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.hideResultDialog();
                                    }
                                }, new AppInterfaces.IBackButtonDialog() {
                                    @Override
                                    public void pressBack() {
                                        dialog.hideResultDialog();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    //Show notification
                    Snackbar.make(view, getString(R.string.fill_all_fields), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to home
                goHome();
            }
        });
    }

    /**
     * Method to get info
     */
    private void getInfo() {
        //Init controllers
        navigationController = new NavigationController();
        dataController = new DataController();

        //Init utils
        utils = new Utils(getActivity());

        //Init dialog
        dialog = new Dialogs();

        //Hide fab
        ((HomeActivity)getActivity()).hideFAB();

        //Check if is editing or adding
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            isEditing = bundle.getBoolean(Constants.IS_EDITING_KEY);
            user = (User)bundle.getSerializable(Constants.USER_KEY);
        } else {
            isEditing = false;
        }
        if (isEditing) {
            setUserInfo();
        }
    }

    /**
     * Method to check if all fields are filled
     */
    private boolean checkFields() {
        if (nameET.getText().toString().isEmpty() || birthdateET.getText().toString().isEmpty() || birthtimeET.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to go home fragment
     */
    private void goHome() {
        //Set Toolbar title
        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.app_name));

        HomeFragment fragment = new HomeFragment();
        if (isEditing) {
            //send old user to be able to undo edit
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.USER_KEY, user);
            navigationController.changeFragment(getActivity(), fragment, bundle, Constants.APLICATION_STATES.HOME_STATE);
        } else {
            navigationController.changeFragment(getActivity(), fragment, null, Constants.APLICATION_STATES.HOME_STATE);
        }
    }

    /**
     * Method to set user info
     */
    private void setUserInfo() {
        nameET.setText(user.getName());
        birthdateET.setText(user.getBirthdate().substring(0, 10));
        birthtimeET.setText(user.getBirthdate().substring(11, 19));
    }

    /**
     * Method to edit user
     */
    private void editUser() {
        User userToSave = new User(user.getId(), nameET.getText().toString(), userBirthdate);
        dialog.showLoadingDialog(getContext());
        dataController.editUser(userToSave, new AppInterfaces.IEditUser() {
            @Override
            public void editUser(Response response) {
                dialog.hideLoadingDialog();
                String result;
                if (response.isSuccessful()) {
                    result = getString(R.string.user_created);
                } else {
                    result = response.message();
                }
                //Show result
                dialog.showResultDialog(getActivity(), result, response.isSuccessful(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Hide dialog
                        dialog.hideResultDialog();
                        //Go to home
                        goHome();
                    }
                }, new AppInterfaces.IBackButtonDialog() {
                    @Override
                    public void pressBack() {
                        //Hide dialog
                        dialog.hideResultDialog();
                        //Go to home
                        goHome();
                    }
                });
            }

            @Override
            public void error(String error) {
                dialog.hideLoadingDialog();
                dialog.showResultDialog(getActivity(), getString(R.string.user_creation_error), true, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Go to home
                        goHome();
                    }
                }, new AppInterfaces.IBackButtonDialog() {
                    @Override
                    public void pressBack() {
                        //Go to home
                        goHome();
                    }
                });
            }
        });
    }

}
