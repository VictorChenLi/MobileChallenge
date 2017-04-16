package com.victorchen.mycurrency.network.api.fixerio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.victorchen.mycurrency.network.api.RawBaseRequest;
import com.victorchen.mycurrency.util.SimpleLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * This service is a simple background component that lives throughout the life cycle
 * of the application. It will be used as the event bus relay center to asynchronously
 * handle Api requests
 * <p>
 * Decouple the network and ui layer
 */
public class FixerApiService extends Service {
    private boolean mStarted;

    public FixerApiService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStarted = true;
        SimpleLog.logD("even bus registered");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStarted = false;
        SimpleLog.logD("even bus unregistered");
        EventBus.getDefault().unregister(this);
    }

    public boolean isServiceStarted() {
        return mStarted;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventFixerApiRequest(RawBaseRequest request) {
        request.postRequestSync();
    }
}
