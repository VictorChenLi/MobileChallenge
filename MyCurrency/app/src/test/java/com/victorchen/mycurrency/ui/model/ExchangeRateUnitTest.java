package com.victorchen.mycurrency.ui.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExchangeRateUnitTest {

    @Test
    public void test_updateConvertValue() {
        assertThat(caculate(new BigDecimal("1000")).floatValue(), equalTo(1200f));
        assertThat(caculate(new BigDecimal("0")).floatValue(), equalTo(0f));
        assertThat(caculate(new BigDecimal("-1000")).floatValue(), equalTo(-1200f));
        assertThat(caculate(new BigDecimal("0.1")).setScale(2, RoundingMode.DOWN).floatValue(), equalTo(0.12f));
    }

    private BigDecimal caculate(BigDecimal convertFrom) {
        ExchangeRate exchangeRate = new ExchangeRate("USD", 1.2f, "2017-04-18");
        exchangeRate.updateConvertValue(convertFrom);
        return exchangeRate.convertValue;
    }
}
