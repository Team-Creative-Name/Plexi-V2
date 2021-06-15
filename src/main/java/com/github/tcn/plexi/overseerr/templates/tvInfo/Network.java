
package com.github.tcn.plexi.overseerr.templates.tvInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Network {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("logoPath")
    @Expose
    private String logoPath;
    @SerializedName("originCountry")
    @Expose
    private String originCountry;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
