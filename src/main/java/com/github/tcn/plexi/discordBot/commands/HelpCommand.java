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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends CommandTemplate {

    public HelpCommand(){
        registerSlashCommand();
        aliases.add("h");
    }

    @Override
    public void executeTextCommand(User author, TextChannel channel, Message message, String content, GuildMessageReceivedEvent event) {
        reply(event, generateHelpEmbed(true).build());
    }

    @Override
    public void executeSlashCommand(SlashCommandEvent event) {
        reply(event, generateHelpEmbed(false).build(),false);
    }

    private EmbedBuilder generateHelpEmbed(boolean isChatCommand){
        EmbedManager manager = new EmbedManager();
        return manager.getHelpEmbed(isChatCommand);
    }

    @Override
    public String getSlashHelp() {
        return "Returns a list of commands and what they do";
    }

    @Override
    public String getChatHelp() {
        return getSlashHelp();
    }

    @Override
    public String getCommandName() {
        return "help";
    }
}
