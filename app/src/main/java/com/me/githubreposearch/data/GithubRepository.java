package com.me.githubreposearch.data;

import android.support.annotation.NonNull;

import com.me.githubreposearch.api.GitHubApi;
import com.me.githubreposearch.api.GithubServiceClient;


public class GithubRepository {
    private final GitHubApi gitHubApi;

    public GithubRepository(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void searchRepos(@NonNull final String query,
                            @NonNull final GithubServiceClient.ApiCallback callback) {
        GithubServiceClient.searchRepos(gitHubApi, query, callback);
    }
}
