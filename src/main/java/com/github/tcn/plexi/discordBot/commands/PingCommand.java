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

package com.github.tcn.plexi.discordBot.commands;

import com.github.tcn.plexi.discordBot.EmbedManager;
import com.github.tcn.plexi.overseerr.OverseerApiCaller;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;


public class PingCommand extends CommandTemplate {

    public PingCommand(){
        registerSlashCommand();
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        EmbedManager eb = new EmbedManager();
        OverseerApiCaller apiCaller = new OverseerApiCaller();
        JDA jda = event.getJDA();
        try{
            reply(event, eb.createPingEmbed(jda.getGatewayPing(), jda.getRestPing().submit().get(), apiCaller.getPingTime()).build());
        }catch (Exception e){
            LoggerFactory.getLogger("Plexi: PingCommand").error("Unable to calculate ping time for one or more items: " + e.getLocalizedMessage());
        }
    }


    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        EmbedManager eb = new EmbedManager();
        OverseerApiCaller apiCaller = new OverseerApiCaller();
        JDA jda = event.getJDA();
        try{
            reply(event, eb.createPingEmbed(jda.getGatewayPing(), jda.getRestPing().submit().get(), apiCaller.getPingTime()).build());
        }catch (Exception e){
            LoggerFactory.getLogger("Plexi: PingCommand").error("Unable to calculate ping time for one or more items: " + e.getLocalizedMessage());
        }

    }


    @Override
    public String getSlashHelp() {
        return "Gets Plexi's ping time to the various enabled apis";
    }

    @Override
    public String getChatHelp() {
        return getSlashHelp();
    }

    @Override
    public String getCommandName() {
        return "ping";
    }
}
