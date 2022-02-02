package ru.ibusewinner.fundaily.newsgui.items;

import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.plugin.buseapi.BuseAPI;

import java.util.ArrayList;
import java.util.List;

public class NewsItem {
    private long id;
    private String title;
    private List<String> textLore;
    private NewsType newsType;
    private String author;
    private long date;
    private boolean published;

    public NewsItem(long id, String title, List<String> textLore, NewsType newsType, String author, long date, boolean published) {
        this.id = id;
        this.title = title;
        this.textLore = textLore;
        this.newsType = newsType;
        this.author = author;
        this.date = date;
        this.published = published;
    }

    public NewsItem(long id) {
        this.id = id;
        this.title = BuseAPI.getMessageManager().convertMessage("&aСтатья №"+id);
        this.textLore = new ArrayList<>();
        this.newsType = NewsGUI.getNewsTypeById("notify");
        this.author = "Администрация FunDaily";
        this.date = System.currentTimeMillis();
        this.published = false;
    }

    public long getId() {
        return id;
    }

    public List<String> getTextLore() {
        return textLore;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for (String str : textLore) {
            text.append(str).append(",bnf,ghd,");
        }
        String stringText = text.toString();
        stringText = stringText.substring(0, stringText.length()-",bnf,ghd,".length());
        return stringText;
    }

    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        List<String> formatFromConfig = NewsGUI.getConfigManager().getConfig().getStringList("gui.style.item.lore");
        for (String s : formatFromConfig) {
            if (s.equalsIgnoreCase("$text$")) {
                for (String str : textLore) {
                    lore.add(BuseAPI.getMessageManager().convertMessage(str));
                }
            } else {
                lore.add(BuseAPI.getMessageManager().convertMessage(s
                        .replace("$news-date-formatted$", NewsGUI.formatDate(date))
                        .replace("$news-author$", author)
                        .replace("$news-title$", title)
                        .replace("$news-id$", String.valueOf(id))
                        .replace("$news-type-name$", newsType.getName())));
            }
        }
        return lore;
    }

    public long getDate() {
        return date;
    }

    public NewsType getNewsType() {
        return newsType;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublished() {
        return published;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNewsType(NewsType newsType) {
        this.newsType = newsType;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setTextLore(List<String> textLore) {
        this.textLore = textLore;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
