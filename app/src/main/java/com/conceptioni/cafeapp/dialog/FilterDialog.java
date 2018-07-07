package com.conceptioni.cafeapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.conceptioni.cafeapp.R;

public class FilterDialog {
    Context context;
    Dialog dialog;

    public FilterDialog(Context context){
        this.context = context;
    }
    public void ShowFilterDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_filter);
        ImageView ivclose = dialog.findViewById(R.id.ivclose);

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
