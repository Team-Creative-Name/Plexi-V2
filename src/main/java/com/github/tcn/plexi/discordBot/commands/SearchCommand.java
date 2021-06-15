package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.ButtonManager;
import com.github.tcn.plexi.discordBot.CommandTemplate;
import com.github.tcn.plexi.discordBot.DiscordBot;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.paginators.SearchPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.Button;

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
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {

        //first lets acknowledge the command before discord gets all grumpy about it
        event.deferReply().setEphemeral(false).queue();

        //now lets go ahead and grab the search results
        EmbedManager embedManager = new EmbedManager();
        MediaSearch results = new OverseerApiCaller().Search(event.getOptions().get(0).getAsString());

        //create the paginator
        SearchPaginator paginator = new SearchPaginator(results, event);
        buttons.addListener(paginator.getId(), paginator::onButtonClick);

        //and now edit the message to show the search results we wanted and add the action row
        event.getHook().editOriginal("Search results for '"+event.getOptions().get(0).getAsString())
                .setEmbeds(embedManager.generateMediaSearchEmbed(results, 0).build())
                .setActionRows(paginator.getPaginatorButtons())
                .queue(linkReply(event, null));


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
