package com.me.githubreposearch.search;

import com.me.githubreposearch.api.GithubServiceClient;
import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResponse;
import com.me.githubreposearch.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SearchPresenter implements SearchPresenterContract, GithubServiceClient.ApiCallback {

    private final SearchViewContract viewContract;
    private final GithubRepository repository;

    public SearchPresenter(SearchViewContract viewContract, GithubRepository repository) {
        this.viewContract = viewContract;
        this.repository = repository;
    }

    @Override
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
                viewContract.displaySearchResults(searchResponse.getSearchResults(), searchResponse.getTotalCount());
            } else {
                viewContract.displayError("E102 - System error");
            }
        } else {
            viewContract.displayError("E101 - System error");
        }
    }

    @Override
    public void handleGitHubError() {
        viewContract.displayError();
    }


}
