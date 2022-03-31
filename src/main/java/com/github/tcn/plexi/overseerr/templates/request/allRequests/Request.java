package com.github.tcn.plexi.overseerr.templates.request.allRequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Request {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("is4k")
    @Expose
    private Boolean is4k;
    @SerializedName("serverId")
    @Expose
    private Object serverId;
    @SerializedName("profileId")
    @Expose
    private Object profileId;
    @SerializedName("rootFolder")
    @Expose
    private Object rootFolder;
    @SerializedName("languageProfileId")
    @Expose
    private Object languageProfileId;
    @SerializedName("tags")
    @Expose
    private Object tags;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;
    @SerializedName("modifiedBy")
    @Expose
    private ModifiedBy modifiedBy;
    @SerializedName("requestedBy")
    @Expose
    private RequestedBy requestedBy;
    @SerializedName("seasonCount")
    @Expose
    private Integer seasonCount;

    /**
     * No args constructor for use in serialization
     */
    public Request() {
    }

    /**
     * @param seasons
     * @param rootFolder
     * @param languageProfileId
     * @param media
     * @param type
     * @param serverId
     * @param tags
     * @param is4k
     * @param createdAt
     * @param requestedBy
     * @param seasonCount
     * @param profileId
     * @param modifiedBy
     * @param id
     * @param status
     * @param updatedAt
     */
    public Request(Integer id, Integer status, String createdAt, String updatedAt, String type, Boolean is4k, Object serverId, Object profileId, Object rootFolder, Object languageProfileId, Object tags, Media media, List<Season> seasons, ModifiedBy modifiedBy, RequestedBy requestedBy, Integer seasonCount) {
        super();
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
        this.is4k = is4k;
        this.serverId = serverId;
        this.profileId = profileId;
        this.rootFolder = rootFolder;
        this.languageProfileId = languageProfileId;
        this.tags = tags;
        this.media = media;
        this.seasons = seasons;
        this.modifiedBy = modifiedBy;
        this.requestedBy = requestedBy;
        this.seasonCount = seasonCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIs4k() {
        return is4k;
    }

    public void setIs4k(Boolean is4k) {
        this.is4k = is4k;
    }

    public Object getServerId() {
        return serverId;
    }

    public void setServerId(Object serverId) {
        this.serverId = serverId;
    }

    public Object getProfileId() {
        return profileId;
    }

    public void setProfileId(Object profileId) {
        this.profileId = profileId;
    }

    public Object getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(Object rootFolder) {
        this.rootFolder = rootFolder;
    }

    public Object getLanguageProfileId() {
        return languageProfileId;
    }

    public void setLanguageProfileId(Object languageProfileId) {
        this.languageProfileId = languageProfileId;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public ModifiedBy getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(ModifiedBy modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public RequestedBy getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(RequestedBy requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(Integer seasonCount) {
        this.seasonCount = seasonCount;
    }

}
