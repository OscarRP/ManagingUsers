package com.misapps.oscarruiz.managingusers.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.activities.HomeActivity;
import com.misapps.oscarruiz.managingusers.adapters.UsersListAdapter;
import com.misapps.oscarruiz.managingusers.controllers.DataController;
import com.misapps.oscarruiz.managingusers.controllers.NavigationController;
import com.misapps.oscarruiz.managingusers.dialogs.Dialogs;
import com.misapps.oscarruiz.managingusers.interfaces.AppInterfaces;
import com.misapps.oscarruiz.managingusers.models.User;
import com.misapps.oscarruiz.managingusers.utils.Constants;
import com.misapps.oscarruiz.managingusers.utils.Utils;

import java.util.ArrayList;

import retrofit2.Response;


public class HomeFragment extends Fragment {

    /**
     * Menu item undo edit
     */
    private MenuItem undoEditing;

    /**
     * User edited with old data
     */
    private User user;

    /**
     * Var to know if an user has been edited as last action
     */
    private boolean userEdited;

    /**
     * Utils instance
     */
    Utils utils;

    /**
     * Filtered users list
     */
    private ArrayList<User> filteredUsers;

    /**
     * Search Edit Text
     */
    private EditText searchET;

    /**
     * Dialog instance
     */
    private Dialogs dialog;

    /**
     * Navigation controller
     */
    private NavigationController navigationController;

    /**
     * Swipe Menu Creator
     */
    private SwipeMenuCreator creator;

    /**
     * Users list adapter
     */
    private UsersListAdapter usersListAdapter;

    /**
     * Data controller
     */
    private DataController dataController;

    /**
     * Users list
     */
    private ArrayList<User> users;

    /**
     * Users List view
     */
    private SwipeMenuListView usersListView;

    /**
     * No users container
     */
    private LinearLayout noUsersContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getViews(view);
        setListeners();
        setInfo();

        return view;
    }

    /**
     * Method to get views
     */
    private void getViews(View view) {
        noUsersContainer = (LinearLayout)view.findViewById(R.id.no_users_container);
        usersListView = (SwipeMenuListView)view.findViewById(R.id.list_view);
        searchET = (EditText)view.findViewById(R.id.search_edit_text);
    }

    /**
     * Method to set listeners
     */
    private void setListeners() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //search coincidences
                String searchText = searchET.getText().toString().toLowerCase();
                filteredUsers = utils.searchUsers(users, searchText);

                //update listview
                usersListAdapter = new UsersListAdapter(getContext(), filteredUsers);
                usersListAdapter = new UsersListAdapter(getActivity(), filteredUsers);
                usersListView.setAdapter(usersListAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        usersListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                utils.hideKeyboard(getContext(), usersListView);
                switch (index) {
                    case 0:
                        //Edit user
                        //Set toolbar title
                        ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.edit_user));

                        //Change fragment
                        AddFragment addFragment = new AddFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.IS_EDITING_KEY, true);
                        bundle.putSerializable(Constants.USER_KEY, users.get(position));
                        navigationController.changeFragment(getActivity(), addFragment, bundle, Constants.APLICATION_STATES.ADD_USER_STATE);
                        break;
                    case 1:
                        //Delete user
                        dataController.deleteUser(users.get(position).getId(), new AppInterfaces.IDeleteUser() {
                            @Override
                            public void deleteUser(Response response) {
                                //Show result dialog
                                String result;
                                if (response.isSuccessful()) {
                                    result = getString(R.string.user_deleted);
                                } else {
                                    result = response.message();
                                }
                                //Show result
                                dialog.showResultDialog(getActivity(), result, response.isSuccessful(), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Hide dialog
                                        dialog.hideResultDialog();
                                        //Reload list
                                        getUsersList();
                                    }
                                }, new AppInterfaces.IBackButtonDialog() {
                                    @Override
                                    public void pressBack() {
                                        //Hide dialog
                                        dialog.hideResultDialog();
                                        //Reload list
                                        getUsersList();
                                    }
                                });
                            }

                            @Override
                            public void error(String error) {
                                //Show result dialog
                                dialog.showResultDialog(getActivity(), getString(R.string.error_deleting), false, new View.OnClickListener() {
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
                        break;
                }
                return false;
            }
        });
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Hide Keyboard
                utils.hideKeyboard(getContext(), usersListView);
                //Set Toolbar title
                ((HomeActivity)getActivity()).setToolbarTitle(getString(R.string.user_detail));
                //Go to user detail
                DetailFragment detailFragment = new DetailFragment();
                //Put selected user in bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.USER_KEY, users.get(i));
                navigationController.changeFragment(getActivity(), detailFragment, bundle, Constants.APLICATION_STATES.USER_DETAIL_STATE);
            }
        });
    }

    /**
     * Method to set info
     */
    private void setInfo() {
        setHasOptionsMenu(true);

        //Init utils
        utils = new Utils(getActivity());

        //Init data controller
        dataController = new DataController();

        //Init navigation controller
        navigationController = new NavigationController();

        //Show floating action button
        ((HomeActivity)getActivity()).showFAB();

        //Create Swipe Menu
        createSwipeMenu();

        //Set swipe direction
        usersListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        //Get users list
        getUsersList();

        //Check if an user was edited to enable undo
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            user = (User)bundle.getSerializable(Constants.USER_KEY);
            userEdited = true;
        } else {
            userEdited = false;
        }

        //Hide Keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        undoEditing = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, getString(R.string.undo_editing));
        undoEditing.setIcon(R.mipmap.undo_icon);
        if (userEdited) {
            undoEditing.setEnabled(true);
            getUsersList();
        } else {
            undoEditing.setEnabled(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            dataController.editUser(user, new AppInterfaces.IEditUser() {
                @Override
                public void editUser(final Response response) {
                    dialog.showResultDialog(getContext(), getString(R.string.changes_undo), true, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hideResultDialog();
                            //Reload list
                            getUsersList();
                            undoEditing.setEnabled(false);
                        }
                    }, new AppInterfaces.IBackButtonDialog() {
                        @Override
                        public void pressBack() {
                            //Reload list
                            dialog.showResultDialog(getContext(), response.message(), false, new View.OnClickListener() {
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

                @Override
                public void error(String error) {
                    Toast.makeText(getActivity(), getString(R.string.undo_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to get users list
     */
    private void getUsersList() {
        dialog.showLoadingDialog(getContext());
        dataController.getUsers(getContext(), new AppInterfaces.IGetUsers() {
            @Override
            public void getUsers(ArrayList<User> usersList) {
                dialog.hideLoadingDialog();
                users = usersList;
                if (users != null) {
                    usersListView.setMenuCreator(creator);
                    //Create adapter
                    usersListAdapter = new UsersListAdapter(getActivity(), users);
                    usersListView.setAdapter(usersListAdapter);
                }
            }
            @Override
            public void error(String error) {
                dialog.hideLoadingDialog();
                dialog.showResultDialog(getContext(), getString(R.string.error_loading_users), false, new View.OnClickListener() {
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

    /**
     * Method to create a swipe menu
     */
    private void createSwipeMenu() {
        //Create menu
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //Create open item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
                //Configure open item
                openItem.setBackground(R.color.grey);
                // set item width
                openItem.setWidth((int)(90*getContext().getResources().getDisplayMetrics().density+0.5f));
                // set item title
                openItem.setTitle(R.string.edit);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.colorPrimary);
                // set item width
                deleteItem.setWidth((int)(90*getContext().getResources().getDisplayMetrics().density+0.5f));
                // set a icon
                deleteItem.setIcon(R.mipmap.delete_icon);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
    }
}
