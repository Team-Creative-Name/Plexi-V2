
package com.github.tcn.plexi.overseerr.templates.users;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPages {

    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
