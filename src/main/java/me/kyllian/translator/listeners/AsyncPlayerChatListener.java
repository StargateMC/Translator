package me.kyllian.translator.listeners;

import me.kyllian.translator.TranslatorPlugin;
import me.kyllian.translator.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import org.bukkit.event.EventPriority;

public class AsyncPlayerChatListener implements Listener {

    private TranslatorPlugin plugin;

    public AsyncPlayerChatListener(TranslatorPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ArrayList<String> names = new ArrayList<String>();
        for (Player p : event.getRecipients()) {
            names.add(p.getName());
        }
        System.out.println("Processing chat event for: " + event.getPlayer().getName() + "to recipients: " + String.join(",",names) + " with body: " + event.getMessage());
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        ArrayList<Player> clonedList = new ArrayList<>(event.getRecipients());

        clonedList.forEach(recipient -> {
            if (player == recipient) return; // If the sender is the recipient.
            if (recipient != player) event.getRecipients().remove(recipient); // If the recipient is not the player.
            PlayerData recipientData = plugin.getPlayerData(recipient.getUniqueId());
            try {
                String translatedMessage = playerData.getLanguage() == recipientData.getLanguage() ?
                        event.getMessage() :
                        plugin.getTranslationHandler().translate(event.getMessage(), playerData.getLanguage(), recipientData.getLanguage(), plugin.getApiKey());
                //recipient.sendMessage(translatedMessage);
                System.out.println("Processing chat event for: " + event.getPlayer().getName() + "to recipients: " + recipient.getName() + " with translationResult: " + translatedMessage);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }
}
