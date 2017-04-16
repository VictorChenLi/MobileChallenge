package com.victorchen.mycurrency.network.api.fixerio.model;

import com.victorchen.mycurrency.network.api.BaseResponse;

import java.util.Map;

// in our case, all the api response format is the same
// so we abstract this structure as our base response
public class FixerBaseResponse extends BaseResponse {
    public String base;
    public String date;
    public Map<String, Float> rates;
}
