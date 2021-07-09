
package com.github.tcn.plexi.overseerr.templates.request.allRequests;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RequestedBy {

    @SerializedName("permissions")
    @Expose
    private Integer permissions;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("plexUsername")
    @Expose
    private String plexUsername;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("recoveryLinkExpirationDate")
    @Expose
    private Object recoveryLinkExpirationDate;
    @SerializedName("userType")
    @Expose
    private Integer userType;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("movieQuotaLimit")
    @Expose
    private Object movieQuotaLimit;
    @SerializedName("movieQuotaDays")
    @Expose
    private Object movieQuotaDays;
    @SerializedName("tvQuotaLimit")
    @Expose
    private Object tvQuotaLimit;
    @SerializedName("tvQuotaDays")
    @Expose
    private Object tvQuotaDays;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("requestCount")
    @Expose
    private Integer requestCount;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RequestedBy() {
    }

    /**
     * 
     * @param plexUsername
     * @param movieQuotaLimit
     * @param requestCount
     * @param recoveryLinkExpirationDate
     * @param tvQuotaDays
     * @param displayName
     * @param movieQuotaDays
     * @param avatar
     * @param tvQuotaLimit
     * @param createdAt
     * @param permissions
     * @param id
     * @param userType
     * @param email
     * @param username
     * @param updatedAt
     */
    public RequestedBy(Integer permissions, Integer id, String email, String plexUsername, Object username, Object recoveryLinkExpirationDate, Integer userType, String avatar, Object movieQuotaLimit, Object movieQuotaDays, Object tvQuotaLimit, Object tvQuotaDays, String createdAt, String updatedAt, Integer requestCount, String displayName) {
        super();
        this.permissions = permissions;
        this.id = id;
        this.email = email;
        this.plexUsername = plexUsername;
        this.username = username;
        this.recoveryLinkExpirationDate = recoveryLinkExpirationDate;
        this.userType = userType;
        this.avatar = avatar;
        this.movieQuotaLimit = movieQuotaLimit;
        this.movieQuotaDays = movieQuotaDays;
        this.tvQuotaLimit = tvQuotaLimit;
        this.tvQuotaDays = tvQuotaDays;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.requestCount = requestCount;
        this.displayName = displayName;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlexUsername() {
        return plexUsername;
    }

    public void setPlexUsername(String plexUsername) {
        this.plexUsername = plexUsername;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public Object getRecoveryLinkExpirationDate() {
        return recoveryLinkExpirationDate;
    }

    public void setRecoveryLinkExpirationDate(Object recoveryLinkExpirationDate) {
        this.recoveryLinkExpirationDate = recoveryLinkExpirationDate;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getMovieQuotaLimit() {
        return movieQuotaLimit;
    }

    public void setMovieQuotaLimit(Object movieQuotaLimit) {
        this.movieQuotaLimit = movieQuotaLimit;
    }

    public Object getMovieQuotaDays() {
        return movieQuotaDays;
    }

    public void setMovieQuotaDays(Object movieQuotaDays) {
        this.movieQuotaDays = movieQuotaDays;
    }

    public Object getTvQuotaLimit() {
        return tvQuotaLimit;
    }

    public void setTvQuotaLimit(Object tvQuotaLimit) {
        this.tvQuotaLimit = tvQuotaLimit;
    }

    public Object getTvQuotaDays() {
        return tvQuotaDays;
    }

    public void setTvQuotaDays(Object tvQuotaDays) {
        this.tvQuotaDays = tvQuotaDays;
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

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
