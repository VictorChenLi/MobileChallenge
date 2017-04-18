package com.victorchen.mycurrency.network.api.fixerio;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.victorchen.mycurrency.MyCurrencyApp;
import com.victorchen.mycurrency.network.NetworkUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
public class FixerApiFactoryUnitTest {

    @Mock
    MyCurrencyApp app;

    @Mock
    PackageManager pkgMgr;

    @Mock
    PackageInfo pkgInfo;

    @PrepareForTest({NetworkUtil.class})
    @Test
    public void test_getApiInstance() {
        PowerMockito.mockStatic(NetworkUtil.class);
        MyCurrencyApp.App = app;
        assertApi(FixerRetroApiService.class);
    }

    private <T> void assertApi(Class<T> clazz) {
        T instance = FixerApiFactory.getInstance().getApiInstance(MyCurrencyApp.App, (clazz));
        assertThat(instance, notNullValue());
        assertThat(FixerApiFactory.getInstance().getApiInstance(MyCurrencyApp.App, (clazz)) == instance, equalTo(true));
    }
}
