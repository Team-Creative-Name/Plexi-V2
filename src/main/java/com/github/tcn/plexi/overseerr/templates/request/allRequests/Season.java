package com.github.tcn.plexi.overseerr.templates.request.allRequests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Season {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seasonNumber")
    @Expose
    private Integer seasonNumber;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public Season() {
    }

    /**
     * @param createdAt
     * @param id
     * @param seasonNumber
     * @param status
     * @param updatedAt
     */
    public Season(Integer id, Integer seasonNumber, Integer status, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
