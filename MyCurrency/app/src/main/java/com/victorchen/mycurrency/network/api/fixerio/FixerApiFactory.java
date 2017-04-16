package com.victorchen.mycurrency.network.api.fixerio;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.victorchen.mycurrency.network.api.BaseApiFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class FixerApiFactory extends BaseApiFactory {
    private static final String FIXER_IO_BASE_URL = "http://api.fixer.io/";

    private static FixerApiFactory sInstance = new FixerApiFactory();

    private FixerApiFactory() {
        //private constructor
    }

    public static FixerApiFactory getInstance() {
        return sInstance;
    }

    @NonNull
    @Override
    public List<Converter.Factory> getRetrofitConverters() {
        ArrayList<Converter.Factory> converters = new ArrayList<>();
        converters.add(GsonConverterFactory.create(new GsonBuilder().create()));
        return converters;
    }

    @Override
    public String getBaseUrl() {
        return FIXER_IO_BASE_URL;
    }
}
