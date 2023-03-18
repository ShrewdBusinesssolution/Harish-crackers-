package com.vedi.vedi_box.adapters.testing;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestClient {

    String BASE_URL = "https://myruppi.com/poppy/";

    private static TestClient instance = null;
    private final TestInterface myApi;

    private TestClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(TestInterface.class);
    }

    public static synchronized TestClient getInstance() {
        if (instance == null) {
            instance = new TestClient();
        }
        return instance;
    }

    public TestInterface getMyApi() {
        return myApi;
    }
}
