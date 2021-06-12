package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.CommandTemplate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.temporal.ChronoUnit;

public class PingCommand extends CommandTemplate {

    public PingCommand(){
        registerSlashCommand("ping", "gets Plexi's ping time to the discord gateway");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, "Ping: ...", m -> m.editMessage("Ping: " + message.getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS) + "ms").queue());
    }


    @Override
    public void executeSlashCommand(SlashCommandEvent event) {

    }

    @Override
    public String getHelp() {
        return "Gets Plexi's ping time to the discord gateway";
    }

    @Override
    public String getCommandName() {
        return "ping";
    }
}
