package me.kyllian.translator.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import me.kyllian.translator.TranslatorPlugin;
import me.kyllian.translator.utils.Language;
import me.kyllian.translator.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private TranslatorPlugin plugin;

    public PlayerJoinListener(TranslatorPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public String getLanguage(Player p){
        try {
            Object ep = getMethod("getHandle", p.getClass()).invoke(p, (Object[]) null);
            Field f = ep.getClass().getDeclaredField("locale");
            f.setAccessible(true);
            String language = (String) f.get(ep);
            return language;
        } catch (Exception e) {
            return null;
        }
    }
    private Method getMethod(String name, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) return m;
        }
        return null;
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        //if (player.hasPermission("autocast.update") && plugin.isUpdateCheck()) player.sendMessage(plugin.getUpdateChecker().getUpdateMessage());
        String playerLanguage = getLanguage(player);
        boolean determinedLanguage = (playerLanguage != null);
        String language = plugin.getDataHandler().getData().getString(player.getUniqueId().toString() + ".language", (determinedLanguage ? playerLanguage : "en"));
        try {            
            playerData.setLanguage(Language.valueOf(language));
            plugin.getDataHandler().getData().set(player.getUniqueId().toString() + ".language", language); // Default to locale.            
            player.sendMessage(ChatColor.GOLD + "Language loaded as: " + ChatColor.GREEN + playerData.getLanguage().name() + ChatColor.GOLD + ". Change it with the /language command.");
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Language failed to load. Defaulted to English, Set it with the /language command.");
            playerData.setLanguage(Language.en);
            plugin.getDataHandler().getData().set(player.getUniqueId().toString() + ".language", playerData.getLanguage().name()); 
        }
    }
}