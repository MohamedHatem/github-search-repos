package com.me.githubreposearch.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @Expose
    @SerializedName("total_count")
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<SearchResult> searchResults = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    @Nullable
    public List<SearchResult> getSearchResults() {
        return searchResults;
    }
}
