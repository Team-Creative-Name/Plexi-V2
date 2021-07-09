package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.paginators.searchPaginator.SearchPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.utils.Misc;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SearchCommand extends CommandTemplate {

    private final ButtonManager buttons;

    public SearchCommand(ButtonManager buttons){
        //register buttonManager
        this.buttons = buttons;


        //This is a custom slash command and we need to register it
        CommandData command = new CommandData(getCommandName(), getSlashHelp());
        command.addOption(OptionType.STRING,"query", "The name of what you are searching for", true);

        //now add some options I guess
        OptionData mediaType = new OptionData(OptionType.STRING, "media-type", "the type of media to search for",false);
        mediaType.addChoice("television", "tv");
        mediaType.addChoice("movie", "movie");
        command.addOptions(mediaType);

        registerSlashCommand(command);
        aliases.add("s");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {

        MediaSearch results;
        //lets determine if the user specified a media type
        String[] args = content.split(" ", 2);
        if(args[0].toLowerCase().matches("tv|television|telly|tele|t") && args.length == 2){
            System.out.println("Searching for: " + args[1]);
            results = new OverseerApiCaller().Search(args[1]);
            Misc.filterByType(results, "tv");
        }else if(args[0].toLowerCase().matches("movie|film|feature|flick|cinematic|cine|movies|films|features|flicks|m") && args.length == 2){
            System.out.println("Searching for: " + args[1]);
            results = new OverseerApiCaller().Search(args[1]);
            Misc.filterByType(results, "movie");
        }else{
            System.out.println("Searching for:" +content);
            results = new OverseerApiCaller().Search(content);
        }

        //ensure that there are any results
        if((results != null) && results.getTotalResults() != 0){
            SearchPaginator paginator = new SearchPaginator.Builder()
                    .setSearchResults(results)
                    .SetMessage(message)
                    .setUserId(author.getIdLong())
                    .wrapPages(true)
                    .setButtonManager(buttons)
                    .build();

            paginator.paginate(1);
        }else{
            event.getMessage().reply("No results found for the query: \""+ content+"\"").mentionRepliedUser(false).queue();
        }

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {

        //first lets acknowledge the command before discord gets all grumpy about it
        event.deferReply().setEphemeral(false).queue();

        //now lets go ahead and grab the search results
        MediaSearch results = new OverseerApiCaller().Search(event.getOptions().get(0).getAsString());

        //filter the search results if requested
        if(event.getOptions().size() == 2){
            Misc.filterByType(results, event.getOptions().get(1).getAsString());
        }

        //ensure that there are any results
        if((results != null) && results.getTotalResults() != 0){
            SearchPaginator paginator = new SearchPaginator.Builder()
                    .setSearchResults(results)
                    .SetSlashCommand(event)
                    .setUserId(event.getUser().getIdLong())
                    .wrapPages(true)
                    .setButtonManager(buttons)
                    .build();

            paginator.paginate(1);
        }else{
            event.getHook().editOriginal("No results found for the query: \""+event.getOptions().get(0).getAsString()+"\"").queue();
        }




    }

    @Override
    public String getSlashHelp() {
        return "Searches Overseerr for the requested media";
    }

    @Override
    public String getChatHelp() {
        return "Searches Overseerr for the requested media\nUSAGE: " + Settings.getInstance().getPrefix() + "[tv|movie] {query}";
    }

    @Override
    public String getCommandName() {
        return "search";
    }
}
