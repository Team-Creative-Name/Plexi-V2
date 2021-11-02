/*
 *  Copyright (C) 2021 Team Creative Name, https://github.com/Team-Creative-Name
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.tcn.plexi.discordBot.eventHandlers;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.commands.*;
import com.github.tcn.plexi.discordBot.DiscordBot;
import com.github.tcn.plexi.utils.MiscUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandHandler extends ListenerAdapter {

    private final Set<CommandTemplate> commandSet = ConcurrentHashMap.newKeySet();
    private final ExecutorService commandPool = Executors.newCachedThreadPool(MiscUtils.newThreadFactory("Command-Runner", false));
    private final ButtonManager buttonManager = new ButtonManager();


    public CommandHandler(){
        //register commands here
        registerCommand(new PingCommand());
        registerCommand(new HelpCommand());
        registerCommand(new SearchCommand(buttonManager));
        registerCommand(new PingCommand());
        registerCommand(new RequestCommand());
        registerCommand(new InfoCommand());
        registerCommand(new ViewRequestsCommand(buttonManager));

        //Log Command Loading
        LoggerFactory.getLogger("Plexi: Commands").info("loaded " + commandSet.size() + " commands!");

        //now we need to register the slash commands
        List<CommandData> commandList = new ArrayList<>();
        for(CommandTemplate command : commandSet){
            if(command.getSlashCommand() != null){
                commandList.add(command.getSlashCommand());
            }
        }
        DiscordBot.getInstance().getJDAInstance().updateCommands().addCommands(commandList).queue();

        //Log Slash Command Loading
        LoggerFactory.getLogger("Plexi: Commands").info("loaded " + commandList.size() + " Slash Commands!");
    }


    @Override
    public void onButtonClick(ButtonClickEvent event){

        //the trash button should ALWAYS remove the buttons. Handle that and escape method if found
        if(event.getButton().getId().equals("endButton")){
            event.deferEdit().setActionRows().queue();
            return;
        }

        buttonManager.onEvent(event);
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
                    LoggerFactory.getLogger("Plexi: Commands").info(event.getAuthor().getName() + " has used the " + command.getCommandName() + " chat command!");
                    executeChatCommand(command, command.getCommandName(), prefix, rawMessage, event );
                }else{
                    //check to see if this matches any of the command aliases instead
                    for(String commandAlias : command.getAliases()){
                        if(rawMessage.toLowerCase().startsWith(prefix.toLowerCase() + commandAlias.toLowerCase() + ' ') || rawMessage.equalsIgnoreCase(prefix + commandAlias)){
                            LoggerFactory.getLogger("Plexi: Commands").info(event.getAuthor().getName() + " has used the " + command.getCommandName() + " chat command!");
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
                LoggerFactory.getLogger("Plexi: Commands").info(event.getUser().getName() + " has used the " + command.getCommandName() + " slash command!");
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
                LoggerFactory.getLogger("Plexi: CommandHandler").error("Unable to handle \"" +alias +"\" chat command! " + e.getLocalizedMessage());

                //Along with that message, we should inform the person running the bot of this issue and how to report it if neccessary
                event.getMessage().reply("Sorry, I was unable to finish executing that command. Please try again later").queue();
                sendErrorReport(e, message, false);
            }
        });
    }

    private void executeSlashCommand(CommandTemplate template, SlashCommandEvent event){
        this.commandPool.submit(() ->{
            try{
                template.executeSlashCommand(event);
            }catch (Exception e){
                LoggerFactory.getLogger("Plexi: CommandHandler").error("Unable to handle \"" + event.getName() +"\" slash command! " + e.getLocalizedMessage() );
                if(!event.isAcknowledged()){
                    event.reply("Sorry, I was unable to finish executing that command. Please try again later.").queue();
                }else{
                    event.getHook().editOriginal("Sorry, I was unable to finish executing that command. Please try again later.").setActionRows().setEmbeds().queue();

                }
                sendErrorReport(e, event.getName(), true);
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

    public Set<CommandTemplate> getCommandSet(){
        return Collections.unmodifiableSet(new HashSet<>(commandSet));
    }

    private void sendErrorReport(Exception e, String commandName, boolean isSlashCommand){
        //just return if the owner has turned this feature off via the config file
        if(!Settings.getInstance().sendErrorReports()){
            return;
        }
        Settings settings = Settings.getInstance();
        StringBuilder reportString = new StringBuilder();

        String versionNumber = settings.getVersionNumber() + " ["+ settings.getBranchName() + ":" + settings.getParentHash() +"]";

        reportString.append("Sorry, Plexi ").append(versionNumber)
                .append(" has run into an issue while executing a command. Please see below for more information.\n\n");

        reportString.append("```\n");

        if(isSlashCommand){
           reportString.append("Command Type: Slash Command\n\n");
        }else{
            reportString.append("Command Type: Chat Command\n\n");
        }

        reportString.append("Command: ")
                .append(commandName)
                .append("\n\n");

        reportString.append("Exception Name: ")
                .append(e.getLocalizedMessage())
                .append("\n\n");

        reportString.append("Stack Trace:\n")
                .append(MiscUtils.formatStackTraceAsString(e.getStackTrace()));

        reportString.append("```");

        reportString.append("\n\nPlease report this bug to our github repo: https://github.com/Team-Creative-Name/Plexi-V2/issues/new/choose");


        DiscordBot.getInstance().getJDAInstance().getUserById(Settings.getInstance().getOwnerID()).openPrivateChannel().flatMap(
                channel -> channel.sendMessage(reportString.toString())

        ).queue();



    }
}