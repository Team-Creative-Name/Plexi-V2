package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordBot {
    private static DiscordBot botObj = null;
    private CommandHandler handler = null;
    private JDA botInstance = null;

    //we lock the constructor so the bot cant be created multiple times
    private DiscordBot(){}

    public static DiscordBot getInstance(){
        //if there is no plexi obj, create one.
        if (botObj == null) {
            botObj = new DiscordBot();
        }
        //return the bot object
        return botObj;
    }

    public JDA getJDAInstance(){
        if(botInstance == null){
            return null;
        }
        return botInstance;
    }

    public CommandHandler getCommandHandler(){
        return handler;
    }

    public boolean isRunning() {
        return botInstance != null;
    }

    public void startBot() {
        //get instance of settings class
        Settings settings = Settings.getInstance();

        //Create JDA bot instance
        JDA botInstance;

        try {
            botInstance = JDABuilder.createDefault(settings.getDiscordToken()).build();
        } catch (Exception e) {
            Settings.getInstance().getLogger().warn("Unable to start discord bot: " + e.getLocalizedMessage() );
            return;
        }
        try {
            botInstance.awaitReady();
            Settings.getInstance().getLogger().info("Startup Complete!");
        } catch (InterruptedException e) {
            Settings.getInstance().getLogger().error("Discord element interrupted while starting. Error: " + e.getLocalizedMessage());
            stopBot();
        }
        //set global botInstance obj to the newly created one
        this.botInstance = botInstance;
        botInstance.getPresence().setActivity(Activity.watching("some movies"));
        this.handler = new CommandHandler();
        botInstance.addEventListener(handler);

    }

    public void stopBot() {
        //ensure that there is a bot instance running
        if (botInstance != null) {
            botInstance.getRegisteredListeners().forEach(botInstance::removeEventListener);
            botInstance.shutdownNow();
            //remove reference to other bot
            botInstance = null;
            Settings.getInstance().getLogger().info("Discord Shutdown Complete");
        } else {
            //we cant call the settings file version of the logger bc the settings file is calling this method on startup
            Logger plexiLogger = LoggerFactory.getLogger("Plexi");
            plexiLogger.error("Unable to shut down discord connection: no bot is currently running");
        }
    }

}
