package com.victorchen.mycurrency.network.api.fixerio.model;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FixerBaseResponseUnitTest {

    @Test
    public void test_getUpdateDate() throws ParseException {
        FixerBaseResponse apiResponse = new FixerBaseResponse();
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(2017, 3, 18, 0, 0, 0);
        apiResponse.base = "USD";
        apiResponse.date = "2017-04-18";
        assertThat(apiResponse.getUpdateDate().getTime(), equalTo(calendar.getTimeInMillis()));
    }
}
