package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.users.UserPages;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class LinkCommand extends CommandTemplate{

    public LinkCommand(){
        CommandData command = new CommandData(getCommandName(), getSlashHelp());
        command.addOption(OptionType.STRING, "plex_email", "The email address you use for Plex", true);

        registerSlashCommand(command);
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        reply(event, "sup", false);
        OverseerApiCaller caller = new OverseerApiCaller();
        UserPages Users = caller.getOverseerrUsers();

        System.out.println("BREAK HERE");
    }

    @Override
    public String getSlashHelp() {
        return "Links your discord account to your plex account";
    }

    @Override
    public String getChatHelp() {
        return "Links your discord account to your Plex account.\n" +
                "USAGE: " + Settings.getInstance().getPrefix() + "link {plex_email_address}";
    }

    @Override
    public String getCommandName() {
        return "link";
    }
}
