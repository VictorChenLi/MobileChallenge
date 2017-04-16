package com.victorchen.mycurrency.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.victorchen.mycurrency.R;
import com.victorchen.mycurrency.network.api.fixerio.FixerApi;
import com.victorchen.mycurrency.ui.binding.ActivityMainBinding;
import com.victorchen.mycurrency.ui.component.ExchangeRate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mBinding;
    private ArrayAdapter<String> mSpinnerAdapter;
    private List<ExchangeRate> mExchangeRateList = new ArrayList<>();
    private String mBaseCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getEventBusController().subscribeEventBus(true);
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //fetch all currency
        fetchRates(null);
    }

    private void fetchRates(String base) {
        new FixerApi.GetLatestRates(base, null).postRequestAsync();
    }

    private void initUI() {
        // inital toolbar
        setSupportActionBar(mBinding.toolbar);

        // setup fab click listener
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchRates(null);
            }
        });

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
    }

    protected void setupSpinnerAdapter(List<String> currencyList) {
        // setup currency spinner adapter
        mSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);

        // Specify the layout to use when the list of choices appears
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mBinding.baseCurrency.setAdapter(mSpinnerAdapter);
        mBinding.baseCurrency.setSelection(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetLatestRates(FixerApi.GetLatestRates.Response response) throws Exception {
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
