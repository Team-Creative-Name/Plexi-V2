
package com.github.tcn.plexi.overseerr.templates.tvInfo;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvInfo {


    @SerializedName("episodeRunTime")
    @Expose
    private List<Integer> episodeRunTime = null;
    @SerializedName("firstAirDate")
    @Expose
    private String firstAirDate;
    @SerializedName("relatedVideos")
    @Expose
    private List<RelatedVideo> relatedVideos = null;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("inProduction")
    @Expose
    private Boolean inProduction;
    @SerializedName("languages")
    @Expose
    private List<String> languages = null;
    @SerializedName("lastAirDate")
    @Expose
    private String lastAirDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("networks")
    @Expose
    private List<Network> networks = null;
    @SerializedName("numberOfEpisodes")
    @Expose
    private Integer numberOfEpisodes;
    @SerializedName("numberOfSeasons")
    @Expose
    private Integer numberOfSeasons;
    @SerializedName("originCountry")
    @Expose
    private List<String> originCountry = null;
    @SerializedName("originalLanguage")
    @Expose
    private String originalLanguage;
    @SerializedName("originalName")
    @Expose
    private String originalName;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Float popularity;
    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("voteAverage")
    @Expose
    private Float voteAverage;
    @SerializedName("voteCount")
    @Expose
    private Integer voteCount;
    @SerializedName("backdropPath")
    @Expose
    private String backdropPath;
    @SerializedName("lastEpisodeToAir")
    @Expose
    private LastEpisodeToAir lastEpisodeToAir;
    @SerializedName("nextEpisodeToAir")
    @Expose
    private NextEpisodeToAir nextEpisodeToAir;
    @SerializedName("posterPath")
    @Expose
    private String posterPath;
    @SerializedName("keywords")
    @Expose
    private List<Keyword> keywords = null;
    @SerializedName("mediaInfo")
    @Expose
    private MediaInfo mediaInfo;

    //new variables added by me
    boolean[] isRequested;
    List<Integer> unrequestedSeasons;
    boolean isFullyRequested;


    /**
     * No args constructor for use in serialization
     * 
     */
    public TvInfo() {
    }

    /**
     * 
     * @param keywords
     * @param networks
     * @param type
     * @param inProduction
     * @param episodeRunTime
     * @param firstAirDate
     * @param numberOfSeasons
     * @param originalName
     * @param popularity
     * @param id
     * @param relatedVideos
     * @param posterPath
     * @param overview
     * @param seasons
     * @param voteAverage
     * @param languages
     * @param originalLanguage
     * @param numberOfEpisodes
     * @param lastEpisodeToAir
     * @param name
     * @param originCountry
     * @param tagline
     * @param nextEpisodeToAir
     * @param voteCount
     * @param backdropPath
     * @param lastAirDate
     * @param mediaInfo
     * @param homepage
     * @param status
     */
    public TvInfo(List<Integer> episodeRunTime, String firstAirDate, List<RelatedVideo> relatedVideos, String homepage, Integer id, Boolean inProduction, List<String> languages, String lastAirDate, String name, List<Network> networks, Integer numberOfEpisodes, Integer numberOfSeasons, List<String> originCountry, String originalLanguage, String originalName, String tagline, String overview, Float popularity, List<Season> seasons, String status, String type, Float voteAverage, Integer voteCount, String backdropPath, LastEpisodeToAir lastEpisodeToAir, NextEpisodeToAir nextEpisodeToAir, String posterPath, List<Keyword> keywords, MediaInfo mediaInfo) {
        super();
        this.episodeRunTime = episodeRunTime;
        this.firstAirDate = firstAirDate;
        this.relatedVideos = relatedVideos;
        this.homepage = homepage;
        this.id = id;
        this.inProduction = inProduction;
        this.languages = languages;
        this.lastAirDate = lastAirDate;
        this.name = name;
        this.networks = networks;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.originCountry = originCountry;
        this.originalLanguage = originalLanguage;
        this.originalName = originalName;
        this.tagline = tagline;
        this.overview = overview;
        this.popularity = popularity;
        this.seasons = seasons;
        this.status = status;
        this.type = type;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.backdropPath = backdropPath;
        this.lastEpisodeToAir = lastEpisodeToAir;
        this.nextEpisodeToAir = nextEpisodeToAir;
        this.posterPath = posterPath;
        this.keywords = keywords;
        this.mediaInfo = mediaInfo;
        System.out.println("Done");
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<RelatedVideo> getRelatedVideos() {
        return relatedVideos;
    }

    public void setRelatedVideos(List<RelatedVideo> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getInProduction() {
        return inProduction;
    }

    public void setInProduction(Boolean inProduction) {
        this.inProduction = inProduction;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public LastEpisodeToAir getLastEpisodeToAir() {
        return lastEpisodeToAir;
    }

    public void setLastEpisodeToAir(LastEpisodeToAir lastEpisodeToAir) {
        this.lastEpisodeToAir = lastEpisodeToAir;
    }

    public NextEpisodeToAir getNextEpisodeToAir() {
        return nextEpisodeToAir;
    }

    public void setNextEpisodeToAir(NextEpisodeToAir nextEpisodeToAir) {
        this.nextEpisodeToAir = nextEpisodeToAir;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(MediaInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    public boolean isFullyRequested(){
        //optimize in case we have to call more than once
        if(isRequested != null){
            return isFullyRequested;
        }

        isRequested = new boolean[getNumberOfSeasons()];
        unrequestedSeasons = new ArrayList<>();

        //if mediaInfo is null then there are no requests. Just request everything
        if(mediaInfo == null){
            isFullyRequested = false;
            //put all of the unrequested items in a list
            for(int i = 0; i < isRequested.length; i++){
                if(!isRequested[i]){
                    unrequestedSeasons.add(i + 1);
                }
            }
            return false;
        }



        //get an array of what is requested and whats not
        for(Request request : getMediaInfo().getRequests()){
            for(Season__1 season : request.getSeasons()){
                isRequested[season.getSeasonNumber() - 1] = true;
            }
        }

        //put all of the unrequested items in a list
        for(int i = 0; i < isRequested.length; i++){
            if(!isRequested[i]){
                unrequestedSeasons.add(i + 1);
            }
        }

        if(unrequestedSeasons.size() == 0){
            isFullyRequested = true;
            return true;
        }
        isFullyRequested = false;
        return false;
    }

    public boolean isFullyAvailable(){
        return (mediaInfo != null) && getMediaInfo().getStatus() == 5;
    }

    public boolean allowRequests(){
        return !isFullyRequested() && !isFullyAvailable();
    }

    public List<Integer> getUnrequestedSeasons(){
        return unrequestedSeasons;
    }

    public String getRequestStatus(){
        if(isFullyRequested){
           return  "Fully Requested";
        }else if(!getUnrequestedSeasons().isEmpty()){
            return "Partially Requested";
        }else{
            return "Not Requested";
        }
    }


}
