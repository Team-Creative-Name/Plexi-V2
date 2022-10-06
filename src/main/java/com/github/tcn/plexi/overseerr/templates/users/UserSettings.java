package com.github.tcn.plexi.overseerr.templates.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSettings {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("discordId")
    @Expose
    private String discordId;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("originalLanguage")
    @Expose
    private String originalLanguage;
    @SerializedName("movieQuotaLimit")
    @Expose
    private String movieQuotaLimit;
    @SerializedName("movieQuotaDays")
    @Expose
    private String movieQuotaDays;
    @SerializedName("tvQuotaLimit")
    @Expose
    private String tvQuotaLimit;
    @SerializedName("tvQuotaDays")
    @Expose
    private String tvQuotaDays;
    @SerializedName("watchlistSyncMovies")
    @Expose
    private String watchlistSyncMovies;
    @SerializedName("watchlistSyncTv")
    @Expose
    private String watchlistSyncTv;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getMovieQuotaLimit() {
        return movieQuotaLimit;
    }

    public void setMovieQuotaLimit(String movieQuotaLimit) {
        this.movieQuotaLimit = movieQuotaLimit;
    }

    public String getMovieQuotaDays() {
        return movieQuotaDays;
    }

    public void setMovieQuotaDays(String movieQuotaDays) {
        this.movieQuotaDays = movieQuotaDays;
    }

    public String getTvQuotaLimit() {
        return tvQuotaLimit;
    }

    public void setTvQuotaLimit(String tvQuotaLimit) {
        this.tvQuotaLimit = tvQuotaLimit;
    }

    public String getTvQuotaDays() {
        return tvQuotaDays;
    }

    public void setTvQuotaDays(String tvQuotaDays) {
        this.tvQuotaDays = tvQuotaDays;
    }

    public String getWatchlistSyncMovies() {
        return watchlistSyncMovies;
    }

    public void setWatchlistSyncMovies(String watchlistSyncMovies) {
        this.watchlistSyncMovies = watchlistSyncMovies;
    }

    public String getWatchlistSyncTv() {
        return watchlistSyncTv;
    }

    public void setWatchlistSyncTv(String watchlistSyncTv) {
        this.watchlistSyncTv = watchlistSyncTv;
    }



}
