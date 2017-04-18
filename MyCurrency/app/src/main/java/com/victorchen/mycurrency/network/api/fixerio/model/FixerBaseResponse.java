package com.victorchen.mycurrency.network.api.fixerio.model;

import com.victorchen.mycurrency.network.api.BaseResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

// in our case, all the api response format is the same
// so we abstract this structure as our base response
public class FixerBaseResponse extends BaseResponse {
    public static final SimpleDateFormat serverSimpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public String base;
    public String date;
    public Map<String, Float> rates;

    public Date getUpdateDate() throws ParseException{
        return serverSimpleDateFormatter.parse(date);
    }
}
