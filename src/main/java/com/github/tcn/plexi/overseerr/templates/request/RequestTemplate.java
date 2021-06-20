
package com.github.tcn.plexi.overseerr.templates.request;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RequestTemplate {

    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("mediaId")
    @Expose
    private Integer mediaId;
    @SerializedName("tvdbId")
    @Expose
    private Integer tvdbId;
    @SerializedName("seasons")
    @Expose
    private List<Integer> seasons = null;
    @SerializedName("is4k")
    @Expose
    private Boolean is4k;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RequestTemplate() {
    }

    /**
     * 
     * @param is4k
     * @param seasons
     * @param tvdbId
     * @param mediaType
     * @param mediaId
     */
    public RequestTemplate(String mediaType, Integer mediaId, Integer tvdbId, List<Integer> seasons, Boolean is4k) {
        super();
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.tvdbId = tvdbId;
        this.seasons = seasons;
        this.is4k = is4k;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public List<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }

    public Boolean getIs4k() {
        return is4k;
    }

    public void setIs4k(Boolean is4k) {
        this.is4k = is4k;
    }

    public static class RequestBuilder{
        private String mediaType;
        private Integer mediaId, tvdbId;
        private List<Integer> seasons = null;
        private Boolean is4k;


        public String build(){
            if(check()){
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                return gson.toJson(new RequestTemplate(mediaType, mediaId, tvdbId, seasons, is4k));
            }
            throw new IllegalArgumentException("Missing required variable!");
        }

        public  RequestBuilder setIs4k(boolean is4k){
            this.is4k = is4k;
            return this;
        }

        public RequestBuilder setMediaType(boolean isMovie){
            if(isMovie){
                this.mediaType = "movie";
            }else{
                this.mediaType = "tv";
            }
            return this;
        }

        public RequestBuilder setSeasons(List<Integer> seasons){
            this.seasons = seasons;
            return this;
        }


        public RequestBuilder setMediaId(int mediaId){
            this.mediaId = mediaId;
            return this;
        }

        private boolean check(){
            if(mediaType != null){
                return true;
            }
            return false;
        }

    }

}
