package ru.ibusewinner.fundaily.newsgui.utils;

import ru.ibusewinner.fundaily.newsgui.NewsGUI;
import ru.ibusewinner.fundaily.newsgui.items.NewsItem;
import ru.ibusewinner.fundaily.newsgui.items.NewsType;
import ru.ibusewinner.plugin.buseapi.BuseAPI;
import ru.ibusewinner.plugin.buseapi.mysql.MySQL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class NewsMySQL extends MySQL {
    public NewsMySQL(String host, int port, String database, String args, String user, String password) {
        super(host, port, database, args, user, password);
    }

    public void createTables() {
        getPreparedStatement("CREATE TABLE IF NOT EXISTS `news` " +
                "(`id` BIGINT(19) NOT NULL AUTO_INCREMENT, " +
                "`title` VARCHAR(50) NULL DEFAULT '-' COLLATE 'utf8_general_ci', " +
                "`text` TEXT(200) NULL COLLATE 'utf8_general_ci', " +
                "`type` VARCHAR(50) NULL DEFAULT '-' COLLATE 'utf8_general_ci', " +
                "`author` VARCHAR(50) NULL DEFAULT '-' COLLATE 'utf8_general_ci', " +
                "`date` BIGINT(50) NOT NULL DEFAULT '0', " +
                "`published` INT(2) NOT NULL DEFAULT '0', " +
                "PRIMARY KEY (`id`) USING BTREE) COLLATE='utf8_general_ci' ENGINE=InnoDB;", preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
    }

    public long createNews() {
        AtomicLong id = new AtomicLong();
        getPreparedStatement("INSERT INTO `news` () VALUES ();", preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
        getResultSet("SELECT * FROM `news` WHERE `title`='-' ORDER BY `id` DESC LIMIT 1;", resultSet -> {
            try {
                if (resultSet.next()) {
                    id.set(resultSet.getLong("id"));
                }
                resultSet.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
        NewsGUI.getNewsItems().add(new NewsItem(id.get()));
        return id.get();
    }

    public void deleteNew(long id) {
        getPreparedStatement("DELETE FROM `news` WHERE `id`='"+id+"';", preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
    }

    public void updateNew(NewsItem newsItem) {
        getPreparedStatement("UPDATE `news` SET " +
                "`title`='"+newsItem.getTitle()+"', " +
                "`text`='"+newsItem.getText()+"', " +
                "`type`='"+newsItem.getNewsType().getId()+"', " +
                "`author`='"+newsItem.getAuthor()+"', " +
                "`date`='"+newsItem.getDate()+"', " +
                "`published`='"+(newsItem.isPublished() ? 1 : 0)+"' " +
                "WHERE `id`='"+newsItem.getId()+"';", preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
    }

    public void updateNews() {
        getResultSet("SELECT * FROM `news` ORDER BY `id` DESC LIMIT 27;", resultSet -> {
            try {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");

                    String textString = resultSet.getString("text");
                    List<String> textLore = new ArrayList<>();
                    if (textString != null) {
                        for (String str : textString.split(",bnf,ghd,")) {
                            textLore.add(BuseAPI.getMessageManager().convertMessage(str));
                        }
                    }

                    String typeString = resultSet.getString("type");
                    NewsType newsType = NewsGUI.getNewsTypeById(typeString);

                    String author = resultSet.getString("author");
                    long date = resultSet.getLong("date");
                    boolean published = resultSet.getInt("published") == 1;

                    NewsItem newsItem = new NewsItem(
                            id,
                            BuseAPI.getMessageManager().convertMessage(title),
                            textLore,
                            newsType,
                            BuseAPI.getMessageManager().convertMessage(author),
                            date,
                            published);
                    NewsGUI.getNewsItems().add(newsItem);
                }
                resultSet.close();
            } catch (SQLException e) {
                BuseAPI.getBuseLogger().error(e);
            }
        });
    }
}
