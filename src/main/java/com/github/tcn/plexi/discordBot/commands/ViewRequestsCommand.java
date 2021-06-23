package com.github.tcn.plexi.discordBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ViewRequestsCommand extends CommandTemplate{

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        //determine the media type the user selected

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {

    }

    @Override
    public String getHelp() {
        return "returns all requests for the given media type";
    }

    @Override
    public String getCommandName() {
        return "viewRequests";
    }
}
