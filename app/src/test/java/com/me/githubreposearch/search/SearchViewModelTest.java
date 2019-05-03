package com.me.githubreposearch.search;

import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResponse;
import com.me.githubreposearch.model.SearchResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class SearchViewModelTest {

    private SearchViewModel viewModel;

    @Mock
    private GithubRepository repository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);// required for the "@Mock" annotations

        // Make viewModel a mock while using mock repository and viewContract created above
        viewModel = Mockito.spy(new SearchViewModel(repository));
    }

    @Test
    public void searchGitHubRepos_noQuery() {
        String searchQuery = null;
        // Trigger
        viewModel.searchGitHubRepos(searchQuery);

        // Validation
        Mockito.verify(repository, Mockito.never()).searchRepos(searchQuery, viewModel);
    }

    @Test
    public void searchGitHubRepos() {
        String searchQuery = "some query";
        // Trigger
        viewModel.searchGitHubRepos(searchQuery);
        // Validation
        Mockito.verify(repository, Mockito.times(1)).searchRepos(searchQuery, viewModel);
    }

    /**
     * By default, <code>Response response = Mockito.mock(Response.class)</code> will not work
     * since Retrofit's Response is final class
     * you will see an error "Mockito cannot mock/spy because : - final class"
     * <p>
     * That problem is overcome for Mockito 2+ following the instruction <a href="https://github
     * .com/mockito/mockito/wiki/What's-new-in-Mockito-2#mock-the-unmockable-opt-in-mocking-of
     * -final-classesmethods">here</a>
     */
    @SuppressWarnings("unchecked")

    /**
     * TODO : IT doesn't succeed how ever it shall.
     * with the following error :-
     * Wanted but not invoked:-
     *
     *  searchViewModel.renderSuccess(
     *   Mock for SearchResponse, hashCode: 2151717
     *    );
     *    -> at com.me.githubreposearch.search.SearchViewModel.renderSuccess(SearchViewModel.java:52)
     *
     *  However, there was exactly 1 interaction with this mock:
     *  searchViewModel.handleGitHubResponse(
     *  Mock for Response, hashCode: 1073763441
     *  );
     *  -> at com.me.githubreposearch.search.SearchViewModelTest.handleGitHubResponse_Success(SearchViewModelTest.java:75)
     */
    @Test
    public void handleGitHubResponse_Success() {
        Response response = Mockito.mock(Response.class);
        SearchResponse searchResponse = Mockito.mock(SearchResponse.class);
        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(searchResponse).when(response).body();
        List<SearchResult> searchResults = Collections.singletonList(new SearchResult());
        Mockito.doReturn(searchResults).when(searchResponse).getSearchResults();

        // Trigger
        viewModel.handleGitHubResponse(response);

        // Validation
        Mockito.verify(viewModel, Mockito.times(1)).renderSuccess(searchResponse);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void renderSuccess() {
        Response response = Mockito.mock(Response.class);
        SearchResponse searchResponse = Mockito.mock(SearchResponse.class);
        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(searchResponse).when(response).body();
        Mockito.doReturn(1001).when(searchResponse).getTotalCount();

        // Trigger
        viewModel.handleGitHubResponse(response);

        // Validation
        Assert.assertEquals("Number of results: 1001", viewModel.status.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_Failure() {
        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(false).when(response).isSuccessful();

        // Trigger
        viewModel.handleGitHubResponse(response);

        // Validation
        Assert.assertEquals("E101 - System error", viewModel.status.get());


    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_EmptyResponse() {
        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(null).when(response).body();

        // Trigger
        viewModel.handleGitHubResponse(response);

        // Validation
        Assert.assertEquals("E102 - System error", viewModel.status.get());
    }

    @Test
    public void handleGitHubError() {
        // Trigger
        viewModel.handleGitHubError();

        // Validation
        Assert.assertEquals("some error happened", viewModel.status.get());
    }
}
