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
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Result> getResults() {
        return results;
    }

}
