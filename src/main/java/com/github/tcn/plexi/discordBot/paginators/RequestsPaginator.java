package com.github.tcn.plexi.discordBot.paginators;

import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

public class RequestsPaginator extends Paginator{

    final MediaRequests REQUESTS;


    public RequestsPaginator(Message message, SlashCommandEvent event, long userId, int numberOfPages, boolean wrap, ButtonManager buttonManager, MediaRequests requests) {
        super(message, event, userId, numberOfPages, wrap, buttonManager);
        this.REQUESTS = requests;
    }

    @Override
    public void onButtonClick(ButtonInteraction interaction) {
        if(interaction.getUser().getIdLong() != USER_ID){
            return;
        }

        String[] pressedButton = interaction.getComponentId().split(":");

        if(!getID().equals(pressedButton[0])){
            if("previous".equals(pressedButton[2])){
                decPageNum();
            }else if("next".equals(pressedButton[2])){
                incPageNum();
            }else{
                //if a rouge button is pressed and we get to this point somehow, we dont need to update the page, return
                return;
            }
            //update page after we change the number
            showPage();
        }
    }

    @Override
    protected void showPage() {

        EmbedManager manager = new EmbedManager();
        if(IS_SLASH_COMMAND && sentMessage == null){
            SLASH_EVENT.getHook()
                    .editOriginal("Requests:")
                    .setEmbeds(manager.createRequestEmbed(REQUESTS, currentPage).build())
                    .setActionRows(getPaginatorButtons())
                    .queue(message -> sentMessage = message);

        }else{
            //check to see if we have already responded once
            if(sentMessage == null){
                Message toSend = new MessageBuilder()
                        .setEmbeds(manager.createRequestEmbed(REQUESTS, currentPage).build())
                        .append("Requests:")
                        .setActionRows(getPaginatorButtons())
                        .build();
                MESSAGE.reply(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
            }else{
                sentMessage.editMessageEmbeds(manager.createRequestEmbed(REQUESTS, currentPage).build()).queue();
            }
        }
    }

    @Override
    protected Button getPreviousButton() {
        return Button.primary(getID() + ":previous", "◀️ Go Left");
    }

    @Override
    protected Button getSelectButton() {
        return null;
    }

    @Override
    protected Button getRightButton() {
        return Button.primary(getID() + ":next", "Go Right ▶️️");
    }

    public static class Builder extends Paginator.Builder<RequestsPaginator.Builder, RequestsPaginator>{
        private int numOfPages;
        private MediaRequests requests;



        @Override
        public RequestsPaginator build() {
            //validate stuff
            if(!runChecks()){
                throw new IllegalArgumentException("Cannot build, invalid arguments!");
            }

            //calculate the number of pages
            numOfPages = requests.getResults().size();
            return new RequestsPaginator(this.MESSAGE, this.EVENT, this.USER_ID, this.numOfPages, this.WRAP, this.BUTTON_MANAGER, this.requests);
        }

        @Override
        protected boolean runAdditionalChecks() {
            if(requests == null){
                throw new IllegalArgumentException("It seems that there aren't any requests. Use the request command to request something!");
            }
            return true;
        }


        public final RequestsPaginator.Builder setRequests(MediaRequests requests){
            this.requests = requests;
            return this;
        }

    }
}
