package com.me.githubreposearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.me.githubreposearch.api.GitHubApi;
import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.model.SearchResult;
import com.me.githubreposearch.search.SearchPresenter;
import com.me.githubreposearch.search.SearchViewContract;

import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchViewContract {

    private ReposRvAdapter rvAdapter;
    private TextView tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);

        final EditText etSearchQuery = findViewById(R.id.et_search_query);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GithubRepository repository = new GithubRepository(retrofit.create(GitHubApi.class));
        final SearchPresenter searchPresenter = new SearchPresenter(this, repository);


        etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v,
                                          int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPresenter.searchGitHubRepos(etSearchQuery.getText().toString());
                    return true;
                }
                return false;
            }
        });

        RecyclerView rvRepos = findViewById(R.id.rv_repos);
        rvAdapter = new ReposRvAdapter();
        rvRepos.setHasFixedSize(true);
        rvRepos.setAdapter(rvAdapter);
    }



    @Override
    public void displaySearchResults(@NonNull List<SearchResult> searchResults, @Nullable Integer totalCount) {
        rvAdapter.updateResults(searchResults);
        tvStatus.setText(String.format(Locale.US, "Number of results: %d", totalCount));


    }

    @Override
    public void displayError() {
        Toast.makeText(this, "some error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
