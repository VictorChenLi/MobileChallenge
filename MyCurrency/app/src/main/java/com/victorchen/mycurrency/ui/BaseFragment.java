package com.victorchen.mycurrency.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.victorchen.mycurrency.network.api.RawBaseRequest;
import com.victorchen.mycurrency.network.eventbus.EventBusController;
import com.victorchen.mycurrency.network.eventbus.IEventBusHandle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment implements IEventBusHandle {

    protected EventBusController mEventBusController;
    protected BaseActivity mActivity;

    @Override
    public EventBusController getEventBusController() {
        if (mEventBusController == null) {
            mEventBusController = new EventBusController() {
                @Override
                public Object getSubscriber() {
                    return BaseFragment.this;
                }
            };
        }
        return mEventBusController;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEventBusController != null) {
            mEventBusController.subscribeEventBus(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventFixerApiRequest(RawBaseRequest request) {
        request.postRequestSync();
    }
}
