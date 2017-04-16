package com.victorchen.mycurrency.network.api;

import com.victorchen.mycurrency.network.retrofit.IRetrofitDataObj;

/***
 * Base status class, extend and add extra status for specific case
 */
public class BaseStatus implements IRetrofitDataObj {
    //server status
    public static final int SUCCESS = 0;
    public static final int FAIL = -1;

    //local status
    public static final int NETWORK = 100;
    public static final int GENERAL = 101;
    public static final int SESSION_EXPIRED = 102;
    public static final int PENDING = 103;
    public static final int SENDING = 104;
    public static final int PARSE_ERROR = 105;

    public int code;
    public String message;
    public String messageDetails;

    public BaseStatus(int code, String message, String messageDetails) {
        this.code = code;
        this.message = message;
        this.messageDetails = messageDetails;
    }

    public BaseStatus() {
        this.code = PENDING;
    }

    @Override
    public String toString() {
        return "code:" + code + ", message:" + message + ", detail:" + messageDetails;
    }

    /**
     * @return true if this error status is generated locally
     */
    public boolean isLocalError() {
        return code >= NETWORK;
    }
}
