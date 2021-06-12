package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.commands.PingCommand;
import com.github.tcn.plexi.utils.Misc;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandHandler extends ListenerAdapter {

    private final Set<CommandTemplate> commandSet = ConcurrentHashMap.newKeySet();
    private final ExecutorService commandPool = Executors.newCachedThreadPool(Misc.newThreadFactory("Command-Runner", false));



    public CommandHandler(){
        //register commands here
        registerCommand(new PingCommand());
    }

    @Override
    public void onButtonClick(ButtonClickEvent event){
        //TODO: Implement a button handler or something
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        //check to make sure the message is meant for us
        if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().toLowerCase().startsWith(Settings.getInstance().getPrefix())){
            String rawMessage = event.getMessage().getContentRaw();
            String prefix = Settings.getInstance().getPrefix();
            //loop through the commandSet and see if any of our commands match the one requested
            for(CommandTemplate command : commandSet){
                //check the name of the command against the one provided
                if(rawMessage.toLowerCase().startsWith(prefix.toLowerCase() + command.getCommandName().toLowerCase() + ' ') || rawMessage.equalsIgnoreCase(prefix + command.getCommandName())){
                    LoggerFactory.getLogger("Plexi: Commands").info(event.getAuthor() + " has used the " + command.getCommandName() + "chat command!");
                    executeChatCommand(command, command.getCommandName(), prefix, rawMessage, event );
                }else{
                    //check to see if this matches any of the command aliases instead
                    for(String commandAlias : command.getAliases()){
                        if(rawMessage.toLowerCase().startsWith(prefix.toLowerCase() + commandAlias.toLowerCase() + ' ') || rawMessage.equalsIgnoreCase(prefix + commandAlias)){
                            LoggerFactory.getLogger("Plexi: Commands").info(event.getAuthor() + " has used the " + command.getCommandName() + "chat command!");
                            executeChatCommand(command, commandAlias, prefix, rawMessage, event);

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event){
        //these events are significantly easier for us to handle. We know that they are meant for us
        //all we need to do is find the right command
        for(CommandTemplate command : commandSet){
            if(event.getName().equalsIgnoreCase(command.getCommandName())){
                LoggerFactory.getLogger("Plexi: Commands").info(event.getUser().getName() + "has used the " + command.getCommandName() + " slash command!");
                executeSlashCommand(command, event);
            }else{
                //we should also check to see if the commands are a sub slash command
                //TODO: Not implemented
            }
        }
    }

    public void registerCommand(CommandTemplate toRegister){
        //check to make sure the command doesnt have any spaces
        if(toRegister.getCommandName().contains(" ")){
            throw new IllegalArgumentException("Command " + toRegister.getCommandName() + "Cannot have spaces in its name!");
        }
        //check to make sure a command with the same name isn't already registered
        if(this.commandSet.stream().map(CommandTemplate::getCommandName).anyMatch(c -> toRegister.getCommandName().equalsIgnoreCase(c))){
            return;
        }
        //if these checks pass, we can add the command
        this.commandSet.add(toRegister);
    }

    private void executeChatCommand(CommandTemplate template, String alias, String prefix, String message, GuildMessageReceivedEvent event){
        this.commandPool.submit(() ->{
            try{
                final String content = stripPrefix(alias, prefix, message);
                template.executeTextCommand(event.getAuthor(), event.getChannel(), event.getMessage(),content, event);
            }catch (final Exception e){
                LoggerFactory.getLogger("Plexi: CommandHandler").error("Unable to handle \"" +alias +"\" chat command! " + e.getLocalizedMessage() );
            }
        });
    }

    private void executeSlashCommand(CommandTemplate template, SlashCommandEvent event){
        this.commandPool.submit(() ->{
            try{
                template.executeSlashCommand(event);
            }catch (Exception e){
                LoggerFactory.getLogger("Plexi: CommandHandler").error("Unable to handle \"" + event.getName() +"\" slash command! " + e.getLocalizedMessage() );
            }
        });
    }


    private String stripPrefix(String commandName, String prefix, String toStrip){
        toStrip = toStrip.substring(commandName.length() + prefix.length());
        if(toStrip.startsWith(" ")){
            toStrip = toStrip.substring(1);
        }
        return toStrip;
    }


}
