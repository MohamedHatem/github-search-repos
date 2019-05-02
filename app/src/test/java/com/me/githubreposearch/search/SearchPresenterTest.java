package com.me.githubreposearch.search;

import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResponse;
import com.me.githubreposearch.model.SearchResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SearchPresenterTest {

    private SearchPresenter presenter;

    @Mock
    private GithubRepository repository;

    @Mock
    private SearchViewContract viewContract;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);// required for the "@Mock" annotations

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(new SearchPresenter(viewContract, repository));
    }

    @Test
    public void searchGitHubRepos_noQuery() {
        String searchQuery = null;
        // Trigger
        presenter.searchGitHubRepos(searchQuery);

        // Validation
        Mockito.verify(repository, Mockito.never()).searchRepos(searchQuery, presenter);
    }

    @Test
    public void searchGitHubRepos() {
        String searchQuery = "some query";
        // Trigger
        presenter.searchGitHubRepos(searchQuery);
        // Validation
        Mockito.verify(repository, Mockito.times(1)).searchRepos(searchQuery, presenter);
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
    @Test
    public void handleGitHubResponse_Success() {
        Response response = Mockito.mock(Response.class);
        SearchResponse searchResponse = Mockito.mock(SearchResponse.class);
        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(searchResponse).when(response).body();
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.add(new SearchResult());
        searchResults.add(new SearchResult());
        searchResults.add(new SearchResult());
        Mockito.doReturn(searchResults).when(searchResponse).getSearchResults();
        Mockito.doReturn(101).when(searchResponse).getTotalCount();

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        Mockito.verify(viewContract, Mockito.times(1)).displaySearchResults(searchResults, 101);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_Failure() {
        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(false).when(response).isSuccessful();

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError("E101 - System error");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_EmptyResponse() {
        Response response = Mockito.mock(Response.class);
        Mockito.doReturn(true).when(response).isSuccessful();
        Mockito.doReturn(null).when(response).body();

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError("E102 - System error");
    }

    @Test
    public void handleGitHubError() {
        // Trigger
        presenter.handleGitHubError();

        // Validation
        Mockito.verify(viewContract, Mockito.times(1)).displayError();
    }
}
