package com.victorchen.mycurrency.network.api.fixerio;

import com.google.common.base.Joiner;
import com.victorchen.mycurrency.MyCurrencyApp;
import com.victorchen.mycurrency.network.api.fixerio.model.FixerBaseRequest;
import com.victorchen.mycurrency.network.api.fixerio.model.FixerBaseResponse;

import java.util.List;

public class FixerApi {
    private static FixerRetroApiService service = FixerApiFactory.getInstance().getApiInstance(MyCurrencyApp.App, FixerRetroApiService.class);

    public static class GetLatestRates extends FixerBaseRequest<GetLatestRates.Response> {
        public static final String SEPARATOR = ",";

        public GetLatestRates(String base, List<String> convertCurrencyList) {
            setApiCall(service.getLatestRates(base, getJoinedCurrencyStr(convertCurrencyList)));
        }

        public static String getJoinedCurrencyStr(List<String> currencyList) {
            if (null == currencyList || currencyList.isEmpty()) return null;
            return Joiner.on(SEPARATOR).skipNulls().join(currencyList);
        }

        public static class Response extends FixerBaseResponse {

        }
    }
}
