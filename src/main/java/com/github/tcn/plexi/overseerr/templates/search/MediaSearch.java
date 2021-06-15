package com.github.tcn.plexi.overseerr.templates.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaSearch {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("totalPages")
    @Expose
    private int totalPages;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
