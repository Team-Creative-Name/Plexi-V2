package com.github.tcn.plexi.overseerr;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import com.github.tcn.plexi.overseerr.templates.users.Result;
import com.github.tcn.plexi.overseerr.templates.users.UserPages;
import com.github.tcn.plexi.utils.MiscUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class OverseerApiCaller {

    public MediaSearch search(String query) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/search?query=" + MiscUtils.urlEncode(query))
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from Overseerr: " + response);
            }

            return gson.fromJson(response.body().string(), MediaSearch.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TvInfo getTvInfo(int tmdbId) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/tv/" + tmdbId)
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from Overseerr: " + response);
            }

            return gson.fromJson(response.body().string(), TvInfo.class);

        } catch (IOException e) {
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get more info about tv show: " + tmdbId);
        }
        return null;
    }

    public MovieInfo getMovieInfo(int tmdbId) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/movie/" + tmdbId)
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            return gson.fromJson(response.body().string(), MovieInfo.class);

        } catch (IOException e) {
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get more info about movie: " + tmdbId);
        }
        return null;
    }

    public MediaRequests getMediaRequests(String status, String sort, String number) {
        //lets get all of our values to something that we can actually use
        if (status == null) {
            status = "processing";
        }
        if (sort == null) {
            sort = "added";
        }
        if (number == null) {
            number = "20"; //this is the default for overseerr's api
        }

        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/request?take=" + MiscUtils.urlEncode(number) + "&skip=0&filter=" + MiscUtils.urlEncode(status) + "&sort=" + MiscUtils.urlEncode(sort) + "&requestedBy=1")
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            String response1 = response.body().string();
            return gson.fromJson(response1, MediaRequests.class);

        } catch (IOException e) {
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get requests");
        }
        return null;
    }

    public long getPingTime() {
        long responseTime = -1;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/status")
                .addHeader("accept", "application/json")
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response != null) {
                response.close();
                responseTime = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("Plexi: OverseerrApiCaller").error("Unable to communicate with Overseerr!");
        }

        return responseTime;
    }

    public boolean requestMedia(String requestJSON) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/request")
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .post(RequestBody.create(requestJSON, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            return response.isSuccessful();

        } catch (IOException e) {
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to request media!");
        }
        return false;
    }

    //The Overseerr API returns pages of users instead of a json with everyone. We'll need everyone so we'll get them all
        //before passing it somewhere.
    public List<Result> getOverseerrUsers(){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/user")
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .build();

        try(Response response = client.newCall(request).execute()){
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            String response1 = response.body().string();

            //
            return gson.fromJson(response1, UserPages.class).getResults();
        }catch (Exception e){
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get the list of Overseerr Users!");
        }
        return null;
    }

    public Result getOverseerrUserPage(int skip){
        //TODO Handle large numbers of users
        return null;
    }
}
