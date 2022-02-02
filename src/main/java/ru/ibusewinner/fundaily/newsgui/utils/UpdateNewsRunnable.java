package ru.ibusewinner.fundaily.newsgui.utils;

import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.newsgui.NewsGUI;

public class UpdateNewsRunnable extends BukkitRunnable {
    @Override
    public void run() {
        NewsGUI.getNewsItems().clear();
        NewsGUI.getMySQL().updateNews();
    }
}
