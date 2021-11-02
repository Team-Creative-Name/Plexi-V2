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

package com.github.tcn.plexi.discordBot.paginators.searchPaginator;

import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.paginators.Paginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.request.RequestTemplate;
import com.github.tcn.plexi.overseerr.templates.search.Result;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;

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

        //add paginator buttons
        addStopButton();
        if(canRequest){
            addButton(Button.success(getID() + ":submenuAccept", "Request"));
        }else{
            addButton(Button.success(getID() + ":submenuAccept", "Request").asDisabled());
        }

    }

    @Override
    public void onButtonClick(ButtonInteraction interaction) {
        //make sure we are still interacting with the user
        if(interaction.getUser().getIdLong() == USER_ID){
            String[] buttonName = interaction.getComponentId().split(":");

            if(!buttonName[0].equals(getID())){
                if(buttonName[2].equals("submenuAccept")){
                    //if the message we are responding to is the last message in the channel, just post a new message. It should look a lot cleaner
                    if(sentMessage.getId().equals(sentMessage.getChannel().getLatestMessageId())){
                        sentMessage.getChannel().sendMessage(requestMedia()).queue();
                    }else{
                        sentMessage.reply(requestMedia()).mentionRepliedUser(false).queue();
                    }
                    sentMessage.editMessage(sentMessage.getContentRaw()).setActionRows().queue();
                }
            }
        }
    }

    private MessageEmbed generateEmbed(){
        EmbedManager eb = new EmbedManager();
        if(isMovie){
            EmbedBuilder embed = eb.generateMovieInfoEmbed(movieInfo);
            return  embed.build();
        }else{
            return eb.generateTvInfoEmbed(tvInfo).build();
        }
    }


    @Override
    protected void showPage() {
        if(IS_SLASH_COMMAND){
            SLASH_EVENT.getHook()
                    .editOriginal(MarkdownSanitizer.escape("Getting more info for: " + searchResult.getActualTitle()))
                    .setEmbeds(infoEmbed)
                    .setActionRows(getPaginatorButtonsAsActionRow())
                    .queue(message -> sentMessage = message);
        }else if(MESSAGE.getAuthor().getId().matches(MESSAGE.getJDA().getSelfUser().getId())){ //check to see if we sent the message that we're replying to
            Message toSend = new MessageBuilder().setEmbeds(infoEmbed).append(MarkdownSanitizer.escape("Getting more info for: " + searchResult.getActualTitle()))
                    .setActionRows(getPaginatorButtonsAsActionRow())
                    .build();
            MESSAGE.editMessage(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
        }else{//since there is no other message, just make a new one
            Message toSend = new MessageBuilder().setEmbeds(infoEmbed).append(MarkdownSanitizer.escape("Getting more info for: " + searchResult.getActualTitle()))
                    .setActionRows(getPaginatorButtonsAsActionRow())
                    .build();
            MESSAGE.reply(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
        }
    }

    private String requestMedia(){
        OverseerApiCaller caller = new OverseerApiCaller();
        String mediaTitle;
        RequestTemplate.RequestBuilder request = new RequestTemplate.RequestBuilder()
                .setMediaType(isMovie);

        if(isMovie){
            if(!canRequest){
                //inform the user that we cant do that
                return MarkdownSanitizer.escape("Cannot request " + movieInfo.getTitle() + ". Movie is either already requested or available on Plex");
            }
            //set the movie title
            mediaTitle = movieInfo.getTitle();
            //set the movie ID number
            request.setMediaId(movieInfo.getId());
        }else{//if not movie, must be tv show
            if(!canRequest){
                //inform the user that we cant do that
                return MarkdownSanitizer.escape("Cannot request " + tvInfo.getName() + ". Show is either already fully requested or fully available on Plex");
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
            return MarkdownSanitizer.escape(mediaTitle + " was successfully added to the request list!");
        }
        return MarkdownSanitizer.escape("Error requesting media!");

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
