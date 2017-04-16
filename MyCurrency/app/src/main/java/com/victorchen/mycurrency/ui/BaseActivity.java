package com.victorchen.mycurrency.ui;

import android.support.v7.app.AppCompatActivity;

import com.victorchen.mycurrency.network.eventbus.EventBusController;
import com.victorchen.mycurrency.network.eventbus.IEventBusHandle;

public class BaseActivity extends AppCompatActivity implements IEventBusHandle {
    protected EventBusController mEventBusController;

    @Override
    public EventBusController getEventBusController() {
        if (mEventBusController == null) {
            mEventBusController = new EventBusController() {
                @Override
                public Object getSubscriber() {
                    return BaseActivity.this;
                }
            };
        }
        return mEventBusController;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mEventBusController != null) {
            mEventBusController.subscribeEventBus(false);
        }
    }
}
