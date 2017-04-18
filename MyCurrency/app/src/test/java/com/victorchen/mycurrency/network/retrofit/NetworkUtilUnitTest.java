package com.victorchen.mycurrency.network.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.victorchen.mycurrency.network.NetworkUtil;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NetworkUtilUnitTest {

    Context ctx = null;
    NetworkInfo[] info = null;
    ConnectivityManager conManager = null;

    private void mockObjects()
    {
        conManager = Mockito.mock(ConnectivityManager.class);
        ctx = Mockito.mock(Context.class);
    }

    @SuppressWarnings("deprecation")
    private void mockConnectivity()
    {
        Mockito.when(ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(conManager);
        info = new NetworkInfo[1];
        info[0] = Mockito.mock(NetworkInfo.class);
        Mockito.when(conManager.getAllNetworkInfo()).thenReturn(info);

    }

    private void mockConnectivityConnecting()
    {
        mockConnectivity();
        Mockito.when(info[0].getState()).thenReturn(NetworkInfo.State.CONNECTING);
    }

    private void mockConnectivityConnected()
    {
        mockConnectivity();
        Mockito.when(info[0].getState()).thenReturn(NetworkInfo.State.CONNECTED);
    }

    private void mockConnectivityNotConnected()
    {
        mockConnectivity();
        Mockito.when(info[0].getState()).thenReturn(NetworkInfo.State.UNKNOWN);
    }


    @Test
    public void test_isConnectingToInternet_false()
    {
        //Arrange
        mockObjects();
        Mockito.when(ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);

        //Act
        boolean bIsConnected = NetworkUtil.isConnectingToInternet(ctx);

        //Assume
        assertThat(bIsConnected, equalTo(false));
    }

    @Test
    public void test_isConnectingToInternet_false_unknown()
    {
        //Arrange
        mockObjects();
        mockConnectivityNotConnected();

        //Act
        boolean bIsConnected = NetworkUtil.isConnectingToInternet(ctx);

        //Assume
        assertThat(bIsConnected, equalTo(false));
    }

    @Test
    public void test_isConnectingToInternet_true_connecting()
    {
        //Arrange
        mockObjects();
        mockConnectivityConnecting();

        //Act
        boolean bIsConnected = NetworkUtil.isConnectingToInternet(ctx);

        //Assume
        assertThat(bIsConnected, equalTo(true));
    }

    @Test
    public void test_isConnectingToInternet_true_connected()
    {
        //Arrange
        mockObjects();
        mockConnectivityConnected();

        //Act
        boolean bIsConnected = NetworkUtil.isConnectingToInternet(ctx);

        //Assume
        assertThat(bIsConnected, equalTo(true));
    }

    @Test
    public void test_normalizeUrl() {
        String url1 = null;
        String url2 = "";
        String url3 = "https://www.login.ca";
        String url4 = "http://wad.adfa.afadsf/";
        String url5 = "HTTP://GOOGLE.CA";
        String url6 = "  http://qwer.afa.qeq/  ";
        String url7 = "http://test.login.com:8000/";
        String url8 = "http://test.login.com:8000/test.php?Val1=1234";
        String url9 = "http://test.login.com:8000/TEST.php?Val1=1234";

        assertThat(NetworkUtil.normalizeUrl(url1), equalTo(""));//test null string
        assertThat(NetworkUtil.normalizeUrl(url2), equalTo(""));//test empty string
        assertThat(NetworkUtil.normalizeUrl(url3), equalTo("https://www.login.ca"));//test appending '/'
        assertThat(NetworkUtil.normalizeUrl(url4), equalTo("http://wad.adfa.afadsf/"));//test normal case
        assertThat(NetworkUtil.normalizeUrl(url5), equalTo("http://google.ca"));//test lowercasing
        assertThat(NetworkUtil.normalizeUrl(url6), equalTo("http://qwer.afa.qeq/"));//test trimming
        assertThat(NetworkUtil.normalizeUrl(url7), equalTo("http://test.login.com:8000/"));//test port
        assertThat(NetworkUtil.normalizeUrl(url8), equalTo("http://test.login.com:8000/test.php?Val1=1234"));//test value name with capital letter
        assertThat(NetworkUtil.normalizeUrl(url9), equalTo("http://test.login.com:8000/TEST.php?Val1=1234"));//test page name with capital letter
    }

    @Test
    public void test_getNormalizedBaseUrl() {
        String url1 = null;
        String url2 = "";
        String url3 = "https://www.login.ca";
        String url4 = "http://wad.adfa.afadsf/";
        String url5 = "HTTP://GOOGLE.CA";
        String url6 = "  http://qwer.afa.qeq/  ";
        String url7 = "HTTP://GOOGLE.CA/PCCMobile";

        assertThat(NetworkUtil.getNormalizedBaseUrl(url1), equalTo(""));//test null string
        assertThat(NetworkUtil.getNormalizedBaseUrl(url2), equalTo(""));//test empty string
        assertThat(NetworkUtil.getNormalizedBaseUrl(url3), equalTo("https://www.login.ca/"));//test appending '/'
        assertThat(NetworkUtil.getNormalizedBaseUrl(url4), equalTo("http://wad.adfa.afadsf/"));//test normal case
        assertThat(NetworkUtil.getNormalizedBaseUrl(url5), equalTo("http://google.ca/"));//test lowercasing
        assertThat(NetworkUtil.getNormalizedBaseUrl(url6), equalTo("http://qwer.afa.qeq/"));//test trimming
        assertThat(NetworkUtil.getNormalizedBaseUrl(url7), equalTo("http://google.ca/PCCMobile/"));//test lowercasing only domain names
    }

    @Test
    public void test_instantiation() {
        NetworkUtil util = new NetworkUtil();
        assertThat(util, notNullValue());
    }

}
