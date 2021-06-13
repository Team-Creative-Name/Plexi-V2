package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.CommandTemplate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.dv8tion.jda.internal.requests.restaction.operator.FlatMapRestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class PingCommand extends CommandTemplate {

    public PingCommand(){
        registerDefaultSlashCommand();
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, "Ping: ...", m -> m.editMessage("Ping: " + message.getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS) + "ms").queue());
    }


    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        long time = System.currentTimeMillis();

        reply(event, "test",event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time));
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
