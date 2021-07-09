
package com.github.tcn.plexi.overseerr.templates.request.allRequests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PageInfo {

    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("results")
    @Expose
    private Integer results;
    @SerializedName("page")
    @Expose
    private Integer page;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PageInfo() {
    }

    /**
     * 
     * @param pages
     * @param pageSize
     * @param page
     * @param results
     */
    public PageInfo(Integer pages, Integer pageSize, Integer results, Integer page) {
        super();
        this.pages = pages;
        this.pageSize = pageSize;
        this.results = results;
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
