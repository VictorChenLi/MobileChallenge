package com.victorchen.mycurrency.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.victorchen.mycurrency.network.api.RawBaseRequest;
import com.victorchen.mycurrency.ui.component.LoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {
    protected LoadingDialog mLoadingDialog;

    public abstract int getMainContainerID();

    public void showMainFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(getMainContainerID(), fragment);
        // Complete the changes added above
        ft.commit();
    }

    public void showLoadingDialog() {
        if(this.mLoadingDialog != null && !this.mLoadingDialog.isShowing()) {
            this.mLoadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if(this.mLoadingDialog != null && this.mLoadingDialog.isShowing()) {
            this.mLoadingDialog.dismiss();
        }
    }
}
