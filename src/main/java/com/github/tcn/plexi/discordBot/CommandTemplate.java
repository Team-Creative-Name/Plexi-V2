package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.utils.FixedSizeCache;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.function.Consumer;

public abstract class CommandTemplate {

    private static final FixedSizeCache<Long, TLongSet> MESSAGE_LINK_MAP = new FixedSizeCache<>(20);

    //abstract methods

    public abstract void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event);
    public abstract void executeSlashCommand(SlashCommandEvent event);
    public abstract String getHelp();
    public abstract String getCommandName();



    public String[] getAliases(){
        return new String[0];
    }

    protected void registerSlashCommand(String commandName, String help){
        DiscordBot.getInstance().getJDAInstance().upsertCommand(commandName, help).queue();
    }

    //reply methods



    protected void reply(GuildMessageReceivedEvent event, String message){
        reply(event, message, null);
    }

    protected void reply(GuildMessageReceivedEvent event, String message, Consumer<Message> successConsumer){
        reply(event, new MessageBuilder(message).build(), successConsumer);
    }

    protected void reply(GuildMessageReceivedEvent event, MessageEmbed embed){
        reply(event, embed, null);
    }

    protected void reply(GuildMessageReceivedEvent event, MessageEmbed embed, Consumer<Message> successConsumer){
        reply(event, new MessageBuilder(embed).build(), successConsumer);
    }

    protected void reply(GuildMessageReceivedEvent event, Message message){
        reply(event, message, null);
    }

    protected void reply(GuildMessageReceivedEvent event, Message message, Consumer<Message> successConsumer){
        event.getChannel().sendMessage(message).queue(linkReply(event, successConsumer));
    }



    protected void reply(SlashCommandEvent event, String message){

    }

    protected void reply(SlashCommandEvent event, Message message, Consumer<Message> successConsumer){
        event.reply(message).queue();
    }




    protected Consumer<Message> linkReply(GuildMessageReceivedEvent event, Consumer<Message> successConsumer) {
        return msg ->
        {
            linkMessage(event.getMessageIdLong(), msg.getIdLong());
            if (successConsumer != null)
                successConsumer.accept(msg);
        };
    }

    protected Consumer<Message> linkReply(SlashCommandEvent event, Consumer<Message> successConsumer){

        return msg ->{
          linkMessage(event.getResponseNumber(), msg.getIdLong());
          if(successConsumer !=null){
              //TODO: read the docs and see if this is the right way to respond to a slash command...
          }
        };
    }

    public static void linkMessage(long commandId, long responseId){
        TLongSet set;
        if(!MESSAGE_LINK_MAP.contains(commandId))
        {
            set = new TLongHashSet(2);
            MESSAGE_LINK_MAP.add(commandId, set);
        }
        else
        {
            set = MESSAGE_LINK_MAP.get(commandId);
        }
        set.add(responseId);
    }

}
