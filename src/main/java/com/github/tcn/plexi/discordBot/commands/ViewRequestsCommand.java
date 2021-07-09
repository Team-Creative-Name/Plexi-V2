package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.paginators.RequestsPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.github.tcn.plexi.utils.Misc;
import com.sun.tools.javac.util.StringUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ViewRequestsCommand extends CommandTemplate{

    private final ButtonManager BUTTON_MANAGER;

    public ViewRequestsCommand(ButtonManager buttons){
        BUTTON_MANAGER = buttons;

        //this is going to be our most complex slash command yet.
        CommandData command = new CommandData(getCommandName(), getHelp());

        //all options are optional
        command.addOptions(new OptionData(OptionType.STRING, "media-type", "The type of media to filter by - Defaults to all", false)
                .addChoice("television", "tv")
                .addChoice("movie", "movie"));

        command.addOptions(new OptionData(OptionType.STRING, "status", "The status of the request - Defaults to processing", false)
                .addChoice("all", "all")
                .addChoice("approved", "approved")
                .addChoice("available", "available")
                .addChoice("pending", "pending")
                .addChoice("processing", "processing")
                .addChoice("unavailable", "unavailable"));

        command.addOptions(new OptionData(OptionType.STRING, "sort", "How to sort requests - Defaults to recently added", false)
                .addChoice("added", "added")
                .addChoice("modified", "modified"));

        command.addOptions(new OptionData(OptionType.STRING, "number", "The number of requests to show - Defaults to 20 and cannot be over 500", false));


        //register this monster of a command
        registerSlashCommand(command);

    }



    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        //This version of the command only supports one option
        String number = "20";

        if(content.matches("^-?\\d+$")){
            int testInt = Integer.parseInt(content);

            //now lets make sure that the number isnt too big
            if( testInt < 0  || 500 < testInt){
                System.out.println("Invalid number, resetting to 20");
                number = "20";
            }
            number = content;
        }

        //call the API and get all of the requests
        MediaRequests requests = new OverseerApiCaller().getMediaRequests("processing", "added", number);

        //make sure there is a result
        if(requests.getResults().isEmpty()){
            reply(event, "Sorry, there aren't any requests matching the requested parameters.");
            return;
        }

        //enter a paginator
        RequestsPaginator rp = new RequestsPaginator.Builder()
                .setRequests(requests)
                .SetMessage(message)
                .setUserId(author.getIdLong())
                .wrapPages(true)
                .setButtonManager(BUTTON_MANAGER)
                .build();
        rp.paginate(1);
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        //before doing anything, acknowledge the command so discord doesnt time out in case our api calls take a second
        event.deferReply().queue();

        String mediaType = "all";
        String status = "processing";
        String sort = "added";
        String number = "20";

        for(OptionMapping option : event.getOptions()){
            switch (option.getName().toLowerCase()){
                case "media-type":
                    mediaType = option.getAsString();
                    break;
                case "status":
                    status = option.getAsString();
                    break;
                case "sort":
                    sort = option.getAsString();
                    break;
                case "number":
                    //lets check to make sure that it is an integer

                    if(option.getAsString().matches(("^-?\\d+$"))){
                        int testInt = Integer.parseInt(option.getAsString());

                        //now lets make sure that the number isnt too big
                        if( testInt < 0  || 500 < testInt){
                            System.out.println("Invalid number, resetting to 20");
                            number = "20";
                        }
                        number = option.getAsString();
                    }
                    break;
            }
        }

        //call the API and get all of the requests
        MediaRequests requests = new OverseerApiCaller().getMediaRequests(status, sort, number);

        //make sure there is a result
        if(requests.getResults().isEmpty()){
            event.getHook().editOriginal("Sorry, there aren't any requests matching the requested parameters.").queue();
            return;
        }

        //filter the media type if set to something other than all
        if(mediaType.matches("movie|film|feature|flick|cinematic|cine|movies|films|features|flicks|m")){
            Misc.filterByType(requests, "movie");
        }else if(mediaType.matches("tv|television|telly|tele|t")){
            Misc.filterByType(requests, "tv");
        }


        //enter a paginator
        RequestsPaginator rp = new RequestsPaginator.Builder()
                .setRequests(requests)
                .SetSlashCommand(event)
                .setUserId(event.getUser().getIdLong())
                .wrapPages(true)
                .setButtonManager(BUTTON_MANAGER)
                .build();
        rp.paginate(1);
    }

    @Override
    public String getHelp() {
        return "returns all requests matching the given parameters";
    }

    @Override
    public String getCommandName() {
        return "view-requests";
    }
}
