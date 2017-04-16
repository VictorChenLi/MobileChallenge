package com.victorchen.mycurrency.network.retrofit;

import java.io.Serializable;

/**
 * A common abstract for all POJOs that's used in retrofit
 * This is used by proguard file to keep class and member names
 * since Retrofit uses reflection in serialization
 */
public interface IRetrofitDataObj extends Serializable, NonObfuscatable {
}
