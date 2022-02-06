package ru.ibusewinner.fundaily.newsgui;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ibusewinner.fundaily.newsgui.command.NewsCommand;
import ru.ibusewinner.fundaily.newsgui.items.NewsItem;
import ru.ibusewinner.fundaily.newsgui.items.NewsType;
import ru.ibusewinner.fundaily.newsgui.items.NewsUser;
import ru.ibusewinner.fundaily.newsgui.utils.NewsMySQL;
import ru.ibusewinner.fundaily.newsgui.utils.UpdateNewsRunnable;
import ru.ibusewinner.plugin.buseapi.BuseAPI;
import ru.ibusewinner.plugin.buseapi.config.ConfigManager;
import ru.ibusewinner.plugin.buseapi.gui.GUIAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class NewsGUI extends JavaPlugin {

    private static NewsGUI instance;
    private static ConfigManager config;
    private static NewsMySQL mySQL;
    private static GUIAPI<NewsGUI> guiapi;
    private static List<NewsItem> newsItems = new ArrayList<>();
    private static List<NewsType> newsTypes = new ArrayList<>();
    private static List<NewsUser> cachedUsers = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        config = new ConfigManager(this, this.getDataFolder(), "config.yml");
        config.createConfig();
        mySQL = new NewsMySQL(config.getConfig().getString("mysql.host"),
                config.getConfig().getInt("mysql.port"),
                config.getConfig().getString("mysql.database"),
                config.getConfig().getString("mysql.args", "?autoReconnect=true"),
                config.getConfig().getString("mysql.user"),
                config.getConfig().getString("mysql.password"));
        mySQL.createTables();

        guiapi = new GUIAPI<>(this);
        addAllTypes();

        new NewsCommand(this, "news");

        new UpdateNewsRunnable().runTaskTimerAsynchronously(this, 20, 20*60);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void addAllTypes() {
        ConfigurationSection typeConfigSection = config.getConfig().getConfigurationSection("gui.style.news-type");
        Set<String> types = typeConfigSection.getKeys(false);
        for (String type : types) {
            String typeName = BuseAPI.getMessageManager().convertMessage(typeConfigSection.getString(type+".type-name"));
            Material material = Material.valueOf(typeConfigSection.getString(type+".material").toUpperCase());
            int amount = typeConfigSection.getInt(type+".amount");
            int modelData = typeConfigSection.getInt(type+".model-data");
            NewsType newsType = new NewsType(type, typeName, material, amount, modelData);
            newsTypes.add(newsType);
        }
    }

    public static List<NewsUser> getCachedUsers() {
        return cachedUsers;
    }

    public static NewsUser getCachedUserByNickname(String nickname) {
        for (NewsUser newsUser : cachedUsers) {
            if (newsUser.getNickname().equals(nickname)) {
                return newsUser;
            }
        }
        return null;
    }

    public static NewsItem getNewsItemById(long id) {
        for (NewsItem newsItem : newsItems) {
            if (newsItem.getId() == id) {
                return newsItem;
            }
        }
        return null;
    }

    public static NewsType getNewsTypeById(String id) {
        for (NewsType newsType : newsTypes) {
            if (newsType.getId().equalsIgnoreCase(id)) {
                return newsType;
            }
        }
        return null;
    }

    public static String formatDate(long date) {
        return new SimpleDateFormat(getConfigManager().getConfig().getString("gui.date-format")).format(date);
    }

    public static ConfigManager getConfigManager() {
        return config;
    }

    public static GUIAPI<NewsGUI> getGuiapi() {
        return guiapi;
    }

    public static NewsGUI getInstance() {
        return instance;
    }

    public static NewsMySQL getMySQL() {
        return mySQL;
    }

    public static List<NewsItem> getNewsItems() {
        return newsItems;
    }
}
