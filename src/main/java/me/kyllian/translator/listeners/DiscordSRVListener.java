/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kyllian.translator.listeners;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import me.kyllian.translator.TranslatorPlugin;
import me.kyllian.translator.utils.Language;

/**
 *
 * @author draks
 */
public class DiscordSRVListener {
    
    private TranslatorPlugin plugin;
    
    public DiscordSRVListener(TranslatorPlugin pl) {
        this.plugin = pl;
    }
    @Subscribe
    public void onDiscordSent(GameChatMessagePreProcessEvent event) {
            try {
                String translatedMessage = plugin.getTranslationHandler().translate(event.getMessage(), Language.unknown, Language.en, plugin.getApiKey());
                event.setMessage(translatedMessage);
            } catch (Exception exc) {
                System.out.println("Failed to translate message going to discord to english: " + event.getMessage());
                exc.printStackTrace();
            }
    }
}
