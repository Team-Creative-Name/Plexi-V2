package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.RequestTemplate;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RequestCommand extends CommandTemplate{

    public RequestCommand(){
        CommandData command = new CommandData(getCommandName(), getSlashHelp());

        OptionData mediaType = new OptionData(OptionType.STRING, "media_type", "The type of media you are requesting", true);
        mediaType.addChoice("television", "tv")
                .addChoice("movie", "movie");

        command.addOptions(mediaType)
            .addOption(OptionType.INTEGER, "tmdb_id", "The TMDb ID number of the media you are requesting", true);

        registerSlashCommand(command);

        aliases.add("r");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        int mediaId;
        boolean isMovie;
        try{
            String temp;
            //get the media type (should be first argument)
            temp = content.substring(0, content.indexOf(' '));

            //lets check the media argument to make sure that its something we recognize
            if(temp.toLowerCase().matches("((m(ovie)?|film|feature|flick)s?)|(cine(matic)?)")){
                isMovie = true;
            }else if(temp.toLowerCase().matches("tv|television|telly|tele|t|s|show")){
                isMovie = false;
            }else{
                throw new IllegalArgumentException("Invalid media type!");
            }

            temp = content.substring(content.indexOf(' ') +1);
            //lets also make sure that the id is just the Id and nothing else
            mediaId = Integer.parseInt(temp);

        }catch (Exception e){
            message.reply("Malformed Command, request command should be in the format `" + Settings.getInstance().getPrefix() +"request tv|movie mediaID`").queue();
            return;
        }
        message.reply(requestMedia(isMovie, String.valueOf(mediaId))).queue();

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        event.deferReply().setEphemeral(false).queue();
        boolean isMovie = event.getOptions().get(0).getAsString().equals("movie");
        event.getHook().editOriginal(requestMedia(isMovie, event.getOptions().get(1).getAsString())).queue();
    }

    private String requestMedia(boolean isMovie, String idNum){
        OverseerApiCaller caller = new OverseerApiCaller();
        String mediaTitle;
        RequestTemplate.RequestBuilder request = new RequestTemplate.RequestBuilder()
                .setMediaType(isMovie)
                .setMediaId(Integer.parseInt(idNum));

        if(isMovie){
            //create the movie object so we can check to see if we should request
            MovieInfo info = caller.getMovieInfo(Integer.parseInt(idNum));
            if(!info.allowRequests()){
                //inform the user that we cant do that
                return "Cannot request " + info.getTitle() + ". Movie is either already requested or available on Plex";
            }
            //set the movie title
            mediaTitle = info.getTitle();
        }else{//if not movie, must be tv show
            //create tvInfo object so we can check to see if we should request
            TvInfo info = caller.getTvInfo(Integer.parseInt(idNum));
            if(!info.allowRequests()){
                //inform the user that we cant do that
                return "Cannot request " + info.getName() + ". Show is either already fully requested or fully available on Plex";
            }
            //set the show title
            mediaTitle = info.getName();
            //set missing seasons
            request.setSeasons(info.getUnrequestedSeasons());
        }
        //build request
        String requestJson = request.build();
        boolean success = caller.requestMedia(requestJson);
        if(success){
            return mediaTitle + " was successfully added to the request list!";
        }
        return "Error requesting media!";
    }

    @Override
    public String getSlashHelp() {
        return "Requests media given the TMDb id and media type";
    }

    @Override
    public String getChatHelp() {
        return "Requests media given the TMDb id and media type\nUSAGE: " + Settings.getInstance().getPrefix() + "request {tv|movie} {TMDb_ID}";
    }

    @Override
    public String getCommandName() {
        return "request";
    }
}
