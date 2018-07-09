package com.conceptioni.cafeapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.activity.MenuActivity;
import com.conceptioni.cafeapp.utils.SharedPrefs;
import com.conceptioni.cafeapp.utils.TextviewRegular;

public class FilterDialog {
    private Context context;
    private Dialog dialog;

    public FilterDialog(Context context){
        this.context = context;
    }
    public void ShowFilterDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_filter);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        ImageView ivclose = dialog.findViewById(R.id.ivclose);
        TextviewRegular vegtvr = dialog.findViewById(R.id.vegtvr);
        TextviewRegular nonvegtvr = dialog.findViewById(R.id.nonvegtvr);
        TextviewRegular alltvr = dialog.findViewById(R.id.alltvr);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        vegtvr.setOnClickListener(v -> {
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.FilterName,"Veg").apply();
            context.startActivity(new Intent(context,MenuActivity.class));
            ((Activity)context).finish();
            dialog.dismiss();
        });

        nonvegtvr.setOnClickListener(v -> {
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.FilterName,"Nonveg").apply();
            context.startActivity(new Intent(context,MenuActivity.class));
            ((Activity)context).finish();
            dialog.dismiss();
        });

        alltvr.setOnClickListener(v -> {
            SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.FilterName,"All").apply();
            context.startActivity(new Intent(context,MenuActivity.class));
            ((Activity)context).finish();
            dialog.dismiss();
        });

        ivclose.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
}
