package com.example.kindremind_mobileappproject.data;

import android.util.Log;
import android.widget.Toast;

import com.example.kindremind_mobileappproject.model.Deed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class DeedApiManager {

    // Singleton instance
    private static DeedApiManager instance;
    private HashMap<String,String> deedList;

    private DeedApiManager() {}

    public static synchronized DeedApiManager getInstance() {
        if (instance == null) {
            instance = new DeedApiManager();
        }
        return instance;
    }



    public void getAllDeedsFromJSON(String url, DeedFetchCallback callback) {
        if (deedList == null) {
            deedList = new HashMap<>();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DeedApi deedApi = retrofit.create(DeedApi.class);
            Call<List<Deed>> call = deedApi.getAllDeeds();

            call.enqueue(new Callback<List<Deed>>() {
                @Override
                public void onResponse(Call<List<Deed>> call, Response<List<Deed>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (Deed deed : response.body()) {
                            deedList.put(deed.getText(), deed.getCat());
                        }
                        //  Call the callback with fetched data
                        callback.onDeedsFetched(deedList);
                    } else {
                        Log.e("API_ERROR", "Response unsuccessful");
                        callback.onDeedsFetched(new HashMap<>());
                    }
                }

                @Override
                public void onFailure(Call<List<Deed>> call, Throwable t) {
                    Log.e("API_ERROR", "Failed to fetch deeds: " + t.getMessage());
                    callback.onDeedsFetched(new HashMap<>());
                }
            });
        } else {
            // Already have data → return it immediately
            callback.onDeedsFetched(deedList);
        }
    }

    // Only executes when API call is finished.
    public interface DeedFetchCallback {
        void onDeedsFetched(HashMap<String, String> deeds);
    }
    public interface DeedApi {
        @GET("deeds.json")
        Call<List<Deed>> getAllDeeds();
    }
}
