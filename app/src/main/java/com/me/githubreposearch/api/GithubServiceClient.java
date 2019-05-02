package com.me.githubreposearch.api;

import android.util.Log;

import com.me.githubreposearch.model.SearchResponse;
import com.me.githubreposearch.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GithubServiceClient {


    private static final String LOG_TAG = GithubServiceClient.class.getSimpleName();


    public static void searchRepos(GitHubApi gitHubApi, String query,
                                   final ApiCallback apiCallback) {


        Call<SearchResponse> call = gitHubApi.searchRepos(query);


        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                Log.d(LOG_TAG, "onResponse: Got response:" + response);

                if (response.isSuccessful()) {

                    List<SearchResult> reposList = response.body() != null ? response.body().getSearchResults() : new ArrayList<SearchResult>();
                    int totalCount = response.body() != null ? response.body().getTotalCount() : 0;
                    apiCallback.onSuccess(reposList, totalCount);

                } else {

                    apiCallback.onError(response.errorBody() != null ? response.errorBody().toString() : "unkown error message");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure: Failed to get data");

                apiCallback.onError(t.getMessage() != null ? t.getMessage() : "unkown error type");
            }
        });
    }

    public interface ApiCallback {

        void onSuccess(List<SearchResult> reposList, int totalCount);

        void onError(String errorMessage);
    }
}
