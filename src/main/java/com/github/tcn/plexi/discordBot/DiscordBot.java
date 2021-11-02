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

package com.github.tcn.plexi.discordBot;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.discordBot.eventHandlers.CommandHandler;
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
