package com.github.tcn.plexi.discordBot.paginators.searchPaginator;

import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.paginators.Paginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.RequestTemplate;
import com.github.tcn.plexi.overseerr.templates.search.Result;
import com.github.tcn.plexi.overseerr.templates.tvInfo.Season;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

import java.util.ArrayList;
import java.util.List;

public class SearchSubmenu extends Paginator {
    private final Boolean isMovie;
    private final Result searchResult;
    private final MessageEmbed infoEmbed;


    private TvInfo tvInfo= null;
    private MovieInfo movieInfo = null;

    protected boolean canRequest;


    public SearchSubmenu(Message message, SlashCommandEvent event, long userId, int numberOfPages, boolean wrap, Result searchResult, ButtonManager buttonManager) {
        super(message, event, userId, numberOfPages, wrap, buttonManager);
        isMovie = searchResult.getMediaType().equalsIgnoreCase("movie");
        this.searchResult = searchResult;
        BUTTON_MANAGER.addListener(getID(), this::onButtonClick);

        //now lets get the info object depending on what media type this is
        OverseerApiCaller caller = new OverseerApiCaller();
        if(isMovie){
            movieInfo = caller.getMovieInfo(searchResult.getId());
            canRequest = movieInfo.allowRequests();
        }else{
            tvInfo = caller.getTvInfo(searchResult.getId());
            canRequest = tvInfo.allowRequests();
        }

        this.infoEmbed = generateEmbed();


    }

    @Override
    public void onButtonClick(ButtonInteraction interaction) {
        //make sure we are still interacting with the user
        if(interaction.getUser().getIdLong() == USER_ID){
            String[] buttonName = interaction.getComponentId().split(":");

            if(!buttonName[0].equals(getID())){
                if(buttonName[2].equals("submenuAccept")){
                    sentMessage.reply(requestMedia()).mentionRepliedUser(false).queue();
                }
            }
        }
    }

    private MessageEmbed generateEmbed(){
        EmbedManager eb = new EmbedManager();
        if(isMovie){
            EmbedBuilder embed = eb.generateMovieInfoEmbed(movieInfo);
            System.out.println("Should have media");
            return  embed.build();
        }else{
            return eb.generateTvInfoEmbed(tvInfo).build();
        }
    }



    @Override
    protected void showPage() {
        if(IS_SLASH_COMMAND){
            SLASH_EVENT.getHook()
                    .editOriginal("Getting more info for: " + searchResult.getName())
                    .setEmbeds(infoEmbed)
                    .setActionRows(getPaginatorButtons())
                    .queue();
        }else{//since there is no other message, just make a new one
            Message toSend = new MessageBuilder()
                    .setEmbed(infoEmbed).append("Getting more info for: ").append(searchResult.getName())
                    .setActionRows(getPaginatorButtons())
                    .build();
            //MESSAGE.reply(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
            MESSAGE.editMessage(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
        }

    }

    @Override
    protected Button getPreviousButton() {
        return null; //no left button
    }

    @Override
    protected Button getSelectButton() {
        if(canRequest){
            return Button.success(getID() + ":submenuAccept", "Request");
        }
        return Button.success(getID() + ":submenuAccept", "Request").asDisabled();
    }

    @Override
    protected Button getRightButton() {
        return null; //no right button
    }

    private String requestMedia(){
        OverseerApiCaller caller = new OverseerApiCaller();
        String mediaTitle;
        RequestTemplate.RequestBuilder request = new RequestTemplate.RequestBuilder()
                .setMediaType(isMovie);

        if(isMovie){
            if(!canRequest){
                //inform the user that we cant do that
                return "Cannot request " + movieInfo.getTitle() + ". Movie is either already requested or available on Plex";
            }
            //set the movie title
            mediaTitle = movieInfo.getTitle();
            //set the movie ID number
            request.setMediaId(movieInfo.getId());
        }else{//if not movie, must be tv show
            if(!canRequest){
                //inform the user that we cant do that
                return "Cannot request " + tvInfo.getName() + ". Show is either already fully requested or fully available on Plex";
            }
            //set the show title
            mediaTitle = tvInfo.getName();
            //set missing seasons
            request.setSeasons(tvInfo.getUnrequestedSeasons());
            //get the ID number
            request.setMediaId(tvInfo.getId());
        }
        //build request
        String requestJson = request.build();
        boolean success = caller.requestMedia(requestJson);
        if(success){
            canRequest = false;
            return mediaTitle + " was successfully added to the request list!";
        }
        return "Error requesting media!";

    }


    public static class Builder extends Paginator.Builder<SearchSubmenu.Builder, SearchSubmenu>{

        private Result searchResults = null;


        @Override
        public SearchSubmenu build() {
            //validate stuff
            if(!runChecks()){
                throw new IllegalArgumentException("Cannot build, invalid arguments!");
            }

            return new SearchSubmenu(this.MESSAGE, this.EVENT, this.USER_ID, 1, this.WRAP, this.searchResults, this.BUTTON_MANAGER);
        }

        @Override
        protected boolean runAdditionalChecks() {
            if(searchResults == null){
                throw new IllegalArgumentException("You MUST provide search results!");
            }
            return true;
        }

        public final Builder setSearchResults(Result searchResult){
            this.searchResults = searchResult;
            return this;
        }
    }
}
