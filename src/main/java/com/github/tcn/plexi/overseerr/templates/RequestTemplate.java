package com.github.tcn.plexi.overseerr.templates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestTemplate {

    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("mediaId")
    @Expose
    private Integer mediaId;
    @SerializedName("seasons")
    @Expose
    private List<Integer> seasons = null;
    @SerializedName("is4k")
    @Expose
    private Boolean is4k;


    private RequestTemplate(String mediaType, Integer mediaId, List<Integer> seasons, Boolean is4k) {
        super();
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.seasons = seasons;
        this.is4k = is4k;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public List<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }

    public Boolean getIs4k() {
        return is4k;
    }

    public void setIs4k(Boolean is4k) {
        this.is4k = is4k;
    }

}