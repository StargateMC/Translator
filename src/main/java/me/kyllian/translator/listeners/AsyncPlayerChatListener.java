package me.kyllian.translator.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import me.kyllian.translator.TranslatorPlugin;
import me.kyllian.translator.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;

import java.util.ArrayList;
import me.kyllian.translator.utils.Language;
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
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        event.getRecipients().forEach(test -> {

        });

        ArrayList<Player> clonedList = new ArrayList<>(event.getRecipients());

        clonedList.forEach(recipient -> {
            if (player instanceof Player) return;
            if (player == recipient) return;
            if (!plugin.getDataHandler().getData().getBoolean(player.getUniqueId().toString() + ".enabled", true)) return;
            if (recipient != player) event.getRecipients().remove(recipient);
            PlayerData recipientData = plugin.getPlayerData(recipient.getUniqueId());
            if (recipientData.getLanguage().equals(Language.en) || recipientData.getLanguage().equals(Language.unknown)) return;
            try {
                String translatedMessage = playerData.getLanguage() == recipientData.getLanguage() ?
                        event.getMessage() :
                        plugin.getTranslationHandler().translate(event.getMessage(), playerData.getLanguage(), recipientData.getLanguage(), plugin.getApiKey());
                recipient.sendMessage(event.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", translatedMessage));
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }
}