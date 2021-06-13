package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import net.dv8tion.jda.api.EmbedBuilder;


import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

//This class generates embeds
public class EmbedManager {

    public EmbedBuilder getHelpEmbed(){
        System.out.println("Start");

            String prefix = Settings.getInstance().getPrefix();
            Set<CommandTemplate> commandSet = DiscordBot.getInstance().getCommandHandler().getCommandSet();

            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(new Color(0x00AE86))
                    .setTitle("Help - Plexi Commands")
                    .setDescription("For additional help with Plexi, please contact " + DiscordBot.getInstance().getJDAInstance().getUserById(Settings.getInstance().getOwnerID()))
                    .setFooter(getRandomSplash(), Settings.getInstance().getHostedIconURL());


            for(CommandTemplate command : commandSet){
                eb.addField(prefix+command.getCommandName(), command.getHelp(),false);
            }

        return eb;

    }


    //in this new implementation, the function automatically determines the size of the splash file
    private String getRandomSplash(){
        List<String> splashList= Settings.getInstance().getSplashList();

        //double check that the list has something in it
        if(splashList.size() > 0){
            //generate random number based on the current number of spashes in the file
            Random random = new Random();
            int randomNum = random.ints(0, splashList.size() +1).findFirst().getAsInt();
            return splashList.get(randomNum);
        }

        return "Error loading spashes!";
    }

}
