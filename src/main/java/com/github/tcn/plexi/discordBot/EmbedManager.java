package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
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

    public EmbedBuilder getHelpEmbed(){
        System.out.println("Start");

            String prefix = Settings.getInstance().getPrefix();
            Set<CommandTemplate> commandSet = DiscordBot.getInstance().getCommandHandler().getCommandSet();

            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(new Color(0x00AE86))
                    .setTitle("Help - Plexi Commands")
                    .setDescription("For additional help with Plexi, please contact " + DiscordBot.getInstance().getJDAInstance().getUserById(Settings.getInstance().getOwnerID()) +
                            "\n And check out [Plexi on Github](https://github.com/Team-Creative-Name/plexi-V2)")
                    .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());


            for(CommandTemplate command : commandSet){
                eb.addField(prefix+command.getCommandName(), "`"+command.getHelp()+"`",false);
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
            eb.setImage("https://www.themoviedb.org/t/p/original"+result.getPosterPath());
        }else{
            eb.setTitle(stringVerifier(result.getTitle(),1), getTmdbTvUrl(result.getId()));
            eb.addField("First Air Date", stringVerifier(result.getFirstAirDate(),8),true);
            eb.setImage("https://www.themoviedb.org/t/p/original"+result.getPosterPath());
        }
        eb.setDescription(stringVerifier(result.getOverview(),2));
        eb.addField("TMDB ID", Integer.toString(result.getId()),true);


        eb.setFooter(stringVerifier("Page " + (resultNum + 1) + " of " + (searchResult.getResults().size()),1));
        return eb;
    }

    //used to generate the Result Embed for a specific search result
    public EmbedBuilder generateMovieInfoEmbed(MovieInfo info){
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x00AE86))
                //.setTitle(info.getTitle(),getTmdbMovieUrl(info.getId()))
                .setTitle("E")
                .setDescription(info.getOverview())
                .setThumbnail("https://www.themoviedb.org/t/p/original" + info.getPosterPath())
                .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());

        System.out.println("EMBED!!!!");

        return eb;

    }

    public EmbedBuilder generateTvInfoEmbed(TvInfo info){
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(0x00AE86))
                .setTitle(info.getName())
                .setDescription(info.getOverview())
                .setThumbnail("https://www.themoviedb.org/t/p/original" + info.getPosterPath())
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
    // 3. TVDB ID
    // 4. Original Network
    // 5. Status - Original Language - URL - number
    // 6. Monitored
    // 7. Footer
    // 8. Date
    // 9. general error
    private String stringVerifier(String toCheck, int fieldID) {
        if (toCheck == null) {
            switch (fieldID) {
                case 1:
                    return "TITLE";
                case 2:
                    return "Error retrieving description.";
                case 3:
                    return "No TVDB ID";
                case 4:
                    return "N/A";
                case 5:
                    return "Unknown";
                case 6:
                    return "ok, how did you get this one?";
                case 7:
                    return " ";
                case 8:
                    return "unknown";
            }
            //shouldn't ever get here unless some idiot (me) forgets to add a case for a new function
            return "N/A";
        } else {
            if (fieldID == 8) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(toCheck);
                    return new SimpleDateFormat("MM/dd/yyyy").format(date);
                } catch (Exception e) {
                    return "Unknown";
                }

            }
        }
        return toCheck;
    }

    private String getTmdbTvUrl(int id) {
        return "https://www.themoviedb.org/tv/" + id;
    }

    private String getTmdbMovieUrl(int tmdbId) {
        return "https://www.themoviedb.org/movie/" + tmdbId;
    }

    private String formatStatus(String toFormat) {
        try {
            return Character.toUpperCase(toFormat.charAt(0)) + toFormat.substring(1).replaceAll("(?=[A-Z])", " ");
        } catch (NullPointerException e) {
            return null;
        }
    }

}
