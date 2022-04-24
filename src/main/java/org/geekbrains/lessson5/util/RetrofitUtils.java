package org.geekbrains.lessson5.util;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@UtilityClass
public class RetrofitUtils {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    LoggingInterceptor logging2 = new LoggingInterceptor();
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//    public Retrofit getRetrofit(){
//        return new Retrofit.Builder()
//                .baseUrl(ConfigUtils.getBaseUrl())
//                .addConverterFactory(JacksonConverterFactory.create())
//                .build();
//    }

    public Retrofit getRetrofit(){
        logging.setLevel(BODY);
        httpClient.addInterceptor(logging2);
        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}

