package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.commands.CommandTemplate;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.Request;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.search.Result;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import net.dv8tion.jda.api.EmbedBuilder;


import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

//This class generates embeds
public class EmbedManager {

    public EmbedBuilder getHelpEmbed(boolean isChatCommand){
            String prefix = Settings.getInstance().getPrefix();
            Set<CommandTemplate> commandSet = DiscordBot.getInstance().getCommandHandler().getCommandSet();

            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(new Color(0x00AE86))
                    .setTitle("Help - Plexi Commands")
                    .setDescription("For additional help with Plexi, please contact " + DiscordBot.getInstance().getJDAInstance().getUserById(Settings.getInstance().getOwnerID()) +
                            "\n And check out [Plexi on Github](https://github.com/Team-Creative-Name/plexi-V2)" +
                            "\n\n Options enclosed in {} are mandatory, [] are optional\n")
                    .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());


            if(!isChatCommand){
                for(CommandTemplate command : commandSet){
                    eb.addField(prefix+command.getCommandName(), "`"+command.getSlashHelp()+"`",false);
                }
            }else{
                for(CommandTemplate command : commandSet){
                    eb.addField(prefix+command.getCommandName(), "`"+command.getChatHelp()+"`",false);
                }
            }


        return eb;
    }

    //used to generate the main embed for a media search object
    public EmbedBuilder generateMediaSearchEmbed(MediaSearch searchResult, int resultNum){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0x00AE86));
        //get a reference to the obj we want so we dont have to call it every time
        Result result = searchResult.getResults().get(resultNum);

        //some things need to be different depending on media type
        if(result.getMediaType().equals("movie")){
            eb.setTitle(stringVerifier(result.getTitle(),1), getTmdbMovieUrl(result.getId()));
            eb.addField("Release Date", stringVerifier(result.getReleaseDate(),8),true);
        }else{
            eb.setTitle(stringVerifier(result.getName(),1), getTmdbTvUrl(result.getId()));
            eb.addField("First Air Date", stringVerifier(result.getFirstAirDate(),8),true);
        }
        if(result.getPosterPath() != null){
            eb.setImage("https://www.themoviedb.org/t/p/original"+result.getPosterPath());
        }else{
            eb.setImage("https://cdn.discordapp.com/attachments/592540131097837578/656822685912793088/poster.png");
        }

        eb.setDescription(stringVerifier(result.getOverview(),2));
        eb.addField("TMDB ID", stringVerifier(Integer.toString(result.getId()), 3),true);
        eb.addField("Type", stringVerifier(result.getMediaType(), 5), true);


        eb.setFooter(stringVerifier("Page " + (resultNum + 1) + " of " + (searchResult.getResults().size()),1));
        return eb;
    }

    //used to generate the Result Embed for a specific search result
    public EmbedBuilder generateMovieInfoEmbed(MovieInfo info){
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x00AE86))
                .setTitle(info.getTitle(),getTmdbMovieUrl(info.getId()))
                .setDescription(stringVerifier(info.getOverview(), 2))
                .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());

        if(info.getPosterPath() == null){
            eb.setThumbnail("https://cdn.discordapp.com/attachments/592540131097837578/656822685912793088/poster.png");
        }else{
            eb.setThumbnail("https://www.themoviedb.org/t/p/original" + info.getPosterPath());
        }

        if(info.getMediaInfo() == null){
            eb.addField("Availability: ", "Not available on plex", true)
                    .addField("Requested: ", "Not Requested", true);
        }else{
            eb.addField("Availability: ", stringVerifier(String.valueOf(info.getMediaInfo().getStatus()), 9), true)
                    .addField("Requested: ", stringVerifier(String.valueOf(info.getMediaInfo().isRequested()), 5), true);
        }

        eb.addField("Release Date: ", stringVerifier(info.getReleaseDate(),8), true)
                .addField("Original Language: ", stringVerifier(info.getOriginalLanguage(), 5), true)
                .addField("Website", stringVerifier("[Homepage]("+info.getHomepage()+")", 5), true)
                .addField("TMDb ID: ", stringVerifier(String.valueOf(info.getId()), 3), true);

        return eb;
    }

    public EmbedBuilder generateTvInfoEmbed(TvInfo info){
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x00AE86))
                .setTitle(info.getName(), getTmdbTvUrl(info.getId()))
                .setDescription(info.getOverview());

        //some bits of media info only exist if we have the show or have requested it
        if(info.getMediaInfo() == null){
            eb.addField("Availability: ", "Not Available", true)
                    .addField("Requested: ", "Not Requested", true);
        }else{
            eb.addField("Availability: ", stringVerifier(String.valueOf(info.getMediaInfo().getStatus()), 9), true);
            eb.addField("Requested: ", stringVerifier(info.getRequestStatus(), 5), true);
        }

        //make sure the poster exists
        if(info.getPosterPath() != null){
            eb.setThumbnail("https://www.themoviedb.org/t/p/original"+info.getPosterPath());
        }else{
            eb.setThumbnail("https://cdn.discordapp.com/attachments/592540131097837578/656822685912793088/poster.png");
        }

        eb.addField("Network: ", stringVerifier(info.getFirstNetwork(), 4), true)
                .addField("Number of Episodes: ", stringVerifier(String.valueOf(info.getNumberOfEpisodes()), 5), true)
                .addField("Status: ", stringVerifier(info.getStatus(), 5), true)
                .addField("Release Date: ",stringVerifier(info.getFirstAirDate(),8), true)
                .addField("Average Runtime: ", stringVerifier(info.getAvgEpisodeRuntime() + " minutes", 5), true)
                .addField("TMDb ID: ", stringVerifier(String.valueOf(info.getId()), 3), true)
                .addField("Latest Episode Air Date: ", stringVerifier(info.getLastAirDate(),8), true)
                .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());

        return eb;
    }

    public EmbedBuilder createPingEmbed(long gatewayPing, long discordPing, long overseerrPing) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0x00Ae86));
        eb.setTitle("Ping Times");
        eb.setDescription("Current ping times for all enabled APIs");
        eb.addField("Gateway", stringVerifier(gatewayPing + "ms", 5), true);
        eb.addField("Discord", stringVerifier(String.valueOf(discordPing), 5) + "ms", true);
        eb.addField("Overseerr", stringVerifier(String.valueOf(overseerrPing), 5) + "ms", true);

        return eb;
    }

    public EmbedBuilder createRequestEmbed(MediaRequests requests, int requestNum){
        Request request = requests.getResults().get(requestNum);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x00Ae86));

        if("movie".equals(request.getType())){
            eb.setTitle("TMDb ID: " + request.getMedia().getTmdbId(), getTmdbMovieUrl(request.getMedia().getTmdbId()));
        }else{
            eb.setTitle("TMDb ID: " + request.getMedia().getTmdbId(), getTmdbTvUrl(request.getMedia().getTmdbId()));
        }

        eb.addField("Requested By:", request.getRequestedBy().getDisplayName(), true)
            .addField("Media Type: ", request.getType(), true)
            .addField("Request Status: ", stringVerifier(request.getMedia().getStatus().toString(), 9), true)
            .addField("First Requested: ", stringVerifier(request.getCreatedAt(), 8), true)
            .addField("Last Updated: ", stringVerifier(request.getUpdatedAt(), 8), true)
            .setFooter("Request " + (requestNum + 1) + " of " + requests.getResults().size(), Settings.getInstance().getHostedIconURL());

        return eb;
    }

    //in this new implementation, the function automatically determines the size of the splash file
    private String getRandomSplash(){
        List<String> splashList= Settings.getInstance().getSplashList();

        //double check that the list has something in it
        if(splashList.size() > 0){
            //generate random number based on the current number of spashes in the file
            Random random = new Random();
            int randomNum = random.ints(0, splashList.size() +1).findFirst().getAsInt();
            return splashList.get(randomNum);
        }

        return "Error loading spashes!";
    }


    //Methods that clean up information
    //Field IDs-------------
    // 1. Title
    // 2. Description
    // 3. Media ID
    // 4. Original Network
    // 5. Status - Original Language - URL - number
    // 6. Monitored
    // 7. Footer
    // 8. Date
    // 9. status int
    //10. general Error
    private String stringVerifier(String toCheck, int fieldID) {
        if (toCheck == null) {
            switch (fieldID) {
                case 1:
                    return "TITLE";
                case 2:
                    return "Error retrieving description.";
                case 3:
                    return "No ID given";
                case 4:
                    return "N/A";
                case 5:
                case 8:
                case 9:
                    return "Unknown";
                case 6:
                    return "ok, how did you get this one?";
                case 7:
                    return " ";

            }
            //shouldn't ever get here unless some idiot (me) forgets to add a case for a new function
            return "N/A";
        } else if (fieldID == 8) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(toCheck);
                    return new SimpleDateFormat("MM/dd/yyyy").format(date);
                } catch (Exception e) {
                    return "Unknown";
                }
        }else if(fieldID == 9){
            switch (toCheck){
                case "2":
                    return "Pending";
                case "3":
                    return "Processing Request";
                case "4":
                    return "Partially Available";
                case "5":
                    return "Fully Available";
            }
            return "Unknown";
        }
        return toCheck;
    }

    private String getTmdbTvUrl(int id) {
        return "https://www.themoviedb.org/tv/" + id;
    }

    private String getTmdbMovieUrl(int tmdbId) {
        return "https://www.themoviedb.org/movie/" + tmdbId;
    }

}
