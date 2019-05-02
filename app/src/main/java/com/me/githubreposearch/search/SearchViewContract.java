package com.me.githubreposearch.search;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.me.githubreposearch.model.SearchResult;

import java.util.List;

public interface SearchViewContract {

    void displaySearchResults(@NonNull List<SearchResult> searchResults,
                              @Nullable Integer totalCount);

    void displayError(String s);

    void displayError();
}
