package com.victorchen.mycurrency.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.victorchen.mycurrency.R;
import com.victorchen.mycurrency.network.api.fixerio.FixerApi;
import com.victorchen.mycurrency.ui.binding.ExchangeRateFragmentBinding;
import com.victorchen.mycurrency.ui.component.DataBoundAdapter;
import com.victorchen.mycurrency.ui.model.ExchangeRate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExchangeRateFragment extends BaseFragment {
    private ExchangeRateFragmentBinding mBinding;
    private DataBoundAdapter<ExchangeRate> mAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private List<ExchangeRate> mExchangeRateList = new ArrayList<>();
    private String mBaseCurrency;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventBusController().subscribeEventBus(true);
        setHasOptionsMenu(true);
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
        mAdapter = new DataBoundAdapter<>(mActivity, R.layout.grid_item_currency);
        mBinding.convertList.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mBinding.convertList.setAdapter(mAdapter);
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
            List<String> currencyList = new ArrayList<>();
            mExchangeRateList.clear();
            mBaseCurrency = response.base;
            currencyList.add(response.base);
            for (Map.Entry<String, Float> rate : response.rates.entrySet()) {
                currencyList.add(rate.getKey());
                mExchangeRateList.add(new ExchangeRate(rate.getKey(), rate.getValue(), response.date));
            }
            setupSpinnerAdapter(currencyList);
            mAdapter.setSource(mExchangeRateList);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //fetch all currency
            fetchRates(mBaseCurrency);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
