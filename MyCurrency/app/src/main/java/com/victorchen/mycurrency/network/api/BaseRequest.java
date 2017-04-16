package com.victorchen.mycurrency.network.api;

/**
 * Base request class representing an API call to API end points that has {@link BaseStatus} object in the response
 *
 * @param <T> Response type
 */
public abstract class BaseRequest<T extends BaseResponse> extends RawBaseRequest<T, T> {
}
