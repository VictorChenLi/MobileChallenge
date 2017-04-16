package com.victorchen.mycurrency.network.api;

import com.victorchen.mycurrency.network.retrofit.Exclude;
import com.victorchen.mycurrency.network.retrofit.IRetrofitDataObj;

public class BaseResponse implements IRetrofitDataObj {
    public BaseStatus status;
    public Object rawData;
    public int httpStatusCode;

    /**
     * Target event receiving object id (OPTIONAL)
     */
    @Exclude
    private Integer receiverId;

    public BaseResponse() {
        this.status = new BaseStatus();
    }

    public boolean isSuccess() {
        return status.code == BaseStatus.SUCCESS;
    }

    public BaseResponse setTargetReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public boolean isTargetReceiverId(Integer receiverId) {
        return this.receiverId == null || this.receiverId.equals(receiverId);
    }
}
