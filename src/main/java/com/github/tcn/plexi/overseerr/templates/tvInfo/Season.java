package com.github.tcn.plexi.overseerr.templates.tvInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Season {

    @SerializedName("airDate")
    @Expose
    private String airDate;
    @SerializedName("episodeCount")
    @Expose
    private Integer episodeCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("seasonNumber")
    @Expose
    private Integer seasonNumber;
    @SerializedName("posterPath")
    @Expose
    private Object posterPath;

    /**
     * No args constructor for use in serialization
     */
    public Season() {
    }

    /**
     * @param overview
     * @param episodeCount
     * @param airDate
     * @param name
     * @param id
     * @param seasonNumber
     * @param posterPath
     */
    public Season(String airDate, Integer episodeCount, Integer id, String name, String overview, Integer seasonNumber, Object posterPath) {
        super();
        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.seasonNumber = seasonNumber;
        this.posterPath = posterPath;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Object getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(Object posterPath) {
        this.posterPath = posterPath;
    }

}
