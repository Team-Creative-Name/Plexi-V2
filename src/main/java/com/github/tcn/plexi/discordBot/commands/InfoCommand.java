package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import com.github.tcn.plexi.overseerr.templates.movieInfo.MovieInfo;
import com.github.tcn.plexi.overseerr.templates.tvInfo.TvInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.graalvm.compiler.lir.amd64.AMD64BinaryConsumer;

public class InfoCommand extends CommandTemplate{

    public InfoCommand(){
        //add slash command with options
        CommandData command = new CommandData(getCommandName(), getHelp());

        OptionData option = new OptionData(OptionType.STRING, "media-type", "The type of media you are requesting", true);
        option.addChoice("television", "tv")
                .addChoice("movie", "movie");

        command.addOptions(option)
                .addOption(OptionType.INTEGER, "tmdb_id", "TMDB ID number of the media", true);

        registerSlashCommand(command);

        //add alias
        aliases.add("i");

    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
       String[] args = content.split(" ", 2);
       //check to see if there are actually 2 args
        if(args.length != 2){
            message.reply("malformed Command").queue();
            return;
        }

        try{
            //check the media type
            if(args[0].toLowerCase().matches("tv|television|telly|tele|t")){
                MessageEmbed embed = getInfoEmbed(false, args[1]).build();
                message.reply("Info for: " + embed.getTitle()).setEmbeds(embed).queue();

            }else if(args[0].toLowerCase().matches("movie|film|feature|flick|cinematic|cine|movies|films|features|flicks|m")){
                MessageEmbed embed = getInfoEmbed(true, args[1]).build();
                message.reply("Info for: " + embed.getTitle()).setEmbeds(embed).queue();
            }else{
                message.reply("Malformed command! - Please specify `movie` or `tv`").queue();
            }
        }catch (IllegalArgumentException e){
            message.reply(e.getMessage()).queue();
        }

    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        //reply to discord
        event.deferReply().queue();

        String wow = event.getOptions().get(0).toString().toLowerCase();
        try{
            //check the media type
            if(event.getOptions().get(0).getAsString().toLowerCase().matches("tv|television|telly|tele|t")){
                MessageEmbed embed = getInfoEmbed(false, event.getOptions().get(1).getAsString()).build();
                event.getHook().editOriginal("Info for: " + embed.getTitle()).setEmbeds(embed).queue();
            }else if(event.getOptions().get(0).getAsString().toLowerCase().matches("movie|film|feature|flick|cinematic|cine|movies|films|features|flicks|m")){
                MessageEmbed embed = getInfoEmbed(true, event.getOptions().get(1).getAsString()).build();
                event.getHook().editOriginal("Info for: " + embed.getTitle()).setEmbeds(embed).queue();
            }else{
                event.getHook().editOriginal("Malformed command! - Please specify `movie` or `tv`").queue();
            }
        }catch (IllegalArgumentException e){
            event.getHook().editOriginal(e.getMessage()).queue();
        }
    }

    private EmbedBuilder getInfoEmbed(boolean isMovie, String mediaId) throws IllegalArgumentException{
        OverseerApiCaller caller = new OverseerApiCaller();
        EmbedManager eb = new EmbedManager();
        int mediaIdAsInt;
        //check to make sure the media ID is usable
        try{
            mediaIdAsInt = Integer.parseInt(mediaId);
        }catch (Exception e){
            throw new IllegalArgumentException("Media id is not an integer number");
        }
        if(isMovie){
            MovieInfo info = caller.getMovieInfo(mediaIdAsInt);
            //check to make sure there is info
            if(info != null){
                return eb.generateMovieInfoEmbed(info);
            }else{
                throw new IllegalArgumentException("Invalid movie ID");
            }
        }else{
            TvInfo info = caller.getTvInfo(mediaIdAsInt);
            //check to make sure the info exists
            if(info != null){
                return eb.generateTvInfoEmbed(info);
            }else{
                throw new IllegalArgumentException("Invalid tv ID");
            }
        }
    }


    @Override
    public String getHelp() {
        return "Gets information about a certain bit of media given the type and TMDb ID number ";
    }

    @Override
    public String getCommandName() {
        return "info";
    }
}
