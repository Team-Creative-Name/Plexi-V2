/*
 *  Copyright (C) 2021 Team Creative Name, https://github.com/Team-Creative-Name
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.tcn.plexi.overseerr;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import com.github.tcn.plexi.utils.MiscUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OverseerApiCaller {

    public MediaSearch Search(String query){
        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() +"/api/v1/search?query=" + MiscUtils.urlEncode(query))
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

    public MediaRequests getMediaRequests(String status, String sort, String number){
        //lets get all of our values to something that we can actually use
        if(status == null){
            status = "processing";
        }
        if(sort == null){
            sort = "added";
        }
        if(number == null){
            number = "20"; //this is the default for overseerr's api
        }

        OkHttpClient client = new OkHttpClient();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/request?take=" + MiscUtils.urlEncode(number) + "&skip=0&filter=" + MiscUtils.urlEncode(status) + "&sort=" + MiscUtils.urlEncode(sort)+ "&requestedBy=1")
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            String response1 = response.body().string();
            return gson.fromJson(response1, MediaRequests.class);

        }catch (IOException e){
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to get requests");
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

    public boolean requestMedia(String requestJSON){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Settings.getInstance().getOverseerrUrl() + "/api/v1/request")
                .addHeader("x-api-key", Settings.getInstance().getOverseerrKey())
                .addHeader("accept", "application/json")
                .post(RequestBody.create(requestJSON, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response from overseerr: " + response);
            }
            return response.isSuccessful();

        }catch (IOException e){
            LoggerFactory.getLogger("Plexi: Overseerr-API").error("Unable to request media!");
        }
        return false;
    }
}
