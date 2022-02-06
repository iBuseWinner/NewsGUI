package ru.ibusewinner.fundaily.newsgui.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.fundaily.newsgui.items.NewsItem;
import ru.ibusewinner.fundaily.newsgui.items.NewsUser;
import ru.ibusewinner.plugin.buseapi.BuseAPI;
import ru.ibusewinner.plugin.buseapi.gui.GUI;

public class NewsMenu extends GUI<NewsGUI> {
    private Player holder;

    public NewsMenu(NewsGUI plugin, Player holder) {
        super(plugin);
        this.holder = holder;
        createInventory();
    }

    public void createInventory() {
        int slot = 0;
        for (NewsItem newsItem : NewsGUI.getNewsItems()) {
            ItemStack itemStack = new ItemStack(newsItem.getNewsType().getMaterial(), newsItem.getNewsType().getAmount());
            ItemMeta itemMeta = itemStack.getItemMeta();

            NewsUser newsUser = NewsGUI.getCachedUserByNickname(holder.getName());

            itemMeta.setDisplayName(BuseAPI.getMessageManager().convertMessage(NewsGUI.getConfigManager().getConfig().getString("gui.style.item.name")
                    .replace("$news-id$", String.valueOf(newsItem.getId()))
                    .replace("$news-title$", newsItem.getTitle())
                    .replace("$news-author$", newsItem.getAuthor())
                    .replace("$news-date$", String.valueOf(newsItem.getDate()))
                    .replace("$news-date-formatted$", NewsGUI.formatDate(newsItem.getDate()))
                    .replace("$news-type-name$", newsItem.getNewsType().getName())
                    .replace("$news-is-new$",
                            newsUser.getWatchedNews().contains(newsItem.getId())
                                    ?
                                    BuseAPI.getMessageManager().convertMessage(
                                            NewsGUI.getConfigManager().getConfig().getString("gui.style.new"))
                                    :
                                    ""
                    )));
            itemMeta.setLore(newsItem.getLore());
            itemMeta.setCustomModelData(newsItem.getNewsType().getModelData());
            itemStack.setItemMeta(itemMeta);

            if (newsItem.isPublished()) {
                set(slot, itemStack, (player, item) -> {
                    if (!newsUser.getWatchedNews().contains(newsItem.getId())) {
                        newsUser.addWatchedNews(newsItem.getId());
                    }
                    return ButtonAction.CANCEL;
                });
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
