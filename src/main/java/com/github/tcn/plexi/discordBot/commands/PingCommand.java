package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;


public class PingCommand extends CommandTemplate {

    public PingCommand() {
        registerSlashCommand();
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        EmbedManager eb = new EmbedManager();
        OverseerApiCaller apiCaller = new OverseerApiCaller();
        JDA jda = event.getJDA();
        try {
            reply(event, eb.createPingEmbed(jda.getGatewayPing(), jda.getRestPing().submit().get(), apiCaller.getPingTime()).build());
        } catch (Exception e) {
            LoggerFactory.getLogger("Plexi: PingCommand").error("Unable to calculate ping time for one or more items: " + e.getLocalizedMessage());
        }
    }


    @Override
    public void executeSlashCommand(SlashCommandEvent slashCommandEvent) {
        EmbedManager eb = new EmbedManager();
        OverseerApiCaller apiCaller = new OverseerApiCaller();
        JDA jda = slashCommandEvent.getJDA();
        try {
            reply(slashCommandEvent, eb.createPingEmbed(jda.getGatewayPing(), jda.getRestPing().submit().get(), apiCaller.getPingTime()).build());
        } catch (Exception e) {
            LoggerFactory.getLogger("Plexi: PingCommand").error("Unable to calculate ping time for one or more items: " + e.getLocalizedMessage());
        }

    }


    @Override
    public String getSlashHelp() {
        return "Gets the current ping time to various api endpoints.";
    }

    @Override
    public String getChatHelp() {
        return "Gets the current ping time to various api endpoints.\n" +
                "USAGE: " + Settings.getInstance().getPrefix() + "ping";
    }

    @Override
    public String getCommandName() {
        return "ping";
    }
}
