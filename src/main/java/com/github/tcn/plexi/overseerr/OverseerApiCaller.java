package com.github.tcn.plexi.overseerr;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class OverseerApiCaller {

    public MediaSearch Search(String query){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() +"/api/v1/search?query=" + removeSpaces(query))
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try(Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response from Overseerr: " + response);
            }

            return gson.fromJson(response.body().string(),MediaSearch.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public TvInfo getTvInfo(int tmdbId){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/tv/" + tmdbId)
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();
        try(Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response from Overseerr: " + response);
            }

            return gson.fromJson(response.body().string(), TvInfo.class);

        }catch (IOException e){
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get more info about tv show: " + tmdbId);
        }
        return null;
    }

    public MovieInfo getMovieInfo(int tmdbId){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/movie/" + tmdbId)
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            return gson.fromJson(response.body().string(), MovieInfo.class);

        }catch (IOException e){
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get more info about movie: " + tmdbId);
        }
        return null;
    }

    public long getPingTime(){
        long responseTime = -1;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl()+"/api/v1/status")
                .addHeader("accept", "application/json")
                .build();


        try(Response response = client.newCall(request).execute()){
            if(response != null){
                response.close();
                responseTime = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
            }
        }catch (Exception e){
            LoggerFactory.getLogger("Plexi: OverseerrApiCaller").error("Unable to communicate with Overseerr!");
        }

        return responseTime;
    }


    private String removeSpaces(String query) {
        String formattedString = query;

        formattedString = formattedString.toLowerCase().replaceAll(" ", "%20");
        formattedString = formattedString.replaceAll("\"", " ");
        formattedString = formattedString.replaceAll("/", " ");
        //format searchQuery
        return formattedString;
    }
}
