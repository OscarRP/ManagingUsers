package com.misapps.oscarruiz.managingusers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.misapps.oscarruiz.managingusers.R;
import com.misapps.oscarruiz.managingusers.models.User;

import java.util.ArrayList;

/**
 * Created by Oscar Ruiz on 25/09/2017.
 */

public class UsersListAdapter extends BaseAdapter {

    /**
     * Holder with all views
     */
    private ViewHolder viewHolder;

    /**
     * Layout inflater
     */
    private LayoutInflater inflater;

    /**
     * Users list
     */
    private ArrayList<User> usersList;

    /**
     * Context
     */
    private Context context;

    public UsersListAdapter(Context context, ArrayList<User> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int i) {
        return usersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return usersList.get(i).getId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            //init inflater
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate view
            view = inflater.inflate(R.layout.item_users_list, viewGroup, false);

            //initialize viewholder
            viewHolder = new ViewHolder();

            //getviews
            viewHolder.nameTV = view.findViewById(R.id.user_name);
            viewHolder.birthdateTV = view.findViewById(R.id.user_birthdate);

            //set tag
            view.setTag(viewHolder);
        } else {
            //get holder
            viewHolder = (ViewHolder)view.getTag();
        }

        //set info
        viewHolder.nameTV.setText(usersList.get(position).getName());
        //get only date without hour
        String birthdate = usersList.get(position).getBirthdate().substring(0, 10);
        viewHolder.birthdateTV.setText(birthdate);

        return view;
    }

    /**
     * View Holder class
     */
    private class ViewHolder{
        //set all view elements
        private TextView nameTV;
        private TextView birthdateTV;
    }
}
