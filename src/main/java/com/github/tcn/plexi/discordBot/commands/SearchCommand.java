package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.paginators.searchPaginator.SearchPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
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
        CommandData command = new CommandData(getCommandName(),getHelp());
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

        //get the search results
        MediaSearch results = new OverseerApiCaller().Search(content);

        SearchPaginator paginator = new SearchPaginator.Builder()
                .setSearchResults(results)
                .SetMessage(message)
                .setUserId(author.getIdLong())
                .wrapPages(true)
                .setButtonManager(buttons)
                .build();

        paginator.paginate(1);
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {

        //first lets acknowledge the command before discord gets all grumpy about it
        event.deferReply().setEphemeral(false).queue();

        //now lets go ahead and grab the search results
        MediaSearch results = new OverseerApiCaller().Search(event.getOptions().get(0).getAsString());


        SearchPaginator paginator = new SearchPaginator.Builder()
                .setSearchResults(results)
                .SetSlashCommand(event)
                .setUserId(event.getUser().getIdLong())
                .wrapPages(true)
                .setButtonManager(buttons)
                .build();

        paginator.paginate(1);

    }

    @Override
    public String getHelp() {
        return "Searches Overseerr for the requested media";
    }

    @Override
    public String getCommandName() {
        return "search";
    }
}
