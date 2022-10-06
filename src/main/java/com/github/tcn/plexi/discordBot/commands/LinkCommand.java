package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.users.UserResult;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;
import java.util.Objects;

public class LinkCommand extends CommandTemplate{

    public LinkCommand(){
        CommandData command = new CommandData(getCommandName(), getSlashHelp());
        command.addOption(OptionType.STRING, "plex_email", "The email address you use for Plex", true);

        registerSlashCommand(command);
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        event.deferReply().setEphemeral(true).queue();
        OverseerApiCaller caller = new OverseerApiCaller();
        List<UserResult> users = caller.getOverseerrUsers();

        if(null == users){
            event.getHook().editOriginal("Sorry, I cant seem to load users at the moment. Please Try again later").queue();
            return;
        }

        UserResult foundUser = null;

        //loop through user list and try to find a matching user
        for(UserResult user : users){
            if(user.getEmail().equals(event.getOptions().get(event.getOptions().size()-1).getAsString())){
                //now make another call to the API and see if they have their discord ID linked to Overseerr
                foundUser = user;
                //event.getHook().editOriginal("Successfully linked Plexi and Overseerr accounts!").queue();
                break;
            }
        }

        //if a user is found try to link them
        if(null != foundUser){
            //TODO Stuff
        }

    }


    @Override
    public String getSlashHelp() {
        return "Links your discord account to your plex account";
    }

    @Override
    public String getChatHelp() {
        return "Links your discord account to your Plex account.\n" +
                "USAGE: " + Settings.getInstance().getPrefix() + "link {plex_email_address}";
    }

    @Override
    public String getCommandName() {
        return "link";
    }
}
