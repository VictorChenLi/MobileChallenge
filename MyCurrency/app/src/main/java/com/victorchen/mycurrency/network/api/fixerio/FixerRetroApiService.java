package com.victorchen.mycurrency.network.api.fixerio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FixerRetroApiService {
    @GET("latest")
    Call<FixerApi.GetLatestRates.Response> getLatestRates(@Query("base") String baseCurrencyName,
                                                          @Query("symbols") String convertTo);
}
