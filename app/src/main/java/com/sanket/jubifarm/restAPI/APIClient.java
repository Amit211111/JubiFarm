package com.sanket.jubifarm.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    // Base URL Paryavaran Sakhi
    public  static final  String BASE_URL2 ="https://jubifarm.icpl.tech/api/ps/";

       // public static final String BASE_URL = "https://icpl.tech/jubifarm/api/";
        public static final String BASE_URL = "https://jubifarm.icpl.tech/api/";
        //public static final String LAND_IMAGE_URL = "https://icpl.tech/jubifarm/uploads/land_images/";
        public static final String LAND_IMAGE_URL = "https://jubifarm.icpl.techuploads/land_images/";
        //public static final String IMAGE_PROFILE_URL = "https://icpl.tech/jubifarm/uploads/users/";
        public static final String IMAGE_PROFILE_URL = "https://jubifarm.icpl.tech/uploads/users/";
        //public static final String IMAGE_PLANTS_URL = "https://icpl.tech/jubifarm/uploads/plants/";
        public static final String IMAGE_PLANTS_URL = "https://jubifarm.icpl.tech/uploads/plants/";
        //public static final String IMAGE_PLANTS_HELPLINE = "https://icpl.tech/jubifarm/uploads/helpline/";
        public static final String IMAGE_PLANTS_HELPLINE = "https://jubifarm.icpl.tech/uploads/helpline/";
        public static final String AUDIO_UPLOAD_FILE = "https://jubifarm.icpl.tech/uploads/helpline/";

        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60000, TimeUnit.SECONDS)
                    .readTimeout(60000, TimeUnit.SECONDS)
                    .addInterceptor(logging).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            return retrofit;
        }

    public static Retrofit getPsClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

}
