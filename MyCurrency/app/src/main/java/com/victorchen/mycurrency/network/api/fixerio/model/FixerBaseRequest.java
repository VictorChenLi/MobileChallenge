package com.victorchen.mycurrency.network.api.fixerio.model;

import com.victorchen.mycurrency.network.api.BaseResponse;
import com.victorchen.mycurrency.network.api.BaseStatus;
import com.victorchen.mycurrency.network.api.RawBaseRequest;


public class FixerBaseRequest<T extends BaseResponse> extends RawBaseRequest<T, T> {
    @Override
    public int preExecuteValidation() {
        // in this case, we don't have any validation yet
        // so we set as success in default
        return BaseStatus.SUCCESS;
    }
}
