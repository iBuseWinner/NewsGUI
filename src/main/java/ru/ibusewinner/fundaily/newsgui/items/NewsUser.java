package ru.ibusewinner.fundaily.newsgui.items;

import java.util.List;

public class NewsUser {
    private long id;
    private String nickname;
    private List<Long> watchedNews;

    public NewsUser(long id, String nickname, List<Long> watchedNews) {
        this.id = id;
        this.nickname = nickname;
        this.watchedNews = watchedNews;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Long> getWatchedNews() {
        return watchedNews;
    }

    public void addWatchedNews(long... ids) {
        for (long id : ids) {
            watchedNews.add(id);
        }
    }
}
