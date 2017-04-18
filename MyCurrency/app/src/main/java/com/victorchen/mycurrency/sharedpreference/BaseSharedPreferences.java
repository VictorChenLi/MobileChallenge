package com.victorchen.mycurrency.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Shared preferences with an optional LRU in-memory
 * cache. By default cache will be used
 */
public class BaseSharedPreferences {
    /**
     * Max number of entries the LRU cache keeps
     * Note: this cache is shared among all SharedPreferences
     */
    private static final int CACHE_SIZE = 50;
    /**
     * static cache of shared preference entries. It is essentially the same as LruCache except
     * that we allow null key or value
     */
    private static LinkedHashMap<String, Object> sCache = new LinkedHashMap<String, Object>() {
        @Override
        protected boolean removeEldestEntry(Entry<String, Object> eldest) {
            return this.size() > CACHE_SIZE;
        }
    };
    protected SharedPreferences mSharedPref;
    protected SharedPreferences.Editor mEditor;
    protected Context mContext;
    private boolean mUseCache;

    /**
     * Constructor for using default shared preference with a in-memory cache enabled
     *
     * @param context
     */
    public BaseSharedPreferences(Context context) {
        this(context, true);
    }

    /**
     * Constructor for using default shared preference uses application package name
     *
     * @param context
     * @param useCache true if to use a LRU in-memory cache for faster access, at a cost
     *                 of memory usage at runtime
     */
    public BaseSharedPreferences(Context context, boolean useCache) {
        mContext = context;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPref.edit();
        mUseCache = useCache;
    }

    /**
     * Constructor for using shared preference with custom name and
     * with a in-memory cache enabled
     *
     * @param context
     */
    public BaseSharedPreferences(Context context, String preferenceName) {
        this(context, preferenceName, true);
    }

    /**
     * Constructor for using a shared preference with custom name
     *
     * @param context
     * @param preferenceName name of the preference
     * @param useCache       true if to use a LRU in-memory cache for faster access, at a cost
     *                       of memory usage at runtime
     */
    public BaseSharedPreferences(Context context, String preferenceName, boolean useCache) {
        mContext = context;
        mSharedPref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
        mUseCache = useCache;
    }


    /**********************************************************************************
     * public methods
     */
    public HashMap<String, Object> getChangesCache() {
        return new HashMap<String, Object>(sCache);
    }

    public void commitChangesAsync() {
        // apply() saves to shared memory preferences and then async to disk where as commit
        // does everything synchronously. apply() however is only supported GINGERBREAD and above
        mEditor.apply();
    }

    public void commitChangesSync() {
        mEditor.commit();
    }

    public void clearChanges() {
        mEditor.clear();
        sCache.clear();
    }

    public BaseSharedPreferences setByKey(String key, String value) {
        if (mUseCache) sCache.put(key, value);
        return putKeyByValue(key, value);
    }

    public BaseSharedPreferences setByKey(String key, boolean value) {
        if (mUseCache) sCache.put(key, value);
        return putKeyByValue(key, Boolean.toString(value));
    }

    public BaseSharedPreferences setByKey(String key, int value) {
        if (mUseCache) sCache.put(key, value);
        return putKeyByValue(key, Integer.toString(value));
    }

    public BaseSharedPreferences setByKey(String key, long value) {
        if (mUseCache) sCache.put(key, value);
        return putKeyByValue(key, Long.toString(value));
    }

    public BaseSharedPreferences setByKey(String key, float value) {
        if (mUseCache) sCache.put(key, value);
        return putKeyByValue(key, Float.toString(value));
    }

    public String getStringByKey(String key) {
        return getStringByKey(key, "");
    }

    public String getStringByKey(String key, String defaultValue) {
        if (mUseCache && sCache.containsKey(key)) {
            Object val = sCache.get(key);
            if (val == null) return defaultValue;
            return (String) val;
        }

        String value = getValueByKey(key);
        if (!TextUtils.isEmpty(value)) {
            if (mUseCache) sCache.put(key, value);
            return value;
        }

        return defaultValue;
    }

    public Boolean getBooleanByKey(String key) {
        return getBooleanByKey(key, false);
    }

    public Boolean getBooleanByKey(String key, boolean defaultValue) {
        if (mUseCache && sCache.containsKey(key)) {
            Object val = sCache.get(key);
            if (val == null) return defaultValue;
            return (Boolean) val;
        }

        String value = getValueByKey(key);
        try {
            Boolean objValue = Boolean.parseBoolean(value);
            if (mUseCache) sCache.put(key, objValue);
            return objValue;
        } catch (Exception e) {
            return defaultValue;
        }

    }

    public int getIntByKey(String key) {
        return getIntByKey(key, 0);
    }

    public int getIntByKey(String key, int defaultValue) {
        if (mUseCache && sCache.containsKey(key)) {
            Object val = sCache.get(key);
            if (val == null) return defaultValue;
            return (Integer) val;
        }

        String value = getValueByKey(key);
        try {
            Integer objValue = Integer.parseInt(value);
            if (mUseCache) sCache.put(key, objValue);
            return objValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getLongByKey(String key) {
        return getLongByKey(key, 0);
    }

    public long getLongByKey(String key, long defaultValue) {
        if (mUseCache && sCache.containsKey(key)) {
            Object val = sCache.get(key);
            if (val == null) return defaultValue;
            return (Long) val;
        }

        String value = getValueByKey(key);
        try {
            Long objValue = Long.parseLong(value);
            if (mUseCache) sCache.put(key, objValue);
            return objValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getFloatByKey(String key) {
        return getFloatByKey(key, 0);
    }

    public float getFloatByKey(String key, float defaultValue) {
        if (mUseCache && sCache.containsKey(key)) {
            Object val = sCache.get(key);
            if (val == null) return defaultValue;
            return (Float) val;
        }

        String value = getValueByKey(key);
        try {
            Float objValue = Float.parseFloat(value);
            if (mUseCache) sCache.put(key, objValue);
            return objValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected BaseSharedPreferences putKeyByValue(String key, String value) {
        mEditor.putString(key, value);
        return this;
    }

    protected String getValueByKey(String key) {
        return mSharedPref.getString(key, null);
    }
}
