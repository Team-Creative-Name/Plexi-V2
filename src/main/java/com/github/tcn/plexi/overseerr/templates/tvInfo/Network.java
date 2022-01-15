package com.github.tcn.plexi.overseerr.templates.tvInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Network {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("originCountry")
    @Expose
    private String originCountry;
    @SerializedName("logoPath")
    @Expose
    private String logoPath;

    /**
     * No args constructor for use in serialization
     */
    public Network() {
    }

    /**
     * @param logoPath
     * @param name
     * @param originCountry
     * @param id
     */
    public Network(Integer id, String name, String originCountry, String logoPath) {
        super();
        this.id = id;
        this.name = name;
        this.originCountry = originCountry;
        this.logoPath = logoPath;
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

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

}
