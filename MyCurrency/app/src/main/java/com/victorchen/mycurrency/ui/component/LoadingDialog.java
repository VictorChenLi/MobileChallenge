package com.victorchen.mycurrency.ui.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.victorchen.mycurrency.R;

public class LoadingDialog extends Dialog {

    private LoadingDialog(Context context) {
        super(context);
    }

    public static LoadingDialog newInstance(Context context) {
        LoadingDialog dialog = new LoadingDialog(context);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.loading_dialog);

    }

}
