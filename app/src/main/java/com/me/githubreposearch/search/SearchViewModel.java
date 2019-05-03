package com.me.githubreposearch.search;

import android.databinding.ObservableField;
import android.support.annotation.VisibleForTesting;

import com.me.githubreposearch.api.GithubServiceClient;
import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResponse;


import java.util.Locale;

import retrofit2.Response;

public class SearchViewModel implements GithubServiceClient.ApiCallback {

    private final GithubRepository repository;

    public ObservableField<String> status = new ObservableField<>();

    public SearchViewModel(GithubRepository repository) {
        this.repository = repository;
    }

    public void searchGitHubRepos(String searchQuery) {
        if (searchQuery != null && searchQuery.length() > 0) {
            repository.searchRepos(searchQuery, this);
        }
    }

    @Override
    public void handleGitHubResponse(Response<SearchResponse> response) {
        if (response.isSuccessful()) {
            SearchResponse searchResponse = response.body();
            if (searchResponse != null && searchResponse.getSearchResults() != null) {

                renderSuccess(searchResponse);
            } else {
                status.set("E102 - System error");
            }
        } else {
            status.set("E101 - System error");

        }

    }



    @VisibleForTesting
     public void renderSuccess(SearchResponse searchResponse) {
        status.set(String.format(Locale.US, "Number of results: %d", searchResponse
                .getTotalCount()));
    }

    @Override
    public void handleGitHubError() {
        status.set("some error happened");
    }


}
