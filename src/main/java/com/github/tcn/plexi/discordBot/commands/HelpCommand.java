package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.EmbedManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends CommandTemplate {

    public HelpCommand(){
        registerSlashCommand();
        aliases.add("h");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, generateHelpEmbed().build());
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        reply(event, generateHelpEmbed().build(),false);
    }

    private EmbedBuilder generateHelpEmbed(){
        EmbedManager manager = new EmbedManager();
        return manager.getHelpEmbed();
    }

    @Override
    public String getHelp() {
        return "Returns a list of commands and what they do";
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}
