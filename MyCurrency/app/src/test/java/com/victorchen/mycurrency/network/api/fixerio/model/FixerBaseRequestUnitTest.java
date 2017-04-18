package com.victorchen.mycurrency.network.api.fixerio.model;

import com.victorchen.mycurrency.network.api.BaseStatus;
import com.victorchen.mycurrency.network.api.fixerio.FixerApi;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FixerBaseRequestUnitTest {
    @Test
    public void test_preExecuteValidation(){
        FixerBaseRequest<FixerApi.GetLatestRates.Response> request = new FixerBaseRequest();
        assertThat(request.preExecuteValidation(),equalTo(BaseStatus.SUCCESS));
    }
}
