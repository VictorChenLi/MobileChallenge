package com.victorchen.mycurrency.ui.model;

import java.math.BigDecimal;

public class ExchangeRate {
    public String currencyName;
    public BigDecimal convertValue;
    public Float convertRate = 1.0f;
    public String updateDateStr;

    public ExchangeRate(String currencyName, Float convertRate, String updateDateStr) {
        this.currencyName = currencyName;
        this.convertRate = convertRate;
        this.updateDateStr = updateDateStr;
    }

    public void updateConvertValue(BigDecimal convertFrom) {
        if (null == convertFrom) {
            this.convertValue = new BigDecimal(convertRate);
            return;
        } else {
            this.convertValue = convertFrom.multiply(new BigDecimal(convertRate));
        }
    }
}
