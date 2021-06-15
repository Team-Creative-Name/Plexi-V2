package com.github.tcn.plexi.overseerr;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
            String json = response.body().string();
            MediaSearch testResult =gson.fromJson(json,MediaSearch.class);
            return testResult;

        } catch (IOException e) {
            e.printStackTrace();
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
