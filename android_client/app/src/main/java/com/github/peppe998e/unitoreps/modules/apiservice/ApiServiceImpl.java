package com.github.peppe998e.unitoreps.modules.apiservice;

import retrofit2.Retrofit;

public class ApiServiceImpl implements ApiService {
    private Retrofit retrofit;

    public ApiServiceImpl(String ApiUrl) {
        this.retrofit = new Retrofit.Builder()
        .baseUrl(ApiUrl)
        .build();
    }
}
