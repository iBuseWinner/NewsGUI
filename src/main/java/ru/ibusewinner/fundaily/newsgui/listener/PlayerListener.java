package ru.ibusewinner.fundaily.newsgui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.fundaily.newsgui.items.NewsUser;
import ru.ibusewinner.plugin.buseapi.BuseAPI;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NewsUser newsUser = NewsGUI.getMySQL().getNewsUser(player.getName());
        if (newsUser == null) {
            NewsGUI.getMySQL().addUser(player.getName());
            newsUser = NewsGUI.getMySQL().getNewsUser(player.getName());
        }

        NewsGUI.getCachedUsers().add(newsUser);
        AtomicBoolean hasNotReadNews = new AtomicBoolean(false);
        NewsUser finalNewsUser = newsUser;
        NewsGUI.getNewsItems().forEach(it -> {
            if (!finalNewsUser.getWatchedNews().contains(it.getId())) hasNotReadNews.set(true);
        });
        if (hasNotReadNews.get()) {
            player.sendMessage(BuseAPI.getMessageManager().convertMessage(NewsGUI.getConfigManager().getConfig().getString("has-not-read-news")));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        NewsUser newsUser = NewsGUI.getCachedUserByNickname(player.getName());
        if (newsUser != null) {
            NewsGUI.getMySQL().updateUser(newsUser);
        }
        NewsGUI.getCachedUsers().remove(newsUser);
    }

}
