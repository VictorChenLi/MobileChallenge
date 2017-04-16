package com.victorchen.mycurrency.ui.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeRate {
    public static final SimpleDateFormat serverSimpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public String currencyName;
    public Float convertRate;
    public Date updateDate;

    public ExchangeRate(String currencyName, Float convertRate, String updateDate) throws Exception {
        this.currencyName = currencyName;
        this.convertRate = convertRate;
        this.updateDate = serverSimpleDateFormatter.parse(updateDate);
    }
}
