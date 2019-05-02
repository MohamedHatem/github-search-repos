package com.me.githubreposearch.search;

import com.me.githubreposearch.api.GitHubApi;
import com.me.githubreposearch.api.GithubServiceClient;
import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResult;

import java.util.List;

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
    public void onSuccess(List<SearchResult> reposList, int totalCount) {
        viewContract.displaySearchResults(reposList, totalCount);

    }

    @Override
    public void onError(String errorMessage) {
        viewContract.displayError(errorMessage);
    }
}
