package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.CommandTemplate;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

//A temporary command used to demo the deferred reply event
public class SlowCommand extends CommandTemplate {

    public SlowCommand(){
        registerSlashCommand();
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, "Sorry, "+author.getName()+", this command only works with slash commands");
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        ReplyAction test = event.deferReply();
        test.queue();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.getHook().editOriginal("2+2=5").queue();
    }

    @Override
    public String getHelp() {
        return "Asks Plexi a hard math question that takes a bit to do";
    }

    @Override
    public String getCommandName() {
        return "think-a-lot";
    }
}
