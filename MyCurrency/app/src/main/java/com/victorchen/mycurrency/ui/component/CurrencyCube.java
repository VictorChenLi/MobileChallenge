package com.victorchen.mycurrency.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.common.base.Strings;
import com.victorchen.mycurrency.R;
import com.victorchen.mycurrency.ui.binding.ViewCurrencyCubeBinding;

public class CurrencyCube extends LinearLayout {
    private Context mContext;
    private ViewCurrencyCubeBinding mBinding;
    private String mCurrencyName;
    private float mConvertValue;
    private String mLastUpdateDateStr;


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
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_currency_cube, this, true);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CurrencyCube,
                    0, 0);

            try {
                mCurrencyName = a.getString(R.styleable.CurrencyCube_currencyName);
                mConvertValue = a.getFloat(R.styleable.CurrencyCube_convertValue, 0);
                mLastUpdateDateStr = a.getString(R.styleable.CurrencyCube_lastUpdateDateStr);
            } finally {
                a.recycle();
            }
        }
        setCurrencyName(mCurrencyName);
        setConvertValue(mConvertValue);
        setLastUpdateDateStr(mLastUpdateDateStr);
    }

    public void setCurrencyName(String currencyName) {
        if (Strings.nullToEmpty(mCurrencyName).equals(currencyName)) return;

        mCurrencyName = currencyName;
        mBinding.setCurrencyName(mCurrencyName);
    }

    public void setConvertValue(float convertValue) {
        if (convertValue == mConvertValue) return;

        mConvertValue = convertValue;
        mBinding.setConvertValue(mConvertValue);
    }

    public void setLastUpdateDateStr(String lastUpdateDateStr) {
        if (Strings.nullToEmpty(mLastUpdateDateStr).equals(lastUpdateDateStr)) return;

        mLastUpdateDateStr = lastUpdateDateStr;
        mBinding.setUpdateDate(mLastUpdateDateStr);
    }

}
