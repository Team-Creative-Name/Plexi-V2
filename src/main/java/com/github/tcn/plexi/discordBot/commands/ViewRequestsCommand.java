package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.discordBot.eventHandlers.ButtonManager;
import com.github.tcn.plexi.discordBot.paginators.RequestsPaginator;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.sun.tools.javac.util.StringUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
        //determine the media type the user selected
        System.out.println("AAAA"); //options[0]
        MediaRequests requests = new OverseerApiCaller().getMediaRequests("processing", "added", "20"); //options[1]
        System.out.println("BBBB"); //options [2]

        //lets change values i

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        //before doing anything, acknowledge the command so discord doesnt time out in case our api calls take a second
        event.deferReply().queue();


        String mediaType = "all";  //options[0]
        String status = "processing";  //options[1]
        String sort = "added";  //options [2]
        String number = "20"; //options[3]


        //lets see if the user provided any options
        if(event.getOptions().size() != 0){
            //check mediaType
            if(event.getOptions().get(0) != null){
                mediaType = event.getOptions().get(0).getAsString();
            }
            if(event.getOptions().get(1) != null){
                status = event.getOptions().get(1).getAsString();
            }
            if(event.getOptions().get(2) != null){
                sort = event.getOptions().get(2).getAsString();
            }
            if(event.getOptions().get(3) != null){
                //lets check to make sure that it is an integer

                if(event.getOptions().get(3).getAsString().matches("(0|[1-9]\\\\d*)")){
                    int testInt = Integer.parseInt(event.getOptions().get(3).getAsString());

                    //now lets make sure that the number isnt too big
                    if( testInt < 0  || 500 < testInt){
                        System.out.println("Invalid number, resetting to 20");
                        number = "20";
                    }
                }
            }
        }

        //call the API and get all of the requests
        MediaRequests requests = new OverseerApiCaller().getMediaRequests(status, sort, number);

        //create the embed
        EmbedManager eb = new EmbedManager();

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
        return "returns all requests for the given media type";
    }

    @Override
    public String getCommandName() {
        return "view-requests";
    }
}
