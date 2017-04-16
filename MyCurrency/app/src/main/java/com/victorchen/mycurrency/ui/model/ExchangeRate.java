package com.victorchen.mycurrency.ui.model;

public class ExchangeRate {
    public String currencyName;
    public Float convertValue;
    public String updateDateStr;

    public ExchangeRate(String currencyName, Float convertValue, String updateDateStr) {
        this.currencyName = currencyName;
        this.convertValue = convertValue;
        this.updateDateStr = updateDateStr;
    }
}
