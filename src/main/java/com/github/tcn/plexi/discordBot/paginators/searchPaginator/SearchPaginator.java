package com.github.tcn.plexi.discordBot.paginators.searchPaginator;

import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.paginators.Paginator;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.search.Result;
import com.github.tcn.plexi.utils.Misc;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

public class SearchPaginator extends Paginator {
    final MediaSearch SEARCH_RESULTS;
    private final SearchSubmenu.Builder submenuBuilder = new SearchSubmenu.Builder();



    public SearchPaginator(Message message, SlashCommandEvent event, long userId, int numberOfPages, boolean wrap, MediaSearch searchResults, ButtonManager buttonManager){
        super(message, event, userId, numberOfPages, wrap, buttonManager);
        BUTTON_MANAGER.addListener(getID(), this::onButtonClick );
        this.SEARCH_RESULTS = searchResults;
        //we should set up the submenu
        submenuBuilder.setButtonManager(buttonManager);
    }


    @Override
    public void onButtonClick(ButtonInteraction interaction) {
        if (interaction.getUser().getIdLong() == USER_ID) {
            //we need to split the button ID and check to make sure that this is the same message ID
            String[] buttonName = interaction.getComponentId().split(":");

            if (!buttonName[0].equals(getID())) {
                //now just go through the buttons that we support and see which one was clicked
                if (buttonName[2].equals("previous")){
                    decPageNum();
                } else if (buttonName[2].equals("next")){
                    incPageNum();
                } else if (buttonName[2].equals("select")){
                    enterSubmenu();

                    //we dont want to show the wrong thing so return and cancel the page update
                    return;
                }else if(buttonName[0].equals("submenuAccept")){
                    System.out.println("Submenu button!");
                }

                else{
                    //This is a button for a different message
                    return;
                }
                //show the updated page
                showPage();
            }
        }
    }

    protected void enterSubmenu(){
        //get a reference to the search result
        Result result = SEARCH_RESULTS.getResults().get(currentPage);

        //finish setting up the submenu
        if(IS_SLASH_COMMAND && sentMessage == null){
            submenuBuilder.SetSlashCommand(SLASH_EVENT);
        }else{
            submenuBuilder.SetMessage(this.sentMessage);
        }
        submenuBuilder.setUserId(USER_ID);
        SearchSubmenu submenu = submenuBuilder.setSearchResults(result).build();

        //show the submenu
        submenu.paginate(0);
    }




    @Override
    protected void showPage() {
        //if there is only one page, just go straight to the submenu
        if(NUMBER_OF_PAGES == 1){
            enterSubmenu();
            return;
        }

        EmbedManager manager = new EmbedManager();
        if(IS_SLASH_COMMAND && sentMessage == null){
            SLASH_EVENT.getHook()
                    .editOriginal("Search Results")
                    .setEmbeds(manager.generateMediaSearchEmbed(SEARCH_RESULTS, currentPage).build())
                    .setActionRows(getPaginatorButtons())
                    .queue(message -> sentMessage = message);

        }else{
            //check to see if we have already responded once
            if(sentMessage == null){
                Message toSend = new MessageBuilder()
                        .setEmbeds(manager.generateMediaSearchEmbed(SEARCH_RESULTS, currentPage).build())
                        .append("Search Results")
                        .setActionRows(getPaginatorButtons())
                        .build();
                MESSAGE.reply(toSend).mentionRepliedUser(false).queue(message -> sentMessage = message);
            }else{
                sentMessage.editMessageEmbeds(manager.generateMediaSearchEmbed(SEARCH_RESULTS, currentPage).build()).queue();
            }
        }
    }

    @Override
    protected Button getPreviousButton() {
        return Button.primary(getID() + ":previous", "◀️ Go Left");
    }

    @Override
    protected Button getSelectButton() {
        return Button.success(getID() + ":select", "Select This");
    }

    @Override
    protected Button getRightButton() {
        return Button.primary(getID() + ":next", "Go Right ▶️️");
    }


    public static class Builder extends Paginator.Builder<SearchPaginator.Builder, SearchPaginator>{
        private int numOfPages;
        private MediaSearch searchResults = null;


        @Override
        public SearchPaginator build() {
            //validate stuff
            if(!runChecks()){
                throw new IllegalArgumentException("Cannot build, invalid arguments!");
            }
            //strip out the people from the search result
            Misc.stripActors(searchResults);
            //calculate the number of pages
            numOfPages = searchResults.getResults().size();
            return new SearchPaginator(this.MESSAGE, this.EVENT, this.USER_ID, this.numOfPages, this.WRAP, this.searchResults, this.BUTTON_MANAGER);
        }

        @Override
        protected boolean runAdditionalChecks() {
            if(searchResults == null){
                throw new IllegalArgumentException("You MUST provide search results!");
            }
            return true;
        }


        public final Builder setSearchResults(MediaSearch searchResults){
            this.searchResults = searchResults;
            return this;
        }

    }
}
