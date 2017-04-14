package com.victorchen.mycurrency.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.victorchen.mycurrency.R;

public class CurrencyCube extends LinearLayout {
    private Context mContext;
    private String mCurrencyName;
    private float mConvertValue;


    public CurrencyCube(Context context) {
        super(context);
        init(context, null);
    }

    public CurrencyCube(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurrencyCube(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_currency_cube, this, true);

        mContext = context;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CurrencyCube,
                    0, 0);

            try {
                mCurrencyName = a.getString(R.styleable.CurrencyCube_currencyName);
                mConvertValue = a.getFloat(R.styleable.CurrencyCube_convertValue, 0);
            } finally {
                a.recycle();
            }
        }
    }

}
