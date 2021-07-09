
package com.github.tcn.plexi.overseerr.templates.request.allRequests;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MediaRequests {

    @SerializedName("pageInfo")
    @Expose
    private PageInfo pageInfo;
    @SerializedName("results")
    @Expose
    private List<Request> requests = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MediaRequests() {
    }

    /**
     * 
     * @param pageInfo
     * @param requests
     */
    public MediaRequests(PageInfo pageInfo, List<Request> requests) {
        super();
        this.pageInfo = pageInfo;
        this.requests = requests;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Request> getResults() {
        return requests;
    }

    public void setResults(List<Request> requests) {
        this.requests = requests;
    }

}
