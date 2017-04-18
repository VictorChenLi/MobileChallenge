package com.victorchen.mycurrency.sharedpreference;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victorchen.mycurrency.MyCurrencyApp;
import com.victorchen.mycurrency.network.api.fixerio.FixerApi;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ExchangeRateSharedPreferences extends BaseSharedPreferences {
    private static final String EXCHANGE_RATE_LIST = "exchange_rate_list";
    private static ExchangeRateSharedPreferences sInstance;

    private ExchangeRateSharedPreferences(Context context) {
        super(context);
    }

    /**
     * @return the singleton instance of SharedPreference
     */
    public static synchronized ExchangeRateSharedPreferences getInstance() {
        if (sInstance == null) {
            sInstance = new ExchangeRateSharedPreferences(MyCurrencyApp.App);
        }
        return sInstance;
    }

    public static long getElapsedTime(Date start, Date upto) {
        Calendar uptoDate = Calendar.getInstance();
        uptoDate.setTimeInMillis(upto.getTime());

        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(start.getTime());

        return uptoDate.getTimeInMillis() - startDate.getTimeInMillis();
    }

    /**********************************************************************************
     * Instance methods
     */
    public FixerApi.GetLatestRates.Response getLatestRateApiResponse(String base) {
        if (null == base) return null;

        String ratesJson = getStringByKey(base);
        if (TextUtils.isEmpty(ratesJson)) return null;

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<FixerApi.GetLatestRates.Response>() {
            }.getType();
            FixerApi.GetLatestRates.Response apiResponse = gson.fromJson(ratesJson, type);
            // since the rates in the api is updated daily
            // so if api response saved more than one day
            if (getElapsedTime(apiResponse.getUpdateDate(), new Date()) > TimeUnit.DAYS.toMillis(1)) {
                // we remove it from shared preference
                setByKey(base, null);
                return null;
            } else {
                return apiResponse;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public ExchangeRateSharedPreferences setLatestRateApiResponse(String base, FixerApi.GetLatestRates.Response apiResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<FixerApi.GetLatestRates.Response>() {
        }.getType();
        setByKey(base, gson.toJson(apiResponse, type));
        return this;
    }
}
