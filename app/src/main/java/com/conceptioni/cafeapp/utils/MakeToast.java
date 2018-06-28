package com.conceptioni.cafeapp.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.conceptioni.cafeapp.CafeApp;

public class MakeToast  {

    public MakeToast(String messageToDisplay){
        Toast toast = Toast.makeText(CafeApp.getContext(),messageToDisplay, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public MakeToast(int stringResource){
        Toast toast = Toast.makeText(CafeApp.getContext(),stringResource, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
