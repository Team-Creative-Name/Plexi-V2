package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.EmbedManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends CommandTemplate {

    public HelpCommand() {
        registerSlashCommand();
        aliases.add("h");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, generateHelpEmbed(true).build());
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        reply(event, generateHelpEmbed(false).build(), false);
    }

    private EmbedBuilder generateHelpEmbed(boolean isChatCommand) {
        return new EmbedManager().getHelpEmbed(isChatCommand);
    }

    @Override
    public String getSlashHelp() {
        return "Shows this message with the version number, support info, and available commands.";
    }

    @Override
    public String getChatHelp() {
        return "Shows this message with the version number, support info, and available commands.\n" +
                "USAGE: " + Settings.getInstance().getPrefix() + "help";
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}
