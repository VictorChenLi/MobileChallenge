package com.victorchen.mycurrency.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.victorchen.mycurrency.R;
import com.victorchen.mycurrency.network.api.fixerio.FixerApi;
import com.victorchen.mycurrency.sharedpreference.ExchangeRateSharedPreferences;
import com.victorchen.mycurrency.ui.binding.ExchangeRateFragmentBinding;
import com.victorchen.mycurrency.ui.component.CurrencyEditTextWatcher;
import com.victorchen.mycurrency.ui.dataBinding.DataBoundAdapter;
import com.victorchen.mycurrency.ui.model.ExchangeRate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExchangeRateFragment extends BaseFragment {
    private ExchangeRateFragmentBinding mBinding;
    private DataBoundAdapter<ExchangeRate> mAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private List<ExchangeRate> mExchangeRateList = new ArrayList<>();
    private String mBaseCurrency = "EUR";
    private BigDecimal mInputValue = new BigDecimal(1.0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventBusController().subscribeEventBus(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchRates(mBaseCurrency);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exchange_rate, container, false);
        initUI();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchRates(mBaseCurrency);
    }

    private void fetchRates(String base) {
        if (null != base) {
            // check shared preference first
            FixerApi.GetLatestRates.Response apiResponse = ExchangeRateSharedPreferences.getInstance().getLatestRateApiResponse(base);
            if (null != apiResponse) {
                onEventGetLatestRates(apiResponse);
                return;
            }
        }
        callFetchRateApi(base);
    }

    private void callFetchRateApi(String base){
        mActivity.showLoadingDialog();
        new FixerApi.GetLatestRates(base, null).postRequestAsync();
    }

    private void initUI() {
        // inital toolbar
        mBinding.toolbar.setTitle(R.string.app_name);

        mBinding.baseCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newBase = (String) adapterView.getItemAtPosition(i);
                if (mBaseCurrency.equals(newBase)) return;

                fetchRates(newBase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // set up the Grid RecyclerView
        mAdapter = new ConvertedCurrencyAdapter(mActivity, mExchangeRateList);
        mBinding.convertList.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mBinding.convertList.setAdapter(mAdapter);
        RecyclerView.ItemAnimator animator = mBinding.convertList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        mBinding.currencyInput.addTextChangedListener(new CurrencyEditTextWatcher(mBinding.currencyInput));
        mBinding.currencyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // every time when user change the input value
                // we update the convert value
                String strForDecimal = CurrencyEditTextWatcher.stripNonDigitsForDecimal(editable.toString());
                mInputValue = !TextUtils.isEmpty(strForDecimal) ? new BigDecimal(strForDecimal) : null;
                for (ExchangeRate rate : mExchangeRateList) {
                    rate.updateConvertValue(mInputValue);
                }
                mAdapter.setSource(mExchangeRateList);
            }
        });

        mBinding.swipeRefreshContainer.setColorSchemeResources(R.color.colorPrimary);
        mBinding.swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callFetchRateApi(mBaseCurrency);
            }
        });
    }

    protected void setupSpinnerAdapter(List<String> currencyList) {
        // setup currency spinner adapter
        mSpinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, currencyList);

        // Specify the layout to use when the list of choices appears
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mBinding.baseCurrency.setAdapter(mSpinnerAdapter);
        mBinding.baseCurrency.setSelection(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetLatestRates(FixerApi.GetLatestRates.Response response) {
        mActivity.dismissLoadingDialog();

        if (response.isSuccess()) {
            // save to shared preference first
            ExchangeRateSharedPreferences.getInstance().setLatestRateApiResponse(response.base, response).commitChangesAsync();

            List<String> currencyList = new ArrayList<>();
            mExchangeRateList.clear();
            mBaseCurrency = response.base;
            currencyList.add(response.base);
            for (Map.Entry<String, Float> rate : response.rates.entrySet()) {
                String currencyName = rate.getKey();
                Float currencyRate = rate.getValue();
                currencyList.add(currencyName);
                ExchangeRate newExchangeRate = new ExchangeRate(currencyName, currencyRate, response.date);
                newExchangeRate.updateConvertValue(mInputValue);
                mExchangeRateList.add(newExchangeRate);
            }
            setupSpinnerAdapter(currencyList);
            mAdapter.setSource(mExchangeRateList);
        } else {
            mActivity.showToast(response.status.message, Toast.LENGTH_LONG);
        }

        mBinding.swipeRefreshContainer.setRefreshing(false);
    }
}
