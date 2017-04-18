package com.victorchen.mycurrency.network.api.fixerio;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FixerApiUnitTest {
    @Test
    public void test_getJoinedCurrencyStr() {
        List<String> list1 = new ArrayList<>();
        list1.add("USD");
        list1.add("EUR");
        list1.add("CAD");
        assertThat(FixerApi.GetLatestRates.getJoinedCurrencyStr(list1),equalTo("USD,EUR,CAD"));

        List<String> list2 = new ArrayList<>();
        list2.add("CAD");
        assertThat(FixerApi.GetLatestRates.getJoinedCurrencyStr(list2),equalTo("CAD"));

        List<String> list3 = new ArrayList<>();
        assertThat(FixerApi.GetLatestRates.getJoinedCurrencyStr(list3),equalTo(null));

        List<String> list4 = null;
        assertThat(FixerApi.GetLatestRates.getJoinedCurrencyStr(list4),equalTo(null));
    }
}
