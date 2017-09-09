package com.githubapp.data;

import com.githubapp.GitHubService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Tosin Onikute.
 */

public class RestClient {

    private static GitHubService REST_CLIENT;
    private static String BASE_URL = "https://api.github.com";

    public RestClient() {}

    public static GitHubService getInstance() {
        if (REST_CLIENT == null) {
            setupRestClient();
        }
        return REST_CLIENT;
    }


    private static void setupRestClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = retrofit.create(GitHubService.class);
    }

}
