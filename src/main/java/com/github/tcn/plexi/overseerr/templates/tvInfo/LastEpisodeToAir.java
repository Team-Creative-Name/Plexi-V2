
package com.github.tcn.plexi.overseerr.templates.tvInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LastEpisodeToAir {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("airDate")
    @Expose
    private String airDate;
    @SerializedName("episodeNumber")
    @Expose
    private Integer episodeNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("productionCode")
    @Expose
    private String productionCode;
    @SerializedName("seasonNumber")
    @Expose
    private Integer seasonNumber;
    @SerializedName("voteAverage")
    @Expose
    private Double voteAverage;
    @SerializedName("stillPath")
    @Expose
    private String stillPath;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LastEpisodeToAir() {
    }

    /**
     * 
     * @param overview
     * @param productionCode
     * @param voteAverage
     * @param airDate
     * @param name
     * @param stillPath
     * @param id
     * @param seasonNumber
     * @param episodeNumber
     */
    public LastEpisodeToAir(Integer id, String airDate, Integer episodeNumber, String name, String overview, String productionCode, Integer seasonNumber, Double voteAverage, String stillPath) {
        super();
        this.id = id;
        this.airDate = airDate;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.overview = overview;
        this.productionCode = productionCode;
        this.seasonNumber = seasonNumber;
        this.voteAverage = voteAverage;
        this.stillPath = stillPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
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

    public String getProductionCode() {
        return productionCode;
    }

    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getStillPath() {
        return stillPath;
    }

    public void setStillPath(String stillPath) {
        this.stillPath = stillPath;
    }

}
