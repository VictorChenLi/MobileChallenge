package com.victorchen.mycurrency.ui.component;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyEditTextWatcher implements TextWatcher {
    // round up the Decimal to two decimals
    public static DecimalFormat displayCurrencyFormat = new DecimalFormat("$###,###,###.##");
    public static DecimalFormat editCurrencyFormatAllowZeroPrefix = new DecimalFormat("$###,###,###.##");
    public static DecimalFormat editCurrencyFormat = new DecimalFormat("$###,###,###.##");

    static {
        displayCurrencyFormat.setMinimumIntegerDigits(1);
        displayCurrencyFormat.setMinimumFractionDigits(2);
        editCurrencyFormat.setRoundingMode(RoundingMode.FLOOR);
        editCurrencyFormat.setMinimumIntegerDigits(1);
        editCurrencyFormatAllowZeroPrefix.setRoundingMode(RoundingMode.FLOOR);
        editCurrencyFormatAllowZeroPrefix.setMinimumIntegerDigits(0);
    }

    private boolean deleteComma = false;
    private EditText mEditText;
    private boolean mDisabled;

    /**
     * Get EditText reference to change
     */
    public CurrencyEditTextWatcher(EditText editText) {
        mEditText = editText;
    }

    /**
     * Strips anything but digits and '.'
     */
    public static String stripNonDigitsForDecimal(String s) {
        if (s == null) return "";
        return s.replaceAll("[^\\d\\.]", "");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count == 1 && after == 0) {
            if (s.charAt(start) == ',')                                         // deleting comma
                deleteComma = true;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mDisabled) return;

        int selection = mEditText.getSelectionStart();
        int oldLength = s.toString().length();

        String suffix = "";
        if (s.toString().endsWith(".0")) {
            suffix = ".0";
        } else if (s.toString().endsWith(".")) {
            suffix = ".";
        }

        if (deleteComma) {
            deleteComma = false;
            mDisabled = true;
            s.delete(selection - 1, selection);
            mDisabled = false;
        }

        String formattedNumber;
        formattedNumber = stripNonDigitsForDecimal(s.toString());
        try {
            if (!TextUtils.isEmpty(formattedNumber)) {
                Double num = Double.parseDouble(formattedNumber);
                formattedNumber = formattedNumber.startsWith(".") ? editCurrencyFormatAllowZeroPrefix.format(num) + suffix :
                        editCurrencyFormat.format(num) + suffix;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //compute the offset difference
        int newLength = formattedNumber.length();
        int offsetDelta = newLength - oldLength;

        //need to disable this watcher to prevent recursive call to afterTextChanged;
        //Note that we can't call removeTextChangeListener because when textview has multiple textwatchers
        //attached, removing and adding this watcher back may change its index in the watcher list
        //and causing this watcher to be called again
        mDisabled = true;

        mEditText.setText(formattedNumber);
        int newSelection = selection + offsetDelta;
        mEditText.setSelection(newSelection > 0 ? selection + offsetDelta : 0);

        //reenable this watcher
        mDisabled = false;
    }


}
