package com.github.tcn.plexi.discordBot.paginators;

import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

import java.util.concurrent.ExecutionException;

public class SearchPaginator {
    //We must lazy load this paginator due to to large number of API calls we would have to make otherwise
    private final MediaSearch searchResults;
    private final Message message; //the message that we need to keep track of
    private final SlashCommandEvent event;
    private final long userID; //the user that we should bind to
    private int currentPage = 1; //by default, we will start on the 1st page (index zero of searchResults.getResults)

    //by only working on the Message object, we should be able to use this paginator from a slash or text command
    public SearchPaginator(MediaSearch searchResults, Message message) {
        this.searchResults = searchResults;
        this.message = message;
        this.event = null;
        this.userID = message.getAuthor().getIdLong();
    }

    public SearchPaginator(MediaSearch searchResults, SlashCommandEvent event){
        this.searchResults = searchResults;
        this.message = null;
        this.event = event;
        this.userID = event.getUser().getIdLong();
    }

    //gets a unique id based on the user and message/event id
    public String getId() {

        if(message != null){
            //one for the message
            return message.getId() + ":" + userID;
        }else{
            //one for the event

            //TODO: MAKE THIS CRASH LESS
            //System.out.println("User ID: " + event.getHook().retrieveOriginal().submit().get().getId());
            return event.getHook().retrieveOriginal().complete().getId() + ":" + userID;

        }
    }

    private long getEventID(){
        if(message != null){
            //one for the message
            return Long.parseLong(message.getId());
        }else{
            //one for the event
            return Long.parseLong(event.getId());
        }
    }

    //handle the different button presses
    public void onButtonClick(ButtonInteraction interaction){
        //make sure the person who clicked the button is the same one who initiated the search
        //also make sure the message being reacted to is the one that this paginator cares about
        //if(interaction.getUser().getIdLong() == userID &&(interaction.getMessage().getIdLong() == getEventID())) {

            if (interaction.getUser().getIdLong() == userID) {
                System.out.println("This passed!");
                //we need to split the button ID and check to make sure that this is the same message ID
                String[] buttonName = interaction.getComponentId().split(":");
                System.out.println("pause");
                if (!buttonName[0].equals(getId())) {
                    System.out.println("Button Name: " + buttonName[1]);
                    //now just go through the buttons that we support and see which one was clicked
                    if (buttonName[2] == null) {//A link button or something - we dont want it
                        return;
                    } else if (buttonName[2].equals("previous")) {
                        //interaction.deferReply(false).queue();
                        interaction.getHook().editOriginalEmbeds(getPreviousPage().build()).queue();
                    } else if (buttonName[2].equals("next")) {
                        //interaction.deferReply(false).queue();
                        interaction.getHook().editOriginalEmbeds(getNextPage().build()).queue();
                    } else if (buttonName[2].equals("select")) {
                        //TODO: enter the submenu
                        interaction.getHook().editOriginal("NOT IMPLEMENTED").setEmbeds().queue();
                        //interaction.reply("This button is not yet implemented!").setEphemeral(true).queue();
                    }else{
                        //interaction.reply("This button does not exist!!! HAHAHAHAHAHAHAHAHAHAHAHAHAHAHA").setEphemeral(true).queue();
                        interaction.getHook().editOriginal("That button does not exist!").setEmbeds().queue();
                    }
                }
            }
        //}
    }

    private EmbedBuilder getPreviousPage(){
        //is there a previous page?
        EmbedManager eb = new EmbedManager();
        if(currentPage < 0){
            return eb.generateMediaSearchEmbed(searchResults, currentPage - 1);
        }else{
            //wrap if we are at the first page
            currentPage = searchResults.getResults().size() - 1;
            return eb.generateMediaSearchEmbed(searchResults, currentPage);
        }
    }

    private EmbedBuilder getNextPage(){
        //is there a next page?
        EmbedManager eb = new EmbedManager();
        if(currentPage < searchResults.getResults().size() - 1){
            return eb.generateMediaSearchEmbed(searchResults, currentPage++);
        }else{
            //wrap it if we are on the last page
            currentPage = 0;
            return eb.generateMediaSearchEmbed(searchResults, currentPage);
        }
    }

    private void EnterSubPaginator(){
        //first generate the embed for the submenu

    }



    //these methods give us the buttons with the message ID that we will need
    private Button getPreviousButton(){
        return Button.primary(getId() + ":previous", "Go Left");
    }

    private Button getSelectButton(){
        return Button.success(getId() + ":select", "Select This");
    }

    private Button getRightButton(){
        return Button.success(getId() + ":next", "Go Right");
    }


    public ActionRow getPaginatorButtons(){
        return ActionRow.of(
          getPreviousButton(),
          Button.danger("endButton", Emoji.fromUnicode("\uD83D\uDDD1️")),
          getSelectButton(),
          getRightButton()
        );
    }

}
