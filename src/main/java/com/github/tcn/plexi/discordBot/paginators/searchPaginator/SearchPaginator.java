package com.github.tcn.plexi.discordBot.paginators.searchPaginator;

import com.github.tcn.plexi.discordBot.ButtonManager;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.paginators.Paginator;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.overseerr.templates.search.Result;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

public class SearchPaginator extends Paginator {
    final MediaSearch SEARCH_RESULTS;
    private SearchSubmenu.Builder submenubuilder = new SearchSubmenu.Builder();



    public SearchPaginator(Message message, SlashCommandEvent event, long userId, int numberOfPages, boolean wrap, MediaSearch searchResults, ButtonManager buttonManager){
        super(message, event, userId, numberOfPages, wrap, buttonManager);
        BUTTON_MANAGER.addListener(getID(), this::onButtonClick );
        this.SEARCH_RESULTS = searchResults;

        //we should set up the submenu
        submenubuilder.setButtonManager(buttonManager);
        if(IS_SLASH_COMMAND){
            submenubuilder.SetSlashCommand(SLASH_EVENT);
        }else{
            submenubuilder.SetMessage(MESSAGE);
        }
        submenubuilder.setUserId(USER_ID);

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
                    //clear the current embed and the buttons
                    //MESSAGE.getEmbeds().clear();
                    //MESSAGE.getButtons().clear();
                    //enter the new paginator
                    //TODO: Enter the new paginator
                    Result result = SEARCH_RESULTS.getResults().get(currentPage);
                    System.out.println("We have a result");
                    SearchSubmenu submenu = submenubuilder.setSearchResults(result).build();
                    System.out.println("We have a submenu!");
                    submenu.paginate(0);

                    //we dont want to show the wrong thing so return and cancel the page update
                    return;
                }else{
                    //This is a button for a different message
                    return;
                }
                //show the updated page
                showPage();
            }
        }
    }




    @Override
    protected void showPage() {
        EmbedManager manager = new EmbedManager();
        System.out.println("showing page number " + currentPage);
        if(IS_SLASH_COMMAND){
            SLASH_EVENT.getHook()
                    .editOriginal("Search Results")
                    .setEmbeds(manager.generateMediaSearchEmbed(SEARCH_RESULTS, currentPage).build())
                    .queue();
        }else{
            MESSAGE.editMessage("Search Results").queue();
            MESSAGE.getEmbeds().clear();
            MESSAGE.getEmbeds().add(manager.generateMediaSearchEmbed(SEARCH_RESULTS, currentPage).build());

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
