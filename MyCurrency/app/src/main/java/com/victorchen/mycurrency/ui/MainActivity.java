package com.victorchen.mycurrency.ui;

import android.os.Bundle;

import com.victorchen.mycurrency.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMainFragment(new ExchangeRateFragment());
    }

    @Override
    public int getMainContainerID() {
        return R.id.main_container;
    }
}
