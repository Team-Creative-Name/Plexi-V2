package com.github.tcn.plexi.discordBot.paginators;

import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;


public abstract class Paginator {

    protected final Message MESSAGE; //The message that we respond to. Only exists if this paginator is in response to a text command
    protected final SlashCommandEvent SLASH_EVENT; //The slash command event we reply to if it exists
    protected final long USER_ID; //The ID of the user that we bind to
    protected final int NUMBER_OF_PAGES; //the number of pages that this paginator contains
    protected final boolean WRAP; //should we wrap at the end of the paginator?
    protected final boolean IS_SLASH_COMMAND; //are we using a slash command instead of a message?
    protected final ButtonManager BUTTON_MANAGER;

    protected int currentPage; //the result number that should be currently displayed
    protected Message sentMessage; //track the message that we sent in response to a command

    public Paginator(Message message, SlashCommandEvent event, long userId, int numberOfPages, boolean wrap, ButtonManager buttonManager){
        SLASH_EVENT = event;
        MESSAGE = message;
        USER_ID = userId;
        NUMBER_OF_PAGES = numberOfPages;
        WRAP = wrap;
        BUTTON_MANAGER = buttonManager;

        //for simplicity, lets keep a boolean value that tells us if we are using a slash command
        if(MESSAGE == null && SLASH_EVENT != null){
            IS_SLASH_COMMAND = true;
        }else if(MESSAGE != null && SLASH_EVENT == null){
            IS_SLASH_COMMAND = false;
        }else{
            throw new IllegalArgumentException("Cannot have both MESSAGE and SLASH_EVENT set in a paginator!");
        }
    }

    public abstract void onButtonClick(ButtonInteraction interaction);

    protected abstract void showPage();

    public void paginate(int pageNum){
        //validate the number and put it within the range if outside
        if(pageNum <1){
            currentPage = 1;
        }else if(pageNum > NUMBER_OF_PAGES){
            currentPage = NUMBER_OF_PAGES;
        }
        showPage();
    }

    //methods that the subclass will need
    public String getID(){
        if(IS_SLASH_COMMAND){
            return SLASH_EVENT.getHook().retrieveOriginal().complete().getId() + ":" + USER_ID;
        }
       return MESSAGE.getId() + ":" + USER_ID;
    }

    public Long getEventId(){
        if(IS_SLASH_COMMAND){
            return Long.parseLong(SLASH_EVENT.getId());
        }
        return Long.parseLong(MESSAGE.getId());
    }

    protected void incPageNum(){
        //are we at the end of the list
        if(currentPage == NUMBER_OF_PAGES - 1 && WRAP){
            currentPage = 0;
        }else{
            currentPage++;
        }
    }

    protected ActionRow getPaginatorButtons() {
        //depending on how many items are in the paginator, we will show different items
        if(NUMBER_OF_PAGES == 1){
            return ActionRow.of(
                    Button.danger("endButton", Emoji.fromUnicode("\uD83D\uDDD1️")),
                    getSelectButton()
            );
        }

        return ActionRow.of(
                getPreviousButton(),
                Button.danger("endButton", Emoji.fromUnicode("\uD83D\uDDD1️")),
                getSelectButton(),
                getRightButton()
        );
    }

    //these methods give us the buttons with the message ID that we will need

    protected abstract Button getPreviousButton();

    protected abstract Button getSelectButton();

    protected abstract Button getRightButton();



    protected void decPageNum(){
        //are we at the beginning of the list
        if(currentPage == 0 && WRAP){
            currentPage = NUMBER_OF_PAGES - 1;
        }else{
            currentPage--;
        }
    }
    @SuppressWarnings("unchecked")
    protected abstract static class Builder<T extends Builder<T,V>, V extends Paginator>{
        protected Message MESSAGE; //The message that we respond to. Only exists if this paginator is in response to a text command
        protected SlashCommandEvent EVENT; //The event that we respond to. Only exists if this paginator is in response to a slash command
        protected long USER_ID; //The ID of the user that we bind to
        protected boolean WRAP; //should we wrap at the end of the paginator?
        protected ButtonManager BUTTON_MANAGER; //the EventHandler for button events

        public abstract V build();

        protected boolean runChecks(){
            if(MESSAGE == null && EVENT == null){
                throw new IllegalArgumentException("Either message or event must be set!");
            }
            if(USER_ID == 0L){
                throw new IllegalArgumentException("User ID must be set!");
            }
            if(BUTTON_MANAGER == null){
                throw new IllegalArgumentException("The buttonManager must be set!");
            }


            return runAdditionalChecks();
        }


        protected abstract boolean runAdditionalChecks();

        public final T SetMessage(Message message){
            this.MESSAGE = message;
            return (T) this;
        }

        public final T SetSlashCommand(SlashCommandEvent event){
            this.EVENT = event;
            return (T) this;
        }

        public final T setUserId(long userId){
            this.USER_ID = userId;
            return (T) this;
        }

        public final T wrapPages(boolean wrap){
            this.WRAP = wrap;
            return (T) this;
        }

        public final T setButtonManager(ButtonManager BUTTON_MANAGER){
            this.BUTTON_MANAGER = BUTTON_MANAGER;
            return (T) this;
        }


    }


}
