package com.github.tcn.plexi.overseerr.templates.tvInfo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Keyword {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     */
    public Keyword() {
    }

    /**
     * @param name
     * @param id
     */
    public Keyword(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}
