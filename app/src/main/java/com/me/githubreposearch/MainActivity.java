package com.me.githubreposearch;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.me.githubreposearch.api.GitHubApi;
import com.me.githubreposearch.data.GithubRepository;
import com.me.githubreposearch.databinding.ActivityMainBinding;
import com.me.githubreposearch.search.SearchViewModel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ReposRvAdapter rvAdapter;
    private TextView tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);

        final EditText etSearchQuery = findViewById(R.id.et_search_query);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GithubRepository repository = new GithubRepository(retrofit.create(GitHubApi.class));
        final SearchViewModel searchViewModel = new SearchViewModel( repository);
        binding.setVm(searchViewModel);

        etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v,
                                          int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewModel.searchGitHubRepos(etSearchQuery.getText().toString());
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



}
