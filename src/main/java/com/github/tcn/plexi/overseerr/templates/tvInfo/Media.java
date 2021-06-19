
package com.github.tcn.plexi.overseerr.templates.tvInfo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Media {

    @SerializedName("downloadStatus")
    @Expose
    private List<Object> downloadStatus = null;
    @SerializedName("downloadStatus4k")
    @Expose
    private List<Object> downloadStatus4k = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("tmdbId")
    @Expose
    private Integer tmdbId;
    @SerializedName("tvdbId")
    @Expose
    private Integer tvdbId;
    @SerializedName("imdbId")
    @Expose
    private Object imdbId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("status4k")
    @Expose
    private Integer status4k;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("lastSeasonChange")
    @Expose
    private String lastSeasonChange;
    @SerializedName("mediaAddedAt")
    @Expose
    private Object mediaAddedAt;
    @SerializedName("serviceId")
    @Expose
    private Object serviceId;
    @SerializedName("serviceId4k")
    @Expose
    private Object serviceId4k;
    @SerializedName("externalServiceId")
    @Expose
    private Object externalServiceId;
    @SerializedName("externalServiceId4k")
    @Expose
    private Object externalServiceId4k;
    @SerializedName("externalServiceSlug")
    @Expose
    private Object externalServiceSlug;
    @SerializedName("externalServiceSlug4k")
    @Expose
    private Object externalServiceSlug4k;
    @SerializedName("ratingKey")
    @Expose
    private Object ratingKey;
    @SerializedName("ratingKey4k")
    @Expose
    private Object ratingKey4k;
    @SerializedName("seasons")
    @Expose
    private List<Object> seasons = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Media() {
    }

    /**
     * 
     * @param seasons
     * @param tvdbId
     * @param externalServiceId
     * @param externalServiceSlug
     * @param imdbId
     * @param mediaType
     * @param externalServiceSlug4k
     * @param ratingKey
     * @param serviceId4k
     * @param downloadStatus4k
     * @param status4k
     * @param externalServiceId4k
     * @param createdAt
     * @param tmdbId
     * @param ratingKey4k
     * @param mediaAddedAt
     * @param lastSeasonChange
     * @param id
     * @param downloadStatus
     * @param serviceId
     * @param status
     * @param updatedAt
     */
    public Media(List<Object> downloadStatus, List<Object> downloadStatus4k, Integer id, String mediaType, Integer tmdbId, Integer tvdbId, Object imdbId, Integer status, Integer status4k, String createdAt, String updatedAt, String lastSeasonChange, Object mediaAddedAt, Object serviceId, Object serviceId4k, Object externalServiceId, Object externalServiceId4k, Object externalServiceSlug, Object externalServiceSlug4k, Object ratingKey, Object ratingKey4k, List<Object> seasons) {
        super();
        this.downloadStatus = downloadStatus;
        this.downloadStatus4k = downloadStatus4k;
        this.id = id;
        this.mediaType = mediaType;
        this.tmdbId = tmdbId;
        this.tvdbId = tvdbId;
        this.imdbId = imdbId;
        this.status = status;
        this.status4k = status4k;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastSeasonChange = lastSeasonChange;
        this.mediaAddedAt = mediaAddedAt;
        this.serviceId = serviceId;
        this.serviceId4k = serviceId4k;
        this.externalServiceId = externalServiceId;
        this.externalServiceId4k = externalServiceId4k;
        this.externalServiceSlug = externalServiceSlug;
        this.externalServiceSlug4k = externalServiceSlug4k;
        this.ratingKey = ratingKey;
        this.ratingKey4k = ratingKey4k;
        this.seasons = seasons;
    }

    public List<Object> getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(List<Object> downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public List<Object> getDownloadStatus4k() {
        return downloadStatus4k;
    }

    public void setDownloadStatus4k(List<Object> downloadStatus4k) {
        this.downloadStatus4k = downloadStatus4k;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public Object getImdbId() {
        return imdbId;
    }

    public void setImdbId(Object imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus4k() {
        return status4k;
    }

    public void setStatus4k(Integer status4k) {
        this.status4k = status4k;
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

    public String getLastSeasonChange() {
        return lastSeasonChange;
    }

    public void setLastSeasonChange(String lastSeasonChange) {
        this.lastSeasonChange = lastSeasonChange;
    }

    public Object getMediaAddedAt() {
        return mediaAddedAt;
    }

    public void setMediaAddedAt(Object mediaAddedAt) {
        this.mediaAddedAt = mediaAddedAt;
    }

    public Object getServiceId() {
        return serviceId;
    }

    public void setServiceId(Object serviceId) {
        this.serviceId = serviceId;
    }

    public Object getServiceId4k() {
        return serviceId4k;
    }

    public void setServiceId4k(Object serviceId4k) {
        this.serviceId4k = serviceId4k;
    }

    public Object getExternalServiceId() {
        return externalServiceId;
    }

    public void setExternalServiceId(Object externalServiceId) {
        this.externalServiceId = externalServiceId;
    }

    public Object getExternalServiceId4k() {
        return externalServiceId4k;
    }

    public void setExternalServiceId4k(Object externalServiceId4k) {
        this.externalServiceId4k = externalServiceId4k;
    }

    public Object getExternalServiceSlug() {
        return externalServiceSlug;
    }

    public void setExternalServiceSlug(Object externalServiceSlug) {
        this.externalServiceSlug = externalServiceSlug;
    }

    public Object getExternalServiceSlug4k() {
        return externalServiceSlug4k;
    }

    public void setExternalServiceSlug4k(Object externalServiceSlug4k) {
        this.externalServiceSlug4k = externalServiceSlug4k;
    }

    public Object getRatingKey() {
        return ratingKey;
    }

    public void setRatingKey(Object ratingKey) {
        this.ratingKey = ratingKey;
    }

    public Object getRatingKey4k() {
        return ratingKey4k;
    }

    public void setRatingKey4k(Object ratingKey4k) {
        this.ratingKey4k = ratingKey4k;
    }

    public List<Object> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Object> seasons) {
        this.seasons = seasons;
    }

}
