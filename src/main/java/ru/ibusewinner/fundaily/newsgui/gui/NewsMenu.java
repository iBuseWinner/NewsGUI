package ru.ibusewinner.fundaily.newsgui.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.fundaily.newsgui.items.NewsItem;
import ru.ibusewinner.plugin.buseapi.BuseAPI;
import ru.ibusewinner.plugin.buseapi.gui.GUI;

public class NewsMenu extends GUI<NewsGUI> {
    public NewsMenu(NewsGUI plugin) {
        super(plugin);
        createInventory();
    }

    public void createInventory() {
        int slot = 0;
        for (NewsItem newsItem : NewsGUI.getNewsItems()) {
            ItemStack itemStack = new ItemStack(newsItem.getNewsType().getMaterial(), newsItem.getNewsType().getAmount());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(BuseAPI.getMessageManager().convertMessage(NewsGUI.getConfigManager().getConfig().getString("gui.style.item.name")
                    .replace("$news-id$", String.valueOf(newsItem.getId()))
                    .replace("$news-title$", newsItem.getTitle())
                    .replace("$news-author$", newsItem.getAuthor())
                    .replace("$news-date$", String.valueOf(newsItem.getDate()))
                    .replace("$news-date-formatted$", NewsGUI.formatDate(newsItem.getDate()))
                    .replace("$news-type-name$", newsItem.getNewsType().getName())));
            itemMeta.setLore(newsItem.getLore());
            itemMeta.setCustomModelData(newsItem.getNewsType().getModelData());
            itemStack.setItemMeta(itemMeta);

            if (newsItem.isPublished()) {
                set(slot, itemStack);
                slot++;
            }
        }
    }

    @Override
    public int getSize() {
        return NewsGUI.getConfigManager().getConfig().getInt("gui.size");
    }

    @Override
    public String getTitle() {
        return BuseAPI.getMessageManager().convertMessage(NewsGUI.getConfigManager().getConfig().getString("gui.title"));
    }

    @Override
    public boolean canClose(Player player) {
        return true;
    }
}
