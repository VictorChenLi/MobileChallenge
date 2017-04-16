package com.victorchen.mycurrency.network.retrofit;

import com.victorchen.mycurrency.network.api.BaseResponse;
import com.victorchen.mycurrency.network.api.BaseStatus;

import java.io.IOException;

import retrofit2.Response;

public interface IBusToRetro<T> extends IRetrofitDataObj {
    void postRequestSync();

    void postRequestAsync();

    Response execute() throws IOException;

    BaseResponse onSuccess(Response<T> response);

    BaseResponse onFailure(BaseStatus status);

    void postResponse(T response);
}
