package com.victorchen.mycurrency.network.api;

import com.google.gson.JsonParseException;
import com.victorchen.mycurrency.network.retrofit.Exclude;
import com.victorchen.mycurrency.network.retrofit.IBusToRetro;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Base request class representing an API call to API end points
 * <p>
 * Implementation: Subclassing the class and override {@code preExecuteValidation}
 * method for last check before executing the request, return {@link BaseStatus#SUCCESS} to authorize
 * the request to be executed, return an error status such as {@link BaseStatus#SESSION_EXPIRED} to
 * prevent the request from executing and invoke error callback.
 * <p>
 * Call {@code setApiCall} in constructor to let the request know what call to make.
 * The api call is a {@link retrofit2.Call} object that you create using
 * {@link retrofit2.Retrofit#create(Class)} method.
 *
 * @param <T> Response type
 * @param <R> Retrofit Response type
 */
public abstract class RawBaseRequest<T extends BaseResponse, R> implements IBusToRetro {

    //Retrofit Call object that must be instantiated in constructor
    @Exclude
    protected Call<R> call;
    //Response object
    @Exclude
    protected BaseResponse response;
    //response class
    @Exclude
    protected Class<?> responseClass;
    /**
     * OPTIONAL: Class id of receiver, used to target event receiving class
     * to prevent UI receiving unintentional event
     */
    @Exclude
    private Integer receiverId;

    /**
     * Subclass must call setApiCall in the constructor to set the Retrofit Call object
     */
    public RawBaseRequest() {
        //recreate a default response object
        try {
            responseClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            response = (BaseResponse) responseClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setApiCall(Call<R> call) {
        this.call = call;
    }

    /**
     * Last check before executing the request. Override this method to do validation
     * check, for example session validity.
     *
     * @return {@link BaseStatus#SUCCESS} if it's okay to execute this request, or any other
     * error status if not, the request will then fail with the error status.
     */
    public abstract int preExecuteValidation();

    @Override
    public Response execute() throws IOException {
        if (call == null) throw new IllegalArgumentException("call not initialized!");
        return call.execute();
    }

    @Override
    public void postRequestAsync() {
        EventBus.getDefault().post(this);
    }

    @Override
    public void postRequestSync() {
        try {
            //do validation before executing the request
            int preExecuteStatus = preExecuteValidation();

            if (preExecuteStatus != BaseStatus.SUCCESS) {
                BaseStatus status = new BaseStatus(preExecuteStatus, null, null);
                response = onFailure(status);
            } else {
                response.status.code = BaseStatus.SENDING;

                //Blocking call
                Response retroResponse = execute();

                if (retroResponse.isSuccessful()) {
                    response = onSuccess(retroResponse);
                } else {
                    Object baseResponse;
                    try {
                        //try parse the error body as BaseStatus
                        baseResponse = BaseApiFactory.getResponseConverter(responseClass).convert(retroResponse.errorBody());
                    } catch (Exception e) {
                        //if not able to parse error as standard BaseStatus object, parse it as plain string
                        baseResponse = retroResponse.errorBody() != null ? retroResponse.errorBody().toString() : "";
                    }
                    if (baseResponse instanceof BaseResponse) {
                        response = onFailure(((BaseResponse) baseResponse).status);
                    } else {
                        response = onFailure(new BaseStatus(BaseStatus.NETWORK, null, baseResponse.toString()));
                    }
                    response.status.code = BaseStatus.FAIL;
                }
                response.httpStatusCode = retroResponse.code();
            }

        } catch (JsonParseException e) {
            BaseStatus status = new BaseStatus(BaseStatus.PARSE_ERROR, e.getMessage(), e.getLocalizedMessage());
            response = onFailure(status);
        } catch (IOException e) {
            // handle the IOException
            e.printStackTrace();
            BaseStatus status = new BaseStatus(BaseStatus.NETWORK, e.getMessage(), e.getLocalizedMessage());
            response = onFailure(status);
        } catch (Exception e) {
            // handle the General Exception
            e.printStackTrace();
            BaseStatus status = new BaseStatus(BaseStatus.GENERAL, e.getMessage(), e.getLocalizedMessage());
            response = onFailure(status);
        }

        // post the event to notify UI
        postResponse(response);
    }


    // If the api don't have standard restful response,
    // then we need to override post event method at specific request class
    @Override
    public void postResponse(Object response) {
        // if request has a target receiver, pass it to response
        if (receiverId != null && response instanceof BaseResponse) {
            ((BaseResponse) response).setTargetReceiverId(receiverId);
        }
        // cast the response to specific response type
        EventBus.getDefault().post(response);
    }

    // If response body is BaseResponse return itself,
    // if not, set response body to rawData.
    @Override
    public BaseResponse onSuccess(Response response) {
        if (response.body() instanceof BaseResponse) {
            return (BaseResponse) response.body();
        } else {
            BaseResponse r = this.response;
            r.rawData = response.body();
            r.status.code = BaseStatus.SUCCESS;
            return r;
        }
    }

    // a general onFailure handler, return a specific response.
    @Override
    public BaseResponse onFailure(BaseStatus status) {
        response.status = status;
        return response;
    }

    /**
     * Set the receiving UI object Id
     */
    public RawBaseRequest setTargetReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
        return this;
    }

}
