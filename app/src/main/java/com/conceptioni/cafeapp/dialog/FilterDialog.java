package com.conceptioni.cafeapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
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
