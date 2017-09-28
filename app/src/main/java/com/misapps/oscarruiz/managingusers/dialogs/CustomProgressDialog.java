package com.misapps.oscarruiz.managingusers.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.misapps.oscarruiz.managingusers.R;


/**
 * Created by Oscar Ruiz on 11/08/2017.
 */

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(@NonNull Context context, boolean isResult) {
        super(context, R.style.customDialog);

        if(isResult){
            //set content view
            setContentView(R.layout.result_dialog);
        }else {
            //set content view
            setContentView(R.layout.loading_dialog);
        }
    }
}
