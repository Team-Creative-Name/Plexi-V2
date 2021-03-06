package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.paginators.searchPaginator.SearchPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import com.github.tcn.plexi.utils.MiscUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.slf4j.LoggerFactory;

public class SearchCommand extends CommandTemplate {

    private final ButtonManager buttons;

    public SearchCommand(ButtonManager buttons) {
        //register buttonManager
        this.buttons = buttons;


        //This is a custom slash command and we need to register it
        CommandData command = new CommandData(getCommandName(), getSlashHelp());
        command.addOption(OptionType.STRING, "query", "The name of what you are searching for", true);

        //now add some options I guess
        OptionData mediaType = new OptionData(OptionType.STRING, "media-type", "the type of media to search for", false);
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
        String mediaName;
        if (args[0].toLowerCase().matches("tv|television|telly|tele|t") && args.length == 2) {
            LoggerFactory.getLogger("Plexi: SearchCommand").info("Searching for: \"" + args[1] + "\" in tv");
            results = new OverseerApiCaller().search(args[1]);
            MiscUtils.filterByType(results, "tv");
            mediaName = args[1];
        } else if (args[0].toLowerCase().matches("movie|film|feature|flick|cinematic|cine|movies|films|features|flicks|m") && args.length == 2) {
            LoggerFactory.getLogger("Plexi: SearchCommand").info("Searching for: \"" + args[1] + "\" in movies");
            results = new OverseerApiCaller().search(args[1]);
            MiscUtils.filterByType(results, "movie");
            mediaName = args[1];
        } else {
            LoggerFactory.getLogger("Plexi: SearchCommand").info("Searching for: \"" + content + "\" in any media type");
            results = new OverseerApiCaller().search(MiscUtils.urlEncode(content));
            MiscUtils.stripActors(results);
            mediaName = content;
        }

        //ensure that there are any results
        if ((results != null) && results.getResults().size() != 0) {
            SearchPaginator paginator = new SearchPaginator.Builder()
                    .setSearchResults(results)
                    .SetMessage(message)
                    .setUserId(author.getIdLong())
                    .wrapPages(true)
                    .setButtonManager(buttons)
                    .build();

            paginator.paginate(1);
        } else {
            event.getMessage().reply(MarkdownSanitizer.escape("No results found for the query: \"" + mediaName + "\"")).mentionRepliedUser(false).queue();
        }

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        //first lets acknowledge the command before discord gets all grumpy about it
        event.deferReply().setEphemeral(false).queue();

        //now lets go ahead and grab the search results
        MediaSearch results = new OverseerApiCaller().search(event.getOptions().get(0).getAsString());

        //filter the search results if requested
        if (event.getOptions().size() == 2) {
            MiscUtils.filterByType(results, event.getOptions().get(1).getAsString());
            LoggerFactory.getLogger("Plexi: SearchCommand").info("Searching for: \"" + event.getOptions().get(0).getAsString() + "\" in " + event.getOptions().get(1).getAsString());
        } else {
            MiscUtils.stripActors(results);
            LoggerFactory.getLogger("Plexi: SearchCommand").info("Searching for: \"" + event.getOptions().get(0).getAsString() + "\" in any media type");
        }

        //ensure that there are any results
        int test = results.getResults().size();
        if ((results != null) && results.getResults().size() != 0) {
            SearchPaginator paginator = new SearchPaginator.Builder()
                    .setSearchResults(results)
                    .SetSlashCommand(event)
                    .setUserId(event.getUser().getIdLong())
                    .wrapPages(true)
                    .setButtonManager(buttons)
                    .build();

            paginator.paginate(1);
        } else {
            event.getHook().editOriginal(MarkdownSanitizer.escape("No results found for the query: \"" + event.getOptions().get(0).getAsString() + "\"")).queue();
        }

    }

    @Override
    public String getSlashHelp() {
        return "Searches Overseerr for the requested media.";
    }

    @Override
    public String getChatHelp() {
        return "Searches Overseerr for the requested media.\n" +
                "USAGE: " + Settings.getInstance().getPrefix() + "search [tv|movie] {query}";
    }

    @Override
    public String getCommandName() {
        return "search";
    }
}
