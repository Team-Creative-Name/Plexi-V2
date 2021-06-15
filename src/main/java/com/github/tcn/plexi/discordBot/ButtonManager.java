package com.github.tcn.plexi.discordBot;

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
        String toCheck = event.getComponentId();

        Consumer<? super ButtonInteraction> callback = listeners.find(prefix -> event.getComponentId().startsWith(prefix));
        if(callback == null){
            event.reply("This menu times out!").setEphemeral(true).queue();
            return;
        }

        event.deferEdit().queue();
        callback.accept(event);
    }

    public void addListener(String prefix, Consumer<? super ButtonInteraction> callback){
        listeners.add(prefix, callback);
        System.out.println("test");
    }

}
