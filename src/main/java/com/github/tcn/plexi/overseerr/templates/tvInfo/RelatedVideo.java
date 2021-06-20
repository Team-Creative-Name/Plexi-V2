
package com.github.tcn.plexi.overseerr.templates.tvInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RelatedVideo {

    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RelatedVideo() {
    }

    /**
     * 
     * @param site
     * @param size
     * @param name
     * @param type
     * @param key
     * @param url
     */
    public RelatedVideo(String site, String key, String name, Integer size, String type, String url) {
        super();
        this.site = site;
        this.key = key;
        this.name = name;
        this.size = size;
        this.type = type;
        this.url = url;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
