package com.example.mukhtaradepoju.deattdeliverers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApolloClient {

    private static String BASE_URL="https://deattapi.herokuapp.com/";
    private static final String SUBSCRIPTION_BASE_URL = "wss://deattapi.herokuapp.com/subscriptions";
    private static ApolloClient MyapolloClient;




    public static ApolloClient getApolloClient2(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
       final String tokenclient = preferences.getString("Token", "");
       Log.i("tokenclient",tokenclient);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder().header("Authorization",tokenclient).build());

            }
        })

                .addInterceptor(logging)
                .build();




        MyapolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(client)
                .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(SUBSCRIPTION_BASE_URL, client))
                .build();
        return MyapolloClient;


    }




    public static ApolloClient getApolloClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        MyapolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(client).build();
        return MyapolloClient;
    }

    }
