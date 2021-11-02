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

import com.github.tcn.plexi.utils.FixedSizeCache;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ButtonInteraction;

import java.util.EventListener;
import java.util.function.Consumer;

public class ButtonManager implements EventListener {
    private final FixedSizeCache<String, Consumer<? super ButtonInteraction>> listeners = new FixedSizeCache<>(100);

    public void onEvent(GenericEvent event)
    {
        if (event instanceof ButtonClickEvent)
            onButton((ButtonClickEvent) event);
    }

    private void onButton(ButtonClickEvent event){
        Consumer<? super ButtonInteraction> callback = listeners.find(prefix -> event.getComponentId().startsWith(prefix));
        if(callback == null){
            event.reply("This menu timed out!").setEphemeral(true).queue();
            return;
        }
        event.deferEdit().queue();
        callback.accept(event);
    }

    public void addListener(String prefix, Consumer<? super ButtonInteraction> callback){
        listeners.add(prefix, callback);
    }

}
