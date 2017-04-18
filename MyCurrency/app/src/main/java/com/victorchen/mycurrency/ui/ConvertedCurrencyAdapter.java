package com.victorchen.mycurrency.ui;

import android.content.Context;

import com.victorchen.mycurrency.R;
import com.victorchen.mycurrency.ui.dataBinding.DataBoundAdapter;
import com.victorchen.mycurrency.ui.model.ExchangeRate;

import java.util.List;


public class ConvertedCurrencyAdapter extends DataBoundAdapter<ExchangeRate> {

    public ConvertedCurrencyAdapter(Context context, List<ExchangeRate> source) {
        super(source, R.layout.grid_item_currency, context);
        // enable stable id
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        // currency name hashcode will become stable id
        return getItem(position).currencyName.hashCode();
    }
}
